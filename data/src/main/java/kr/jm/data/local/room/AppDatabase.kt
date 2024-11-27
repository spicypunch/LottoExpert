package kr.jm.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kr.jm.domain.model.ItemEntity

@Database(entities = [ItemEntity::class], version = 1, exportSchema = true)
@TypeConverters(LottoNumberConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
}