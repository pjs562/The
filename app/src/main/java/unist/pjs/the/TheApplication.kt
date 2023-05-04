package unist.pjs.the

import android.app.Application
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.HiltAndroidApp
import unist.pjs.the.extends.Preferences
import unist.pjs.the.extends.Preferences.preferencesInit

@HiltAndroidApp
class TheApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        preferencesInit()
    }
}