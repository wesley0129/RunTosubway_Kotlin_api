package com.example.myreal_timeapi

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GyeonguiApi{
    @GET("api/subway/425171754a77657335354b726d7069/json/realtimeStationArrival/0/10/화전")
    fun changeEnd(): Call<Gyeongui>

}