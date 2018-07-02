package create.persion.com.prosslayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fj on 2018/7/2 0002.
 */
public class ProgressLayout extends RelativeLayout {
    private final Context mContext;
    private CometView cometView;
    private RelativeLayout progressLayout;
    private ProgressView progressView;
    private int poointY;
    private ValueAnimator valueAnimator;
    private List<Drawable> mDrawList;
    private AnimatorSet animatorSet;
    private ObjectAnimator alphaAnimator;

    public ProgressLayout(Context context) {
        this(context, null);
    }

    public ProgressLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        addLayout();
    }

    private void addLayout() {
        if (progressLayout == null) {
            progressLayout = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.layout_progresslayout, null);
        }
        LayoutParams params = new LayoutParams(DensityUtil.dip2px(mContext,165), DensityUtil.dip2px(mContext,165) );
        params.addRule(CENTER_IN_PARENT);
        progressLayout.setLayoutParams(params);
        addView(progressLayout);

        mDrawList = new ArrayList<>();
        Drawable drawable1 = getResources().getDrawable(R.drawable.my_shape);
        Drawable drawable2 = getResources().getDrawable(R.drawable.pl_blue);
        Drawable drawable3 = getResources().getDrawable(R.drawable.lp_red);
        Drawable drawable4 = getResources().getDrawable(R.drawable.lp_yellow);
        mDrawList.add(0,drawable1);
        mDrawList.add(1,drawable2);
        mDrawList.add(2,drawable3);
        mDrawList.add(3,drawable4);
        findId();
    }

    private void findId() {
        progressView = ((ProgressView) progressLayout.findViewById(R.id.good_progress));
        cometView = ((CometView) progressLayout.findViewById(R.id.play));
        cometView.addDrawable(mDrawList);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = progressLayout.getMeasuredWidth();
        int measuredHeight = progressLayout.getMeasuredHeight();
        int width = cometView.getMeasuredWidth();
        int height = cometView.getMeasuredHeight();
        poointY = height - (measuredHeight / 2);  //计算旋转圆心的高度；


    }


    public void startProgressAnimaor(int progress) {
        if (animatorSet == null && valueAnimator == null && alphaAnimator == null) {
            animatorSet = new AnimatorSet();
            float value = (float) (progress *0.01);
            valueAnimator = ValueAnimator.ofFloat(0f, value);
            alphaAnimator = ObjectAnimator.ofFloat(cometView, "rotation", 0f, value * 360);
        }
        cometView.setPivotX(DensityUtil.dip2px(mContext,0f));
        cometView.setPivotY(poointY);
        animatorSet.setDuration(3000);
        animatorSet.playTogether(valueAnimator, alphaAnimator);
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                cometView.startPlay(50);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                cometView.stopPlay();
            }
        });
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedValue = (float) valueAnimator.getAnimatedValue();
                progressView.setProgress(animatedValue*100);
            }
        });
        animatorSet.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        alphaAnimator.cancel();
        valueAnimator.cancel();
        animatorSet.cancel();
        removeAllViews();
    }

}
