package unist.pjs.the.repository

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

var gson: Gson = GsonBuilder().setLenient().create()

fun provideRetrofit(): Retrofit =
    Retrofit.Builder()
        .baseUrl("https://api.theloveyouth.com")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addConverterFactory(NullOnEmptyConverterFactory.create())
        .build()