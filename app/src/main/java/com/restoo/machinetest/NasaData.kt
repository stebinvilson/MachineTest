package com.restoo.machinetest

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "NasaData")
class NasaData(@PrimaryKey(autoGenerate = true)
               var id: Int?,
               @ColumnInfo(name = "title") var title: String,
               @ColumnInfo(name = "description") var content: String,
               @ColumnInfo(name = "date") var date: String,
               @ColumnInfo(name = "imageurl") var imageurl: String)
