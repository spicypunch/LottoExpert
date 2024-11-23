package kr.jm.lottoexpert.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kr.jm.lottoexpert.data.ItemEntity
import kr.jm.lottoexpert.data.LottoNumber
import kr.jm.lottoexpert.room.ItemDao
import kr.jm.lottoexpert.service.LottoApiService
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val apiService: LottoApiService,
    private val itemDao: ItemDao
) : Repository {
    override suspend fun getLottoNum(drawNumber: String): LottoNumber {
        return apiService.getLottoNum(drawNumber = drawNumber)
    }

    override suspend fun getItem(startNum: String, endNum: String): Flow<ItemEntity> {
        return itemDao.getItem(startNum, endNum)
    }

    override suspend fun getAllItems(): Flow<List<ItemEntity>> {
        return itemDao.getAllItems()
    }

    override suspend fun insertItem(itemEntity: ItemEntity) = withContext(Dispatchers.IO) {
        itemDao.insertItem(itemEntity)
    }

    override suspend fun deleteItem(itemEntity: ItemEntity) = withContext(Dispatchers.IO) {
        itemDao.deleteItem(itemEntity)
    }
}