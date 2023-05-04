package unist.pjs.the.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import unist.pjs.the.MainActivity
import unist.pjs.the.R
import unist.pjs.the.extends.Preferences

/**
 * 앱이 실행되어 있는 상태에서, fcm 을 받았을 경우 처리
 */
class MyFirebaseMessagingService : FirebaseMessagingService() {
    /** 푸시 알림으로 보낼 수 있는 메세지는 2가지
     * 1. Notification: 앱이 실행중(포그라운드)일 떄만 푸시 알림이 옴
     * 2. Data: 실행중이거나 백그라운드(앱이 실행중이지 않을때) 알림이 옴 -> TODO: 대부분 사용하는 방식 */

    private val TAG = "FirebaseService"

    /** Token 생성 메서드(FirebaseInstanceIdService 사라짐) */
    override fun onNewToken(token: String) {
        Log.d(TAG, "new Token: $token")

        Preferences.fcmToken = token
        Log.i("로그: ", "성공적으로 토큰을 저장함")
    }

    /** 메시지 수신 메서드(포그라운드) */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: " + remoteMessage!!.from)

        //받은 remoteMessage의 값 출력해보기. 데이터메세지 / 알림메세지
        Log.d(TAG, "Message data : ${remoteMessage.notification?.body}")
        Log.d(TAG, "Message noti : ${remoteMessage.notification?.title}")

        sendNotification(remoteMessage)
    }

    /** 알림 생성 메서드 */
    private fun sendNotification(remoteMessage: RemoteMessage) {
        // RequestCode, Id를 고유값으로 지정하여 알림이 개별 표시
        val uniId: Int = (System.currentTimeMillis() / 7).toInt()

        // 일회용 PendingIntent : Intent 의 실행 권한을 외부의 어플리케이션에게 위임
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // Activity Stack 을 경로만 남김(A-B-C-D-B => A-B)
        val pendingIntent : PendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(this, uniId, intent, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_MUTABLE)
        }else{
            PendingIntent.getActivity(this, uniId, intent, PendingIntent.FLAG_ONE_SHOT)
        }

        // 알림 채널 이름
        val channelId = "더청년부"
        // 알림 소리
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        // 알림에 대한 UI 정보, 작업
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher) // 아이콘 설정
            .setContentTitle(remoteMessage.notification?.title.toString()) // 제목
            .setContentText(remoteMessage.notification?.body.toString()) // 메시지 내용
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setAutoCancel(true) // 알람클릭시 삭제여부
            .setSound(soundUri)  // 알림 소리
            .setContentIntent(pendingIntent) // 알림 실행 시 Intent

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 오레오 버전 이후에는 채널이 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, "더청년부 알림", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        // 알림 생성
        notificationManager.notify(uniId, notificationBuilder.build())
    }

}