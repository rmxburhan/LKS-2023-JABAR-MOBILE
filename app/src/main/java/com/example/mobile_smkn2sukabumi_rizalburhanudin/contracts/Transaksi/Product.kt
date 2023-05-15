package com.example.mobile_smkn2sukabumi_rizalburhanudin.contracts.Transaksi

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: Int,
    val image: String,
    val name: String,
    val price: Int,
    val rating: Double
) : Parcelable