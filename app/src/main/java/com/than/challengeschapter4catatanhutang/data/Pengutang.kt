package com.than.challengeschapter4catatanhutang.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class Pengutang(
    @PrimaryKey(autoGenerate = true) var id_pengutang: Int?,
    @ColumnInfo(name = "nama_pengutang") var nama_pengutang: String,
    @ColumnInfo(name = "jumlah_utang") var jumlah_utang: Int,
    @ColumnInfo(name = "deskripsi") var deskripsi: String,
    @ColumnInfo(name = "tanggal") var tanggal: String,
    @ColumnInfo(name = "kasir") var nama_kasir: String
) : Parcelable
