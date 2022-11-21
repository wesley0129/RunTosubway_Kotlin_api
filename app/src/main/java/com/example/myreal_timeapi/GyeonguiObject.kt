package com.example.myreal_timeapi

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GyeonguiObject {
    private const val baseUrl = "http://swopenapi.seoul.go.kr/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


        //getApi는 Gyeongui_Api객체이다.
        val getApi = retrofit.create(GyeonguiApi::class.java)

}