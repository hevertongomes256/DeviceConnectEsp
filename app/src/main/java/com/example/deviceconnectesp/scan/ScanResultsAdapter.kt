package com.example.deviceconnectesp.scan

import android.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.polidea.rxandroidble2.scan.ScanResult

internal class ScanResultsAdapter(
    private val onClickListener: (ScanResult) -> Unit
) : RecyclerView.Adapter<DeviceViewHolder>() {

    private val data = mutableListOf<ScanResult>()

    fun addScanResult(bleScanResult: ScanResult) {
        // Not the best way to ensure distinct devices, just for the sake of the demo.
        data.withIndex()
            .firstOrNull { it.value.bleDevice == bleScanResult.bleDevice }
            ?.let {
                // device already in data list => update
                data[it.index] = bleScanResult
                notifyItemChanged(it.index)
            }
            ?: run {
                // new device => add to data list
                with(data) {
                    add(bleScanResult)
                    sortBy { it.bleDevice.macAddress }
                }
                notifyDataSetChanged()
            }
    }

    fun clearScanResults() {
        data.clear()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        with(data[position]) {
            holder.device.text = String.format("%s (%s)", bleDevice.macAddress, bleDevice.name)
            holder.rssi.text = String.format("RSSI: %d", rssi)
            holder.itemView.setOnClickListener { onClickListener(this) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder =
        LayoutInflater.from(parent.context)
            .inflate(R.layout.two_line_list_item, parent, false)
            .let { DeviceViewHolder(it) }

}