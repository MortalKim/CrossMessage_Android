package com.kim.jetpackmvi.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response

/**
 * @ClassName: LogoutInterceptor
 * @Description: java类作用描述
 * @Author: kim
 * @Date: 3/27/23 9:06 PM
 */
/**
 *  Logout Interceptor.
 *  Here i extend the BaseActivity to navigate user to another screen. i need runOnUIThread to execute navigateToLogin() method.
 */
class LogoutInterceptor(private val unAuthenticator: () -> Unit) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        val code = response.code
        if(code == 401){
            unAuthenticator.invoke()
        }
        // Re-create the response before returning it because body can be read only once
        return response.newBuilder().body(response.body).build()
    }
}