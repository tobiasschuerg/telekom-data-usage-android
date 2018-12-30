package com.tobiasschuerg.telekom.backend

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StatusDto(
        val title: String,

        val subscriptions: List<String>,
        val hasOffers: Boolean,

        val nextUpdate: Int,
        val validityPeriod: Int,

        val passName: String,
        val passStage: Int, // 1 = ok, 2 = all data used
        val passType: Int,

        val initialVolume: Long?, // available if pass stage = 1
        val initialVolumeStr: String?, // available if pass stage = 1

        val usedVolume: Long?, // available if pass stage = 1
        val usedPercentage: Int,
        val usedVolumeStr: String?, // available if pass stage = 1
        val usedAt: Long,

        val remainingSeconds: Int,
        val remainingTimeStr: String

)
