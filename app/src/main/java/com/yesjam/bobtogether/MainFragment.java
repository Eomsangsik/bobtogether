package com.yesjam.bobtogether;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.yesjam.bobtogether.Listner.EndlessRecyclerViewScrollListener;
import com.yesjam.bobtogether.Listner.RecyclerItemClickListener;
import com.yesjam.bobtogether.Preferences.FCMRTokenPreferences;
import com.yesjam.bobtogether.Preferences.GeneralPreferences;
import com.yesjam.bobtogether.Preferences.UserDataPreferences;
import com.yesjam.bobtogether.Adapter.MainFirstRecyclerAdapter;
import com.yesjam.bobtogether.Adapter.MainSecondRecyclerAdapter;
import com.yesjam.bobtogether.sign.InformationActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainFragment {

    private static View view;
    public static RecyclerView.Adapter firstAdapter;
    public static RecyclerView.Adapter secondAdapter;

    public static SwipeRefreshLayout firstRefreshLayout;
    public static SwipeRefreshLayout secondRefreshLayout;

    public static EndlessRecyclerViewScrollListener secondScrollListener;
    public static EndlessRecyclerViewScrollListener firstScrollListener;

    public static class First extends Fragment {

        private RecyclerView recyclerView;
        private UserDataPreferences userDataPreferences;
        private ConnServer connServer;

        private AsyncHttpClient asyncHttpClient;
        private JSONObject jsonObject;
        private JSONArray jsonArray;
        private ByteArrayEntity byteArrayEntity;

        private ProgressDialog progressDialog;
        private LinearLayoutManager linearLayoutManager;

        static First newInstance() {
            First first = new First();
            return first;
        }

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            view = inflater.inflate(R.layout.fragment_first, container, false);

            recyclerView = (RecyclerView) view.findViewById(R.id.main_first_recyclerview);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            return view;
        }

        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            init();
            listner();

        }

        private void init() {

            userDataPreferences = new UserDataPreferences(getContext());
            connServer = new ConnServer(getContext());
            firstRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.first_swipeRefresh);
            firstAdapter = new MainFirstRecyclerAdapter(getContext(), Etc.firstContentsList);
            firstAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(firstAdapter);
            linearLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage(StringKor.PROGRESS_DIALOG);
        }

        private void listner() {
            recyclerView.addOnItemTouchListener(
                    new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {

                        @Override
                        public void onItemClick(View view, int position) {
                            fullRoomCheck(position);
                            progressDialog.show();
                        }

                        @Override
                        public void onLongItemClick(View view, int position) {
//                            Toast.makeText(getContext(), "롱 클릭 테스트" + position, Toast.LENGTH_SHORT).show();
                        }
                    }));

            firstRefreshLayout.setOnRefreshListener(
                    new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {

                            connServer.callBobs();
                            connServer.callChats();
                        }
                    }
            );

            firstScrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

