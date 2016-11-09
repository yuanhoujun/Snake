package me.foji.demo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private ViewPager mViewPager;
    private RadioButton message;
    private RadioButton follow;
    private RadioButton discover;
    private RadioButton mine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        message = (RadioButton) findViewById(R.id.message);
        follow = (RadioButton) findViewById(R.id.follow);
        discover = (RadioButton) findViewById(R.id.discover);
        mine = (RadioButton) findViewById(R.id.mine);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0: {
                        message.setChecked(true);
                        break;
                    }
                    case 1: {
                        discover.setChecked(true);
                        break;
                    }
                    case 2: {
                        follow.setChecked(true);
                        break;
                    }
                    case 3: {
                        mine.setChecked(true);
                        break;
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        ArrayList<BaseFragment> fragments = new ArrayList<>();
        fragments.add(new FragmentMesssage());
        fragments.add(new FragmentDiscover());
        fragments.add(new FragmentFollow());
        fragments.add(new FragmentMine());
        mViewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(), fragments));

        message.setOnClickListener(this);
        follow.setOnClickListener(this);
        discover.setOnClickListener(this);
        mine.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.message: {
                mViewPager.setCurrentItem(0, false);
                break;
            }
            case R.id.discover: {
                mViewPager.setCurrentItem(1, false);
                break;
            }
            case R.id.follow: {
                mViewPager.setCurrentItem(2, false);
                break;
            }
            case R.id.mine: {
                mViewPager.setCurrentItem(3, false);
                break;
            }
        }
    }

    @Override
    protected boolean openSlideToClose() {
        return false;
    }

    class PagerAdapter extends FragmentPagerAdapter {
        private ArrayList<BaseFragment> fragments = new ArrayList<>();

        public PagerAdapter(FragmentManager fm, ArrayList<BaseFragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}
