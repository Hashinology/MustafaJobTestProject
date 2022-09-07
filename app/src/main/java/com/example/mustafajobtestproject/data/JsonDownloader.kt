package com.example.mustafajobtestproject.data

import android.content.Context
import android.content.SharedPreferences
import android.os.AsyncTask
import android.widget.RemoteViews
import android.widget.Toast
import com.example.mustafajobtestproject.R
import com.example.mustafajobtestproject.model.Weather
import com.example.mustafajobtestproject.util.Constants
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL


@Suppress("DEPRECATION")
class JsonDownloader(
    private var context: Context,
    private var jsonUrl: String

) : AsyncTask<Void, Void, String>() {
    lateinit var _weather: Weather

    private lateinit var sharedPreferences: MyPrefrence



    // connect to network via HttpUrlConnection

    private fun connect(jsonUrl: String): Any {
        try {

            val url = URL(jsonUrl)
            val con = url.openConnection() as HttpURLConnection
            con.requestMethod = "GET"
            con.connectTimeout = 15000
            con.readTimeout = 15000
            con.doInput = true

            return con

        } catch (e: MalformedURLException) {
            e.printStackTrace()
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            return "Url error ${e.message}"

        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()

            return "Connection Error ${e.message}"
        }
    }


    private fun getDataAsString(): String {
        val connection = connect(Constants.url_endpoint)

        // check if there is an error with connection
        if (connection.toString().startsWith("Error")) {
            return connection.toString()
        }

        //getData
        try {
            val con = connection as HttpURLConnection

            //if connection is okay
            if (con.responseCode == 200) {
                // get input from bufferedstream
                val input = BufferedInputStream(con.inputStream)
                val bufferReader = BufferedReader(InputStreamReader(input))
                val jsonData = StringBuffer()
                var line: String?

                do {
                    line = bufferReader.readLine()
                    if (line == null) {
                        break
                    }
                    jsonData.append(line + "\n")

                } while (true)

                // when finished close buffer
                bufferReader.close()
                input.close()

                return jsonData.toString()

            } else {
                return "Error buffer ${con.responseMessage}"
            }

        } catch (e: IOException) {
            e.printStackTrace()
            return "Error io ${e.message}"
        }

    }

    override fun onPreExecute() {
        super.onPreExecute()
    }

    override fun doInBackground(vararg p0: Void?): String {
        return getDataAsString()
    }

    override fun onPostExecute(jsonData: String) {
        super.onPostExecute(jsonData)

        if (jsonData.startsWith("error")) {
            Toast.makeText(context, jsonData, Toast.LENGTH_SHORT).show()
            Toast.makeText(context, "Fetching data is successful", Toast.LENGTH_SHORT).show()

        } else if (jsonData.startsWith("CONNECT ERROR")) {
            Toast.makeText(context, jsonData, Toast.LENGTH_SHORT).show()
            Toast.makeText(context, "Connection Error", Toast.LENGTH_SHORT).show()

        } else {
            Toast.makeText(context, "Fetching data is successful", Toast.LENGTH_SHORT).show()
            JsonParser(context, jsonData).parseJson()

        }
    }



    @Suppress("DEPRECATION")
    inner class JsonParser(private val context: Context, private val jsonData: String) :
        AsyncTask<Void, Void, Boolean>() {
        lateinit var mWeather: Weather

        fun parseJson(): Boolean {
            try {

                val js = JSONObject(jsonData)

                val city = js.getString("city")
                val country = js.getString("country")
                val temperature = js.getInt("temperature")
                val description = js.getString("description")
                mWeather = Weather(city, country, temperature, description)

                sharedPreferences = MyPrefrence(context)
                sharedPreferences.setCity(city)
                sharedPreferences.setCountry(country)
                sharedPreferences.setTemp(temperature)
                sharedPreferences.setDescrip(description)





                Toast.makeText(context, "views data is successful", Toast.LENGTH_SHORT).show()


                return true

            } catch (e: JSONException) {
                e.printStackTrace()
                return false
            }

        }

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg p0: Void?): Boolean {
            return parseJson()
        }

        override fun onPostExecute(isParsed: Boolean?) {
            super.onPostExecute(isParsed)

            if (isParsed!!) {
               println(this.mWeather.toString())

            } else {
                Toast.makeText(context, "Unable to Parse Json", Toast.LENGTH_SHORT).show()
            }

        }
    }

    fun getdata():Weather{
        return _weather
    }
}


