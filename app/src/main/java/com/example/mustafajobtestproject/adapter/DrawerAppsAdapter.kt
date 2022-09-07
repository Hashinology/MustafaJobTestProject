package com.example.mustafajobtestproject.adapter

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mustafajobtestproject.R
import com.example.mustafajobtestproject.model.AppInfo


class DrawerAppsAdapter(
    val context: Context,
    val appList: ArrayList<AppInfo>,
    val itemClickListener : ItemClickListener
) :
    RecyclerView.Adapter<DrawerAppsAdapter.AppsViewHolder>() {



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): DrawerAppsAdapter.AppsViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.app_list_item,parent,false)
        return AppsViewHolder(view)
    }

    override fun onBindViewHolder(holder: DrawerAppsAdapter.AppsViewHolder, position: Int) {
       val app  = appList[position]
        holder.tvAppName.text = app.label.toString()
        holder.ivAppIcon.setImageDrawable(app.icon)

    }

    override fun getItemCount(): Int {
        return appList.size
    }

    inner class AppsViewHolder(itemview: View): RecyclerView.ViewHolder(itemview), View.OnClickListener{
        val ivAppIcon = itemview.findViewById<ImageView>(R.id.ivAppIcon)
        val tvAppName = itemview.findViewById<TextView>(R.id.tvAppName)

        init {
            itemview.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            itemClickListener.onClick(adapterPosition)
        }

    }


}