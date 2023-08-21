package com.kim.jetpackmvi.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

/**
 * @ClassName: FileUtils
 * @Description: java类作用描述
 * @Author: kim
 * @Date: 3/22/23 9:17 PM
 */
object FileUtils {
    fun copyFileToCacheByUri(context: Context, uri: Uri, fileName:String){
        val contentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(uri)
        val file = File(context.cacheDir, fileName)
        val outputStream = FileOutputStream(file)

        inputStream?.copyTo(outputStream)

        outputStream.flush()
        outputStream.close()
        inputStream?.close()

    }

    //read file form asset
    fun readAssetsFile(fileName:String):String{
        val inputStream = javaClass.classLoader?.getResourceAsStream("assets/$fileName")
        val buffer = ByteArray(inputStream!!.available())
        inputStream.read(buffer)
        inputStream.close()
        return String(buffer)
    }
}
