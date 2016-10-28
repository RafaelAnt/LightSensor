package com.example.rafael.lightsensor;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class TouchScreenActivity extends Activity {


    private GLSurfaceView mGLView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGLView = new MyGLSurfaceView(this);
        setContentView(mGLView);


    }


}
