package com.example.movieproject.utill

import android.content.SharedPreferences
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenInterceptor @Inject constructor(
) : Interceptor {
    var token: String? = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJkZTJlNzgxMzIwY2FlNjgyZDA5ZDMyZjFiNmU4ZjJmNCIsInN1YiI6IjYwNDdlOTY2OTBkZGUwMDA1YTEwOWIzNyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.44yuRzFn3ZSAExnpzWoyYXC1OmTph5jio8F-IBOXA-I"

    override fun intercept(chain: Interceptor.Chain): Response {

        val original = chain.request()

        if (original.url.encodedPath.contains("/login") && original.method.equals("post")
            || (original.url.encodedPath.contains("/register") && original.method.equals("post"))
        ) {
            return chain.proceed(original)
        }

        val originalHttpUrl = original.url
        val requestBuilder = original.newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .url(originalHttpUrl)

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}