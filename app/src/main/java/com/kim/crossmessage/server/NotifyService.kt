package com.kim.crossmessage.server

import android.app.Notification
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.kim.jetpackmvi.network.apiCall
import com.kim.crossmessage.App
import com.orhanobut.logger.Logger
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream


class NotifyService : NotificationListenerService() {
    override fun onBind(intent: Intent): IBinder {
        return super.onBind(intent)!!
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        val notification = sbn!!.notification ?: return
        val pkg = sbn.packageName
        val flags = notification.flags

        val checkedPackages = MMKV.defaultMMKV().decodeStringSet("checkedPackages", mutableSetOf<String>())!!
        if (flags and Notification.FLAG_ONGOING_EVENT != 0 || flags and Notification.FLAG_FOREGROUND_SERVICE != 0) {
            // 常驻通知
            Log.e("TAG", "onNotificationPosted: 常驻通知：" + notification.extras.getString(Notification.EXTRA_TITLE))
            if(checkedPackages.contains(sbn.packageName)){
                pushNoti(notification, pkg)
            }
        } else {
            // 临时通知
            Log.e("TAG", "onNotificationPosted: 临时通知：" + notification.extras.getString(Notification.EXTRA_TITLE))
            if(checkedPackages.contains(sbn.packageName)){
                pushNoti(notification, pkg)
            }
        }
    }

    fun pushNoti(notification: Notification, pkg: String){
        GlobalScope.launch {
            val map = HashMap<String, Any>();
            //标题/内容
            val notf_title: String? = notification.extras.getString(Notification.EXTRA_TITLE)
            val notf_text: String? = notification.extras.getString(Notification.EXTRA_TEXT)

            var d: Drawable? = notification.getLargeIcon()?.loadDrawable(this@NotifyService) // the drawable (Captain Obvious, to the rescue!!!)
            var bitmapdata: ByteArray? = null
            if(d != null){
                val bitmap = (d as BitmapDrawable).bitmap
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                bitmapdata = stream.toByteArray()
            }
            else{
                bitmapdata = ByteArray(0)
            }
            if(notf_title.isNullOrEmpty() || notf_text.isNullOrEmpty()) return@launch

            val pm = applicationContext.packageManager
            val ai: ApplicationInfo? = try {
                pm.getApplicationInfo(pkg, 0)
            } catch (e: PackageManager.NameNotFoundException) {
                null
            }
            val applicationName =
                (if (ai != null) pm.getApplicationLabel(ai) else "(unknown)") as String

            map["value1"] = applicationName
            map["value2"] = notf_title
            map["value3"] = notf_text
            //map["icon"] = bitmapdata!!

            //在view model中调用协程

            apiCall({
                App.apiService.sendNotifyToIFTTT(map)
            }, {
                Logger.i(it.string())
            }, {
                Logger.i(it.message + "")
            })
        }
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        val notification = sbn?.notification
        val flags = notification?.flags ?: 0

        if (flags and Notification.FLAG_ONGOING_EVENT != 0) {
            // 这是一个常驻通知，已被取消
        } else {
            // 这是一个临时通知，已被取消
        }
    }

}
