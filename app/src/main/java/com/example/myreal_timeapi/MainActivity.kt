package com.example.myreal_timeapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {
    var subwayUp =  mutableMapOf<Int, TimeTo>()
    private var upCount = 0
    var subwayDown =  mutableMapOf<Int, TimeTo>()
    private var downCount = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadTimeInfo()
    }

    private fun loadTimeInfo() {
        val call = GyeonguiObject.getApi.changeEnd()

        call.enqueue(object: Callback<Gyeongui> {
            override fun onResponse(call: Call<Gyeongui>, response: Response<Gyeongui>) {
                if(response.isSuccessful()){
                    //데이터 확인차 객체를 출력.
                    response.body()?.let{
                        Log.d("MainActivity",it.toString())

                        it.body.forEach{ data ->
                            Log.d("MainActivity", data.toString())
                        }
                    }
                    //api로 가져온 데이터를 상행, 하행으로 나누어서 저장.
                    dataSave(response.body()!!.body)

                }else{
                    return
                }
            }

            override fun onFailure(call: Call<Gyeongui>, t: Throwable) {
                Log.d("MainActivity", "ErrorMsg: $t")
            }
        })
    }

    fun dataSave(body: ArrayList<Body>) {  //메인 액티비티에서 loadTimeInfo 함수를 실행시켰다면 body에 data가 들어있음.
        upCount = 0     //실행시점 마다 열차 개수 다르므르 일단 0으로 초기화
        downCount = 0
        for(i in body.indices){
            val array = splitString(body[i].arvlMsg2, body[i].trainLineNm)
            val timeTo = TimeTo(body[i].updnLine, array[0], array[1])
            when(body[i].updnLine){
                "상행" -> {
                    subwayUp.put(i, timeTo)
                    upCount++
                    Log.d("MainActivity", "방향: ${subwayUp[i]!!.destination}, 메시지: ${subwayUp[i]!!.msg2}" +
                            ", 방면: ${subwayUp[i]!!.lineNm}"
                    )
                }
                "하행" -> {
                    subwayDown.put(i, timeTo)
                    downCount++
                    Log.d("MainActivity", "방향: ${subwayDown[i]!!.destination}, 메시지: ${subwayDown[i]!!.msg2}" +
                            ", 방면: ${subwayDown[i]!!.lineNm}"
                    )
                }
            }
        }
        Log.d("MainActivity", "상행: ${upCount}개 , 하행: ${downCount}개 저장")
    }

    fun splitString(str1: String, str2: String):Array<String> {
        val returnArr:Array<String> = arrayOf<String>("","")
        if(str1.length > 8){
            val targetNum = str1.split("[", "]")
            val arvlMsg = targetNum[1] + "번째 전역"
            returnArr[0] = arvlMsg
        }
        else{
            val arvlMsg = str1
            returnArr[0] = arvlMsg
        }
        val targetStr = str2.split(" - ", "방면")
        val trainLine = targetStr[0] + "-" + targetStr[1]
        returnArr[1] = trainLine

        return returnArr
    }
}

class TimeTo(dest: String, arvlMsg2:String, trainLineNm: String){
    val destination = dest
    val msg2 = arvlMsg2
    val lineNm = trainLineNm
}