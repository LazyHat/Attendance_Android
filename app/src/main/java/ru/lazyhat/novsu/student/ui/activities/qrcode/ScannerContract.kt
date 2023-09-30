package ru.lazyhat.novsu.student.ui.activities.qrcode

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

class ScannerContract : ActivityResultContract<Unit, String>() {

    companion object {
        val KEY = "key"
    }

    override fun createIntent(context: Context, input: Unit): Intent {
        val intent = Intent(context, ScannerActivity::class.java)
        return intent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): String {
        return intent?.getStringExtra(KEY) ?: throw IllegalArgumentException("Invalid code")
    }
}