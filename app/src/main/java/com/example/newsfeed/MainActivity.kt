package com.example.newsfeed

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.newsfeed.databinding.ActivityMainBinding
import com.example.newsfeed.network.WeatherApi
import com.example.newsfeed.network.WeatherData
import com.example.newsfeed.repository.WeatherRepository
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {

    private var PERMISSION_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION
    private var PERMISSION_REQ_CODE = 1903
    private lateinit var binding : ActivityMainBinding
    private var data : WeatherData? = null
    private val cancellationSource by lazy{ CancellationTokenSource() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        requestPermissionRuntime()
        binding.enable.setOnClickListener{
            requestPermissionRuntime()
        }
    }

    private fun requestPermissionRuntime(){
        when{
            ContextCompat.checkSelfPermission(this,
                PERMISSION_LOCATION) == PackageManager.PERMISSION_GRANTED ->{
                    makeVisible()
                    getUserLocation()
            }
            ActivityCompat.shouldShowRequestPermissionRationale(this,
                PERMISSION_LOCATION) -> {
                val builder : AlertDialog.Builder = AlertDialog.Builder(this)
                builder.setTitle("Permission Required")
                    .setMessage("To show the weather information you must turn on the location.")
                    .setCancelable(false)
                    .setNegativeButton("Cancel"){dialog,_ ->
                        //makeInvisible()
                        dialog.dismiss()
                    }
                    .setPositiveButton("Permit"){dialog,_ ->
                        println("1st")
                        ActivityCompat.requestPermissions(this, arrayOf(PERMISSION_LOCATION), PERMISSION_REQ_CODE)
                        dialog.dismiss()
                    }
                builder.create().show()
            }
            else -> {
                println("2nd")
                ActivityCompat.requestPermissions(this, arrayOf(PERMISSION_LOCATION), PERMISSION_REQ_CODE)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == PERMISSION_REQ_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getUserLocation()
                makeVisible()
            }
            else if (!ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSION_LOCATION)) {
                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                builder.setTitle("Permission Required")
                    .setMessage("To show the weather details you must give location permission to this app. Please give the Permission.")
                    .setCancelable(false)
                    .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
                    .setPositiveButton("Go to Settings") { dialog, _ ->
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", packageName, null)
                        intent.setData(uri)
                        startActivity(intent)
                        dialog.dismiss()
                    }
                builder.create().show()
            }
            else {
                println("3rd")
                requestPermissionRuntime()
            }
        }
    }

    private fun getUserLocation() {
        val locationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, PERMISSION_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(PERMISSION_LOCATION), PERMISSION_REQ_CODE)
            return
        }
        locationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                //println(location)
                val repo = WeatherRepository(location)
                lifecycleScope.launch {
                    data = repo.getWeather()
                    binding.weatherData = data
                }
            }
            else{
                locationProviderClient.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY,cancellationSource.token)
                    .addOnSuccessListener { currLocation ->
                        if (currLocation != null) {
                            Log.i("MainActivity", "Getting current location")
                            val repo = WeatherRepository(currLocation)
                            lifecycleScope.launch {
                                data = repo.getWeather()
                                binding.weatherData = data
                            }
                        }
                        else {
                            makeInvisible()
                            Toast.makeText(this, "Please enable location", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }

    private fun makeVisible() {
        binding.weatherIcon.visibility = View.VISIBLE
        binding.tempAndArea.visibility = View.VISIBLE
        binding.weatherDescription.visibility = View.VISIBLE
        binding.enable.visibility = View.GONE
    }

    private fun makeInvisible() {
        binding.weatherIcon.visibility = View.GONE
        binding.tempAndArea.visibility = View.GONE
        binding.weatherDescription.visibility = View.GONE
        binding.enable.visibility = View.VISIBLE
    }
}
