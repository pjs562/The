package unist.pjs.the.extends

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

object Preferences {
    private lateinit var preferences: SharedPreferences

    private const val USER_ID: String = "userId"
    private const val USER_NAME: String = "userName"
    private const val USER_AGE: String = "age"
    private const val USER_GROUP: String = "group"
    private const val USER_IMAGE: String = "image"
    private const val USER_BIO: String = "bio"
    private const val USER_REMAIN_HEART: String = "remainHeart"
    private const val USER_REMAIN_LIKE: String = "remainLike"
    private const val USER_GENDER: String = "gender"
    private const val USER_ADMIN: String = "admin"
    private const val FCM_TOKEN: String = "fcmToken"

    fun Application.preferencesInit() {
        preferences = applicationContext.getSharedPreferences("the", Context.MODE_PRIVATE)
    }

    var userId
        get() = preferences.getString(USER_ID, "") ?: ""
        set(value) {
            preferences.edit().putString(USER_ID, value).apply()
        }


    var userName
        get() = preferences.getString(USER_NAME, "") ?: ""
        set(value) {
            preferences.edit().putString(USER_NAME, value).apply()
        }

    var userAge
        get() = preferences.getString(USER_AGE, "") ?: ""
        set(value) {
            preferences.edit().putString(USER_AGE, value).apply()
        }

    var userGroup
        get() = preferences.getString(USER_GROUP, "") ?: ""
        set(value) {
            preferences.edit().putString(USER_GROUP, value).apply()
        }

    var userImage
        get() = preferences.getString(USER_IMAGE, "") ?: ""
        set(value) {
            preferences.edit().putString(USER_IMAGE, value).apply()
        }

    var userBio
        get() = preferences.getString(USER_BIO, "") ?: ""
        set(value) {
            preferences.edit().putString(USER_BIO, value).apply()
        }

    var remainHeart
        get() = preferences.getInt(USER_REMAIN_HEART, 0)
        set(value) {
            preferences.edit().putInt(USER_REMAIN_HEART, value).apply()
        }

    var remainLike
        get() = preferences.getInt(USER_REMAIN_LIKE, 0)
        set(value) {
            preferences.edit().putInt(USER_REMAIN_LIKE, value).apply()
        }

    var userGender
        get() = preferences.getString(USER_GENDER, "") ?: ""
        set(value) {
            preferences.edit().putString(USER_GENDER, value).apply()
        }

    var userAdmin
        get() = preferences.getString(USER_ADMIN, "false") ?: "false"
        set(value) {
            preferences.edit().putString(USER_ADMIN, value).apply()
        }

    var fcmToken
        get() = preferences.getString(FCM_TOKEN, "none") ?: "none"
        set(value) {
            preferences.edit().putString(FCM_TOKEN, value).apply()
        }
}