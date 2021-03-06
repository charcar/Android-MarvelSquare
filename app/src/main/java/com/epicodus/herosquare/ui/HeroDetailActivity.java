package com.epicodus.herosquare.ui;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.epicodus.herosquare.R;
import com.epicodus.herosquare.adapters.HeroPagerAdapter;
import com.epicodus.herosquare.models.Hero;
import com.epicodus.herosquare.util.ScaleAndFadePageTransformer;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HeroDetailActivity extends AppCompatActivity {
    @Bind(R.id.viewPager)
    ViewPager mViewPager;
    private HeroPagerAdapter adapterViewPager;
    ArrayList<Hero> mHeroes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hero_detail);
        ButterKnife.bind(this);

        mHeroes = Parcels.unwrap(getIntent().getParcelableExtra("heroes"));
        int startingPosition = Integer.parseInt(getIntent().getStringExtra("position"));
        adapterViewPager = new HeroPagerAdapter(getSupportFragmentManager(), mHeroes);
        mViewPager.setAdapter(adapterViewPager);
        mViewPager.setCurrentItem(startingPosition);
        mViewPager.setPageTransformer(true, new ScaleAndFadePageTransformer());
    }
}
