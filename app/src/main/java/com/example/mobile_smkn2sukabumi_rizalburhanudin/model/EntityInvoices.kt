package com.example.mobile_smkn2sukabumi_rizalburhanudin.model

data class EntityInvoices(
    val created_at: String,
    val id: Int,
    val invoice_num: String,
    val price_total: Int,
    val qty_total: Int,
    val user_id: Int
)