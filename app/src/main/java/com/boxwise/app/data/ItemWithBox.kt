package com.boxwise.app.data

import androidx.room.Embedded

/** Item joined with its box's label — the "where is X?" result row. */
data class ItemWithBox(
    @Embedded val item: Item,
    val boxLabel: String
)
