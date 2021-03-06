package com.than.challengeschapter4catatanhutang.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class Kasir(
    @PrimaryKey(autoGenerate = true) var id_kasir: Int?,
    @ColumnInfo(name = "username") var username: String,
    @ColumnInfo(name = "password") var password: String
) : Parcelable
