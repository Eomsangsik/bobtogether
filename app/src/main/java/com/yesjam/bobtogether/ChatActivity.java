package com.yesjam.bobtogether;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.AsyncHttpClient;
import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubException;
import com.yesjam.bobtogether.Preferences.UserDataPreferences;
import com.yesjam.bobtogether.SQLite.ChatDBHelper;
import com.yesjam.bobtogether.Adapter.ChatAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.entity.ByteArrayEntity;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<User> UserList;
    private EditText messageEditText;
    private Button sendButton;
    private Pubnub pubnub;
    private ChatAdapter chatAdapter;
    private UserDataPreferences userDataPreferences;
    private String myName;
    private AsyncHttpClient asyncHttpClient;
    private JSONObject jOSendMessage;
    private JSONObject JOReceiveMessage;
    private ByteArrayEntity entity;
    private ChatDBHelper chatDBHelper;
    private ArrayList<String> senderList;
    private ArrayList<String> messageList;
    private ArrayList<String> dateList;
    private ArrayList<String> emailList;

    private String roomUid;
    private String roomName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Etc.running = true;

        pubnub = new Pubnub(Etc.PUBNUB_PUBLISH_KEY, Etc.PUBNUB_SUBSCRIBE_KEY);

        chatDBHelper = new ChatDBHelper(ChatActivity.this);

        roomUid = getIntent().getExtras().getString("roomUid");
        roomName = getIntent().getExtras().getString("roomName");

        messageEditText = (EditText) findViewById(R.id.messageEditText);
        sendButton = (Button) findViewById(R.id.sendButton);
        recyclerView = (RecyclerView) findViewById(R.id.messageRecyclerView);


        userDataPreferences = new UserDataPreferences(ChatActivity.this);
        myName = userDataPreferences.getFName() + userDataPreferences.getGName();

        asyncHttpClient = new AsyncHttpClient();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        UserList = new ArrayList<>();

        senderList = chatDBHelper.getSender(roomUid);
        messageList = chatDBHelper.getMessage(roomUid);
        dateList = chatDBHelper.getDate(roomUid);

        for (int i = 0; i < senderList.size(); i++) {
            UserList.add(new User(0
                    , userDataPreferences.getEmail()
                    , senderList.get(i)
                    , messageList.get(i)
                    , dateList.get(i))
            );
        }

        chatAdapter = new ChatAdapter(this, UserList);
        recyclerView.setAdapter(chatAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                recyclerView.smoothScrollToPosition(chatAdapter.getItemCount());
            }
        });

        try {
            pubnub.subscribe(roomUid, new Callback() {
                @Override
                public void successCallback(String channel, Object message) {
                    super.successCallback(channel, message);

                    JOReceiveMessage = (JSONObject) message;
                    ChatActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                UserList.add(new User(
                                        0
                                        , JOReceiveMessage.get("email").toString()
                                        , JOReceiveMessage.get("myName").toString()
                                        , JOReceiveMessage.get("message").toString()
                                        , JOReceiveMessage.get("date").toString())
                                );

                                chatAdapter.notifyDataSetChanged();
                                chatDBHelper.setChat(roomUid, jOSendMessage.get("message").toString(), jOSendMessage.get("myName").toString()
                                        , jOSendMessage.get("date").toString(), 0, jOSendMessage.get("email").toString());

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    recyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.smoothScrollToPosition(chatAdapter.getItemCount());
                        }
                    });
                }
            });
        } catch (PubnubException e) {
            e.printStackTrace();
        }

        messageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    sendButton.setEnabled(true);
                } else {
                    sendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                jOSendMessage = new JSONObject();
                try {
                    jOSendMessage.put("email", userDataPreferences.getEmail());
                    jOSendMessage.put("roomUid", roomUid);
                    jOSendMessage.put("roomName", roomName);
                    jOSendMessage.put("message", messageEditText.getText().toString().trim());
                    jOSendMessage.put("myName", myName);
                    jOSendMessage.put("date", new SimpleDateFormat("HH:mm", Locale.KOREA).format(new Date()));

                    entity = new ByteArrayEntity(jOSendMessage.toString().getBytes("UTF-8"));

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                pubnub.publish(roomUid, jOSendMessage, new Callback() {
                    @Override
                    public void successCallback(String channel, Object message) {
                        super.successCallback(channel, message);
                    }
                });

//                asyncHttpClient.post(ChatActivity.this, Etc.PUSH_URL, entity, "application/json", new TextHttpResponseHandler() {
//                    @Override
//                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                        //실패했을경우 사용자에게 메세지 전송이 실패 했음을 알려야 한다.
//                    }
//
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
//
//                    }
//                });

                messageEditText.setText("");
            }
        });

        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                recyclerView.smoothScrollToPosition(chatAdapter.getItemCount());

            }
        });


    }

    public class User {

        //        int imageResourceId;
        public String email;
        public String userName;
        public String date;
        public String message;


        public User(int imageResourceId, String email, String userName, String message, String date) {
//            this.imageResourceId = imageResourceId;
            this.email = email;
            this.userName = userName;
            this.date = date;
            this.message = message;
        }
    }

    public void onResume() {
        super.onResume();
        Etc.running = true;
    }

    public void onPause() {
        super.onPause();
        Etc.running = false;
    }

}