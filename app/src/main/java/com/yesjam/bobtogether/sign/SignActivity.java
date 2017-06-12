package com.yesjam.bobtogether.sign;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.yesjam.bobtogether.Adapter.MainFirstRecyclerAdapter;
import com.yesjam.bobtogether.Adapter.MainSecondRecyclerAdapter;
import com.yesjam.bobtogether.Etc;
import com.yesjam.bobtogether.ConnServer;
import com.yesjam.bobtogether.MainActivity;
import com.yesjam.bobtogether.Preferences.FCMRTokenPreferences;
import com.yesjam.bobtogether.Preferences.UserDataPreferences;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.yesjam.bobtogether.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;

public class SignActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "Sign";
    private static final int RC_SIGN_IN = 9001;
    private SignInButton signWithGoogleBtn;
    private ProgressDialog mProgressDialog;
    private GoogleApiClient mGoogleApiClient;

    private String googleIdToken;
    private Handler handler;

    private AsyncHttpClient asyncHttpClient;
    private JSONObject jsonObject;
    private JSONArray jsonArray;
    private ByteArrayEntity byteArrayEntity;
    private ConnServer connServer;

    private UserDataPreferences userDataPreferences;
    private FCMRTokenPreferences fcmrTokenPreferences;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        userDataPreferences = new UserDataPreferences(SignActivity.this);
        fcmrTokenPreferences = new FCMRTokenPreferences(SignActivity.this);
        connServer = new ConnServer(SignActivity.this);
        signWithGoogleBtn = (SignInButton) findViewById(R.id.sign_googlesign_btn);

        progressDialog = new ProgressDialog(SignActivity.this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(Etc.GOOGLE_CLIENT_ID)
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        signWithGoogleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {

            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
//                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            googleIdToken = acct.getIdToken();
            userDataPreferences.setGoogleIdToken(googleIdToken);

            update(true);
        } else {
//            Toast.makeText(this, "구글 인증 실패", Toast.LENGTH_LONG).show();

            update(false);
        }
    }

    private void update(boolean signedIn) {
        if (signedIn) {
            start();
        } else {

        }
    }

    private void start() {
        Etc.bobPagingNum = 0;
        Etc.chatPagingNum = 0;
        signUp();
    }


    private void signUp() {

        asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.addHeader("google-id-token", userDataPreferences.getGoogleIdToken());
        jsonObject = new JSONObject();

        try {
            jsonObject.put("googleIdToken", userDataPreferences.getGoogleIdToken());
            jsonObject.put("FCMRToken", fcmrTokenPreferences.getFCMRToken());
            byteArrayEntity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));

            jsonObject = null;
            System.gc();    //jsonObject 갹체를 재활용 하기 위해 기사용한 객체는 가비지콜렉션.
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        asyncHttpClient.post(SignActivity.this, Etc.SIGN_UP, byteArrayEntity, "application/json", new TextHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(SignActivity.this, responseString, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    jsonObject = new JSONObject(responseString.toString());
                    userDataPreferences.setEmail(jsonObject.getString("email"));
                    userDataPreferences.setFName(jsonObject.getString("fName"));
                    userDataPreferences.setGName(jsonObject.getString("gName"));
                    userDataPreferences.setPicUrl(jsonObject.getString("pictureUrl"));
                    userDataPreferences.setGoogleIdToken(jsonObject.getString("googleIdToken"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                Toast.makeText(SignActivity.this, "서버와 통신을 성공", Toast.LENGTH_LONG).show();
                callBobs();
            }
        });
    }

    private void callBobs() {

        asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.addHeader("google-id-token", userDataPreferences.getGoogleIdToken());
        jsonObject = new JSONObject();

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

        asyncHttpClient.post(SignActivity.this, Etc.CALL_BOBS, byteArrayEntity, "application/json", new TextHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(SignActivity.this, responseString, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (!Etc.secondContentsList.isEmpty()) {
                    Etc.secondContentsList.clear();
                }
                try {
                    jsonArray = new JSONArray(responseString.toString().trim());

                    Etc.bobNumber.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Etc.bobNumber.add(Integer.parseInt(jsonArray.getJSONObject(i).getString("number")));
                        Etc.secondContentsList.add(new MainSecondRecyclerAdapter.secondItem(
                                Etc.IMAGE_FOOD + jsonArray.getJSONObject(i).getString("foodPicUrl")
                                , jsonArray.getJSONObject(i).getString("title")
                                , jsonArray.getJSONObject(i).getString("place")
                                , jsonArray.getJSONObject(i).getString("time")
                                , jsonArray.getJSONObject(i).getString("nNum") + "명 / " + jsonArray.getJSONObject(i).getString("tNum") + "명"
                                , jsonArray.getJSONObject(i).getString("email")
                        ));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonObject = null;
                jsonArray = null;
                byteArrayEntity = null;
                asyncHttpClient = null;
                System.gc();
                callChats();
            }
        });
    }

    private void callChats() {
        asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.addHeader("google-id-token", userDataPreferences.getGoogleIdToken());
        jsonObject = new JSONObject();

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

        asyncHttpClient.post(SignActivity.this, Etc.CALL_CHATS, byteArrayEntity, "application/json", new TextHttpResponseHandler() {

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(SignActivity.this, responseString, Toast.LENGTH_LONG).show();


            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                if (!Etc.firstContentsList.isEmpty()) {
                    Etc.firstContentsList.clear();
                }
                try {
                    jsonArray = new JSONArray(responseString.toString().trim());
                    Etc.chatRoomUid.clear();
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
                jsonObject = null;
                jsonArray = null;
                byteArrayEntity = null;
                asyncHttpClient = null;
                System.gc();

                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(SignActivity.this, MainActivity.class));
                        finish();
                    }
                }, 1000);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    public void onBackPressed() {
        startActivity(new Intent(SignActivity.this, InformationActivity.class));
        finish();
    }

    //-------------------------------------노필요-----------------------------------------------------------------------
//    private void signOut() {
//        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
//                new ResultCallback<Status>() {
//                    @Override
//                    public void onResult(Status status) {
//                        update(false);
//                        // [END_EXCLUDE]
//                    }
//                });
//    }
//
//    private void revokeAccess() {
//        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
//                new ResultCallback<Status>() {
//                    @Override
//                    public void onResult(Status status) {
//                        update(false);
//                        // [END_EXCLUDE]
//                    }
//                });
//    }
}
