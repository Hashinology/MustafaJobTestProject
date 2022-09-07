package com.example.mustafajobtestproject.data

import android.content.Context
import com.example.mustafajobtestproject.util.Constants

class MyPrefrence(context: Context) {

    val prefrence = context.getSharedPreferences(Constants.PREFRENCE,Context.MODE_PRIVATE)

    fun getCity():String{
        return prefrence.getString(Constants.CITY,"")!!
    }

    fun setCity(city:String){
        val editor = prefrence.edit()
        editor.putString(Constants.CITY,city)
        editor.apply()
    }


    fun getCountry():String{
        return prefrence.getString(Constants.COUNTRY,"")!!
    }

    fun setCountry(country:String){
        val editor = prefrence.edit()
        editor.putString(Constants.COUNTRY,country)
        editor.apply()
    }



    fun getTemp():Int{
        return prefrence.getInt(Constants.TEMP,0)
    }

    fun setTemp(temp:Int){
        val editor = prefrence.edit()
        editor.putInt(Constants.TEMP,temp)
        editor.apply()
    }



    fun getDescrip():String{
        return prefrence.getString(Constants.DESCRIP,"")!!
    }

    fun setDescrip(descrip:String){
        val editor = prefrence.edit()
        editor.putString(Constants.DESCRIP,descrip)
        editor.apply()
    }
}