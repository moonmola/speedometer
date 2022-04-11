package com.moonmola.speedometer

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat

import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager

import androidx.core.content.ContextCompat
import com.moonmola.speedometer.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), LocationListener {
    var locationManager: LocationManager? = null
    var lastLocation: Location? = null
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestPermission()
        location()
    }

    private fun requestPermission() {
        val permissionCheck =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)

        if (permissionCheck == PackageManager.PERMISSION_DENIED) { //포그라운드 위치 권한 확인
            //위치 권한 요청
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                0
            )
        }


        val permissionCheck2 =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)

        if (permissionCheck2 == PackageManager.PERMISSION_DENIED) { //백그라운드 위치 권한 확인
            //위치 권한 요청
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                0
            )
        }

    }
    private fun location() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        locationManager = this.getSystemService(LOCATION_SERVICE) as LocationManager
        lastLocation = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,0F,this)

    }

    override fun onProviderEnabled(provider: String) {
        super.onProviderEnabled(provider)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,0F,this)
    }

    override fun onLocationChanged(location: Location) {
        lastLocation?.let { lastLocation ->
            val deltaTime = (location.time - lastLocation.time)/1000.0
            val currentSpeed = lastLocation.distanceTo(location)/deltaTime
            binding.speed.text = String.format("%d",currentSpeed.toInt())
            binding.speedometerCurrent.rotation = currentSpeed.toFloat()

            this@MainActivity.lastLocation = location

        }
    }
}