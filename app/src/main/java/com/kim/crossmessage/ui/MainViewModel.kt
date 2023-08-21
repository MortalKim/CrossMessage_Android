package com.kim.crossmessage.ui

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import com.kim.jetpackmvi.ui.activity.BaseViewModel
import com.kim.jetpackmvi.ui.state.UIState
import com.tencent.mmkv.MMKV

/**
 * @ClassName: MainViewModel
 * @Description: java类作用描述
 * @Author: kim
 * @Date: 6/14/23 8:49 PM
 */
class MainViewModel : BaseViewModel<UIState>(){
    val checkedPackages = mutableStateListOf<String>()

    fun onPackageCheck(packageName: String, i: Boolean) {
        if(i){
            checkedPackages.add(packageName)
        }
        else{
            checkedPackages.remove(packageName)
        }
        MMKV.defaultMMKV().encode("checkedPackages",checkedPackages.toSet())
    }


    // Get all packages in phone
    fun getAllPackages(context: Context): MutableList<ApplicationInfo> {
        // Get all packages form package manager
        val pm = context.packageManager
        val packages = pm.getInstalledApplications(PackageManager.GET_META_DATA)
        for (packageInfo in packages) {
            Log.d("Installed package :", packageInfo.packageName)
            Log.d("Source dir : ", packageInfo.sourceDir)
            Log.d("Launch Activity :", pm.getLaunchIntentForPackage(packageInfo.packageName).toString())
        }
        return packages
    }

    override fun initUiState(): UIState {
        return UIState(0,"")
    }
}
