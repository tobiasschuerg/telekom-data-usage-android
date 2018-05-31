package com.tobiasschuerg.telekom.backend

import kotlinx.coroutines.experimental.Deferred
import retrofit2.Response
import retrofit2.http.GET

interface TelekomApi {

    companion object {
        const val API_BASE_URL = "http://pass.telekom.de/api/service/generic/v1/"
    }

    @GET("status")
    fun getStatus(): Deferred<Response<StatusDto>>

}