package com.than.challengeschapter4catatanhutang.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.than.challengeschapter4catatanhutang.data.Kasir

@Dao
interface KasirDao {
    @Query("SELECT * FROM kasir WHERE username = :username AND password = :password")
    fun loginKasir(username: String, password: String): Boolean

    @Insert(onConflict = REPLACE)
    fun insertKasir(kasir: Kasir):Long

}