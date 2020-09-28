package com.example.apitutorial.retrofit

import com.example.apitutorial.utils.API
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitInterface {

    //https://www.unsplash.com/search/photos/?query="{매개변수}"

    @GET(API.SEARCH_PHOTOS)
    //                                      매개변수부분          반환값부분
    fun searchPhotos(@Query("query") searchTerm:String) : Call<JsonElement>

    @GET(API.SEARCH_USERS)
    fun searchUsers(@Query("query") searchTerm: String) : Call<JsonElement>

}