//                    Toast.makeText(getContext(), "::" + page + "::" + totalItemsCount + "::", Toast.LENGTH_SHORT).show();
                    Etc.chatPagingNum = page;
                    Etc.chatPagingBool = true;
                    connServer.callChats();

                }
            };
            recyclerView.addOnScrollListener(firstScrollListener);

        }

        private void fullRoomCheck(final int position) {

            asyncHttpClient = new AsyncHttpClient();
            asyncHttpClient.addHeader("google-id-token", userDataPreferences.getGoogleIdToken());
            jsonObject = new JSONObject();

            try {
                jsonObject.put("googleIdToken", userDataPreferences.getGoogleIdToken());
                jsonObject.put("email", userDataPreferences.getEmail());
                jsonObject.put("chatRoomUid", Etc.chatRoomUid.get(position));
                byteArrayEntity = new ByteArrayEntity(jsonObject.toString().getBytes("UTF-8"));

                jsonObject = null;
                System.gc();    //jsonObject 갹체를 재활용 하기 위해 기사용한 객체는 가비지콜렉션.
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            asyncHttpClient.post(getContext(), Etc.FULL_ROOM_CHECK, byteArrayEntity, "application/json", new TextHttpResponseHandler() {

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Toast.makeText(getContext(), responseString, Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {

                    progressDialog.dismiss();

                    try {
                        jsonObject = new JSONObject(responseString.toString().trim());


                        //Toast.makeText(getContext(), "" + jsonObject.getString("nNum"), Toast.LENGTH_LONG).show();

                        if (jsonObject.getInt("nNum") >= jsonObject.getInt("tNum")) {
                            Intent intent = new Intent(getContext(), ChatActivity.class);
                            intent.putExtra("roomUid", Etc.chatRoomUid.get(position));
                            intent.putExtra("roomName", "테스트방");
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(getContext(), ChatWaitActivity.class);
                            intent.putExtra("roomUid", Etc.chatRoomUid.get(position));
                            intent.putExtra("place", jsonObject.getString("place"));
                            intent.putExtra("time", jsonObject.getString("time"));
                            intent.putExtra("tNum", jsonObject.getString("tNum"));
                            intent.putExtra("nNum", jsonObject.getString("nNum"));
                            startActivity(intent);
                        }

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

    public static class Second extends Fragment {

        private CoordinatorLayout secondParent;
        private RecyclerView recyclerView;
        private UserDataPreferences userDataPreferences;
        private FloatingActionButton floatingActionButton1, floatingActionButton2;
        private FloatingActionsMenu floatingActionsMenu;
        private ConnServer connServer;

        private AsyncHttpClient asyncHttpClient;
        private JSONObject jsonObject;
        private JSONArray jsonArray;
        private ByteArrayEntity byteArrayEntity;

        private AlertDialog.Builder joinBobAlertDialog;

        private ProgressDialog progressDialog;
        private LinearLayoutManager linearLayoutManager;


        static Second newInstance() {
            Second second = new Second();
            return second;
        }

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            view = inflater.inflate(R.layout.fragment_second, container, false);
            init();
            listner();
            return view;
        }

        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);


        }

        private void init() {
            secondParent = (CoordinatorLayout) view.findViewById(R.id.second_parent);
            userDataPreferences = new UserDataPreferences(getContext());
            secondRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.second_swipeRefresh);
            floatingActionButton1 = (FloatingActionButton) view.findViewById(R.id.fragment_second_fab1);
            floatingActionButton2 = (FloatingActionButton) view.findViewById(R.id.fragment_second_fab2);
            floatingActionsMenu = (FloatingActionsMenu) view.findViewById(R.id.fragment_second_fam);
            connServer = new ConnServer(getContext());
            recyclerView = (RecyclerView) view.findViewById(R.id.main_second_recyclerview);
            linearLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            secondAdapter = new MainSecondRecyclerAdapter(getContext(), Etc.secondContentsList);
            secondAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(secondAdapter);
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage(StringKor.PROGRESS_DIALOG);
        }

        private void listner() {

            secondParent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    floatingActionsMenu.collapse();
                }
            });

            floatingActionButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivityForResult(new Intent(getContext(), MakeBobActivity.class), Etc.MAKE_BOB_REQUEST_CODE);
                    floatingActionsMenu.collapse();
                }
            });

            floatingActionButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivityForResult(new Intent(getContext(), ConditionActivity.class), Etc.CONDITION_REQUEST_CODE);
                    floatingActionsMenu.collapse();
                }
            });

            recyclerView.addOnItemTouchListener(
                    new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {

                        @Override
                        public void onItemClick(View view, final int position) {
                            progressDialog.show();
                            joinBobCheck(position);
                        }

                        @Override
                        public void onLongItemClick(View view, int position) {
//                            Toast.makeText(getContext(), "롱 클릭 테스트" + position, Toast.LENGTH_SHORT).show();
                        }
                    }));


            secondRefreshLayout.setOnRefreshListener(
                    new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {

                            connServer.callBobs();
                            connServer.callChats();

                        }
                    }
            );

            secondScrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

