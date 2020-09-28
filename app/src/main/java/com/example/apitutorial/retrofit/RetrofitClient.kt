package com.example.apitutorial.retrofit

import android.util.Log
import com.example.apitutorial.utils.API
import com.example.apitutorial.utils.Constants.TAG
import com.example.apitutorial.utils.isJsonArray
import com.example.apitutorial.utils.isJsonObject
import okhttp3.Interceptor
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception


// kotlin에서 object는 싱글턴
object RetrofitClient {
    //레트로핏 클라이언트 선언

    //이 둘중에 하나 형태로 작성하면됨
    private var retrofitClient : Retrofit?=null
//    private lateinit var retrofitClient : Retrofit

    //레트로핏 클라이언트 가져오기
    fun getClient(baseUrl:String):Retrofit?{
        Log.d(TAG, "RetrofitClient - getClient: called")


        //로깅 인터셉터 추가
        //okhttp 인스턴스 생성
        val client = OkHttpClient.Builder()

        //로그를 찍기 위해 로깅 인터셉터 설정
        val loggingInterceptor= HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Log.d(TAG, "RetrofitClient - log() called / message: $message")

                when {
                    message.isJsonObject() -> {
//                        indentSpace는 들여쓰기같은거..?
                        Log.d(TAG, JSONObject(message).toString(4))
                    }
                    message.isJsonArray() -> {
//                        indentSpace는 들여쓰기같은거..?
                        Log.d(TAG, JSONArray(message).toString(4))
                    }
                    else -> {
                        try {
                            Log.d(TAG, JSONObject(message).toString(4))
                        } catch (e: Exception) {
                            Log.d(TAG, message)
                        }
                    }

                }
            }

        })

        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        //위에서 설정한 로깅 인터셉터를 okhttp 클라이언트에 추가한다.
        client.addInterceptor(loggingInterceptor)

        //기본 파라메터 인터셉터 설정
        val baseParameterInterceptor:Interceptor = (object :Interceptor{
            override fun intercept(chain: Interceptor.Chain): Response {
                Log.d(TAG, "RetrofitClient - intercept() called")
                //오리지널 리퀘스트
                val originalRequest = chain.request()

                //쿼리 리퀘스트
                //?client_id="!@#@!#WQE@$!%" 부분을 넣어주는 과정
                val addedUrl = originalRequest.url.newBuilder().addQueryParameter("client_id",API.CLIENT_ID).build()

                val finalRequest = originalRequest.newBuilder().url(addedUrl).method(originalRequest.method,originalRequest.body).build()

                return chain.proceed(finalRequest)
            }

        })

        //위에서 설정한 기본파라메터 인터셉터를 okhttp 클라이언트에 추가한다.
        client.addInterceptor(baseParameterInterceptor)


    // 레트로핏 클라이언트 없으면
        if(retrofitClient==null){
            //레트로핏 빌더를 통해 인스턴스 생성
            // build 패턴은 디자인패턴 중 객체 생성 관련 패턴으로 Retrofit.Builder().옵션.옵션.옵션....build() 마지막 build() 하면 객체 생성됨
            retrofitClient=Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                //위에서 설정한 클라이언트로 레트로핏 클라이언트를 설정한다.
                .client(client.build())

                .build()
        }
        //만들어진 객체 리턴
        return retrofitClient
    }

}