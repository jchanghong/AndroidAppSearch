package com.jchanghong.appsearch.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class MydeleteButton extends View {
    public MydeleteButton(Context context) {
        super(context);
    }

    public MydeleteButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(100,100);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //调用父View的onDraw函数，因为View这个类帮我们实现了一些
        // 基本的而绘制功能，比如绘制背景颜色、背景图片等
        super.onDraw(canvas);
        int r = getMeasuredWidth();//也可以是getMeasuredHeight()/2,本例中我们已经将宽高设置相等了
        int r4 = r / 4;
        //圆心的横坐标为当前的View的左边起始位置+半径
        int centerX = getLeft() + r;
        //圆心的纵坐标为当前的View的顶部起始位置+半径
        int centerY = getTop() + r;

        Paint paint = new Paint();
        paint.setStrokeWidth(4);
        paint.setColor(Color.BLACK);
        canvas.drawLine(r4,r4,r-r4,r-r4,paint);
        canvas.drawLine(r4,r-r4,r-r4,r4,paint);
        //开始绘制
//        canvas.drawCircle(centerX, centerY, r, paint);


    }
}
