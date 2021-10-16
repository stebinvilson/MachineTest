package com.restoo.machinetest.Helper

import android.app.Activity
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.AsyncTask
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.room.Room
import com.restoo.machinetest.*
import com.restoo.machinetest.Listner.ApiInterface
import com.restoo.machinetest.Listner.MainActivityListner
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivityHelper(
    var context: Context,
    var activity: Activity,
    var listner: MainActivityListner
) {

    protected var dialog: Dialog? = null
    private val dialogLock = Any()


    fun callApi() {
        if (if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                isNetworkConnected()
            } else {
                TODO("VERSION.SDK_INT < M")
            }
        ) {
            showProgress()
            val service: ApiInterface? = getRetrofitObject()?.create(ApiInterface::class.java)
            var call: Call<List<Apod>?>? = service?.fetch("DEMO_KEY", 10)
            call?.enqueue(object : Callback<List<Apod>?> {
                override fun onResponse(call: Call<List<Apod>?>, response: Response<List<Apod>?>) {
                    //  closeProgress()
                    if (response.isSuccessful() && response.body() != null) {
                        var responsedata: List<Apod> = response.body()!!
                        listner.onsuccess(responsedata, false)
                        closeProgress()

                    } else {
                        closeProgress()
                        listner.onfail()
                    }
                }


                override fun onFailure(call: Call<List<Apod>?>, t: Throwable) {
                    listner.onfail()
                    Log.d(ContentValues.TAG, t.toString())
                    closeProgress()
                }
            })
        } else {
            fetchdata(context, listner).execute()
        }
    }

    class fetchdata(var context: Context, var listner: MainActivityListner) :
        AsyncTask<Void, Void, List<NasaData>>() {


        override fun doInBackground(vararg params: Void?): List<NasaData>? {
            var mDb: AppDatabase =
                Room.databaseBuilder(context, AppDatabase::class.java, "RoomDatabase")
                    .allowMainThreadQueries().build()
            var fetchdata = mDb.datadao().getAll() as List<NasaData>

            return fetchdata
        }

        override fun onPostExecute(result: List<NasaData>?) {
            super.onPostExecute(result)
            if (result != null) {

                var apodData: ArrayList<Apod> = ArrayList()

                for (items in result) {
                    var apod = Apod()
                    apod.title = items.title
                    apod.explanation = items.content
                    apod.date = items.date
                    apod.url = items.imageurl
                    apodData.add(apod)
                }
                listner.onsuccess(apodData, true)
            }
        }
    }


    fun getRetrofitObject(): Retrofit? {
        val url: String = "https://api.nasa.gov/"
        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun isNetworkConnected(): Boolean {

        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetwork

        val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)

        return networkCapabilities != null &&
                networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    fun showProgress() {
        synchronized(dialogLock) {
            if ((dialog == null || !dialog!!.isShowing())
                && !activity.isFinishing()
            ) {
                dialog = Dialog(activity)
                dialog!!.setContentView(R.layout.layout_progress)
                dialog!!.setCancelable(false)
                val dialogWinfow: Window? = dialog!!.getWindow()
                val WMLP = dialogWinfow?.attributes
                WMLP?.gravity = Gravity.CENTER
                WMLP?.width = WindowManager.LayoutParams.WRAP_CONTENT
                dialog!!.show()
            }
        }
    }

    fun closeProgress() {
        synchronized(dialogLock) {
            dialog!!.dismiss()
        }
    }
}