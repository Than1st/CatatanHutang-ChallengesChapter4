package com.than.challengeschapter4catatanhutang

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.than.challengeschapter4catatanhutang.HomepageFragment.Companion.SHAREDFILE
import com.than.challengeschapter4catatanhutang.data.Kasir
import com.than.challengeschapter4catatanhutang.database.UtangDatabase
import com.than.challengeschapter4catatanhutang.databinding.FragmentRegisterBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private var utangDatabase: UtangDatabase? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        utangDatabase = UtangDatabase.getInstance(requireContext())
        val sharedPreferences = requireContext().getSharedPreferences(SHAREDFILE, Context.MODE_PRIVATE)
        binding.btnRegister.setOnClickListener {
            when {
                binding.etUsername.text.toString().isEmpty() || binding.etPassword.text.toString().isEmpty() || binding.etConfirmPassword.text.toString().isEmpty() -> {
                    Toast.makeText(requireContext(), "Form tidak boleh Kosong!", Toast.LENGTH_SHORT).show()
                }
                binding.etPassword.text.toString() == binding.etConfirmPassword.text.toString() -> {
                    val editor = sharedPreferences.edit()
                    editor.putString("username", binding.etUsername.text.toString())
                    editor.putString("password", binding.etPassword.text.toString())
                    editor.apply()
                    Toast.makeText(requireContext(), "Berhasil Daftar", Toast.LENGTH_SHORT).show()
                    val data = Kasir(null, binding.etUsername.text.toString(), binding.etPassword.text.toString())
                    lifecycleScope.launch(Dispatchers.IO){
                        val register = utangDatabase?.kasirDao()?.insertKasir(data)
                        runBlocking(Dispatchers.Main){
                            if (register != 0.toLong()){
                                Toast.makeText(requireContext(), "Berhasil Registrasi", Toast.LENGTH_SHORT).show()
                                findNavController().navigate(R.id.action_registerFragment_to_homepageFragment22)
                            } else {
                                Toast.makeText(requireContext(), "Gagal Registrasi", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
                else -> {
                    Toast.makeText(requireContext(), "Password Tidak Sama!", Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}