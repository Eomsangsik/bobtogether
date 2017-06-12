package com.yesjam.bobtogether;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.yesjam.bobtogether.Adapter.MainFirstRecyclerAdapter;
import com.yesjam.bobtogether.Adapter.MainSecondRecyclerAdapter;
import com.yesjam.bobtogether.Preferences.UserDataPreferences;
import com.yesjam.bobtogether.sign.InformationActivity;
import com.yesjam.bobtogether.sign.SignActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;

public class SplashActivity extends AppCompatActivity {

    private Handler handler;
    private UserDataPreferences userDataPreferences;

    private AsyncHttpClient asyncHttpClient;
    private JSONObject jsonObject;
    private JSONArray jsonArray;
    private ByteArrayEntity byteArrayEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        userDataPreferences = new UserDataPreferences(SplashActivity.this);
        handler = new Handler();

        if (userDataPreferences.hasItem("email")) {
            handler.postDelayed(runToSign, 2000);


        } else {
            handler.postDelayed(runToInfo, 2000);
        }

    }

    Runnable runToSign = new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(SplashActivity.this, SignActivity.class));
            finish();
        }
    };

    Runnable runToInfo = new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(SplashActivity.this, InformationActivity.class));
            finish();
        }
    };

//    public void callChats() {
//        asyncHttpClient = new AsyncHttpClient();
//        asyncHttpClient.addHeader("google-id-token", userDataPreferences.getGoogleIdToken());
//        jsonObject = new JSONObject();
//
//        try {
//            jsonObject.put("googleIdToken", userDataPreferences.getGoogleIdToken());
//            jsonObject.put("email", userDataPreferences.getEmail());
//            byteArrayEntity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
//
//            jsonObject = null;
//            System.gc();    //jsonObject 갹체를 재활용 하기 위해 기사용한 객체는 가비지콜렉션.
//        } catch (JSONException e) {
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//        asyncHttpClient.post(SplashActivity.this, Etc.CALL_CHATS, byteArrayEntity, "application/json", new TextHttpResponseHandler() {
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                Toast.makeText(SplashActivity.this, responseString, Toast.LENGTH_LONG).show();
//
//            }
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, String responseString) {
//                if (!Etc.firstContentsList.isEmpty()) {
//                    Etc.firstContentsList.clear();
//                }
//                try {
//                    jsonArray = new JSONArray(responseString.toString().trim());
//                    for (int i = 0; i < jsonArray.length(); i++) {
//
//                        Etc.firstContentsList.add(new MainFirstRecyclerAdapter.firstItem(
//                                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRvnIPS4klvQ7Wj69L5nKvtdSwfy0zcnqJ4iB4qV7MdjlfxZdPK"));
//
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                jsonObject = null;
//                jsonArray = null;
//                byteArrayEntity = null;
//                asyncHttpClient = null;
//                System.gc();
//                callBobs();
//
//            }
//        });
//    }
//
//    private void callBobs() {
////        AsyncHttpClient asyncHttpClient;
////        JSONObject jsonObject;
////        JSONArray jsonArray;
////        ByteArrayEntity byteArrayEntity;
//
//        asyncHttpClient = new AsyncHttpClient();
//        asyncHttpClient.addHeader("google-id-token", userDataPreferences.getGoogleIdToken());
//        jsonObject = new JSONObject();
//
//        try {
//            jsonObject.put("googleIdToken", userDataPreferences.getGoogleIdToken());
//            jsonObject.put("email", userDataPreferences.getEmail());
//            byteArrayEntity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));
//
//            jsonObject = null;
//            System.gc();    //jsonObject 갹체를 재활용 하기 위해 기사용한 객체는 가비지콜렉션.
//        } catch (JSONException e) {
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//        asyncHttpClient.post(SplashActivity.this, Etc.CALL_BOBS, byteArrayEntity, "application/json", new TextHttpResponseHandler() {
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                Toast.makeText(SplashActivity.this, responseString, Toast.LENGTH_LONG).show();
//
//            }
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, String responseString) {
//                if (!Etc.secondContentsList.isEmpty()) {
//                    Etc.secondContentsList.clear();
//                }
//                try {
//                    jsonArray = new JSONArray(responseString.toString().trim());
//
////                        secondContentsList.add(new MainSecondRecyclerAdapter.secondItem(
////                                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRvnIPS4klvQ7Wj69L5nKvtdSwfy0zcnqJ4iB4qV7MdjlfxZdPK"
////                                , jsonArray.getJSONObject(0).getString("title"), "구리시 곱창언니", "5월 29일 오후5시", "6명중 3명", "밥잘먹어"));
//
//                    for (int i = 0; i < jsonArray.length(); i++) {
//
//                        Etc.secondContentsList.add(new MainSecondRecyclerAdapter.secondItem(
//                                jsonArray.getJSONObject(i).getString("foodPicUrl")
//                                , jsonArray.getJSONObject(i).getString("title")
//                                , jsonArray.getJSONObject(i).getString("place")
//                                , "5월 29일 오후5시"
//                                , "6명중 3명"
//                                , jsonArray.getJSONObject(i).getString("eamil")//오타 나중에 디비를 새로 만들어야 함!!
//                        ));
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                jsonObject = null;
//                jsonArray = null;
//                byteArrayEntity = null;
//                asyncHttpClient = null;
//                System.gc();
//
//                startActivity(new Intent(SplashActivity.this, SignActivity.class));
//                finish();
//
//            }
//        });
//    }
}
