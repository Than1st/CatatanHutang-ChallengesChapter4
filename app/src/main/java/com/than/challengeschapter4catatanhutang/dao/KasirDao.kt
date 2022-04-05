package com.than.challengeschapter4catatanhutang.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.than.challengeschapter4catatanhutang.data.Kasir

@Dao
interface KasirDao {
    @Query("SELECT * FROM kasir")
    fun getAllKasir(): List<Kasir>

    @Insert(onConflict = REPLACE)
    fun insertKasir(kasir: Kasir):Long

    @Update
    fun updateKasir(kasir: Kasir):Int

    @Delete
    fun deleteKasir(kasir: Kasir):Int
}