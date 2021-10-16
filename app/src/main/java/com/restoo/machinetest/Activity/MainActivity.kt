package com.restoo.machinetest.Activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.restoo.machinetest.Adapter.ListAdapter
import com.restoo.machinetest.Apod
import com.restoo.machinetest.AppDatabase
import com.restoo.machinetest.Helper.MainActivityHelper
import com.restoo.machinetest.Listner.ApodItemListner
import com.restoo.machinetest.Listner.MainActivityListner
import com.restoo.machinetest.NasaData
import com.restoo.machinetest.R

class MainActivity : AppCompatActivity(), MainActivityListner, ApodItemListner {
    lateinit var mRvApodList: RecyclerView
    lateinit var mAdapter: ListAdapter
    lateinit var mTxtTitle: TextView
    lateinit var helper: MainActivityHelper
    lateinit var mDb: AppDatabase
    lateinit var activity: Activity
    lateinit var mTxtNoData: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.hide()

        helper = MainActivityHelper(applicationContext, this, this)
        setUI()
        setData()
    }


    private fun setUI() {
        mTxtTitle = findViewById(R.id.txt_title)
        mRvApodList = findViewById(R.id.rv_apod)
        mRvApodList.layoutManager = LinearLayoutManager(this)
        mRvApodList.setItemAnimator(DefaultItemAnimator())
        mTxtNoData = findViewById(R.id.no_data)
    }


    private fun setData() {

        mTxtTitle.setText(getString(R.string.apod_listing))
        helper.callApi()

        mDb = Room.databaseBuilder(this, AppDatabase::class.java, "RoomDatabase")
            .allowMainThreadQueries().build()
        mDb = AppDatabase.getInstance(applicationContext)
    }


    override fun onsuccess(responsedata: List<Apod>, local: Boolean) {

        if (responsedata.size > 0) {
            mRvApodList.visibility = View.VISIBLE

            val adapter = ListAdapter(responsedata, applicationContext, this)
            mRvApodList.adapter = adapter
            if (!local) {
                doAsync(responsedata, applicationContext).execute()
            }
            mTxtNoData.visibility = View.GONE
        } else {

            mTxtNoData.visibility = View.VISIBLE
        }
    }


    class doAsync(var response: List<Apod>, var con: Context) : AsyncTask<Void, Void, Boolean>() {


        override fun doInBackground(vararg params: Void?): Boolean? {
            var mDb: AppDatabase =
                Room.databaseBuilder(con, AppDatabase::class.java, "RoomDatabase")
                    .allowMainThreadQueries().build()

            for (items in response) {
                var data = NasaData(
                    id = null,
                    items.title,
                    items.explanation,
                    items.date,
                    items.url
                )
                mDb.datadao().insert(data)
            }

            return true
        }

        override fun onPostExecute(result: Boolean?) {
            super.onPostExecute(result)
        }
    }

    override fun onfail() {

    }

    override fun onItemClick(item: Apod) {
        var intent = Intent(this, ApodDetailActivity::class.java)
        intent.putExtra(getString(R.string.apoddata), item)
        startActivity(intent)
    }
}
