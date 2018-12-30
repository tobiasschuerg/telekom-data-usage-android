package com.tobiasschuerg.telekom.service.data

import com.tobiasschuerg.data.Volume

data class Remaining(
        val remainingSeconds: Int,
        val remainingTimeStr: String,
        val remainingVolume: Volume
)