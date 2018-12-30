package com.tobiasschuerg.telekom.service.data

data class Used(
        val usedVolume: Long,
        val usedPercentage: Int,
        val usedVolumeStr: String,
        val usedAt: Long
)