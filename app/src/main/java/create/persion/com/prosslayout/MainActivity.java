package create.persion.com.prosslayout;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout bigView;
    private ProgressView progre;
    private CometView cometView;
    private ValueAnimator valueAnimator;
    private ValueAnimator viewAn;
    private int measuredWidth;
    private int measuredHeight;
    private Button startBtn;
    private boolean isflag =false;
    private ProgressLayout progressLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressLayout = ((ProgressLayout) findViewById(R.id.prossOrgnize));
        bigView= ((RelativeLayout) findViewById(R.id.bigView));
        startBtn = ((Button) findViewById(R.id.startBtn));
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isflag) {

                    //此处可能会报错，可以定义一个接口判断动画是否执行完毕。
                    progressLayout.startProgressAnimaor(80);
                    isflag = true;
                }else{
                    isflag=false;
                    return;
                }

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
//

    }




    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
