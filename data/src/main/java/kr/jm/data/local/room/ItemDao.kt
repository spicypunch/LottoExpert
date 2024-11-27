package kr.jm.data.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kr.jm.domain.model.ItemEntity

@Dao
interface ItemDao {
    @Query("SELECT * FROM ItemEntity ORDER BY id")
    fun getAllItems(): Flow<List<ItemEntity>>

    @Query("SELECT * FROM ItemEntity WHERE :startNum = startNum AND endNum = :endNum")
    fun getItem(startNum: String, endNum: String): Flow<ItemEntity>

    @Insert
    fun insertItem(itemEntity: ItemEntity)

    @Delete
    fun deleteItem(itemEntity: ItemEntity)

}