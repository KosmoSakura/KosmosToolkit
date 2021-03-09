package cos.mos.toolkit.io

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import cos.mos.toolkit.init.App
import cos.mos.toolkit.java.UText
import cos.mos.toolkit.listener.NormalStrBoolListener
import java.io.*

/**
 * @Description IO工具
 * @Author Kosmos
 * @Date 2020.08.27 19:45
 * @Email KosmoSakura@gmail.com
 * @tip 2021.1.22 io读写
 * @tip 2021.3.9 Assets文件复制
 * */
object UIo {
    //SD卡中加载图片
    fun loadBmp(dirFile: String?): Bitmap? {
        if (UText.isEmpty(dirFile)) return null
        val options: BitmapFactory.Options = BitmapFactory.Options()
        options.inSampleSize = 2
        return BitmapFactory.decodeFile(dirFile, options)
    }

    //读取：字符类文件（非二进制）
    fun loadStr(path: String, name: String, listener: NormalStrBoolListener?) {
        try {
            val fis = FileInputStream(path + name)
            val size = fis.available()
            val buffer = ByteArray(size)
            fis.read(buffer)
            fis.close()
            listener?.onResult(String(buffer), true)
        } catch (e: Exception) {
            listener?.onResult(e.message, false)
        }
    }

    //读取：字符类二进制形式文件
    fun loadObjStr(path: String, name: String, listener: NormalStrBoolListener?) {
        try {// 获取指定文件对应的输入流
            val dis = DataInputStream(BufferedInputStream(FileInputStream(path + name)))
            val out = dis.readUTF()
            close(dis)
            listener?.onResult(out, false)
        } catch (e: Exception) {
            listener?.onResult(e.message, false)
        }
    }

    //读取：文件类二进制文件对象,传入全路径
    fun <T> loadObj(fullDir: String): T? {
        return try {// 获取指定文件对应的输入流
            val fis = FileInputStream(fullDir)
            val ois = ObjectInputStream(fis)
            val out = ois.readObject() as? T
            close(fis, ois)
            out
        } catch (e: Exception) {
            null
        }
    }

    fun <T> loadObj(path: String, name: String): T? = loadObj(path + name)

    /**
     * @param json 要保存的字符
     * @param path 保存路径
     * @param name 文件名.后缀：xxx.txt
     * @param listener 监听
     *  @tip 保存字符串
     */
    fun saveStr(json: String, path: String, name: String, listener: NormalStrBoolListener?) {
        val saveFile = File(path, name)
        try {
            if (!UFile.createDir(path)) {
                listener?.onResult("文件夹创建失败", false)
                return
            }
            UFile.createFile(saveFile)//新建文件
            val fos = FileOutputStream(saveFile)
            fos.write(json.toByteArray())
            fos.flush()
            fos.close()
            listener?.onResult("保存成功", true)
        } catch (e: Exception) {
            listener?.onResult("保存失败", false)
        }
    }

    /**
     * @param json 序列号对象
     * @param path 保存路径
     * @param name 文件名.后缀：xxx.txt
     * @param listener 监听
     * @tip 保存字符类二进制文件对象
     */
    fun saveObjStr(json: String, path: String, name: String, listener: NormalStrBoolListener?) {
        val saveFile = File(path, name)
        try {
            if (!UFile.createDir(path)) {
                listener?.onResult("文件夹创建失败", false)
                return
            }
            UFile.createFile(saveFile)//新建文件
            val dos = DataOutputStream(BufferedOutputStream(FileOutputStream(saveFile)))
            dos.writeUTF(json)
            dos.flush()
            close(dos)
            listener?.onResult("保存成功", true)
        } catch (e: Exception) {
            listener?.onResult("保存失败", false)
        }
    }

    //保存文件类二进制文件对象
    fun saveObj(obj: Serializable, path: String, name: String, listener: NormalStrBoolListener?) {

        try {
            if (!UFile.createDir(path)) {
                listener?.onResult("文件夹创建失败", false)
                return
            }
            val saveFile = File(path, name)
            UFile.createFileReplace(saveFile)//新建文件
            val oos = ObjectOutputStream(FileOutputStream(saveFile))
            oos.writeObject(obj)
            oos.flush()
            close(oos)
            listener?.onResult("保存成功", true)
        } catch (e: Exception) {
            listener?.onResult("保存失败" + e.message, false)
        }
    }

    /**
     * @param src 被复制文件的完整（路径+文件名+后缀名）
     * @param dst 复制到哪里（路径+文件名+后缀名）
     * @return 复制是否成功
     * @apiNote 复制文件
     */
    fun copyFile(src: String?, dst: String?): Boolean {
        val file = File(src)
        return if (file.exists() && file.isFile) {
            var ism: InputStream? = null
            var otm: OutputStream? = null
            try {
                ism = BufferedInputStream(FileInputStream(src))
                otm = BufferedOutputStream(FileOutputStream(dst))
                val buf = ByteArray(1024)
                var len: Int
                while (ism.read(buf).also { len = it } > 0) {
                    otm.write(buf, 0, len)
                }
                true
            } catch (e: Exception) {
                false
            } finally {
                close(ism, otm)
            }
        } else {
            false //不是一个文件，或者文件不存在
        }
    }
    /**
     * @param name assets目录下的文件名，如：test.db
     * @param dir 数据库复制目标目录，如：/data/data/包名/dbs
     * @return 返回复制后的文件File对象
     */
    fun copyAssetsToSDcard(ctx: Context?, dir: String, name: String): File? {
        val fileDB = File("${dir}/${name}")
        if (fileDB.exists()) {
            return fileDB
        }
        if (!UFile.createDir(dir)) {
            return null//文件夹创建失败
        }
        try {
            val input = (ctx ?: App.instance).assets.open(name)//数据库的输入流
            val fos = FileOutputStream(fileDB)//输出流写到SDcard上
            val buffer = ByteArray(1024)//byte数组,1KB写一次
            var count: Int
            while (input.read(buffer).also { count = it } > 0) {
                fos.write(buffer, 0, count)
            }
            //关流
            fos.flush()
            fos.close()
            input.close()
            return fileDB
        } catch (e: IOException) {
            return null
        }
    }
    // 关流
    fun close(vararg clos: Closeable?) {
        clos.forEach {
            try {
                it?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}