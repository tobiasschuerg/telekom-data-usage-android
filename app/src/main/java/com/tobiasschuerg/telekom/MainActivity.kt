package com.tobiasschuerg.telekom

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.tobiasschuerg.telekom.backend.StatusDto
import com.tobiasschuerg.telekom.backend.TelekomBackend
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private var job: Job? = null

    override fun onResume() {
        super.onResume()

        job = launch(CommonPool) {
            try {
                val response = TelekomBackend.instance.getStatus().await()
                Timber.d(response.toString())

                if (response.isSuccessful) {
                    val statusDto = response.body()!!
                    onStatusReceived(statusDto)
                } else {
                    onError(response.code())
                }
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }

    }

    override fun onPause() {
        super.onPause()
        job?.cancel()
    }

    private fun onStatusReceived(status: StatusDto) = launch(UI) {
        if (status.title.isNotEmpty()) {
            title = status.title
        }
        status_text.text = "Used ${status.usedPercentage}%"
        status_remaining.text = "Verbleibend ${status.remainingTimeStr}"
    }

    private fun onError(code: Int) = launch(UI) {
        when (code) {
            else -> status_text.text = "Error $code"
        }
    }
}
