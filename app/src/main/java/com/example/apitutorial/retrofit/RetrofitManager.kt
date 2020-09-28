package com.example.apitutorial.retrofit

import android.util.Log
import com.example.apitutorial.utils.API
import com.example.apitutorial.utils.Constants.TAG
import com.example.apitutorial.utils.RESPOMSE_STATE
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.Response

class RetrofitManager {
    //싱글턴 적용되도록
    companion object {
        val instance = RetrofitManager()
    }

    //http call만들기
    //레트로핏 인터페이스 가져오기
    private val iRetrofit: RetrofitInterface? = RetrofitClient.getClient(API.BASE_URL)?.create(RetrofitInterface::class.java)

    //사진 검색 api 호출
    //요청이 끝나면 responsebody를 string으로 받아온 뒤 보내줌 이걸 completion으로 처리..?
    fun searchPhotos(searchTerm : String?, completion:(RESPOMSE_STATE,String)->Unit){

        val term=searchTerm.let { it }?:""

        val call= iRetrofit?.searchPhotos(searchTerm = term).let {
            it  // it(Call<JsomElement>)을 call에 넣어줌
        }?:return

//        val call= iRetrofit?.searchPhotos(searchTerm = term)?:return

//        enqueue 는 비동기식 사용
        call.enqueue(object : retrofit2.Callback<JsonElement> {
            //응답 성공시
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                Log.d(TAG, "RetrofitManager - onResponse() called / response:${response.body()}")

                //completion 발동
                completion(RESPOMSE_STATE.OKAY,response.body().toString())
            }
            //응답 실패시
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d(TAG, "RetrofitManager - onFailure() called / t: $t")

                completion(RESPOMSE_STATE.FAIL,t.toString())
            }

        })

    }

}