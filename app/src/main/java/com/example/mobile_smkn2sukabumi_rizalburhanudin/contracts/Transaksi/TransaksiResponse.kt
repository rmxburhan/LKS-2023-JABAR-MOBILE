package com.example.mobile_smkn2sukabumi_rizalburhanudin.contracts.Transaksi

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TransaksiResponse(
    val invoice_details: List<InvoiceDetail>,
    val invoice_num: String,
    val price_total: Int,
    val qty_total: Int
) : Parcelable