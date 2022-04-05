package com.than.challengeschapter4catatanhutang

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.than.challengeschapter4catatanhutang.adapter.PengutangAdapter
import com.than.challengeschapter4catatanhutang.database.UtangDatabase
import com.than.challengeschapter4catatanhutang.databinding.FormAddPengutangBinding
import com.than.challengeschapter4catatanhutang.databinding.FragmentHomepageBinding
import com.than.challengeschapter4catatanhutang.data.Pengutang
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

    override fun onResume() {
        super.onResume()
        fetchData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        utangDatabase = UtangDatabase.getInstance(requireContext())
        fetchData()

        //
        binding.fabAdd.setOnClickListener{
            val dialogBinding = FormAddPengutangBinding.inflate(LayoutInflater.from(requireContext()))
            val dialogBuilder = AlertDialog.Builder(requireContext())
            dialogBuilder.setView(dialogBinding.root)
            val dialog = dialogBuilder.create()
            dialog.setCancelable(false)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogBinding.btnCancel.setOnClickListener{
                dialog.dismiss()
            }
            dialogBinding.btnSubmit.setOnClickListener{
                val myDB = UtangDatabase.getInstance(requireContext())
                val dataPengutang = Pengutang(
                    null,
                    dialogBinding.etNamaPengutang.text.toString(),
                    dialogBinding.etJumlahHutang.text.toString().toInt(),
                    dialogBinding.etDeskripsi.text.toString(),
                    dialogBinding.tvTanggal.text.toString(),
                    "Sulthan"
                )
                lifecycleScope.launch(Dispatchers.IO){
                    val result = myDB?.pengutangdao()?.insertPengutang(dataPengutang)
                    runBlocking(Dispatchers.Main){
                        if (result != 0.toLong()){
                            Toast.makeText(
                                requireContext(),
                                "Pengutang ${dataPengutang.nama_pengutang} Berhasil Di tambahkan!",
                                Toast.LENGTH_SHORT
                            ).show()
                            fetchData()
                            dialog.dismiss()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Pengutang ${dataPengutang.nama_pengutang} Gagal Di tambahkan!",
                                Toast.LENGTH_SHORT
                            ).show()
                            dialog.dismiss()
                        }
                    }
                }
            }
            dialog.show()
        }
    }

    private fun fetchData(){
        lifecycleScope.launch(Dispatchers.IO){
            val listPengutang = utangDatabase?.pengutangdao()?.getAllPengutang()
            activity?.runOnUiThread{
                listPengutang?.let {
                    val adapter = PengutangAdapter(
                        it,
                        detail = { pengutang ->
                            AlertDialog.Builder(requireContext()).apply {
                                setMessage("${pengutang.deskripsi}")
                                setTitle("${pengutang.nama_pengutang}")
                                show()
                            }
                        },
                        delete = { pengutang ->
                            AlertDialog.Builder(requireContext())
                                .setPositiveButton("Iya"){_,_ ->
                                    val mDb = UtangDatabase.getInstance(requireContext())
                                    lifecycleScope.launch(Dispatchers.IO){
                                        val result = mDb?.pengutangdao()?.deletePengutang(pengutang)
                                        activity?.runOnUiThread{
                                            if(result != 0){
                                                Toast.makeText(
                                                    requireContext(),
                                                    "${pengutang.nama_pengutang} berhasil dihapus",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            } else {
                                                Toast.makeText(
                                                    requireContext(),
                                                    "${pengutang.nama_pengutang} gagal dihapus",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                        fetchData()
                                    }
                                }
                                .setNegativeButton("Tidak"){ dialog, _ ->
                                    dialog.dismiss()
                                }
                                .setMessage("Apakah anda yakin ingin menghapus ${pengutang.nama_pengutang}")
                                .setTitle("Konfirmasi Hapus")
                                .create()
                                .show()
                        }
                    )
                    binding.rvHomepage.adapter = adapter
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        UtangDatabase.destroyInstance()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}