package com.example.mustafajobtestproject.ui

import android.app.Activity.*
import android.app.AlertDialog
import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetHostView
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mustafajobtestproject.R
import com.example.mustafajobtestproject.widgets.BatteryIndicatorWidget
import com.example.mustafajobtestproject.widgets.MyWeatherWidget
import kotlinx.android.synthetic.main.fragment_home.*


const val APPWIDGET_HOST_ID = 111
const val REQUEST_PICK_APPWIDGET = 222
const val REQUEST_CREATE_APPWIDGET = 333
const val TAG = "WidgetHostExampleActivity"


class HomeFragment : Fragment() , View.OnLongClickListener{

    lateinit var list : ArrayList<AppWidgetProviderInfo>

    lateinit var appWidgetManager: AppWidgetManager
    lateinit var appWidgetHost: AppWidgetHost
    lateinit var homeLayout: ViewGroup

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appWidgetManager = AppWidgetManager.getInstance(requireContext().applicationContext)
        appWidgetHost = AppWidgetHost(requireContext().applicationContext, APPWIDGET_HOST_ID)
        homeLayout = view.findViewById<ViewGroup>(R.id.home)


        getMyWidgets()




        ivDrawerIcon.setOnClickListener {
            loadFragment(AppDrawerFragment())
        }

        home.isLongClickable = true
        home.setOnLongClickListener(this)


    }

    private fun getMyWidgets() {
        list = ArrayList<AppWidgetProviderInfo>()
        val infoList = appWidgetManager.installedProviders
        for (info in infoList) {
            list.add(info)
        }

    }


    private fun loadFragment(fragment: Fragment?): Boolean {
        //switching fragment
        if (fragment != null) {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit()
            return true
        }
        return false
    }





    override fun onLongClick(p0: View?): Boolean {
        when(p0!!.id){
            home.id -> {
                    openDialog()
            }

            else -> return false
        }
        return true
    }


    private fun addWidgetToHome(widgetPosition: Int){

        val info = list.filter { it.provider.packageName.contains( "${requireContext().packageName}") }
            .get(widgetPosition)
        val appWidgetId = appWidgetHost.allocateAppWidgetId()

        val hostView: AppWidgetHostView =
            appWidgetHost.createView(requireContext().applicationContext, 1, info)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            val allowed = appWidgetManager.bindAppWidgetIdIfAllowed(appWidgetId, info.provider)
            if (!allowed) {
                // Request permission - https://stackoverflow.com/a/44351320/1816603
                val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_BIND)
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_PROVIDER,
                    info.provider)
                val REQUEST_BIND_WIDGET = 1987
                startActivityForResult(intent, REQUEST_BIND_WIDGET)
            }
        }
        hostView.setAppWidget(1, info)
        homeLayout.addView(hostView)

    }

    private fun openDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(R.string.select_widget)
        builder.setItems(R.array.widgets_array){_, which ->

            when(which){
                0 -> {

                    addWidgetToHome(0)
                }
                1 ->{
                    addWidgetToHome(1)
                }
                2 -> addWidgetToHome(2)
            }

            builder.create()

        }.show()

    }


}

