package com.boxwise.app.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/** A thing inside a box. */
@Entity(
    tableName = "items",
    foreignKeys = [
        ForeignKey(
            entity = Box::class,
            parentColumns = ["id"],
            childColumns = ["boxId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("boxId")]
)
data class Item(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val boxId: Long,
    val name: String,
    val quantity: Int = 1,
    val notes: String? = null,
    val photoPath: String? = null
)
