package com.yesjam.bobtogether;


import android.content.Context;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.yesjam.bobtogether.Adapter.MainFirstRecyclerAdapter;
import com.yesjam.bobtogether.Adapter.MainSecondRecyclerAdapter;
import com.yesjam.bobtogether.Preferences.UserDataPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;

public class ConnServer {

    private Context context;
    private AsyncHttpClient asyncHttpClient;
    private JSONArray jsonArray;
    private JSONObject jsonObject;
    private ByteArrayEntity byteArrayEntity;
    private UserDataPreferences userDataPreferences;
    private int chatFlag = Etc.YET;
    private int bobFlag = Etc.YET;
    private int flag = Etc.YET;

    public ConnServer(Context context) {
        this.context = context;
        userDataPreferences = new UserDataPreferences(context);
    }

    public int callChats() {
        asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.addHeader("google-id-token", userDataPreferences.getGoogleIdToken());
        jsonObject = new JSONObject();

        if (Etc.chatPagingBool == false) {
            Etc.chatPagingNum = 0;
        }

        try {
            jsonObject.put("googleIdToken", userDataPreferences.getGoogleIdToken());
            jsonObject.put("email", userDataPreferences.getEmail());
            jsonObject.put("paging", Etc.chatPagingNum);
            byteArrayEntity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));

            jsonObject = null;
            System.gc();    //jsonObject 갹체를 재활용 하기 위해 기사용한 객체는 가비지콜렉션.
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        asyncHttpClient.post(context, Etc.CALL_CHATS, byteArrayEntity, "application/json", new TextHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(context, responseString, Toast.LENGTH_LONG).show();
                chatFlag = Etc.FALSE;

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                if (Etc.chatPagingBool == false) {
                    if (!Etc.firstContentsList.isEmpty()) {
                        Etc.firstContentsList.clear();
                    }
                    Etc.chatRoomUid.clear();
                    MainFragment.firstScrollListener.resetState();
                }
                Etc.chatPagingBool = false;

                try {
                    jsonArray = new JSONArray(responseString.toString().trim());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Etc.chatRoomUid.add(jsonArray.getJSONObject(i).getString("roomUid"));
                        Etc.firstContentsList.add(new MainFirstRecyclerAdapter.firstItem(
                                Etc.IMAGE_FOOD + jsonArray.getJSONObject(i).getString("foodPicUrl")
                                , jsonArray.getJSONObject(i).getString("title")
                                , jsonArray.getJSONObject(i).getString("place")
                                , jsonArray.getJSONObject(i).getString("nNum") + "명 / " + jsonArray.getJSONObject(i).getString("tNum") + "명"
                                , jsonArray.getJSONObject(i).getString("time")));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                MainFragment.firstAdapter.notifyDataSetChanged();
                MainFragment.secondAdapter.notifyDataSetChanged();

                refreshDismiss();

                chatFlag = Etc.TRUE;
                jsonObject = null;
                jsonArray = null;
                byteArrayEntity = null;
                asyncHttpClient = null;
                System.gc();

            }
        });
        return chatFlag;
    }

    public int callBobs() {

        asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.addHeader("google-id-token", userDataPreferences.getGoogleIdToken());
        jsonObject = new JSONObject();

        if (Etc.bobPagingBool == false) {
            Etc.bobPagingNum = 0;
        }


        try {
            jsonObject.put("googleIdToken", userDataPreferences.getGoogleIdToken());
            jsonObject.put("email", userDataPreferences.getEmail());
            jsonObject.put("paging", Etc.bobPagingNum);
            byteArrayEntity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));

            jsonObject = null;
            System.gc();    //jsonObject 갹체를 재활용 하기 위해 기사용한 객체는 가비지콜렉션.
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        asyncHttpClient.post(context, Etc.CALL_BOBS, byteArrayEntity, "application/json", new TextHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(context, responseString, Toast.LENGTH_LONG).show();
                bobFlag = Etc.FALSE;
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                if (Etc.bobPagingBool == false) {
                    if (!Etc.secondContentsList.isEmpty()) {
                        Etc.secondContentsList.clear();
                    }
                    Etc.bobNumber.clear();
                    MainFragment.secondScrollListener.resetState();
                }
                Etc.bobPagingBool = false;

                try {
                    jsonArray = new JSONArray(responseString.toString().trim());

//                        secondContentsList.add(new MainSecondRecyclerAdapter.secondItem(
//                                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRvnIPS4klvQ7Wj69L5nKvtdSwfy0zcnqJ4iB4qV7MdjlfxZdPK"
//                                , jsonArray.getJSONObject(0).getString("title"), "구리시 곱창언니", "5월 29일 오후5시", "6명중 3명", "밥잘먹어"));
//                    if (!Etc.bobPagingBool) {
//                        Etc.bobNumber.clear();
//                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Etc.bobNumber.add(Integer.parseInt(jsonArray.getJSONObject(i).getString("number")));
                        Etc.secondContentsList.add(new MainSecondRecyclerAdapter.secondItem(
                                Etc.IMAGE_FOOD + jsonArray.getJSONObject(i).getString("foodPicUrl")
                                , jsonArray.getJSONObject(i).getString("title")
                                , jsonArray.getJSONObject(i).getString("place")
                                , jsonArray.getJSONObject(i).getString("time")
                                , jsonArray.getJSONObject(i).getString("nNum") + "명 / " + jsonArray.getJSONObject(i).getString("tNum") + "명"
                                , jsonArray.getJSONObject(i).getString("email") //오타 나중에 디비를 새로 만들어야 함!!
                        ));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                MainFragment.firstAdapter.notifyDataSetChanged();
                MainFragment.secondAdapter.notifyDataSetChanged();

                refreshDismiss();

                bobFlag = Etc.TRUE;
                jsonObject = null;
                jsonArray = null;
                byteArrayEntity = null;
                asyncHttpClient = null;
                System.gc();

            }
        });
        return bobFlag;
    }

    public int joinBob(int position) {

        asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.addHeader("google-id-token", userDataPreferences.getGoogleIdToken());
        jsonObject = new JSONObject();

        try {
            jsonObject.put("googleIdToken", userDataPreferences.getGoogleIdToken());
            jsonObject.put("email", userDataPreferences.getEmail());
            jsonObject.put("chatRoomUid", Etc.bobNumber.get(position));
            byteArrayEntity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));

            jsonObject = null;
            System.gc();    //jsonObject 갹체를 재활용 하기 위해 기사용한 객체는 가비지콜렉션.
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        asyncHttpClient.post(context, Etc.JOIN_BOB, byteArrayEntity, "application/json", new TextHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(context, responseString, Toast.LENGTH_LONG).show();
                flag = Etc.FALSE;
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    jsonObject = new JSONObject(responseString.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                flag = Etc.TRUE;
            }
        });

        MainFragment.firstAdapter.notifyDataSetChanged();
        MainFragment.secondAdapter.notifyDataSetChanged();

        jsonObject = null;
        jsonArray = null;
        byteArrayEntity = null;
        asyncHttpClient = null;
        System.gc();
        return flag;
    }

