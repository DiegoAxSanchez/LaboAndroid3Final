package com.example.inventory.data

import java.text.NumberFormat

class VolModel(
    val code: Int?,
    val depart: String,
    val destination: String,
    val transporteur: String,
    )


    fun isEntryValid(
        code: String,
        depart: String,
        destination : String,
        transporteur: String
    ): Boolean {
        if (code.isBlank() || depart.isBlank() || destination.isBlank() || transporteur.isBlank()) {
            return false
        }
        return true
    }

