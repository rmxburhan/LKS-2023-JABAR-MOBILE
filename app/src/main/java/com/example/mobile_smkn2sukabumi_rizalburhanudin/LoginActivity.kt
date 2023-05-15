package com.example.mobile_smkn2sukabumi_rizalburhanudin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.mobile_smkn2sukabumi_rizalburhanudin.api.Connect
import com.example.mobile_smkn2sukabumi_rizalburhanudin.contracts.auth.LoginRequest
import com.example.mobile_smkn2sukabumi_rizalburhanudin.databinding.ActivityLoginBinding
import kotlin.concurrent.thread

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val username = binding.edtUsername.text.toString()
            val password = binding.edtPassword.text.toString()
            if (username.isNullOrEmpty() || password.isNullOrEmpty()) {
                Toast.makeText(this, "Username dan password tidak boleh kosong", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
            thread {
                try {
                    val status = Connect.login(LoginRequest(password, username))

                    if (status) {
                        startActivity(Intent(this, HomeActivity::class.java))
                        runOnUiThread {
                            binding.edtPassword.text.clear()
                            binding.edtUsername.text.clear()
                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(
                                this@LoginActivity,
                                "Login gagal, username atau password salah",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } catch (ex : Exception) {
                    Log.d("err-login", ex.toString())
                }
            }
        }

        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            })
        }
    }
}