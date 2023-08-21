package com.kim.crossmessage.network

import com.kim.crossmessage.Constant.DOMAIN_NAME
import com.kim.crossmessage.Constant.DOMAIN_NAME_IFTTT_Webhook_Server
import com.kim.crossmessage.Constant.DOMAIN_NAME_postTest
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * @ClassName: ApiService
 * @Description: java类作用描述
 * @Author: kim
 * @Date: 6/19/23 5:12 PM
 */
interface ApiService {
    @Headers(DOMAIN_NAME+ DOMAIN_NAME_postTest)
    @POST("post")
    // 注意这里的suspend
    suspend fun postTest(@Body json: String): ResponseBody

    @Headers(DOMAIN_NAME+ DOMAIN_NAME_IFTTT_Webhook_Server)
    @POST("/trigger/Notification/with/key/kz6HLZNgHmzcORpiOLdubiIrJYbDVy5DLDDetLXaTBW")
    // 注意这里的suspend
    suspend fun sendNotifyToIFTTT(@Body json: HashMap<String, @JvmSuppressWildcards Any>): ResponseBody
}
