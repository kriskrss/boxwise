package com.boxwise.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/** A physical storage box. Label is the human identifier ("Box 6", "Diwali decor"). */
@Entity(tableName = "boxes")
data class Box(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val label: String,
    val location: String? = null,
    val photoPath: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
