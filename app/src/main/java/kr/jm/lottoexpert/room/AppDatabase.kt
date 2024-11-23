package kr.jm.lottoexpert.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kr.jm.lottoexpert.data.ItemEntity
import kr.jm.lottoexpert.utils.LottoNumberConverter

@Database(entities = [ItemEntity::class], version = 1, exportSchema = true)
@TypeConverters(LottoNumberConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
}