package com.example.myapplication

//import java.security.SecureRandom
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.TripGen.TripGen
import com.example.myapplication.storage.AppPreferences
import kotlinx.coroutines.*
import java.util.*


class MainActivity : AppCompatActivity() {

    private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9') + '\'' + '@' + '|' + '[' + ';' + '\\' + '/' + '[' + ']' + '=' + '{' + '}' + '.' + ':' + '~' + '*' + '?' + '$' + '_' + '^' + '-' + '!' + '%' + ','

    var editTripEnter: EditText? = null
    var generatedTripcodes: TextView? = null
    var pref: AppPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        pref = AppPreferences(this)


        val btnStart = findViewById<Button>(R.id.btn_start)
        val btnReset = findViewById<Button>(R.id.btn_reset)
        val btnStop = findViewById<Button>(R.id.btn_stop)
        val btnExit = findViewById<Button>(R.id.btn_exit)
        editTripEnter = findViewById<EditText>(R.id.edit_trip_enter)
        generatedTripcodes = findViewById<TextView>(R.id.trips)
        updateGeneratedTripcodes()


        btnStart.setOnClickListener(this::onBtnStartGenClick)
        btnReset.setOnClickListener(this::onBtnResetClick)
        btnStop.setOnClickListener(this::onBtnStopGenClick)
        btnExit.setOnClickListener(this::onBtnExitClick)
    }

    private fun onBtnStartGenClick(view: View){
//        var reqPat: String = editTripEnter?.getText().toString()
//        var reqPatArr: Array<String> = reqPat.map{it.toString()}.toTypedArray()
//        var test: String = TripGen.tripGen(reqPatArr)
        pref?.saveStop(false)
        pref?.saveRequestedPattern(editTripEnter?.getText().toString())
        mine()
    }

    private fun onBtnResetClick(view: View){
        pref?.resetTrips()
        updateGeneratedTripcodes()
    }

    private fun onBtnStopGenClick(view: View){
        pref?.saveStop(true)
    }

    private fun onBtnExitClick(view: View){
        System.exit(0)
    }

    private fun updateGeneratedTripcodes(){
        generatedTripcodes?.setText(pref?.getTrips())
    }

    fun mine(){
        GlobalScope.launch {
        val reqPat = pref?.getRequestedPattern()?.toRegex()
        while (pref?.getStop() != true){
            val testTrip = TripGen.tripGen(arrayOf<String>(randStr()))
            val tripOnlyPart = testTrip.substring(13)
            if (reqPat!!.containsMatchIn(tripOnlyPart)){
                pref?.addToTripList("${pref?.getTrips()}" + "\n" + testTrip)
                runBlocking {
                    runOnUiThread(java.lang.Runnable {
                        updateGeneratedTripcodes()
                    })
                    delay(10)
            }
            }
        }
        }
    }

    fun randStr(): String {
        val random = SplittableRandom()
        val bytes = ByteArray(10)
        random.nextBytes(bytes)

        val randomString = (0..bytes.size - 1)
                .map { i -> charPool[random.nextInt(charPool.size)] }.joinToString("")
        return randomString
    }
}

fun SplittableRandom.nextBytes(bytes: ByteArray) {
    var i = 0
    val len = bytes.size
    var words = len shr 3
    while (words-- > 0) {
        var rnd: Long = nextLong()
        var n = 8
        while (n-- > 0) {
            bytes[i++] = rnd.toByte()
            rnd = rnd ushr java.lang.Byte.SIZE
        }
    }
    if (i < len) {
        var rnd: Long = nextLong()
        while (i < len) {
            bytes[i++] = rnd.toByte()
            rnd = rnd ushr java.lang.Byte.SIZE
        }
    }
}

