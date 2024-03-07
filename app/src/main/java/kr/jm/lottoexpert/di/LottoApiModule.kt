package kr.jm.lottoexpert.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kr.jm.lottoexpert.service.LottoApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LottoApiModule {
    private const val BASE_URL = "https://www.dhlottery.co.kr/"

    @Singleton
    @Provides
    fun provideOpenApiService(): LottoApiService {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(LottoApiService::class.java)
    }
}