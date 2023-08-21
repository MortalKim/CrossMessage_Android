package com.comtour.base.utils

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * @ClassName: Md5
 * @Description: <MD5加密>
 * @Author: Kim
 * @Date: 4/18/2022 10:23 AM
 */
class Md5 {
    companion object{
        fun encode32(password: String): String {
            try {
                val  instance: MessageDigest = MessageDigest.getInstance("MD5")//获取md5加密对象
                val digest:ByteArray = instance.digest(password.toByteArray())//对字符串加密，返回字节数组
                var sb : StringBuffer = StringBuffer()
                for (b in digest) {
                    var i :Int = b.toInt() and 0xff//获取低八位有效值
                    var hexString = Integer.toHexString(i)//将整数转化为16进制
                    if (hexString.length < 2) {
                        hexString = "0" + hexString//如果是一位的话，补0
                    }
                    sb.append(hexString)
                }
                return sb.toString()

            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            }

            return ""
        }

        fun encode16(password: String): String {
            val str = encode32(password)
            return str.substring(8, 24)
        }
    }
}
