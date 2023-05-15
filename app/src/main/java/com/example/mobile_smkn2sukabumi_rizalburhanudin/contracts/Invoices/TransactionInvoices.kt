package com.example.mobile_smkn2sukabumi_rizalburhanudin.contracts.Invoices

data class TransactionInvoices(
    val created_at: String,
    val invoice_num: String,
    val price_total: Int,
    val qty_total: Int
)