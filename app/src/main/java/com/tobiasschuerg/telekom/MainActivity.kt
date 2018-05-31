package com.tobiasschuerg.telekom

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.tobiasschuerg.telekom.backend.StatusDto
import com.tobiasschuerg.telekom.backend.TelekomBackend
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()

        launch(CommonPool) {
            try {
                val result = TelekomBackend.instance.getStatus().await()
                Log.d("Status", result.toString())
                val status = result.body()!!
                launch(UI) {
                    onStatusReceived(status)
                }
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }

    }

    private fun onStatusReceived(status: StatusDto) {
        if (status.title.isNotEmpty()) {
            title = status.title
        }
        status_text.text = "Used ${status.usedPercentage}%"
        status_remaining.text = "Verbleibend ${status.remainingTimeStr}"
    }
}
