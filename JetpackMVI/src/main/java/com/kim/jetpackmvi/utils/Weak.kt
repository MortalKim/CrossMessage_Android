package com.comtour.base.utils

import android.util.Log
import java.lang.ref.WeakReference
import kotlin.reflect.KProperty

/**
 *   @author JHH
 *   @date 2020/4/8
 *   @Describe: 弱引用工具类
 */
class Weak<T : Any>(initializer: () -> T?) {
    var weakReference = WeakReference<T?>(initializer())

    constructor():this({
        null
    })

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        Log.d("Weak Delegate","-----------getValue")
        return weakReference.get()
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        Log.d("Weak Delegate","-----------setValue")
        weakReference = WeakReference(value)
    }

}
