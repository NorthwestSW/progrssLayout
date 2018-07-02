package create.persion.com.prosslayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Fj on 2018/6/29 0029.
 */
public class CometView extends RelativeLayout {
    // 控件的宽高
    private int mWidth, mHeight;
    // 图片的宽高
    private int mDrawableWidth, mDrawableHeight;
    //图片集合
    private List<Drawable> mDrawAbleList;
    //要产生多少个view
    private int viewNum = 1;
    //随机数
    private Random random;
    private boolean isintercept = false;
    private long updateTime = 100;
    private int stopValue = 0x1;
    private int startValue = 0x2;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x2:
                    createLargeView();
                    break;
                case 0x1:
                    createLargeView();
                    break;
            }
        }
    };
    private double minWidth;

    public CometView(Context context) {
        this(context, null);
    }

    public CometView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CometView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        random = new Random();
        //默认的drawable
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.pl_blue);
        Drawable redDrawable = ContextCompat.getDrawable(context, R.drawable.my_shape);
        mDrawAbleList = new ArrayList<>();
        mDrawAbleList.add(drawable);
        mDrawAbleList.add(redDrawable);
        //获取图片的固有宽高得到的是dp单位
        mDrawableHeight = drawable.getIntrinsicHeight();
        mDrawableWidth = drawable.getIntrinsicWidth();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获取控件的宽高
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        minWidth = mWidth / 2.;
        mHeight = MeasureSpec.getSize(heightMeasureSpec);

    }

    public void startPlay(int num) {
        this.viewNum = num;
        this.isintercept = false;
        mHandler.sendEmptyMessage(startValue);
    }

    private void createLargeView() {
        if (!isintercept) {
            for (int i = 0; i < viewNum; i++) {
                addView();
            }
            mHandler.sendEmptyMessageDelayed(startValue, updateTime);
        } else {
            return;
        }

    }

    /**
     * 1.for循环添加生成view并随机从drawable集合中添加drawable；
     * 2. 开启动画（1.beizer动画，透明度动画。）
     * 3.动画边界的限制
     * 4.动画执行完毕。性能优化。
     * 5.测试
     * 6.采用handler，或者线程让动画轮询。
     * 7.向外部提供开关。用来开启或者关闭动画的方法。
     */
    //添加一个view
    private void addView() {
        final ImageView imageView = new ImageView(getContext());
        //给imageView添加drawable；
        int minList = mDrawAbleList.size();
        if (minList > 0) {
            int num = Math.abs(random.nextInt() % mDrawAbleList.size());
            imageView.setImageDrawable(mDrawAbleList.get(num));
            // 怎么添加左边中心位置呢？ LayoutParams
            LayoutParams params
                    = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(ALIGN_PARENT_LEFT);
            params.addRule(CENTER_VERTICAL);
            imageView.setLayoutParams(params);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            addView(imageView);


            //开启动画
            AnimatorSet animatorSet = startAnimators(imageView);
            animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    removeView(imageView);
                }
            });
            animatorSet.start();
        } else {
            throw new IllegalStateException("drawable集合的size必须是大于等于1的");
        }
    }

    private AnimatorSet startAnimators(ImageView viewIv) {

        AnimatorSet allAnimatorSet = new AnimatorSet();// 待会再用
        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(viewIv, "alpha", 0.3f, 1.0f);
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(viewIv, "scaleX", 0.3f, 1.0f);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(viewIv, "scaleY", 0.3f, 1.0f);

        animatorSet.playTogether(alphaAnimator, scaleXAnimator, scaleYAnimator);
        animatorSet.setDuration(100);

        Animator beizerAnimator = addBeizerAnimator(viewIv);

        // 运行的路径动画  playSequentially 按循序执行
        allAnimatorSet.playSequentially(animatorSet, beizerAnimator);

        return allAnimatorSet;
    }

    private Animator addBeizerAnimator(final ImageView iv) {
        // 怎么确定四个点
        //PointF point0 = new PointF(((float) minWidth), (mHeight - mDrawableHeight) / 2);
        PointF point0 = new PointF(0, (mHeight - mDrawableHeight) / 2);
       // PointF point0 = getPointOne();
        // 确保 p2 点的 y 值 一定要大于 p1 点的 y 值
        PointF point1 = getPoint(1);
        PointF point2 = getPoint(2);
        PointF point3 = new PointF(mWidth - mDrawableWidth, random.nextInt(mHeight) - mDrawableHeight);

        BeizerEvaluator beizerEvaluator = new BeizerEvaluator(point1, point2);
        // ofFloat  第一个参数 LoveTypeEvaluator 第二个参数 p0, 第三个是 p3
        ValueAnimator bezierAnimator = ObjectAnimator.ofObject(beizerEvaluator, point0, point3);
        bezierAnimator.setInterpolator(new AccelerateInterpolator());   //先慢后快的插值器
        bezierAnimator.setDuration(1000);
        bezierAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF pointF = (PointF) animation.getAnimatedValue();
                iv.setX(pointF.x);
                iv.setY(pointF.y);
                // 透明度
                float t = animation.getAnimatedFraction();
                iv.setAlpha(1 - t + 0.05f);
            }
        });
        return bezierAnimator;


    }

    private PointF  getPointOne() {
        return new PointF(random.nextInt(1) , random.nextInt(mHeight/10) );
    }

    //计算点的范围
    private PointF getPoint(int index) { // 1
       // return new PointF(random.nextInt(mWidth+(int) (minWidth / 2)) + (index - 1) * (int) (minWidth / 2), random.nextInt(mHeight) - mDrawableHeight);
        return new PointF(random.nextInt(mWidth/2) + (index - 1) * mWidth/2, random.nextInt(mHeight) - mDrawableHeight);
    }

    //从外部添加drawable对象集合
    public void addDrawable(List<Drawable> mList) {
        if (mList != null) {
            this.mDrawAbleList.clear();
            this.mDrawAbleList.addAll(mList);
        }
    }


    public void clearDrawable() {
        if (mDrawAbleList != null) {
            mDrawAbleList.clear();
            mDrawAbleList = null;
        }
    }


    public void stopPlay() {
        this.isintercept = true;
        mHandler.sendEmptyMessage(stopValue);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.isintercept = false;
        if (mHandler != null) {
            mHandler.removeMessages(startValue);
            mHandler.removeMessages(stopValue);
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        removeAllViews();
    }
}
