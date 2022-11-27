package com.example.myreal_timeapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    var subwayUp =  mutableMapOf<Int, TimeTo>()
    var subwayDown =  mutableMapOf<Int, TimeTo>()
    var offUp =  mutableMapOf<Int, TimeTo>()
    var offDown =  mutableMapOf<Int, TimeTo>()
    var isOff = 0
    private var upCount = 0
    private var downCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadTimeInfo(1)
    }

    private fun loadTimeInfo(day: Int) {
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
                callJson(day)
            }
        })
    }

    fun callJson(day: Int){         //assets에 있는 시간표 데이터 json을 파싱해서 저장
        var jsonString: String = ""
        when(day) {
            1 -> jsonString = assets.open("WeekDay.json").reader().readText()
            2 -> jsonString = assets.open("Saturday.json").reader().readText()
            3 -> jsonString = assets.open("WeekDay.json").reader().readText()
        }
        val jsonArray = JSONArray(jsonString)
        upCount = 0
        downCount = 0

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)

            val dest = jsonObject.getString("dest")
            val arriveTime = jsonObject.getString("arvTm")

            val timeTo = TimeTo(dest, "", "", arriveTime.toInt())

            if(dest == "상행") {
                offUp.put(upCount, timeTo)
                Log.d("MainActivity", "${offUp[upCount]!!.destination} ${offUp[upCount]!!.arvTm}")
                upCount++
            }
            else {
                offDown.put(downCount, timeTo)
                Log.d("MainActivity", "${offDown[downCount]!!.destination} ${offDown[downCount]!!.arvTm}")
                downCount++
            }
        }
    }

    fun dataSave(body: ArrayList<Body>) {  //메인 액티비티에서 loadTimeInfo 함수를 실행시켰다면 body에 data가 들어있음.
        upCount = 0     //실행시점 마다 열차 개수 다르므르 일단 0으로 초기화, 보낼때도 사용해야함.
        downCount = 0
        for(i in body.indices){
            val array = splitString(body[i].arvlMsg2, body[i].trainLineNm)
            val timeTo = TimeTo(body[i].updnLine, array[0], array[1])
            when(body[i].updnLine){
                "상행" -> {
                    subwayUp.put(upCount, timeTo)
                    upCount++
                    Log.d("MainActivity", "방향: ${subwayUp[upCount]!!.destination}, 메시지: ${subwayUp[upCount]!!.Msg2}" +
                            ", 방면: ${subwayUp[upCount]!!.lineNm}"
                    )
                }
                "하행" -> {
                    subwayDown.put(downCount, timeTo)
                    downCount++
                    Log.d("MainActivity", "방향: ${subwayDown[upCount]!!.destination}, 메시지: ${subwayDown[upCount]!!.Msg2}" +
                            ", 방면: ${subwayDown[upCount]!!.lineNm}"
                    )
                }
            }
        }
        Log.d("MainActivity", "상행: ${upCount}개 , 하행: ${downCount}개 저장")
    }

    private fun splitString(str1: String, str2: String):Array<String> {
        val returnArr:Array<String> = arrayOf<String>("","")
        if(str1.length > 8){
            val targetNum = str1.split("[", "]")
            val arvlMsg = targetNum[1] + "번째 전역"
            returnArr[0] = arvlMsg
        }
        else {
            returnArr[0] = str1
        }
        val targetStr = str2.split(" - ", "방면")
        val trainLine = targetStr[0] + targetStr[2]
        returnArr[1] = trainLine

        return returnArr
    }
}