//    private void getProfile() {
//
//        asyncHttpClient = new AsyncHttpClient();
//        asyncHttpClient.addHeader("google-id-token", userDataPreferences.getGoogleIdToken());
//        jsonObject = new JSONObject();
//
//        try {
//            jsonObject.put("googleIdToken", userDataPreferences.getGoogleIdToken());
//            jsonObject.put("email", userDataPreferences.getEmail());
//            jsonObject.put("roomUid", roomUid);
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
//        asyncHttpClient.post(context, Etc.CHAT_WAIT_ROOM_INFO, byteArrayEntity, "application/json", new TextHttpResponseHandler() {
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                Toast.makeText(context, responseString, Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, String responseString) {
//                try {
//                    jsonObject = new JSONObject(responseString.toString());
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        MainFragment.firstAdapter.notifyDataSetChanged();
//        MainFragment.secondAdapter.notifyDataSetChanged();
//
//        jsonObject = null;
//        jsonArray = null;
//        byteArrayEntity = null;
//        asyncHttpClient = null;
//        System.gc();
//    }

    private void refreshDismiss() {

        if (MainFragment.firstRefreshLayout.isRefreshing()) {
            MainFragment.firstRefreshLayout.setRefreshing(false);
            Toast.makeText(context, "업데이트 되었습니다.", Toast.LENGTH_SHORT).show();
        }

        if (MainFragment.secondRefreshLayout.isRefreshing()) {
            MainFragment.secondRefreshLayout.setRefreshing(false);
            Toast.makeText(context, "업데이트 되었습니다.", Toast.LENGTH_SHORT).show();
        }

    }
}
