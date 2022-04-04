package com.than.challengeschapter4catatanhutang.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.than.challengeschapter4catatanhutang.model.Pengutang

@Dao
interface PengutangDao {
    @Query("SELECT * FROM pengutang")
    fun getAllPengutang(): List<Pengutang>

    @Insert(onConflict = REPLACE)
    fun insertPengutang(pengutang: Pengutang):Long

    @Update
    fun updatePengutang(pengutang: Pengutang):Int

    @Delete
    fun deletePengutang(pengutang: Pengutang):Int
}