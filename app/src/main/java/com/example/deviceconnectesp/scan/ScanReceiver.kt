package com.example.deviceconnectesp.scan

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.deviceconnectesp.SampleApplication
import com.polidea.rxandroidble2.exceptions.BleScanException

private const val SCAN_REQUEST_CODE = 42

class ScanReceiver : BroadcastReceiver() {

    companion object {
        fun newPendingIntent(context: Context): PendingIntent =
            Intent(context, ScanReceiver::class.java).let {
                PendingIntent.getBroadcast(context, SCAN_REQUEST_CODE, it, 0)
            }
    }

    @RequiresApi(26 /* Build.VERSION_CODES.O */)
    override fun onReceive(context: Context, intent: Intent) {
        val backgroundScanner = SampleApplication.rxBleClient!!.backgroundScanner
        try {
            val scanResults = backgroundScanner.onScanResultReceived(intent)
            Log.i("ScanReceiver", "Scan results received: $scanResults")
        } catch (exception: BleScanException) {
            Log.e("ScanReceiver", "Failed to scan devices", exception)
        }
    }
}