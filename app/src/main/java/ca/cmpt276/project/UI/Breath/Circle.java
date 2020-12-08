package ca.cmpt276.project.UI.Breath;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/*
 * The Circle class provides the backbone of the circle animation
 * used in the "Take A Breath" activity.
 */
public class Circle extends View {

    private final Paint paint;


    private float x = 400;
    private float y = 300;
    private float radius = 0;
    private float savedRadius = 0;

    public Circle(Context context, AttributeSet attrs) {
        super(context, attrs);

        final int strokeWidth = 75;

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        paint.setColor(Color.GREEN);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(x, y, radius, paint);
    }

    public void setRadius(float newRadius){
        this.radius = newRadius;
    }

    public float getRadius() {
        return radius;
    }

    public void swapColor(Boolean isInhale){
        if(isInhale){
            paint.setColor(Color.GREEN);
        } else {
            paint.setColor(Color.BLUE);
        }
    }

    public void saveRadius() {
        savedRadius = radius;
    }

    public float getSavedRadius() {
        return savedRadius;
    }
}
