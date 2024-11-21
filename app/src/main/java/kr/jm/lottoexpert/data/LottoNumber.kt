package kr.jm.lottoexpert.data


import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class LottoNumber(
    val bonusNo: Int, // 2
    val drwNo: Int, // 1109
    val drwNoDate: String, // 2024-03-02
    val drwtNo1: Int, // 10
    val drwtNo2: Int, // 12
    val drwtNo3: Int, // 13
    val drwtNo4: Int, // 19
    val drwtNo5: Int, // 33
    val drwtNo6: Int, // 40
//    val firstAccumamnt: Long, // 26933998875
    val firstPrzwnerCo: Int, // 17
//    val firstWinamnt: Int, // 1584352875
    val returnValue: String, // success
    val totSellamnt: Long // 117196327000
) : Parcelable