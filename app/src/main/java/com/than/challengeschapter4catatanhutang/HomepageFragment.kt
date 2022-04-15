@file:Suppress("DEPRECATION")

package com.than.challengeschapter4catatanhutang

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.than.challengeschapter4catatanhutang.adapter.PengutangAdapter
import com.than.challengeschapter4catatanhutang.database.UtangDatabase
import com.than.challengeschapter4catatanhutang.databinding.FragmentHomepageBinding
import com.than.challengeschapter4catatanhutang.data.Pengutang
import com.than.challengeschapter4catatanhutang.database.PengutangRepository
import com.than.challengeschapter4catatanhutang.databinding.FormPengutangBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.system.exitProcess

class HomepageFragment : Fragment() {
    private var _binding: FragmentHomepageBinding? = null
    private val binding get() = _binding!!
    private var utangDatabase: UtangDatabase? = null
    lateinit var repository: PengutangRepository
    companion object{
        const val SHAREDFILE = "kotlinsharedreferences"
    }
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
        repository = PengutangRepository(requireContext())
        utangDatabase = UtangDatabase.getInstance(requireContext())
        fetchData()

        var backPressed = false
        utangDatabase = UtangDatabase.getInstance(requireContext())
        val sharedPreferences = requireContext().getSharedPreferences(SHAREDFILE, Context.MODE_PRIVATE)
        val nama = sharedPreferences.getString("username", "default_username")
        requireActivity().onBackPressedDispatcher.addCallback(requireActivity()) {
            if (backPressed) {
                exitProcess(0)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Tekan sekali lagi untuk keluar!",
                    Toast.LENGTH_SHORT
                ).show()
                backPressed = true
                Handler().postDelayed({
                    backPressed = false
                }, 2000)
            }
        }
        binding.toolbar.tvWelcome.text = getString(R.string.dummy_selamat_datang_text, nama)
        binding.toolbar.btnTextLogout.setOnClickListener{
            AlertDialog.Builder(requireContext()).setPositiveButton("Logout"){ _, _ ->
                val editor = sharedPreferences.edit()
                editor.clear()
                editor.apply()
                Toast.makeText(requireContext(), "Anda Telah Logout!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_homepageFragment2_to_loginFragment)
            }.setNegativeButton("Batal"){dialog,_->
                dialog.dismiss()
            }
            .setMessage("Anda ingin Logout?")
            .setTitle("Konfirmasi Logout")
            .create()
            .show()
        }
        //
        binding.fabAdd.setOnClickListener{
            val dialogBinding = FormPengutangBinding.inflate(LayoutInflater.from(requireContext()))
            val dialogBuilder = AlertDialog.Builder(requireContext())
            dialogBuilder.setView(dialogBinding.root)
            val dialog = dialogBuilder.create()
            dialog.setCancelable(false)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialogBinding.tvTanggal.text = getDate()
            dialogBinding.btnCancel.setOnClickListener{
                dialog.dismiss()
            }
            dialogBinding.btnSubmit.setOnClickListener{
                val dataPengutang = Pengutang(
                    null,
                    dialogBinding.etNamaPengutang.text.toString(),
                    dialogBinding.etJumlahHutang.text.toString().toInt(),
                    dialogBinding.etDeskripsi.text.toString(),
                    getDate(),
                    nama.toString()
                )
                lifecycleScope.launch(Dispatchers.IO){
                    val result = repository.insertpengutang(dataPengutang)
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

    @SuppressLint("NewApi")
    private fun getDate(): String {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy (HH:mm)")

        return current.format(formatter)
    }

    private fun fetchData(){
        lifecycleScope.launch(Dispatchers.IO){
            val listPengutang =  repository.getALlPengutang()

            activity?.runOnUiThread{

                listPengutang?.let {
                    if (PengutangAdapter(it, {}, {}, {}).itemCount == 0){
                        binding.ivKosong.visibility = View.VISIBLE
                        binding.tvKosong.visibility = View.VISIBLE
                    } else {
                        binding.ivKosong.visibility = View.GONE
                        binding.tvKosong.visibility = View.GONE
                    }
                    val adapter = PengutangAdapter(
                        it,
                        detail = { pengutang ->
                            AlertDialog.Builder(requireContext()).apply {
                                setMessage(pengutang.deskripsi)
                                setTitle(pengutang.nama_pengutang)
                                show()
                            }
                        },
                        delete = { pengutang ->
                            AlertDialog.Builder(requireContext())
                                .setPositiveButton("Iya"){_,_ ->

                                    lifecycleScope.launch(Dispatchers.IO){
                                        val result = repository.deletePengutang(pengutang)
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
                        },
                        update = { pengutang ->
                            val sharedPreferences = requireContext().getSharedPreferences(SHAREDFILE, Context.MODE_PRIVATE)
                            val nama = sharedPreferences.getString("username", "default_username")
                            val dialogBinding = FormPengutangBinding.inflate(LayoutInflater.from(requireContext()))
                            val dialogBuilder = AlertDialog.Builder(requireContext())
                            dialogBuilder.setView(dialogBinding.root)
                            val dialog = dialogBuilder.create()
                            dialog.setCancelable(false)
                            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                            dialogBinding.tvTanggal.text = getDate()
                            dialogBinding.tvTitle.text = getString(R.string.edit_pengutang_text)
                            dialogBinding.btnSubmit.text = getString(R.string.update_text)
                            dialogBinding.etNamaPengutang.setText(pengutang.nama_pengutang)
                            dialogBinding.etJumlahHutang.setText("${pengutang.jumlah_utang}")
                            dialogBinding.etDeskripsi.setText(pengutang.deskripsi)
                            dialogBinding.btnCancel.setOnClickListener{
                                dialog.dismiss()
                            }
                            dialogBinding.btnSubmit.setOnClickListener{
                                val dataPengutang = Pengutang(
                                    pengutang.id_pengutang,
                                    dialogBinding.etNamaPengutang.text.toString(),
                                    dialogBinding.etJumlahHutang.text.toString().toInt(),
                                    dialogBinding.etDeskripsi.text.toString(),
                                    getDate(),
                                    nama.toString()
                                )
                                lifecycleScope.launch(Dispatchers.IO){
                                    val result = repository.updatePengutang(dataPengutang)
                                    runBlocking(Dispatchers.Main){
                                        if (result != 0){
                                            Toast.makeText(
                                                requireContext(),
                                                "Pengutang ${dataPengutang.nama_pengutang} Berhasil Di Update!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            fetchData()
                                            dialog.dismiss()
                                        } else {
                                            Toast.makeText(
                                                requireContext(),
                                                "Pengutang ${dataPengutang.nama_pengutang} Gagal Di update!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            dialog.dismiss()
                                        }
                                    }
                                }
                            }
                            dialog.show()
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