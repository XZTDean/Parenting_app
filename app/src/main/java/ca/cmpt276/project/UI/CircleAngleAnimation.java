package ca.cmpt276.project.UI;

import android.view.animation.Animation;
import android.view.animation.Transformation;

/*
 * CircleAngleAnimation puts the Circle class in motion.
 * This is the animation eventually used in the "Take A Breath"
 * activity.
 */
public class CircleAngleAnimation extends Animation {

    private Circle circle;

    private float oldRadius;
    private float newRadius;

    public CircleAngleAnimation(Circle circle, float newRadius) {
        this.oldRadius = circle.getRadius();
        this.newRadius = newRadius;
        this.circle = circle;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation transformation) {
        float radius = oldRadius + ((newRadius - oldRadius) * interpolatedTime);

        circle.setRadius(radius);
        circle.requestLayout();
    }

    public void pause(){
        circle.setRadius(circle.getRadius());
        circle.requestLayout();
    }


}