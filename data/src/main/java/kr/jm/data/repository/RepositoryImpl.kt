package kr.jm.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kr.jm.data.local.room.ItemDao
import kr.jm.data.remote.api.LottoApiService
import kr.jm.domain.model.ItemEntity
import kr.jm.domain.model.LottoNumber
import kr.jm.domain.repository.Repository
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