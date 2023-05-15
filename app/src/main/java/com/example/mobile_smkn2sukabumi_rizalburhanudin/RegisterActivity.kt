package com.example.mobile_smkn2sukabumi_rizalburhanudin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.mobile_smkn2sukabumi_rizalburhanudin.api.Connect
import com.example.mobile_smkn2sukabumi_rizalburhanudin.contracts.auth.RegisterRequest
import com.example.mobile_smkn2sukabumi_rizalburhanudin.databinding.ActivityRegisterBinding
import kotlin.concurrent.thread

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            val nama = binding.edtNama.text.toString()
            val username = binding.edtUsername.text.toString()
            val alamat = binding.edtPassword.text.toString()
            val password = binding.edtPassword.text.toString()
            val passwordConfirm = binding.edtPasswordConfirm.text.toString()

            if (nama.isNullOrEmpty() || username.isNullOrEmpty() || alamat.isNullOrEmpty() || password.isNullOrEmpty() || passwordConfirm.isNullOrEmpty()) {
                Toast.makeText(this@RegisterActivity, "Lengkapi semua data", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            if (password.trim() != passwordConfirm.trim()) {
                Toast.makeText(this@RegisterActivity, "Password tidak sama", Toast.LENGTH_SHORT)
                    .show()
            }

            thread {
                try {
                    val status = Connect.register(RegisterRequest(alamat, nama, password, passwordConfirm, username))

                    if (status) {
                        runOnUiThread {
                            Toast.makeText(
                                this@RegisterActivity,
                                "Registrasi berhasil",
                                Toast.LENGTH_SHORT
                            ).show()
                            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java).apply {
                                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            })
                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(
                                this@RegisterActivity,
                                "Registrasi gagal",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } catch (ex : Exception ) {
                    Log.d("err-register", ex.toString())
                }
            }
        }

        binding.btnSudahPunyaAkun.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            })
        }

    }
}