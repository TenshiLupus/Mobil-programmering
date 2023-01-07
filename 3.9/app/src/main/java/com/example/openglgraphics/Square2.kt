package com.example.openglgraphics

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

const val COORDS_PER_VERTERX = 3
val squareCoords = floatArrayOf(
    -0.5f, 0.5f, 0.0f, //Top left
    -0.5f, 0.5f, 0.0f, //bottom left
    0.5f, -0.5f, 0.0f, // bottom right
    0.5f, 0.5f, 0.0f // top right
)

class Square2 {
    private val drawOrder = shortArrayOf(0,1,2,0,2,3)

    private val vertexBuffer : FloatBuffer = ByteBuffer.allocateDirect(squareCoords.size * 4).run {
        order(ByteOrder.nativeOrder())
        asFloatBuffer().apply {
            put(squareCoords)
            position(0)
        }
    }

    //draw the fragment rendering pass
    private val drawListBuffer : ShortBuffer = ByteBuffer.allocateDirect(drawOrder.size * 2).run {
        ByteBuffer.allocateDirect(drawOrder.size * 2).run {
            order(ByteOrder.nativeOrder())
            asShortBuffer().apply {
                put(drawOrder)
                position(0)
            }
        }
    }
}