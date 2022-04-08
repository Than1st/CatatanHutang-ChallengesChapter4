@file:Suppress("DEPRECATION")

package com.than.challengeschapter4catatanhutang

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.than.challengeschapter4catatanhutang.HomepageFragment.Companion.SHAREDFILE
import com.than.challengeschapter4catatanhutang.database.UtangDatabase
import com.than.challengeschapter4catatanhutang.databinding.FragmentLoginBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.system.exitProcess

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private var utangDatabase: UtangDatabase? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var backPressed = false
        utangDatabase = UtangDatabase.getInstance(requireContext())
        val sharedPreferences = requireContext().getSharedPreferences(SHAREDFILE, Context.MODE_PRIVATE)
        requireActivity().onBackPressedDispatcher.addCallback(requireActivity()) {
            if (backPressed) {
                exitProcess(0)
            } else {
                Toast.makeText(requireContext(), "Tekan sekali lagi untuk keluar!", Toast.LENGTH_SHORT).show()
                backPressed = true
                Handler().postDelayed({
                    backPressed = false
                }, 2000)
            }
        }
        binding.btnRegister.setOnClickListener{
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        binding.btnLogin.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO){
                val login = utangDatabase?.kasirDao()?.loginKasir(binding.etUsername.text.toString(), binding.etPassword.text.toString())
                runBlocking(Dispatchers.Main){
                    when {
                        binding.etUsername.text.toString().isEmpty() || binding.etPassword.text.toString().isEmpty() -> {
                            Toast.makeText(requireContext(), "Form tidak boleh Kosong!", Toast.LENGTH_SHORT).show()
                        }
                         login == true-> {
                             val editor: SharedPreferences.Editor = sharedPreferences.edit()
                             editor.putString("username", binding.etUsername.text.toString())
                             editor.putString("password", binding.etPassword.text.toString())
                             editor.apply()
                             Toast.makeText(requireContext(), "Login Success", Toast.LENGTH_SHORT).show()
                             findNavController().navigate(R.id.action_loginFragment_to_homepageFragment2)
                        }
                        else -> {
                            Toast.makeText(requireContext(), "Username/Password Salah!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}