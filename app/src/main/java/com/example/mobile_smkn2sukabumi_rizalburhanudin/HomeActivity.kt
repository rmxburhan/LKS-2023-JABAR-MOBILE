package com.example.mobile_smkn2sukabumi_rizalburhanudin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.mobile_smkn2sukabumi_rizalburhanudin.databinding.ActivityHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)



        val nav_host = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        val nav_con = findViewById<BottomNavigationView>(R.id.nav_con)

        val navController = nav_host.navController
        nav_con.setupWithNavController(navController)
    }
}