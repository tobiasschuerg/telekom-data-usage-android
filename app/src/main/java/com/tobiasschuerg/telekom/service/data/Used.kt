package com.tobiasschuerg.telekom.service.data

import com.tobiasschuerg.data.Volume

data class Used(
        val usedVolume: Volume,
        val usedPercentage: Int,
        val usedVolumeStr: String,
        val usedAt: Long
)