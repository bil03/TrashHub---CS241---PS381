package com.neisha.trashhub.data.pref

import com.neisha.trashhub.data.retrofit.ApiService
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection

class TokenInterceptor(
    private var token: String?,
    private val userId: String?,
    private val apiService: ApiService
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        var response = chain.proceed(addAuthorizationHeader(request))

        if (response.code == HttpURLConnection.HTTP_UNAUTHORIZED) {
            val newToken = runBlocking {
                refreshAuthToken()
            }
            if (newToken != null) {
                response.close()
                request = addAuthorizationHeader(request.newBuilder().build(), newToken)
                response = chain.proceed(request)
            }
        }
        return response
    }

    private fun addAuthorizationHeader(request: Request, token: String? = this.token): Request {
        return token?.let {
            request.newBuilder()
                .header("Authorization", "Bearer $it")
                .build()
        } ?: request
    }

    private suspend fun refreshAuthToken(): String? {
        return try {
            val currentToken = this.token ?: return null
            val response = apiService.refreshToken("Bearer $currentToken", userId ?: "")
            if (!response.error!!) {
                val newToken = response.token
                updateToken(newToken)
                newToken
            } else {
                null
            }
        } catch (e: HttpException) {
            null
        }
    }

    @Synchronized
    private fun updateToken(token: String?) {
        this.token = token
    }
}
