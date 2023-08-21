package com.kim.jetpackmvi.utils

import android.widget.Toast
import com.kim.jetpackmvi.BaseApplication

/**
 * @ClassName: ToastUtils
 * @Description: java类作用描述
 * @Author: kim
 * @Date: 4/29/23 4:52 PM
 */
class ToastUtils {
    companion object {
        private var application: BaseApplication? = null
        fun init(application: BaseApplication){
            this.application = application
        }
        fun showToast(msg: String) {
            if(application != null){
                ThreadUtils.runOnMainThread {
                    Toast.makeText(application, msg, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
