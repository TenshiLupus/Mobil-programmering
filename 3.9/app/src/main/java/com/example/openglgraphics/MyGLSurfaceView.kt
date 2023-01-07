package com.example.openglgraphics

import android.content.Context
import android.opengl.GLSurfaceView
import android.view.MotionEvent

private const val TOUCH_SCALE_FACTOR: Float = 180.0f / 320f

class MyGLSurfaceView(context: Context) : GLSurfaceView(context) {
    private val renderer: MyGLRenderer
    private var previousX: Float = 0f
    private var previousY: Float = 0f

    init {
        setEGLContextClientVersion(2)
        renderer = MyGLRenderer()
        setRenderer(renderer)
        renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {

        val x : Float = e.x
        val y : Float = e.y

        when (e.action){
            MotionEvent.ACTION_MOVE -> {
                var dx : Float = x - previousX
                var dy : Float = y - previousY

                //reverse direction of rotation above the midline
                if(y > height / 2){
                    dx *= -1
                }

                //reverse direction of rotation to the left of the midline
                if(x < width / 2){
                    dy *= -1
                }

                renderer.angle += (dx + dy) * TOUCH_SCALE_FACTOR
                requestRender()
            }
        }

        previousX = x
        previousY = y
        return true
    }
}