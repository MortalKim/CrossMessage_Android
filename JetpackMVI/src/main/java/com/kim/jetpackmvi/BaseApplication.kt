package com.kim.jetpackmvi

import android.app.Application
import com.kim.jetpackmvi.utils.ToastUtils

/**
 * @ClassName: BaseApplication
 * @Description: java类作用描述
 * @Author: kim
 * @Date: 4/29/23 4:51 PM
 */
open class BaseApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        ToastUtils.init(this)
    }
}
