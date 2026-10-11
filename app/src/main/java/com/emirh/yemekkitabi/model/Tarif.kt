package com.emirh.yemekkitabi.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// data classda yapabilrsiniz normal clasda bu roomdb kabul ediyor.
// entitty eklemeyi unutma bu sayfadan yazıyoz:https://developer.android.com/training/data-storage/room?hl=tr
@Entity
data class Tarif(

    @ColumnInfo(name = "isim")
    var isim: String, // zorunlu olmasını istiyorusanız sori isareti koymazsınız benim gibi

    @ColumnInfo(name ="malzeme")
    var malzeme : String,

    @ColumnInfo(name = "gorsel")
    var gorsel : ByteArray
     // gorseller bytearray olarak tutulur.veritabanında png jpeg oalrak tutulmaz yani

){

    @PrimaryKey(autoGenerate = true) // sen kendin olustur demek 0 1 2 kendisi olusturcak
    var id = 0
}