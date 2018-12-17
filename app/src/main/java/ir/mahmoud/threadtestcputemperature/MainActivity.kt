package ir.mahmoud.threadtestcputemperature

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.io.RandomAccessFile
import java.util.*

class MainActivity : AppCompatActivity() {

    val language:Language = Language.Kotlin
    var isStopped:Boolean = false
    var count = 8
    var threadList: MutableList<Thread> = ArrayList()
    var integerList: MutableList<Int> = ArrayList()
    var kotlinResult: String ="Empty"

    companion object
    {

        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        editText.setText(count.toString())
        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val result = s.toString()
                if(result.isNotEmpty())
                    count = result.toInt()
                else
                    count = 8
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })
    }

    fun startBtn(view: View){

        when(language){
            Language.Kotlin -> kotlin_start()
            Language.C -> c_start()
            Language.Java -> java_start()
        }

    }

    fun endBtn(view: View){
        when(language){
            Language.Kotlin -> kotlin_end()
            Language.C -> c_end()
            Language.Java -> java_end()
        }
    }

    fun getDataBtn(view: View){
        when(language){
            Language.Kotlin -> kotlin_getData()
            Language.C -> c_getData()
            Language.Java -> java_getData()
        }
    }

    fun temperatureBtn(view: View){
        getTemperature()
    }

    private fun getTemperature() {
        //az 3 ravesh bedast miad

        // cat sys/class/thermal/thermal_zone0/temp
        // cat sys/devices/virtual/thermal/thermal_zone0/temp
        // cat sys/class/hwmon/hwmonX/temp1_input

        var maxTemp = ""
        try {
            val reader = RandomAccessFile("/sys/class/thermal/thermal_zone1/temp", "r")

            var done = false
            while (!done) {
                val line = reader.readLine()
                if (null == line) {
                    done = true
                    break
                }
                maxTemp = line
            }

        } catch (ex: IOException) {
            ex.printStackTrace()
        }

        tempTextView.text = (maxTemp.toDouble() / 1000).toString()
    }


    /////////////////////  Kotlin  //////////////////////////////////

    fun kotlin_start(){
        isStopped = false
        initial()
        execute()
    }

    fun kotlin_end(){
        try {
            isStopped = true
            kotlinResult = ""


            for(i in 0 until count){
                threadList[i].interrupt()
            }

            for (i in 0 until count){
                kotlinResult += (i+1).toString()+"=> "+integerList[i]+"\n"
            }

        }
        catch (e:Exception){

        }
    }

    fun initial() {

        integerList.clear()
        threadList.clear()

        for (i in 0 until count)
            integerList.add(0)

        for (i in 0 until count){
            threadList.add(createMainThread(i))
        }
    }

    fun createMainThread(value: Int): Thread {

        return Thread(Runnable {
            while (true) {
                if (isStopped)
                    break
                else
                    integerList[value] = integerList[value] + 1
            }
        })
    }

    fun execute(){
        for(i in 0 until count){
            threadList[i].start()
        }
        Toast.makeText(this,"started",Toast.LENGTH_SHORT).show()
    }

    fun kotlin_getData(){
        sample_text.text = kotlinResult
    }


    /////////////////////  C    //////////////////////////////////////

    fun c_start(){
        createThread(count)
        Toast.makeText(this,"started",Toast.LENGTH_SHORT).show()
        // end thread at 20 seconds

//            Handler().postDelayed({
//                stopThread()
//                Toast.makeText(this,"ended",Toast.LENGTH_SHORT).show()
//            },20000)
    }

    fun c_end(){
        stopThread()
        Toast.makeText(this,"ended",Toast.LENGTH_SHORT).show()
    }

    fun c_getData(){
        sample_text.text = intFromJNI()
    }

    /////////////////////  JAVA    //////////////////////////////////////


    fun java_start(){
        Java.getInstance().isStopped = false
        Java.getInstance().initialList(count)
        Toast.makeText(this,"started",Toast.LENGTH_SHORT).show()

        Handler().postDelayed({
            java_end()
        },20000)
    }

    fun java_end(){
        Java.getInstance().stop()
        Toast.makeText(this,"ended",Toast.LENGTH_SHORT).show()
    }

    fun java_getData(){
        sample_text.text = Java.getInstance().getResult()
    }

    ///////////////////////////////////////////////////////////////////////////////////
    external fun stringFromJNI(): String
    external fun intFromJNI(): String
    external fun createThread(threadCount:Int)
    external fun stopThread()
}
