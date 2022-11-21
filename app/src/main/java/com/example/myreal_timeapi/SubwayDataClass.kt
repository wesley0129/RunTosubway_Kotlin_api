package com.example.myreal_timeapi

import com.google.gson.annotations.SerializedName

data class Gyeongui(
    @SerializedName("realtimeArrivalList")val body: ArrayList<Body>
)

//Body 데이터 클래스 안에는 조회되는 열차수, 열차 방향, 열차노선 및 종착역 정보, 도착메세지([n]전역)(n만 파싱해서 비교할거임.)
data class Body (
    @SerializedName("totalCount")val totalCount: Int,
    @SerializedName("updnLine")val updnLine: String,
    @SerializedName("trainLineNm")val trainLineNm: String,
    @SerializedName("arvlMsg2")val arvlMsg2: String
)