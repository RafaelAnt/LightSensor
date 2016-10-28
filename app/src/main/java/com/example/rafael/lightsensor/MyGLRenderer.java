package com.example.rafael.lightsensor;


import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Rafael on 27/10/2016.
 */

public class MyGLRenderer implements GLSurfaceView.Renderer {

    public volatile float mAngle;
    private Line mLine;

    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    }

    public void onDrawFrame(GL10 unused) {
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
    }

    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    public float getAngle() {
        return mAngle;
    }

    public void setAngle(float angle) {
        mAngle = angle;
    }

    public void drawLine(float x, float y){
        float coords [] = {};
        float color [] = {1,0,0,0};
        mLine = new Line(coords ,color);
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
    }
}