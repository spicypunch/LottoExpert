package kr.jm.lottoexpert

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.initialize
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class LottoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Firebase.initialize(this)
        FirebaseCrashlytics.getInstance().isCrashlyticsCollectionEnabled = true
    }
}