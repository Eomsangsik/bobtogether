package com.yesjam.bobtogether;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.yesjam.bobtogether.Adapter.JoinedRecyclerAdapter;
import com.yesjam.bobtogether.Preferences.UserDataPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;

public class ChatWaitActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private JoinedRecyclerAdapter joinedRecyclerAdapter;
    private static ArrayList<JoinedRecyclerAdapter.joinedItem> joinedList;
    private AsyncHttpClient asyncHttpClient;
    private JSONObject jsonObject;
    private JSONArray jsonArray;
    private ByteArrayEntity byteArrayEntity;
    private UserDataPreferences userDataPreferences;

    private TextView placeTv;
    private TextView timeTv;
    private TextView tNumTv;
    private TextView nNumTv;
    private String roomUid;
    private String place, time, tNum, nNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_wait);

        init();

    }

    private void init() {
        roomUid = getIntent().getExtras().getString("roomUid");
        place = getIntent().getExtras().getString("place");
        time = getIntent().getExtras().getString("time");
        tNum = getIntent().getExtras().getString("tNum");
        nNum = getIntent().getExtras().getString("nNum");
        joinedList = new ArrayList<>();
        userDataPreferences = new UserDataPreferences(ChatWaitActivity.this);
        placeTv = (TextView) findViewById(R.id.chat_wait_place);
        timeTv = (TextView) findViewById(R.id.chat_wait_time);
        tNumTv = (TextView) findViewById(R.id.chat_wait_tNum);
        nNumTv = (TextView) findViewById(R.id.chat_wait_nNum);
        placeTv.setText(place);
        timeTv.setText(time);
        tNumTv.setText(tNum);
        nNumTv.setText(nNum);
        recyclerView = (RecyclerView) findViewById(R.id.joined_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(ChatWaitActivity.this));
        joinedRecyclerAdapter = new JoinedRecyclerAdapter(ChatWaitActivity.this, joinedList);
        joinedRecyclerAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(joinedRecyclerAdapter);
        getProfile();
    }

    private void getProfile() {

        asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.addHeader("google-id-token", userDataPreferences.getGoogleIdToken());
        jsonObject = new JSONObject();

        try {
            jsonObject.put("googleIdToken", userDataPreferences.getGoogleIdToken());
            jsonObject.put("email", userDataPreferences.getEmail());
            jsonObject.put("roomUid", Integer.parseInt(roomUid));
            byteArrayEntity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));

            jsonObject = null;
            System.gc();    //jsonObject 갹체를 재활용 하기 위해 기사용한 객체는 가비지콜렉션.
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        asyncHttpClient.post(ChatWaitActivity.this, Etc.CHAT_WAIT_ROOM_INFO, byteArrayEntity, "application/json", new TextHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(ChatWaitActivity.this, responseString, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                joinedList.clear();
                try {
                    jsonArray = new JSONArray(responseString.toString().trim());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        joinedList.add(new JoinedRecyclerAdapter.joinedItem(
                                jsonArray.getJSONObject(i).getString("picUrl")
                                , jsonArray.getJSONObject(i).getString("nick")
                                , jsonArray.getJSONObject(i).getString("stateMessage")
                        ));
                    }
                    joinedRecyclerAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        MainFragment.firstAdapter.notifyDataSetChanged();
        MainFragment.secondAdapter.notifyDataSetChanged();

        jsonObject = null;
        jsonArray = null;
        byteArrayEntity = null;
        asyncHttpClient = null;
        System.gc();
    }
}