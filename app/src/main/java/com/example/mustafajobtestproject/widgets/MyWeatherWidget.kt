package com.example.mustafajobtestproject.widgets

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.AsyncQueryHandler
import android.content.Context
import android.os.AsyncTask
import android.provider.Settings.Global.getString
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.loader.content.AsyncTaskLoader
import com.example.mustafajobtestproject.R
import com.example.mustafajobtestproject.data.JsonDownloader
import com.example.mustafajobtestproject.data.MyPrefrence
import com.example.mustafajobtestproject.model.Weather
import com.example.mustafajobtestproject.util.Constants
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MyWeatherWidget : AppWidgetProvider() {
    lateinit var shared: MyPrefrence
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray,
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateWeatherAppWidget(context, appWidgetManager, appWidgetId)

            AsyncTask.execute {  JsonDownloader(context,Constants.url_endpoint).execute()}


        }




    }




    override fun onEnabled(context: Context) {
        AsyncTask.execute {  JsonDownloader(context,Constants.url_endpoint)}

    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }




    internal fun updateWeatherAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
    ) {


        shared = MyPrefrence(context)
        // Construct the RemoteViews object
        val views = RemoteViews(context.packageName, R.layout.my_weather_widget)
        AsyncTask.execute {  JsonDownloader(context,Constants.url_endpoint)}

        val weather = Weather(shared.getCity(), shared.getCountry(), shared.getTemp(), shared.getDescrip())

        Toast.makeText(context,weather.toString(),Toast.LENGTH_SHORT).show()

        views.setTextViewText(R.id.tvCity,weather.city)
        views.setTextViewText(R.id.tvCountry,weather.country)
        views.setTextViewText(R.id.tvTemp,weather.temperature.toString())
        views.setTextViewText(R.id.tvDecrip,weather.description)


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }


}