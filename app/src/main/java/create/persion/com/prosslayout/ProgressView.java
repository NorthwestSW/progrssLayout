package create.persion.com.prosslayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Fj on 2018/1/8 0008.
 */

public class ProgressView extends View {
    private Context mContext;
    //背景圆的颜色
    private int mCircleBackColor;
    private Paint circlePaint;
    //控件的宽高
    private int mWidth;
    private int mHeight;
    //控件的真实的半径
    private int mCircleRadiou;
    //确定的控件尺寸
    private int mRadiou;
    //进度条的颜色
    private int mProgressColor;
    //进度条的宽度
    private int mProgressWidth;
    //进度条的进度值
    private float mProgress;
    private Paint progressPaint;
    //进度条的角度
    private double sweepProgress;


    public ProgressView(@NonNull Context context) {
        this(context, null);
    }

    public ProgressView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
        initPaint();
    }

    private void initPaint() {
        //初始化背景图画笔
        circlePaint = new Paint();
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setAntiAlias(true);
        circlePaint.setDither(true);
        circlePaint.setColor(mCircleBackColor);
        circlePaint.setStrokeWidth(mProgressWidth);

        //初始化圆弧画笔
        progressPaint = new Paint();
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);
        progressPaint.setAntiAlias(true);
        progressPaint.setDither(true);
        progressPaint.setColor(mProgressColor);
        progressPaint.setStrokeWidth(mProgressWidth);


    }

    private void initView(Context context, AttributeSet attrs) {
        this.mContext = context;
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ProgressView);
        mCircleBackColor = array.getColor(R.styleable.ProgressView_circleBackColor, mCircleBackColor);
        mProgressColor = array.getColor(R.styleable.ProgressView_progressColor, mProgressColor);
        mProgressWidth = (int) array.getDimension(R.styleable.ProgressView_progressWidth, dip2px(context, mProgressColor));
        mProgress = array.getInt(R.styleable.ProgressView_progress, 0);
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //获取当前控件的测量模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        //获取当前控件的宽高
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        //确定宽度
        if (MeasureSpec.AT_MOST == widthMode) {
            widthSize = getResources().getDisplayMetrics().widthPixels;
            mWidth = widthSize;
        } else if (MeasureSpec.EXACTLY == widthMode) {
            mWidth = widthSize;
        } else {
            mWidth = widthSize;
        }
        //确定高度
        if (MeasureSpec.AT_MOST == heightMode) {
            heightSize = getResources().getDisplayMetrics().heightPixels;
            mHeight = heightSize;
        } else if (MeasureSpec.EXACTLY == heightMode) {
            mHeight = heightSize;
        } else {
            mHeight = heightSize;
        }
        //取最小的的尺寸控件的尺寸，
        mRadiou = Math.min(mWidth, mHeight);
        //求取半径
        mCircleRadiou = mRadiou / 2;
        //设置控件的的半径
        setMeasuredDimension(mRadiou, mRadiou);
    }


    protected void onDraw(Canvas canvas) {
        int prowidth = mProgressWidth / 2;
        //画背景图
//        canvas.drawCircle(mCircleRadiou, mCircleRadiou, mCircleRadiou, circlePaint);
        RectF rectC = new RectF(prowidth, prowidth, mRadiou - prowidth, mRadiou - prowidth);
        canvas.drawArc(rectC, -90, 360, false, circlePaint);
        //画进度条是个圆弧

        sweepProgress = mProgress * 3.6;
        //解决边界冲突问题因为在画圆的时候有画笔有他的宽度。默认是以画笔的的心开始画，所以要进行边界处理.

        RectF rectF = new RectF(prowidth, prowidth, mRadiou - prowidth, mRadiou - prowidth);
        canvas.drawArc(rectF, 90, (float) sweepProgress, false, progressPaint);


    }

    //设置进度值
    public void setProgress(float progress) {
        if (progress <= 100) {
            this.mProgress = progress;
            //请求立即刷新界面重新执行ondraw（）方法；
            postInvalidate();
        }

    }

    /**
     * 根据手机分辨率从DP转成PX
     *
     * @param context
     * @param dpValue
     * @return
     */
    public int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @return
     */
    public int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 根据手机的分辨率PX(像素)转成DP
     *
     * @param context
     * @param pxValue
     * @return
     */
    public int px2dip(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @return
     */

    public int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }
}
