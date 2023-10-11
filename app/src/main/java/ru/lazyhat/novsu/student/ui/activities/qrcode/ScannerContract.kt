package ru.lazyhat.novsu.student.ui.activities.qrcode

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

class ScannerContract : ActivityResultContract<Unit, Unit>() {

    override fun createIntent(context: Context, input: Unit): Intent =
        Intent(context, ScannerActivity::class.java)

    override fun parseResult(resultCode: Int, intent: Intent?){}
}