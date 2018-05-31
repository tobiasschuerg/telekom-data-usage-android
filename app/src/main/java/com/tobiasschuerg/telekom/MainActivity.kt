package com.tobiasschuerg.telekom

import android.content.Intent
import android.net.Uri
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

    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            with(Intent(Intent.ACTION_VIEW)) {
                data = Uri.parse("http://pass.telekom.de")
                startActivity(this)
            }
        }
    }


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
        Timber.d("Status received: $status")
        if (status.title.isNotEmpty()) {
            title = status.title
        }
        text_pass_name.text = "Name: ${status.passName}"
        text_initial.text = "Initial: ${status.initialVolumeStr}"
        text_used.text = "Verbraucht: ${status.usedVolumeStr}"

        text_remaining.text = "Verbleibender Zeitraum: ${status.remainingTimeStr}"
        val remainingPercentage = 100 - status.usedPercentage
        progress_usage.progress = remainingPercentage
        status_text.text = "Verbleibendes Datenvolumen: $remainingPercentage%"
    }

    private fun onError(code: Int) = launch(UI) {
        when (code) {
            else -> status_text.text = "Error $code"
        }
    }
}
