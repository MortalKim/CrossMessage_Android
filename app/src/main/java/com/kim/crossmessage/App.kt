package com.kim.crossmessage

import com.kim.jetpackmvi.BaseApplication
import com.kim.jetpackmvi.network.RetrofitClient
import com.kim.crossmessage.Constant.DOMAIN_NAME_IFTTT_Webhook_Server
import com.kim.crossmessage.Constant.IFTTT_Webhook_ServerUrl
import com.kim.crossmessage.network.ApiService
import com.tencent.mmkv.MMKV
import me.jessyan.retrofiturlmanager.RetrofitUrlManager

/**
 * @ClassName: App
 * @Description: java类作用描述
 * @Author: kim
 * @Date: 6/19/23 5:10 PM
 */
class App:BaseApplication() {
    companion object{
        lateinit var apiService: ApiService
    }

    override fun onCreate() {
        super.onCreate()
        MMKV.initialize(this)
        initRetrofit()
    }

    private fun initRetrofit() {
        apiService = RetrofitClient.instance.create(
            IFTTT_Webhook_ServerUrl,
            ApiService::class.java
        )
        //RetrofitClient.getInstance().apiService = apiService;
        RetrofitUrlManager.getInstance().putDomain(DOMAIN_NAME_IFTTT_Webhook_Server, IFTTT_Webhook_ServerUrl)
        RetrofitUrlManager.getInstance().setGlobalDomain(IFTTT_Webhook_ServerUrl)
    }
}
