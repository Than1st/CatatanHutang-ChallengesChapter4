package com.than.challengeschapter4catatanhutang.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.than.challengeschapter4catatanhutang.data.Pengutang
import com.than.challengeschapter4catatanhutang.databinding.ListUtangItemBinding

class PengutangAdapter(
    private val listPengutang: List<Pengutang>,
): RecyclerView.Adapter<PengutangAdapter.ViewHolder>() {

    class ViewHolder(val binding: ListUtangItemBinding): RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ListUtangItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: PengutangAdapter.ViewHolder, position: Int) {
        holder.binding.tvNamaPengutang.text = listPengutang[position].nama_pengutang
        holder.binding.tvJumlahHutang.text = listPengutang[position].jumlah_utang.toString()
        holder.binding.tvNamaKasir.text = listPengutang[position].nama_kasir
        holder.binding.tvTanggalUtang.text = listPengutang[position].tanggal

    }

    override fun getItemCount(): Int {
        return listPengutang.size
    }
}