package com.kim.crossmessage.ui

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.kim.jetpackmvi.ui.activity.BaseActivity
import com.kim.jetpackmvi.ui.state.SingleUIEvent
import com.kim.jetpackmvi.ui.state.UIState
import com.kim.crossmessage.ui.ui.theme.SyncNotifyToServerTheme
import com.tencent.mmkv.MMKV

class MainActivity : BaseActivity<MainViewModel, UIState>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(!isNotificationListenerEnabled()){
            openNotificationListenSettings()
        }

    }

    override fun onUIEvent(uiEvent: SingleUIEvent) {
    }

    @Composable
    override fun GetContent() {
        SyncNotifyToServerTheme {
            // A surface container using the 'background' color from the theme
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                val checkedPackages = MMKV.defaultMMKV().decodeStringSet("checkedPackages", mutableSetOf<String>())!!
                viewModel.checkedPackages.addAll(checkedPackages)
                Greeting(viewModel)
            }
        }
    }

    override fun onUIStateChange(uiState: UIState) {
    }

    fun isNotificationListenerEnabled(): Boolean {
        val s = Settings.Secure.getString(
            this@MainActivity.contentResolver,
            "enabled_notification_listeners"
        )
        return if (s != null && s.contains(this.getPackageName())) true else false
    }

    fun openNotificationListenSettings() {
        try {
            val intent: Intent
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
            } else {
                intent = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
            }
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

@Composable
fun Greeting(viewModel: MainViewModel) {
    val context = LocalContext.current
    val packages = viewModel.getAllPackages(context)
    LazyColumn(){
        items(packages.size){
            Row {
                val pkg = packages[it].packageName
                Checkbox(checked = viewModel.checkedPackages.contains(pkg),
                    onCheckedChange = { i ->
                        viewModel.onPackageCheck(pkg, i)
                    })
                val pm = LocalContext.current.packageManager
                val ai: ApplicationInfo? = try {
                    pm.getApplicationInfo(pkg, 0)
                } catch (e: PackageManager.NameNotFoundException) {
                    null
                }
                val applicationName =
                    (if (ai != null) pm.getApplicationLabel(ai) else "(unknown)") as String
                Text(text = applicationName)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SyncNotifyToServerTheme {
        Greeting(MainViewModel())
    }
}
