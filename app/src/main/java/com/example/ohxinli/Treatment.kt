package com.example.ohxinli

data class Treatment(
    val description: String,
    val plan_name: String,
    val treatment_session: Int,
    val price: Int,
    val regis_no: String,
    val treatment_id: String
)

data class Treatment2(
    val plan_name: String,
    val treatment_id: String
)