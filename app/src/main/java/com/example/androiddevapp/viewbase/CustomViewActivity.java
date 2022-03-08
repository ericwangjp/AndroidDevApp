package com.example.androiddevapp.viewbase;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androiddevapp.databinding.ActivityCustomViewBinding;
import com.example.androiddevapp.utils.AppUtils;

public class CustomViewActivity extends AppCompatActivity {

    private ActivityCustomViewBinding binding;
    private static final String TAG = "CustomViewActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initData();
    }


    private void initData() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1).setDuration(2000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedFraction = valueAnimator.getAnimatedFraction();
                binding.tvView2.scrollTo(((int) (20 * animatedFraction)), ((int) (20 * animatedFraction)));
            }
        });
        binding.btnMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) binding.tvView1.getLayoutParams();
                layoutParams.width += 100;
                layoutParams.rightMargin += 100;
                binding.tvView1.setLayoutParams(layoutParams);
//                binding.tvView1.requestLayout();
            }
        });

        binding.btnMove2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.customView1.scrollBy(-20, -20);
            }
        });

        binding.btnMove3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                valueAnimator.start();
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        TextView tvView1 = binding.tvView1;
        tvView1.setTranslationX(30);
        Log.w(TAG, "getLeft: " + AppUtils.px2dip(this, tvView1.getLeft()));
        Log.w(TAG, "getRight: " + AppUtils.px2dip(this, tvView1.getRight()));
        Log.w(TAG, "getTop: " + AppUtils.px2dip(this, tvView1.getTop()));
        Log.w(TAG, "getBottom: " + AppUtils.px2dip(this, tvView1.getBottom()));
        Log.w(TAG, "getX: " + AppUtils.px2dip(this, tvView1.getX()));
        Log.w(TAG, "getY: " + AppUtils.px2dip(this, tvView1.getY()));
        Log.w(TAG, "getTranslationX: " + AppUtils.px2dip(this, tvView1.getTranslationX()));
        Log.w(TAG, "getTranslationY: " + AppUtils.px2dip(this, tvView1.getTranslationY()));
        Log.w(TAG, "getTranslationZ: " + AppUtils.px2dip(this, tvView1.getTranslationZ()));
        Log.w(TAG, "width: " + AppUtils.px2dip(this, (tvView1.getRight() - tvView1.getLeft())));
        Log.w(TAG, "height: " + AppUtils.px2dip(this, (tvView1.getBottom() - tvView1.getTop())));
    }
}