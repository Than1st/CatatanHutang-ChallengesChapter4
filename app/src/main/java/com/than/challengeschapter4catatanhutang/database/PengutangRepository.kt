package com.than.challengeschapter4catatanhutang.database

import android.content.Context
import androidx.annotation.WorkerThread
import com.than.challengeschapter4catatanhutang.data.Pengutang

class PengutangRepository(context: Context) {
    val mDb = UtangDatabase.getInstance(context)

    @WorkerThread
    suspend fun getALlPengutang(): List<Pengutang>? {
        return mDb?.pengutangdao()?.getAllPengutang()
    }

    suspend fun insertpengutang(pengutang: Pengutang): Long? {
        return mDb?.pengutangdao()?.insertPengutang(pengutang)
    }

    suspend fun  updatePengutang(pengutang: Pengutang): Int? {
        return mDb?.pengutangdao()?.updatePengutang(pengutang)
    }

    suspend fun deletePengutang(pengutang: Pengutang): Int? {
        return mDb?.pengutangdao()?.deletePengutang(pengutang)
    }
}