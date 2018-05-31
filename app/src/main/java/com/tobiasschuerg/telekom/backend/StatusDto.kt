package com.tobiasschuerg.telekom.backend

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StatusDto(
        val title: String,

        val subscriptions: List<String>,
        val hasOffers: Boolean,

        val passName: String,
        val passStage: Int,
        val passType: Int,

        val nextUpdate: Int,
        val validityPeriod: Int,

        val initialVolume: Long,
        val initialVolumeStr: String,

        val usedVolume: Long,
        val usedPercentage: Int,
        val usedVolumeStr: String,
        val usedAt: Long,

        val remainingSeconds: Int,
        val remainingTimeStr: String

)
