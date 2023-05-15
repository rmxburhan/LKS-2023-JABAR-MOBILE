package com.example.mobile_smkn2sukabumi_rizalburhanudin.contracts.Transaksi

data class TransaksiRequest(
    val items: List<ItemTransaksi>,
    val price_total: Int,
    val qty_total: Int
)