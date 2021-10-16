package com.restoo.machinetest.Listner

import com.restoo.machinetest.Apod

interface MainActivityListner {

    fun onsuccess(data :List<Apod>,local : Boolean)
    fun onfail()
}