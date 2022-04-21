package com.example.ohxinli

import androidx.room.PrimaryKey

data class BookingClass(
    val booking_date: String,
    val booking_time: String,
    val session: Int,
    val rehab_name: String?,
    val treatment_plan: String,
    val treatment_price: Int,
    val user_ic: String?,
    val booking_status: String
)

data class BookingClass2(
    val booking_id: String,
    val booking_date: String,
    val booking_time: String,
    val rehab_name: String?,
    val treatment_plan: String,
    val booking_status: String
)

data class BookingClass3(
    val booking_date: String,
    val booking_time: String,
    val session: Int,
    val name: String,
    val user_ic: String
)

data class BookingClass4(
    val booking_date: String,
    val booking_time: String,
    val session: Int,
    val name: String,
    val user_ic: String,
    val status: String
)