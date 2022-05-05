package com.example.weathery

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    val city = "São Paulo, SP"
    val api = "70d0c9a3906de21957a0df3ecd97e29c70d0c9a3906de21957a0df3ecd97e29c"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        WeatherTask().execute()
    }

    @SuppressLint("StaticFieldLeak")
    inner class WeatherTask : AsyncTask<String, Void, String>() {
        @Deprecated("Deprecated in Java")
        override fun onPreExecute() {
            super.onPreExecute()
            findViewById<ProgressBar>(R.id.PB_loader).visibility = View.VISIBLE
            findViewById<RelativeLayout>(R.id.mainLayout).visibility = View.GONE
            findViewById<TextView>(R.id.errorText).visibility = View.GONE
        }

        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg p0: String?): String? {

            val response: String? = try {
                URL("https://api.openweathermap.org/data/2.5/weather?q=$city&units=metric&appid=$api")
                    .readText(Charsets.UTF_8)
            } catch (e: Exception) {
                null
            }

            return response
        }


        @Deprecated("Deprecated in Java")
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            try {
                val jsonObj = result?.let { JSONObject(it) }
                val main = jsonObj?.getJSONObject("main")
                val sys = jsonObj?.getJSONObject("sys")
                val wind = jsonObj?.getJSONObject("wind")
                val weather = jsonObj?.getJSONArray("weather")?.getJSONObject(0)
                val updatedAt: Long = jsonObj!!.getLong("dt")
                val updatedAtText = "Updated at: " + SimpleDateFormat(
                    "dd/MM/yyyy hh:mm a", Locale.ENGLISH
                ).format(updatedAt * 1000)
                val temp = main?.getString("temp") + "ºC"
                val tempMin = " Min Temp: " + main?.getString("temp_min") + "ºC"
                val tempMax = "Max Temp: " + main?.getString("temp_max") + "ºC"
                val pressure = main?.getString("pressure")
                val humidity = main?.getString("humidity")
                val sunrise: Long = sys!!.getLong("sunrise")
                val sunset: Long = sys.getLong("sunset")
                val windSpeed = wind?.getString("speed")
                val weatherDescription = weather?.getString("description")
                val address = jsonObj.getString("name") + ", " +
                        sys.getString("county")

                findViewById<TextView>(R.id.addressLayout).text = address
                findViewById<TextView>(R.id.updated_at).text = updatedAtText
                if (weatherDescription != null) {
                    findViewById<TextView>(R.id.TV_status).text =
                        weatherDescription.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(
                                Locale.getDefault()
                            ) else it.toString()
                        }
                }

                findViewById<TextView>(R.id.TV_temp).text = temp
                findViewById<TextView>(R.id.TV_minTemp).text = tempMin
                findViewById<TextView>(R.id.TV_maxTemp).text = tempMax
                findViewById<TextView>(R.id.TV_sunrise).text = SimpleDateFormat(
                    "hh:mm a",
                    Locale.ENGLISH
                ).format(Date(sunrise * 1000))
                findViewById<TextView>(R.id.TV_sunset).text = SimpleDateFormat(
                    "hh:mm a",
                    Locale.ENGLISH
                ).format(Date(sunset * 1000))
                findViewById<TextView>(R.id.TV_wind).text = windSpeed
                findViewById<TextView>(R.id.TV_pressure).text = pressure
                findViewById<TextView>(R.id.TV_humidity).text = humidity

                findViewById<ProgressBar>(R.id.PB_loader).visibility = View.GONE
                findViewById<RelativeLayout>(R.id.mainLayout).visibility = View.VISIBLE
            }

            catch (e: Exception) {
                findViewById<ProgressBar>(R.id.PB_loader).visibility = View.GONE
                findViewById<TextView>(R.id.errorText).visibility = View.VISIBLE
            }
        }
    }
}