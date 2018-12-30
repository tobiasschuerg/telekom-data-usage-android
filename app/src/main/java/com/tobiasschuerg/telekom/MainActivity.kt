package com.tobiasschuerg.telekom

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.tobiasschuerg.telekom.service.TelekomService
import com.tobiasschuerg.telekom.service.TelekomServiceImpl
import com.tobiasschuerg.telekom.service.data.Status
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    private var job: Job? = null
    private val telekomService: TelekomService by lazy {
        TelekomServiceImpl() // TODO: inject
    }

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

        // FIXME: remove global scope
        job = GlobalScope.launch {
            try {
                val status = telekomService.getStatus()
                when (status) {
                    is Status.Ok.Normal       -> onStatusReceived(status)
                    is Status.Ok.LimitExeeded -> TODO()
                    is Status.Error           -> onError(status)
                }
            } catch (t: Throwable) {
                Timber.e(t)
                onError(null)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        job?.cancel()
    }

    // FIXME: GlobalScope
    private fun onStatusReceived(status: Status.Ok.Normal) = GlobalScope.launch(Dispatchers.Main) {
        Timber.d("Status received: $status")
        if (status.pass.name.isNotEmpty()) {
            title = status.pass.name
        }
        text_pass_name.text = "Name: ${status.pass.name}"
        text_initial.text = "Initial: ${status.initial.initialVolumeStr}"
        text_used.text = "Verbraucht: ${status.used.usedVolumeStr}"

        text_remaining.text = "Verbleibender Zeitraum: ${status.remaining.remainingTimeStr}"
        val remainingPercentage = 100 - status.used.usedPercentage
        progress_usage.progress = status.used.usedPercentage
        status_text.text = "Verbleibendes Datenvolumen: $remainingPercentage%"
    }

    // FIXME: GlobalScope
    private fun onError(error: Status.Error?) = GlobalScope.launch(Dispatchers.Main) {
        when (error?.code) {
            null -> status_text.text = "Nicht verfügbar"
            else -> status_text.text = "Error $error.code"
        }
    }
}
