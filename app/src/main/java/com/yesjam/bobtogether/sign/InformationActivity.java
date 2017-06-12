package com.yesjam.bobtogether.sign;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.matthewtamlin.sliding_intro_screen_library.DotIndicator;
import com.yesjam.bobtogether.R;


import java.util.ArrayList;
import java.util.List;

public class InformationActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private List<Fragment> informationListFragments;
    private InformationPagerAdapter informationPagerAdapter;
    private DotIndicator indicator;
    private static View view;
    private Button signBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        viewPager = (ViewPager) findViewById(R.id.information_viewpager);
        informationListFragments = new ArrayList<>();
        signBtn = (Button) findViewById(R.id.info_sign_btn);

        informationListFragments.add(InformationFirst.newInstance());
        informationListFragments.add(InformationSecond.newInstance());
        informationListFragments.add(InformationThird.newInstance());
        informationListFragments.add(InformationForth.newInstance());

        informationPagerAdapter = new InformationPagerAdapter(getSupportFragmentManager(), informationListFragments);
        viewPager.setAdapter(informationPagerAdapter);


        indicator = (DotIndicator) findViewById(R.id.information_dot_indicator);
        indicator.setSelectedDotColor(Color.parseColor("#FFFFFF"));
        indicator.setUnselectedDotColor(Color.parseColor("#CFCFCF"));
        indicator.setNumberOfItems(informationListFragments.size());

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                indicator.setSelectedItem(viewPager.getCurrentItem(), true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        signBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InformationActivity.this, SignActivity.class));
                finish();
            }
        });

    }

    public class InformationPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> listFragments;

        public InformationPagerAdapter(FragmentManager fm, List<Fragment> listFragments) {
            super(fm);
            this.listFragments = listFragments;
        }

        @Override
        public Fragment getItem(int i) {
            return listFragments.get(i);
        }

        @Override
        public int getCount() {
            return listFragments.size();
        }
    }

    public static class InformationFirst extends Fragment {

        static InformationFirst newInstance() {
            InformationFirst informationFirst = new InformationFirst();
            return informationFirst;
        }

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            view = inflater.inflate(R.layout.fragment_information_first, container, false);

            return view;
        }

        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

        }
    }

    public static class InformationSecond extends Fragment {

        static InformationSecond newInstance() {
            InformationSecond informationSecond = new InformationSecond();
            return informationSecond;
        }

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            view = inflater.inflate(R.layout.fragment_information_second, container, false);

            return view;
        }

        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

        }
    }

    public static class InformationThird extends Fragment {

        static InformationThird newInstance() {
            InformationThird informationThird = new InformationThird();
            return informationThird;
        }

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            view = inflater.inflate(R.layout.fragment_information_third, container, false);

            return view;
        }

        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

        }
    }

    public static class InformationForth extends Fragment {


        static InformationForth newInstance() {
            InformationForth informationForth = new InformationForth();
            return informationForth;
        }

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            view = inflater.inflate(R.layout.fragment_information_forth, container, false);

            return view;
        }

        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

        }
    }
}
