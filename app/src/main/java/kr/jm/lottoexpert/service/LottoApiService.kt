package kr.jm.lottoexpert.service

import kr.jm.lottoexpert.data.LottoNumber
import retrofit2.http.GET
import retrofit2.http.Query

interface LottoApiService {
    @GET("common.do")
    suspend fun getLottoNum(
        @Query("method") method: String = "getLottoNumber",
        @Query("drwNo") drawNumber: String
    ): LottoNumber
}
