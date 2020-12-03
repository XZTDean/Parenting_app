package ca.cmpt276.project.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;



public class Circle extends View {

    private static final int START_ANGLE_POINT = 90;

    private final Paint paint;
    //private final RectF rect;

    private float angle;

    private float x = 360;
    private float y = 250;
    private float radius = 0;

    public Circle(Context context, AttributeSet attrs) {
        super(context, attrs);

        final int strokeWidth = 75;

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        //Circle color
        paint.setColor(Color.GREEN);

        //size 200x200 example


        //Initial Angle (optional, it can be zero)
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(x, y, radius, paint);
        //canvas.drawArc(rect, START_ANGLE_POINT, angle, false, paint);
    }

    public void setRadius(float newRadius){
        this.radius = newRadius;
    }

    public float getRadius() {
        return radius;
    }
}
/*
import android.animation.ObjectAnimator;
import android.util.Property;

public class Circle {
    public static final Property<Circle, Float> GROW_FACTOR = new Property<Circle, Float>(
            Float.class, "growFactor") {
        @Override
        public void set(Circle object, Float value) {
            object.setGrowGfactor(value);
        }

        @Override
        public Float get(Circle object) {
            return object.getGrowFactor();
        }
    };

    private float mGrowFactor = 0f;

    public void setGrowGfactor(float grow) {
        mGrowFactor = grow;
        //invalidate the view to cause a redraw
        //in the onDraw() use the mGrowFactor to calculate the new radius
    }

    public float getGrowFactor() {
        return mGrowFactor;
    }

    public void startGrowAnimation() {
        ObjectAnimator anim = ObjectAnimator.ofFloat(this, GROW_FACTOR, 1f);
        anim.setDuration(3000); //make animation 3 seconds long
        anim.start();
    }
}*/