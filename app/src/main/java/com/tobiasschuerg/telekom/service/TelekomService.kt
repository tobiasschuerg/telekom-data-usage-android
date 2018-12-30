package com.tobiasschuerg.telekom.service

import com.tobiasschuerg.telekom.service.data.Status

interface TelekomService {

    suspend fun getStatus(): Status

}