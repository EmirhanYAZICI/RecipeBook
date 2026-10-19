package com.emirh.yemekkitabi.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.emirh.yemekkitabi.model.Tarif
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface TarifDAO {

    // list yerine flowable yapdık rxjava kullanrak daha performanslı
    @Query("SELECT *  FROM Tarif   ")
    fun getAll(): Flowable<List<Tarif>>

    //id ye gore secme oto olandan gosterme:
    @Query("SELECT * FROM Tarif WHERE id = :id")
    fun findById(id: Int): Flowable<Tarif>




    @Insert
    fun insert (tarif: Tarif): Completable


    @Delete
    fun delete(tarif: Tarif) : Completable
}