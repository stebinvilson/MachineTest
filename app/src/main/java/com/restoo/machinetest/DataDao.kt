package com.restoo.machinetest

import androidx.room.*


@Dao
interface DataDao {
    @Query("SELECT * FROM nasadata")
    fun getAll(): List<NasaData?>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(nasanfo: NasaData?)

    @Delete
    fun delete(nasanfo: NasaData?)

    @Update
    fun update(nasanfo: NasaData?)
}
