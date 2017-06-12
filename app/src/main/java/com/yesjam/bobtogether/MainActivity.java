package com.yesjam.bobtogether;

import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.support.design.widget.TabLayout;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.yesjam.bobtogether.Adapter.MainPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Fragment> mainFragments;
    private MainPagerAdapter mainPagerAdapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private static MainActivity mainActivity;   //다른 액티비티에서 메인 액티비티를 종료시키기 위함
    private boolean isTwo = false;
    public static GoogleSignInOptions gso;
    public static GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = this;    //다른 액티비티에서 메인 액티비티를 종료시키기 위함

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(Etc.GOOGLE_CLIENT_ID)
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mGoogleApiClient.connect();

        setMainFragments();
        setTabLayout();

    }

    public static MainActivity getMainActivity() {   //다른 액티비티에서 메인 액티비티를 종료시키기 위함
        return mainActivity;
    }

    private void setMainFragments() {

        viewPager = (ViewPager) findViewById(R.id.mainViewpager_main);
        mainFragments = new ArrayList<>();
        mainFragments.add(MainFragment.First.newInstance());
        mainFragments.add(MainFragment.Second.newInstance());
        mainFragments.add(MainFragment.Third.newInstance());
//        mainFragments.add(MainFragment.Forth.newInstance());
        mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), mainFragments);
        viewPager.setAdapter(mainPagerAdapter);
    }

    private void setTabLayout() {

        tabLayout = (TabLayout) findViewById(R.id.tab_main);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void onBackPressed() {
        if (!isTwo) {
            Toast.makeText(getApplicationContext(), "종료하려면 한번 더 누르세요.", Toast.LENGTH_SHORT).show();
            Timer timer = new Timer(2000, 1);
            timer.start();
        } else {
            finish();
        }
    }

    public class Timer extends CountDownTimer {
        public Timer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            isTwo = true;
        }

        public void onFinish() {
            isTwo = false;
        }

        public void onTick(long millisUntilFinished) {
            Log.i("Test", "isTwo" + isTwo);
        }
    }

}
