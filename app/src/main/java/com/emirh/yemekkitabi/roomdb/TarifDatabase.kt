package com.emirh.yemekkitabi.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.emirh.yemekkitabi.model.Tarif

/*
@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
 */

// otomatik dokumantasyondakini yapıstırdık.Tarif dizi icinde vermek zorundayız
// version 1 de diyelimki yeni sutun ekledik version 2 diye degistircez
@Database(entities = [Tarif::class], version = 1)
abstract class TarifDatabase : RoomDatabase() {
    abstract fun tarifDao(): TarifDAO
}

