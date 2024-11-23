package kr.jm.lottoexpert.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class ItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "startNum") val startNum: String,
    @ColumnInfo(name = "endNum") val endNum: String,
    @ColumnInfo(name = "lottoNumbers") val lottoNumbers: List<Pair<Int, Int>>
) : Parcelable
