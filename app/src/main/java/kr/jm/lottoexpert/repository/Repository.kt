package kr.jm.lottoexpert.repository

import kr.jm.lottoexpert.data.LottoNumber

interface Repository {

    suspend fun getLottoNum(drawNumber: String): LottoNumber
}