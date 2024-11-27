package kr.jm.data.remote.api

import kr.jm.domain.model.LottoNumber
import retrofit2.http.GET
import retrofit2.http.Query

interface LottoApiService {
    @GET("common.do")
    suspend fun getLottoNum(
        @Query("method") method: String = "getLottoNumber",
        @Query("drwNo") drawNumber: String
    ): LottoNumber
}
