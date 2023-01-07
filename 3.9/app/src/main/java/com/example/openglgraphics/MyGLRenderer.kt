package com.example.openglgraphics

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.os.SystemClock
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MyGLRenderer : GLSurfaceView.Renderer {

    private val vPMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val rotationMatrix = FloatArray(16)


    private lateinit var mTriangle: Triangle
    private lateinit var mSquare: Square2

    @Volatile
    var angle : Float = 0f

    override fun onSurfaceCreated(unused: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0.0f, 0.0f,0.0f, 1.0f)
        mTriangle = Triangle()
        mSquare = Square2()
    }

    override fun onSurfaceChanged(unused: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0,0, width, height)

        val ratio : Float = width.toFloat() / height.toFloat()
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio,-1f,1f, 3f, 7f)
    }

    override fun onDrawFrame(p0: GL10?) {
        val scratch = FloatArray(16)

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        Matrix.setLookAtM(viewMatrix, 0, 0f , 0f, 3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)

        Matrix.multiplyMM(vPMatrix, 0 , projectionMatrix, 0, viewMatrix, 0)

        //val time = SystemClock.uptimeMillis() % 4000L
        //val angle = 0.090f * time.toInt()
        Matrix.setRotateM(rotationMatrix, 0, angle, 0f,0f,-1.0f)

        Matrix.multiplyMM(scratch, 0 , vPMatrix, 0, rotationMatrix, 0)
        mTriangle.draw(scratch)
    }

}