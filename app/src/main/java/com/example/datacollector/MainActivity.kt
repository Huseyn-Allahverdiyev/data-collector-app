package com.example.datacollector

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.telephony.*
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import org.json.JSONObject
import java.io.File
import java.net.URL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val button = Button(this)
        button.text = "Collect Data"
        setContentView(button)

        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            1
        )

        button.setOnClickListener {
            collectData()
        }
    }

    private fun collectData() {

        val fusedLocation = LocationServices.getFusedLocationProviderClient(this)

        fusedLocation.lastLocation.addOnSuccessListener { loc ->

            val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val cellInfos = telephonyManager.allCellInfo

            val json = JSONObject()

            json.put("lat", loc?.latitude)
            json.put("lon", loc?.longitude)

            for (cell in cellInfos) {
                if (cell is CellInfoLte) {
                    json.put("cellId", cell.cellIdentity.ci)
                    json.put("tac", cell.cellIdentity.tac)
                    json.put("signal", cell.cellSignalStrength.dbm)
                }
            }

            val ip = try {
                URL("https://api.ipify.org").readText()
            } catch (e: Exception) {
                "unknown"
            }

            json.put("ip", ip)

            val file = File(getExternalFilesDir(null), "data.json")
            file.writeText(json.toString())
        }
    }
}
