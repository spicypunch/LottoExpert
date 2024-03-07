package kr.jm.lottoexpert.repository

import kr.jm.lottoexpert.data.LottoNumber
import kr.jm.lottoexpert.service.LottoApiService
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val apiService: LottoApiService
) : Repository {
    override suspend fun getLottoNum(drawNumber: String): LottoNumber {
        return apiService.getLottoNum(drawNumber = drawNumber)
    }
}