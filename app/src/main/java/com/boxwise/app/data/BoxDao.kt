package com.boxwise.app.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface BoxDao {
    @Query("SELECT * FROM boxes ORDER BY label")
    fun observeBoxes(): Flow<List<Box>>

    @Query("SELECT * FROM boxes WHERE id = :id")
    suspend fun getBox(id: Long): Box?

    @Insert
    suspend fun insert(box: Box): Long

    @Update
    suspend fun update(box: Box)

    @Delete
    suspend fun delete(box: Box)

    /** Global search: "where is X?" across every box. */
    @Query(
        """
        SELECT items.*, boxes.label AS boxLabel FROM items
        JOIN boxes ON boxes.id = items.boxId
        WHERE items.name LIKE '%' || :q || '%' OR items.notes LIKE '%' || :q || '%'
        ORDER BY items.name
        """
    )
    fun searchItems(q: String): Flow<List<ItemWithBox>>
}
