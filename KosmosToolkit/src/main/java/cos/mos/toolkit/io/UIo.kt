package cos.mos.toolkit.io

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import cos.mos.toolkit.java.UText
import cos.mos.toolkit.listener.NormalStrBoolListener
import java.io.*

/**
 * @Description IO工具
 * @Author Kosmos
 * @Date 2020.08.27 19:45
 * @Email KosmoSakura@gmail.com
 * */
object UIo {
    //SD卡中加载图片
    fun loadBmp(dirFile: String?): Bitmap? {
        if (UText.isEmpty(dirFile)) {
            return null
        }
        val options: BitmapFactory.Options = BitmapFactory.Options()
        options.inSampleSize = 2
        return BitmapFactory.decodeFile(dirFile, options)
    }

    fun <T> loadStr(cls: Class<T>, path: String, name: String, listener: NormalStrBoolListener) {
        try {// 获取指定文件对应的输入流
            val fis = FileInputStream(path + name)
            val ois = ObjectInputStream(fis)
            val outObj = ois.readObject() as String
            ois.close()
            fis.close()
            listener.onResult(outObj, true)
        } catch (e: Exception) {
            listener.onResult("", false)
        }
    }

    fun <T> loadObj(cls: Class<T>, path: String, name: String): T? {
        return try {// 获取指定文件对应的输入流
            val fis = FileInputStream(path + name)
            val ois = ObjectInputStream(fis)
            val outObj = ois.readObject() as T
            ois.close()
            fis.close()
            outObj
        } catch (e: Exception) {
            null
        }
    }

    /**
     * @param json 要保存的字符
     * @param path 保存路径
     * @param name 文件名.后缀：xxx.txt
     * @param listener 监听
     *  @tip 保存字符串
     */
    fun saveStr(json: String, path: String, name: String, listener: NormalStrBoolListener) {
        val saveFile = File(path, name)
        try {
            if (!createDir(path)) {
                listener.onResult("文件夹创建失败", false)
                return
            }
            createFile(saveFile)//新建文件
            val outStream = FileOutputStream(saveFile)
            outStream.write(json.toByteArray())
            outStream.flush()
            outStream.close()
            listener.onResult("保存成功", true)
        } catch (e: Exception) {
            listener.onResult("保存失败", false)
        }
    }

    /**
     * @param json 序列号对象
     * @param path 保存路径
     * @param name 文件名.后缀：xxx.txt
     * @param listener 监听
     * @tip 保存文件对象
     */
    fun saveObj(json: Any, path: String, name: String, listener: NormalStrBoolListener) {
        val saveFile = File(path, name)
        try {
            if (!createDir(path)) {
                listener.onResult("文件夹创建失败", false)
                return
            }
            createFile(saveFile)//新建文件
            val fos = FileOutputStream(path + name)
            val oos = ObjectOutputStream(fos)
            oos.writeObject(json)
            oos.close()
            fos.close()
            listener.onResult("保存成功", true)
        } catch (e: Exception) {
            listener.onResult("保存失败", false)
        }
    }

    //新建文件:true-文件不存在，并且已经成功创建 ,false-文件已经存在
    @Throws(IOException::class)
    fun createFile(dir: String): Boolean = createFile(File(dir))

    //新建文件:true-文件不存在，并且已经成功创建 ,false-文件已经存在
    @Throws(IOException::class)
    fun createFile(file: File): Boolean {
        return if (file.exists()) {
            true
        } else file.createNewFile()
    }

    //创建文件夹
    @Throws(IOException::class)
    fun createDir(dir: String): Boolean = createDir(File(dir))

    //创建文件夹
    @Throws(IOException::class)
    fun createDir(file: File): Boolean {
        return if (file.exists()) {
            true
        } else file.mkdir()
    }

}