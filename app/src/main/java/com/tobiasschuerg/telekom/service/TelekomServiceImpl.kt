package com.tobiasschuerg.telekom.service

import com.tobiasschuerg.data.Volume
import com.tobiasschuerg.telekom.backend.StatusDto
import com.tobiasschuerg.telekom.backend.TelekomBackend
import com.tobiasschuerg.telekom.service.data.*
import retrofit2.Response
import timber.log.Timber

class TelekomServiceImpl : TelekomService {

    override suspend fun getStatus(): Status {

        val response: Response<StatusDto> = TelekomBackend.instance.getStatus().await()
        Timber.d(response.toString())

        return if (response.isSuccessful) {
            val statusDto = response.body()!!

            val passStage = statusDto.passStage
            return when (passStage) {
                1    -> {
                    val initialVolume = Volume(statusDto.initialVolume!!)
                    val usedVolume = Volume(statusDto.usedVolume!!)
                    val remainingVolume = initialVolume - usedVolume
                    Status.Ok.Normal(
                            pass = Pass(statusDto.passName, statusDto.passType),
                            initial = Initial(initialVolume, statusDto.initialVolumeStr!!),
                            used = Used(usedVolume, statusDto.usedPercentage, statusDto.usedVolumeStr!!, statusDto.usedAt),
                            remaining = Remaining(statusDto.remainingSeconds, statusDto.remainingTimeStr, remainingVolume)
                    )
                }
                0    -> {
                    Status.Ok.LimitExeeded(statusDto)
                }
                else -> {
                    Timber.e(Error("Unknown pass stage $passStage"))
                    Status.Error(0)
                }
            }

        } else {
            Status.Error(response.code())
        }
    }

}