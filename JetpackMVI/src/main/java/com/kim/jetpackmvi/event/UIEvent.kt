package com.kim.jetpackmvi.event

import android.app.Activity
import android.os.Bundle

/**
 * @ClassName: UIEvent
 * @Description: UI事件
 * @Author: kim
 * @Date: 2/18/23 4:16 PM
 */
class UIEvent {
    companion object{
        class StartActivityEvent<T: Activity>(val clazz: Class<T>, val bundle: Bundle?)
    }
}
