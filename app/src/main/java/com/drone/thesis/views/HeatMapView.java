package com.drone.thesis.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class HeatMapView extends View {
    private float[][] thermalData;
    private Paint paint = new Paint();

    public HeatMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void updateHeatMap(float[][] newThermalData) {
        this.thermalData = newThermalData;
        invalidate(); // Redraw the view
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (thermalData == null) return;

        int rows = thermalData.length;
        int cols = thermalData[0].length;
        float cellWidth = getWidth() / (float) cols;
        float cellHeight = getHeight() / (float) rows;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                paint.setColor(getColorForTemperature(thermalData[i][j]));
                canvas.drawRect(j * cellWidth, i * cellHeight, (j + 1) * cellWidth, (i + 1) * cellHeight, paint);
            }
        }
    }

    private int getColorForTemperature(float temp) {
        int minTemp = 20;  // Adjust based on your sensor's range
        int maxTemp = 40;

        float ratio = (temp - minTemp) / (maxTemp - minTemp);
        ratio = Math.max(0, Math.min(1, ratio)); // Clamp to [0,1]

        int red = (int) (255 * ratio);
        int blue = (int) (255 * (1 - ratio));
        return Color.rgb(red, 0, blue);
    }

}
