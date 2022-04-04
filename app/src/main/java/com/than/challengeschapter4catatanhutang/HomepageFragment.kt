package com.than.challengeschapter4catatanhutang

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.than.challengeschapter4catatanhutang.adapter.PengutangAdapter
import com.than.challengeschapter4catatanhutang.database.UtangDatabase
import com.than.challengeschapter4catatanhutang.databinding.FormAddPengutangBinding
import com.than.challengeschapter4catatanhutang.databinding.FragmentHomepageBinding
import com.than.challengeschapter4catatanhutang.model.Pengutang
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class HomepageFragment : Fragment() {
    private var _binding: FragmentHomepageBinding? = null
    private val binding get() = _binding!!
    private var utangDatabase: UtangDatabase? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomepageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        utangDatabase = UtangDatabase.getInstance(requireContext())
        binding.rvHomepage.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        fetchData()
        binding.fabAdd.setOnClickListener{
            val dialogBinding = FormAddPengutangBinding.inflate(LayoutInflater.from(requireContext()))
            val dialogBuilder = AlertDialog.Builder(requireContext())
            dialogBuilder.setView(dialogBinding.root)
            val dialog = dialogBuilder.create()
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogBinding.btnSubmit.setOnClickListener{
                val dataPengutang = Pengutang(
                    null,
                    dialogBinding.etNamaPengutang.text.toString(),
                    dialogBinding.etJumlahHutang.text.toString().toInt(),
                    dialogBinding.etDeskripsi.text.toString(),
                    dialogBinding.tvTanggal.text.toString(),
                    "Sulthan"
                )
                lifecycleScope.launch(Dispatchers.IO){
                    val result = utangDatabase?.pengutangdao()?.insertPengutang(dataPengutang)
                    runBlocking(Dispatchers.Main){
                        if (result != 0.toLong()){
                            Toast.makeText(
                                requireContext(),
                                "Pengutang ${dataPengutang.nama_pengutang} Berhasil Di tambahkan!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Pengutang ${dataPengutang.nama_pengutang} Gagal Di tambahkan!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        dialog.dismiss()
                    }
                }
            }
            dialog.show()
        }
    }
    private fun fetchData(){
        lifecycleScope.launch(Dispatchers.IO){
            val listPengutang = utangDatabase?.pengutangdao()?.getAllPengutang()
            runBlocking(Dispatchers.Main) {
                listPengutang?.let {
                    val adapter = PengutangAdapter(it)
                    binding.rvHomepage.adapter = adapter
                }
            }
        }
    }

}