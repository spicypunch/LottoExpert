package kr.jm.domain.repository

import kotlinx.coroutines.flow.Flow
import kr.jm.domain.model.ItemEntity
import kr.jm.domain.model.LottoNumber

interface Repository {

    suspend fun getLottoNum(drawNumber: String): LottoNumber

    suspend fun getItem(startNum: String, endNum: String): Flow<ItemEntity>

    suspend fun getAllItems(): Flow<List<ItemEntity>>

    suspend fun insertItem(itemEntity: ItemEntity)

    suspend fun deleteItem(itemEntity: ItemEntity)
}