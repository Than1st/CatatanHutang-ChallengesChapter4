package com.than.challengeschapter4catatanhutang.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.than.challengeschapter4catatanhutang.data.Pengutang
import com.than.challengeschapter4catatanhutang.databinding.ListUtangItemBinding
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvNamaPengutang.text = listPengutang[position].nama_pengutang
        holder.binding.tvJumlahHutang.text = currency(listPengutang[position].jumlah_utang)
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
    private fun currency(angka: Int): String {
        val kursIndonesia = DecimalFormat.getCurrencyInstance() as DecimalFormat
        val formatRp = DecimalFormatSymbols()

        formatRp.currencySymbol = "Rp "
        formatRp.monetaryDecimalSeparator = ','
        formatRp.groupingSeparator = '.'

        kursIndonesia.decimalFormatSymbols = formatRp
        return kursIndonesia.format(angka).dropLast(3)
    }
}