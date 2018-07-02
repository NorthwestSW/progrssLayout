package create.persion.com.prosslayout;

import android.animation.TypeEvaluator;
import android.graphics.PointF;

/**
 * Created by Fj on 2018/6/29 0029.
 * 自定义路径动画
 */
public class BeizerEvaluator implements TypeEvaluator<PointF> {
    private PointF point1,point2;
    public BeizerEvaluator(PointF point1,PointF point2){
        this.point1 = point1;
        this.point2  =point2;
    }
    @Override
    public PointF evaluate(float t, PointF point0, PointF point3) {
        // t 是 [0,1]
        //利用二街贝塞尔公式绘制点的路径
        PointF pointF = new PointF();
        pointF.x = point0.x*(1-t)*(1-t)*(1-t)
                + 3*point1.x*t*(1-t)*(1-t)
                + 3*point2.x*t*t*(1-t)
                + point3.x*t*t*t;

        pointF.y = point0.y*(1-t)*(1-t)*(1-t)
                + 3*point1.y*t*(1-t)*(1-t)
                + 3*point2.y*t*t*(1-t)
                + point3.y*t*t*t;

        return pointF;
    }
}
