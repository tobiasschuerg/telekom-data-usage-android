package com.tobiasschuerg.telekom.service.data

import com.tobiasschuerg.telekom.backend.StatusDto

sealed class Status {

    sealed class Ok {

        abstract val title: String

        abstract val subscriptions: List<String>
        abstract val hasOffers: Boolean

        abstract val nextUpdate: Int
        abstract val validityPeriod: Int

        class Normal(
                val pass: Pass,
                val initial: Initial,
                val used: Used,
                val remaining: Remaining
        ) : Status()


        class LimitExeeded(
                statusDto: StatusDto
        ) : Status() {
            init {
                TODO()
            }
        }

    }

    data class Error(val code: Int) : Status()


}
