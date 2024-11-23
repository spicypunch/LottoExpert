package kr.jm.lottoexpert.repository

import kotlinx.coroutines.flow.Flow
import kr.jm.lottoexpert.data.ItemEntity
import kr.jm.lottoexpert.data.LottoNumber

interface Repository {

    suspend fun getLottoNum(drawNumber: String): LottoNumber

    suspend fun getItem(startNum: String, endNum: String): Flow<ItemEntity>

    suspend fun getAllItems(): Flow<List<ItemEntity>>

    suspend fun insertItem(itemEntity: ItemEntity)

    suspend fun deleteItem(itemEntity: ItemEntity)
}