package com.example.mustafajobtestproject.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mustafajobtestproject.R
import com.example.mustafajobtestproject.adapter.DrawerAppsAdapter
import com.example.mustafajobtestproject.adapter.ItemClickListener
import com.example.mustafajobtestproject.model.AppInfo
import kotlinx.android.synthetic.main.fragment_app_drawer.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppDrawerFragment : Fragment() , ItemClickListener {
    lateinit var appsList: ArrayList<AppInfo>
    lateinit var appAdapter : DrawerAppsAdapter
    lateinit var pManager: PackageManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_app_drawer, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pManager = requireContext().packageManager

        appsList = ArrayList<AppInfo>()
        loadApps(pManager)
        setUpRecyclerView()

        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED)
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED)
        intentFilter.addAction(Intent.ACTION_PACKAGE_CHANGED)
        intentFilter.addDataScheme("package")
        activity?.registerReceiver(PackagerReceiver(),intentFilter)

    }

    private fun setUpRecyclerView() {
        appAdapter = DrawerAppsAdapter(requireContext(),appsList,this)
        rvApps.apply {
            adapter = appAdapter
            layoutManager = GridLayoutManager(requireContext(),4)
        }
    }

        fun loadApps(packageManager: PackageManager) {
            val loadList = mutableListOf<AppInfo>()

            val i = Intent(Intent.ACTION_MAIN, null)
            i.addCategory(Intent.CATEGORY_LAUNCHER)
            val allApps = packageManager.queryIntentActivities(i, 0)
            for (ri in allApps) {
                val app = AppInfo(
                    ri.loadLabel(packageManager),
                    ri.activityInfo.packageName,
                    ri.activityInfo.loadIcon(packageManager)
                )
                loadList.add(app)
            }
            loadList.sortBy { it.label.toString() }

            appsList.clear()
            appsList.addAll(loadList)

        }



    override fun onClick(position: Int) {
        val packageName = appsList[position].packageName
        val launchIntent = requireContext().packageManager.getLaunchIntentForPackage(packageName)
        if (launchIntent != null) {
            startActivity(launchIntent)
        }
    }


    //refresh apps package if anything changes
   inner class PackagerReceiver : BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {
           lifecycleScope.launch(Dispatchers.IO){
              loadApps(pManager)

           }
        }

    }
}