package com.kim.jetpackmvi.network

import android.util.Log
import com.kim.jetpackmvi.network.logging.Level
import com.kim.jetpackmvi.network.logging.LoggingInterceptor
import me.jessyan.retrofiturlmanager.RetrofitUrlManager
import okhttp3.ConnectionPool
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * @ClassName: RetrofitClient
 * @Description: Retrofit Kotlin 封装
 * @Author: kim
 * @Date: 2/18/23 5:53 PM
 */
class RetrofitClient  {
    companion object {
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            RetrofitClient()
        }
    }

    private lateinit var retrofit: Retrofit
    private lateinit var okHttpClient: OkHttpClient

    private constructor() {
        //禁用缓存
//        if (httpCacheDirectory == null) {
//            httpCacheDirectory = File(mContext.getCacheDir(), "goldze_cache")
//        }
//        try {
//            if (cache == null) {
//                cache = Cache(httpCacheDirectory, CACHE_TIMEOUT)
//            }
//        } catch (e: Exception) {
//            KLog.e("Could not create http cache", e)
//        }

        //原本初始化位置，现转移到create内
//        retrofit = Retrofit.Builder()
//            .client(okHttpClient)
//            .addConverterFactory(GsonConverterFactory.create())
//            //.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//            .baseUrl(url)
//            .build()
    }

    /**
     * create you ApiService
     * Create an implementation of the API endpoints defined by the `service` interface.
     */
    fun <T> create(url: String, service: Class<T>?, customInterceptors: List<Interceptor> = emptyList()): T {
        if (service == null) {
            throw RuntimeException("Api service is null!")
        }

        val sslParams: HttpsUtils.SSLParams = HttpsUtils.getSslSocketFactory()
        val builder = RetrofitUrlManager.getInstance().with(OkHttpClient.Builder())
            //.cookieJar(CookieJarImpl(PersistentCookieStore(mContext))) //                .cache(cache)
            //.addInterceptor(BaseInterceptor(headers))
            //.addInterceptor(CacheInterceptor(mContext))
            .sslSocketFactory(sslParams.sSLSocketFactory!!, sslParams.trustManager!!)
            .addInterceptor(
                LoggingInterceptor.Builder() //构建者模式
                    .loggable(true/*BuildConfig.DEBUG*/) //是否开启日志打印
                    .setLevel(Level.BASIC) //打印的等级
                    .log(Log.INFO) // 打印类型
                    .request("Request") // request的Tag
                    .response("Response") // Response的Tag
                    //.addHeader("log-header", "I am the log request header.") // 添加打印头, 注意 key 和 value 都不能是中文
                    .build()
            )
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .connectionPool(
                ConnectionPool(
                    8,
                    15,
                    TimeUnit.SECONDS
                )
            ) // 这里你可以根据自己的机型设置同时连接的个数和时间，我这里8个，和每个保持时间为10s

        //添加自定义拦截器
        customInterceptors.forEach {
            builder.addInterceptor(it)
        }
        okHttpClient = builder.build()
        retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            //.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(url)
            .build()
        return retrofit.create(service)
    }
}
