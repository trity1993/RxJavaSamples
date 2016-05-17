package com.rengwuxian.rxjavasamples;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.app.Fragment;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.rengwuxian.rxjavasamples.module.buffer_9.BufferFragment;
import com.rengwuxian.rxjavasamples.module.interval_11.IntervalFragment;
import com.rengwuxian.rxjavasamples.module.key_search_8.SearchKeyFragment;
import com.rengwuxian.rxjavasamples.module.merge_10.MergeFragment;
import com.rengwuxian.rxjavasamples.module.not_more_click_7.NotMoreClickFragment;
import com.rengwuxian.rxjavasamples.module.token_advanced_5.TokenAdvancedFragment;
import com.rengwuxian.rxjavasamples.module.token_4.TokenFragment;
import com.rengwuxian.rxjavasamples.module.cache_6.CacheFragment;
import com.rengwuxian.rxjavasamples.module.zip_3.ZipFragment;
import com.rengwuxian.rxjavasamples.module.elementary_1.ElementaryFragment;
import com.rengwuxian.rxjavasamples.module.map_2.MapFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @Bind(android.R.id.tabs) TabLayout tabLayout;
    @Bind(R.id.viewPager) ViewPager viewPager;
    @Bind(R.id.toolBar) Toolbar toolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolBar);

        viewPager.setAdapter(new FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public int getCount() {
                return 11;
            }

            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return new ElementaryFragment();
                    case 1:
                        return new MapFragment();
                    case 2:
                        return new ZipFragment();
                    case 3:
                        return new TokenFragment();
                    case 4:
                        return new TokenAdvancedFragment();
                    case 5:
                        return new CacheFragment();
                    case 6:
                        return new NotMoreClickFragment();
                    case 7:
                        return new SearchKeyFragment();
                    case 8:
                        return new BufferFragment();
                    case 9:
                        return new MergeFragment();
                    case 10:
                        return new IntervalFragment();
                    default:
                        return new ElementaryFragment();
                }
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return getString(R.string.title_elementary);
                    case 1:
                        return getString(R.string.title_map);
                    case 2:
                        return getString(R.string.title_zip);
                    case 3:
                        return getString(R.string.title_token);
                    case 4:
                        return getString(R.string.title_token_advanced);
                    case 5:
                        return getString(R.string.title_cache);
                    case 6:
                        return getString(R.string.title_throttleFirst);
                    case 7:
                        return getString(R.string.title_debounce);
                    case 8:
                        return getString(R.string.title_buffer);
                    case 9:
                        return getString(R.string.title_merge);
                    case 10:
                        return getString(R.string.title_interval);
                    default:
                        return getString(R.string.title_elementary);
                }
            }
        });
        tabLayout.setupWithViewPager(viewPager);
    }
}