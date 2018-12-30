package com.tobiasschuerg.telekom.service

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
                    Status.Ok.Normal(
                            pass = Pass(statusDto.passName, statusDto.passType),
                            initial = Initial(statusDto.initialVolume!!, statusDto.initialVolumeStr!!),
                            used = Used(statusDto.usedVolume!!, statusDto.usedPercentage, statusDto.usedVolumeStr!!, statusDto.usedAt),
                            remaining = Remaining(statusDto.remainingSeconds, statusDto.remainingTimeStr)
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