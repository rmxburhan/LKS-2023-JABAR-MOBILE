package com.example.mobile_smkn2sukabumi_rizalburhanudin.contracts.Transaksi

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class InvoiceDetail(
    val id: Int,
    val product: Product,
    val qty: Int,
    val subtotal: Int
) : Parcelable