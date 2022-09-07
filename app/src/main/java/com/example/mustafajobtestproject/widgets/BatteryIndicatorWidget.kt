package com.example.mustafajobtestproject.widgets

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.BATTERY_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.widget.RemoteViews
import android.widget.Toast
import com.example.mustafajobtestproject.R


//var batteryPct = ""
//var mBatInfoReceiver: BroadcastReceiver = object : BroadcastReceiver() {
//    override fun onReceive(ctxt: Context, intent: Intent) {
//        val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
//        val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
//         batteryPct += level * 100 / scale.toFloat()
//    }
//}



class BatteryIndicatorWidget : AppWidgetProvider() {
//    var battery = ""
//
//    override fun onReceive(context: Context?, intent: Intent?) {
//        super.onReceive(context, intent)
//        val level = intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
//        battery = level.toString()
//    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray,
    ) {

        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateBatteryAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {

    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }


    private fun updateBatteryAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
    ) {

//        context.applicationContext.registerReceiver(mBatInfoReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))

        val bm = context.getSystemService(BATTERY_SERVICE) as BatteryManager
        val batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        val batPercentage = "$batLevel %"

        val views = RemoteViews(context.packageName, R.layout.battery_indicator_widget)


        views.setTextViewText(R.id.tvBattery, batPercentage)


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

}