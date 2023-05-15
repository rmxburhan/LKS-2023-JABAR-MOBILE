package com.example.mobile_smkn2sukabumi_rizalburhanudin.model

data class EntityMenu(
    val id: Int,
    val image: String,
    val name: String,
    val price: Int,
    val rating: Double,
    var qty : Int = 0
)