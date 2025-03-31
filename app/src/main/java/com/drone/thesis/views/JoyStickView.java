package com.drone.thesis.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class JoyStickView extends View {
    private float centerX, centerY; // Center position of joystick
    private float knobX, knobY; // Current knob position
    private float radius, knobRadius;
    private Paint outerCirclePaint, knobPaint;
    private JoystickListener listener;

    public JoyStickView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        outerCirclePaint = new Paint();
        outerCirclePaint.setColor(Color.GRAY);
        outerCirclePaint.setStyle(Paint.Style.FILL);

        knobPaint = new Paint();
        knobPaint.setColor(Color.RED);
        knobPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w / 2f;
        centerY = h / 2f;
        radius = Math.min(w, h) / 3f;
        knobRadius = radius / 2.5f;
        resetJoystick();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Draw outer circle
        canvas.drawCircle(centerX, centerY, radius, outerCirclePaint);
        // Draw joystick knob
        canvas.drawCircle(knobX, knobY, knobRadius, knobPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float dx = event.getX() - centerX;
        float dy = event.getY() - centerY;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if (distance < radius) {
                    knobX = event.getX();
                    knobY = event.getY();
                } else {
                    float angle = (float) Math.atan2(dy, dx);
                    knobX = centerX + radius * (float) Math.cos(angle);
                    knobY = centerY + radius * (float) Math.sin(angle);
                }
                if (listener != null) {
                    listener.onJoystickMoved((knobX - centerX) / radius, (knobY - centerY) / radius);
                }
                break;

            case MotionEvent.ACTION_UP:
                resetJoystick();
                if (listener != null) {
                    listener.onJoystickMoved(0, 0);
                }
                break;
        }
        invalidate();
        return true;
    }

    private void resetJoystick() {
        knobX = centerX;
        knobY = centerY;
        invalidate();
    }

    public void setJoystickListener(JoystickListener listener) {
        this.listener = listener;
    }

    public interface JoystickListener {
        void onJoystickMoved(float xPercent, float yPercent);
    }
}
