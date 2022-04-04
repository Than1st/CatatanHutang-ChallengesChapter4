package com.than.challengeschapter4catatanhutang.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.than.challengeschapter4catatanhutang.dao.KasirDao
import com.than.challengeschapter4catatanhutang.dao.PengutangDao
import com.than.challengeschapter4catatanhutang.model.Kasir
import com.than.challengeschapter4catatanhutang.model.Pengutang

@Database(entities = [Pengutang::class, Kasir::class], version = 1)
abstract class UtangDatabase : RoomDatabase() {
    abstract fun pengutangdao(): PengutangDao
    abstract fun kasirDao(): KasirDao
    companion object{
        private var INSTANCE: UtangDatabase? = null
        fun getInstance(context: Context):UtangDatabase?{
            if(INSTANCE != null){
                synchronized(UtangDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, UtangDatabase::class.java, "utangDatabase.db").build()
                }
            }
            return INSTANCE
        }
        fun destroyInstance(){
            INSTANCE = null
        }
    }
}