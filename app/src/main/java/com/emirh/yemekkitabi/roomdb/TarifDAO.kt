package com.emirh.yemekkitabi.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.emirh.yemekkitabi.model.Tarif

@Dao
interface TarifDAO {

    @Query("SELECT *  FROM Tarif   ")
    fun getAll(): List<Tarif>

    /*id ye gore secme oto oladan gosterme:
    @Query("SELECT * FROM WHERE id = :id")
    fun findById(id: Int): Tarif
    */

    @Insert
    fun insert (tarif: Tarif) {
    }

    @Delete
    fun delete(tarif: Tarif) {
    }
}