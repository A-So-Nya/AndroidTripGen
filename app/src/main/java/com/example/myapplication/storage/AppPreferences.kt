package com.example.myapplication.storage

import android.content.Context
import android.content.SharedPreferences

class AppPreferences(ctx: Context) {
    var data: SharedPreferences = ctx.getSharedPreferences("APP_PREFERENCES", Context.MODE_PRIVATE)

    fun saveRequestedPattern(reqTrip: String){
        data.edit().putString("PATTERN", reqTrip).apply()
    }

    fun getRequestedPattern(): String?{
        return data.getString("PATTERN", "")
    }

    fun saveStop(stp: Boolean){
        data.edit().putBoolean("STOP", stp).apply()
    }

    fun getStop(): Boolean{
        return data.getBoolean("STOP", true)
    }

    fun addToTripList(gottenTrip: String){
        data.edit().putString("TRIPS", gottenTrip).apply()
    }

    fun getTrips(): String?{
        return data.getString("TRIPS", "")
    }

    fun resetTrips(){
        data.edit().putString("TRIPS", null).apply()
    }
}