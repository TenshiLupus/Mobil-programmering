package com.example.compassapp

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlin.math.sqrt


const val ALPHA : Float = 0.15f

class MainActivity : AppCompatActivity(), SensorEventListener{
    //Declare a sensor manager to obtain auxiliary data
    private lateinit var sensorManager : SensorManager


    private var sensor: Sensor? = null
    private val accelerometerReading = FloatArray(3)
    private val magnetometerReading = FloatArray(3)

    private val rotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

    }

    override fun onResume() {
        super.onResume()
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also { accelerometer ->
            sensorManager.registerListener(
             this,
                accelerometer,
                SensorManager.SENSOR_DELAY_GAME,
                SensorManager.SENSOR_DELAY_GAME
            )
        }

        sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)?.also { magneticField ->
            sensorManager.registerListener(
                this,
                magneticField,
                SensorManager.SENSOR_DELAY_GAME,
                SensorManager.SENSOR_DELAY_GAME
            )
        }
    }

    override fun onPause() {
        super.onPause()

        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER)
            //lowpasss filter
            System.arraycopy(event.values, 0, accelerometerReading, 0, accelerometerReading.size)
        else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD)
            System.arraycopy(event.values, 0, magnetometerReading, 0, magnetometerReading.size)
    }

    fun calculateheading(accelerometerReading: FloatArray, magnetometerReading: FloatArray) : Float{
        var Ax : Float = accelerometerReading[0]
        var Ay : Float = accelerometerReading[1]
        var Az : Float = accelerometerReading[2]

        var Ex : Float = magnetometerReading[0]
        var Ey : Float = magnetometerReading[1]
        var Ez : Float = magnetometerReading[2]

        var Hx : Float = Ey * Az - Ez * Ay
        var Hy : Float = Ez * Ax - Ex * Az
        var Hz : Float = Ex * Ay - Ey * Ax

        //normalize the values of gravity vector
        val invH : Float = 1.0f / sqrt((Hx * Hx + Hy * Hy + Hz * Hz).toDouble()) as Float

        Hx *= invH
        Hy *= invH
        Hz *= invH

        val invA = 1.0f / Math.sqrt((Ax * Ax + Ay * Ay + Az * Az).toDouble()) as Float

        Ax *= invA;
        Ay *= invA;
        Az *= invA;

        val Mx = Ay * Hz - Az * Hy;
        val My= Az * Hx - Ax * Hz;
        val Mz= Ax * Hy - Ay * Hx;

        return Math.atan2(Hy.toDouble(), My.toDouble()) as Float
    }

    fun convertRadtoDeg(rad :Float) : Float{
        return ((rad / Math.PI) * 180) as Float
    }

    fun map180to360(angle : Float) : Float{
        return (angle + 360) % 360
    }

    fun lowPassFilter(input : FloatArray, output : FloatArray) : FloatArray{
        if (output == null) return input

        for(i in 0 .. input.size - 1){
            output[i] = output[i] + ALPHA * (input[i] - output[i])
        }
        return output
    }

    fun calculateMagneticDeclination(latitude : Double, longitude : Boolean, altitude : Double){
        TSAGeoMag

    }


    fun updateOrientationAngles(){
        SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerReading, magnetometerReading)
        SensorManager.getOrientation(rotationMatrix, orientationAngles)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
}