package com.than.challengeschapter4catatanhutang.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.than.challengeschapter4catatanhutang.data.Pengutang
import com.than.challengeschapter4catatanhutang.databinding.ListUtangItemBinding

class PengutangAdapter(
    private val listPengutang: List<Pengutang>,
    private val detail: (Pengutang)->Unit,
    private val delete: (Pengutang)->Unit,
    private val update: (Pengutang)->Unit
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

        holder.binding.btnDetil.setOnClickListener{
            detail.invoke(listPengutang[position])
        }
        holder.binding.btnDelete.setOnClickListener{
            delete.invoke(listPengutang[position])
        }
        holder.binding.btnEdit.setOnClickListener {
            update.invoke(listPengutang[position])
        }
    }

    override fun getItemCount(): Int {
        return listPengutang.size
    }
}