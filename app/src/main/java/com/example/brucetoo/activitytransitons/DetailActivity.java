package com.example.brucetoo.activitytransitons;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by brucetoo
 * On 15/4/9.
 */
public class DetailActivity extends ActionBarActivity {

    @InjectView(R.id.iv_detail_photo)
    ImageView mImageVIew;
    @InjectView(R.id.tv_desc_photo)
    TextView mTextView;
    @InjectView(R.id.rv_root)
    RelativeLayout mRoot;

    private int left, top, right, bottom;
    private float leftDelta, topDelta; // 大图片到小图片坐上点的距离
    private float rightScale, bottomScale; //大图片缩放到小图片的宽高缩放比

    private ColorDrawable rootColor; //background color

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.inject(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent intent = getIntent();
        left = intent.getIntExtra("left", 0);
        top = intent.getIntExtra("top", 0);
        right = intent.getIntExtra("right", 0);
        bottom = intent.getIntExtra("bottom",0);

        //设置背景色
        rootColor = new ColorDrawable(Color.BLACK);
        mRoot.setBackground(rootColor);

        //只有在该activity没有实例存在的时候才执行动画
        if (savedInstanceState == null) {
            mImageVIew.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    mImageVIew.getViewTreeObserver().removeOnPreDrawListener(this);
                    int[] screenLocation = new int[2];
                    mImageVIew.getLocationOnScreen(screenLocation);
                    leftDelta = left - screenLocation[0];
                    topDelta = top - screenLocation[1];

                    rightScale = (float)right / mImageVIew.getWidth();
                    bottomScale = (float)bottom / mImageVIew.getHeight();

                    Log.e("leftDelta:",leftDelta+"");
                    Log.e("topDelta:",topDelta+"");
                    Log.e("rightScale:",rightScale+"");
                    Log.e("bottomScale:",bottomScale+"");
                    runEnterAnimation();
                    return true;
                }
            });
        }
    }

    /**
     * 进入时的动画
     */
    private void runEnterAnimation() {
        //设置imageView 锚点为原点（默认是在中心点）
        mImageVIew.setPivotX(0);
        mImageVIew.setPivotY(0);
        //imageView上下移动的距离
        mImageVIew.setTranslationX(leftDelta);
        mImageVIew.setTranslationY(topDelta);
        //imageView宽高的缩放比
        mImageVIew.setScaleX(rightScale);
        mImageVIew.setScaleY(bottomScale);
        //字体的初态设置
        mTextView.setVisibility(View.VISIBLE);
        mTextView.setTranslationY(-100);
        mTextView.setAlpha(0);

        //imageView animation
        mImageVIew.animate().translationX(0).translationY(0).scaleX(1).scaleY(1)
                .setDuration(1000).setInterpolator(new DecelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() { //动画结束监听两种方式中的第一种AnimatorListenerAdapter
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mTextView.animate().translationY(0).alpha(1).
                                setDuration(1000).start();
                    }
                }).start();
        //动态改变背景颜色的透明度
        ObjectAnimator bgAnim = ObjectAnimator.ofInt(rootColor,"alpha",0,255);
        bgAnim.setDuration(1000);
        bgAnim.start();
    }

    public void runExitAnimation(final Runnable endAction){
          //退出时的动画就是进入的反动画
          mTextView.animate().translationY(-mTextView.getHeight()).alpha(0)
                  .setDuration(400)
                  .setListener(new AnimatorListenerAdapter() {
                      @Override
                      public void onAnimationEnd(Animator animation) {
                          mImageVIew.animate().translationX(leftDelta).translationY(topDelta)
                                  .scaleX(rightScale).scaleY(bottomScale)
                                  .setInterpolator(new AccelerateInterpolator())
                                  .setDuration(1000)
                                  .withEndAction(endAction) //结束的动画后的操作放在外面处理
                                  .start();

                          //change ColorDrawable value
                          ObjectAnimator bgAnim = ObjectAnimator.ofInt(rootColor,"alpha",255,0);
                          bgAnim.setDuration(1000);
                          bgAnim.start();
                      }
                  })
                  .start();
    }

    @Override
    public void onBackPressed() {
        //退出时的动画   动画结束监听两种方式中的第一种withEndAction--Runnable
        runExitAnimation(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        //屏蔽系统默认动画
        overridePendingTransition(0,0);
    }
}
