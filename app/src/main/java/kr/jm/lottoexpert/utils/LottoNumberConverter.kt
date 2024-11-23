package kr.jm.lottoexpert.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LottoNumberConverter {
    @TypeConverter
    fun fromLottoNumbers(value: List<Pair<Int, Int>>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toLottoNumbers(value: String): List<Pair<Int, Int>> {
        val listType = object : TypeToken<List<Pair<Int, Int>>>() {}.type
        return Gson().fromJson(value, listType)
    }
}