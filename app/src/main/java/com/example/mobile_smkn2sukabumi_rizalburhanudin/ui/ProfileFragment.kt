package com.example.mobile_smkn2sukabumi_rizalburhanudin.ui

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.mobile_smkn2sukabumi_rizalburhanudin.R
import com.example.mobile_smkn2sukabumi_rizalburhanudin.api.Connect
import kotlin.concurrent.thread


class ProfileFragment : Fragment() {
    private lateinit var txtNama : TextView
    private lateinit var txtAlamat : TextView
    private lateinit var imgProfile : ImageView
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        txtNama = view.findViewById(R.id.txtNama)
        txtAlamat = view.findViewById(R.id.txtAlamat)
        imgProfile = view.findViewById(R.id.imgProfile)
        sharedPreferences = activity?.getSharedPreferences("user-data", MODE_PRIVATE)!!
        txtNama.text = sharedPreferences.getString("nama", "Nama user")
        txtAlamat.text = sharedPreferences.getString("alamat", "Alamat")
        val image = sharedPreferences.getString("image", null)
        if (!image.isNullOrEmpty()) {
            Glide.with(this@ProfileFragment)
                .load(image)
                .into(imgProfile)
        }
        getProfile()

        val btnLogout = view.findViewById<LinearLayout>(R.id.btnLogout1)
        btnLogout.setOnClickListener {
            activity?.finish()
            sharedPreferences.edit().clear().apply()
        }
        return view
    }

    fun getProfile() {
        thread {
            try {
                val data = Connect.getProfile()
                if (data != null) {
                    activity?.runOnUiThread {
                        txtNama.text = data.name
                        txtAlamat.text = data.address
                        Glide.with(this@ProfileFragment)
                            .load(data.image)
                            .into(imgProfile)
                        sharedPreferences
                            .edit()
                            .putString("nama", data.name)
                            .putString("alamat", data.address)
                            .putString("image", data.image)
                            .apply()
                    }
                } else {
                    activity?.runOnUiThread {
                        Toast.makeText(activity, "Gagal mengambil data user", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (ex : Exception) {
                Log.d("err-getProfile", ex.toString())
            }
        }
    }
}