//                    Toast.makeText(getContext(), "::" + page + "::" + totalItemsCount + "::", Toast.LENGTH_SHORT).show();
                    Etc.bobPagingNum = page;
                    Etc.bobPagingBool = true;
                    connServer.callBobs();

                }
            };
            recyclerView.addOnScrollListener(secondScrollListener);

        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == Etc.MAKE_BOB_REQUEST_CODE) {
                connServer.callBobs();
                connServer.callChats();
                firstAdapter.notifyDataSetChanged();
                secondAdapter.notifyDataSetChanged();
            }
        }

        private void joinBobCheck(final int position) {

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

            asyncHttpClient.post(getContext(), Etc.JOIN_BOB_CHECK, byteArrayEntity, "application/json", new TextHttpResponseHandler() {

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), responseString, Toast.LENGTH_LONG).show();

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
//                Toast.makeText(context, responseString, Toast.LENGTH_SHORT).show();

                    progressDialog.dismiss();

                    if (responseString.toString().trim().equals("no")) {
                        joinBobAlertDialog = new AlertDialog.Builder(getContext());
                        joinBobAlertDialog.setMessage("이 \'밥\'에 참여하시겠습니까?").setCancelable(false).setPositiveButton("네",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        connServer.joinBob(position);
                                        connServer.callBobs();
                                        connServer.callChats();
                                        joinBobAlertDialog = null;
                                        System.gc();

                                    }
                                }).setNegativeButton("아니요",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        return;
                                    }
                                });
                    } else {
                        joinBobAlertDialog = new AlertDialog.Builder(getContext());
                        joinBobAlertDialog.setMessage("이미 참여한 \'밥\'입니다.").setCancelable(false).setPositiveButton("네",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
//                                        connServer.callBobs();
//                                        connServer.callChats();
                                        joinBobAlertDialog = null;
                                        System.gc();
                                    }
                                });
                    }
                    joinBobAlertDialog.create().show();
                }
            });

            byteArrayEntity = null;
            asyncHttpClient = null;
            System.gc();
            return;
        }
    }

    public static class Third extends Fragment {

        private CircleImageView prifileImageView;
        private TextView profileName;
        private TextView profileEmail;
        private TextView profilestateMess;

        private LinearLayout signOut;
//        private GoogleApiClient mGoogleApiClient;

        private UserDataPreferences userDataPreferences;
        private GeneralPreferences generalPreferences;
        private FCMRTokenPreferences fcmrTokenPreferences;

        static Third newInstance() {
            Third third = new Third();
            return third;
        }

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            view = inflater.inflate(R.layout.fragment_third, container, false);

            prifileImageView = (CircleImageView) view.findViewById(R.id.profile_image);
            profileName = (TextView) view.findViewById(R.id.profile_name_tv);
            profileEmail = (TextView) view.findViewById(R.id.profile_email_tv);
            profilestateMess = (TextView) view.findViewById(R.id.profile_state_message);
            userDataPreferences = new UserDataPreferences(getContext());
            generalPreferences = new GeneralPreferences(getContext());
            fcmrTokenPreferences = new FCMRTokenPreferences(getContext());

            Glide.with(this).load(userDataPreferences.getPicUrl()).into(prifileImageView);
            profileName.setText(userDataPreferences.getFName() + userDataPreferences.getGName());
            profileEmail.setText(userDataPreferences.getEmail());

            return view;
        }

        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            signOut();
        }

        private void signOut() {
            signOut = (LinearLayout) view.findViewById(R.id.setting_signOut);

//            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                    .requestEmail()
//                    .build();
//            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
//                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
//                    .build();
//            MainActivity.mGoogleApiClient.connect();

            signOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (MainActivity.mGoogleApiClient.isConnected()) {
                        Auth.GoogleSignInApi.revokeAccess(MainActivity.mGoogleApiClient).setResultCallback(
                                new ResultCallback<Status>() {
                                    @Override
                                    public void onResult(Status status) {
                                        generalPreferences.clear();
                                        userDataPreferences.clear();
                                        startActivity(new Intent(getContext(), InformationActivity.class));
                                        Toast.makeText(getContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                                        MainActivity.getMainActivity().finish();
                                    }
                                });
                    } else {
                        generalPreferences.clear();
                        userDataPreferences.clear();
                        startActivity(new Intent(getContext(), InformationActivity.class));
                        Toast.makeText(getContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                        MainActivity.getMainActivity().finish();
                    }
                }
            });

        }

    }


    public static class Forth extends Fragment {

        static Forth newInstance() {
            Forth forth = new Forth();
            return forth;
        }

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            view = inflater.inflate(R.layout.fragment_forth, container, false);

            return view;
        }

        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

        }
    }
}
