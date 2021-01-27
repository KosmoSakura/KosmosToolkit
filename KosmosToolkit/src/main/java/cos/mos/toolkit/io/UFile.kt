package cos.mos.toolkit.io

import cos.mos.toolkit.init.KApp
import cos.mos.toolkit.java.UText
import java.io.*
import java.util.*

/**
 * @Description 文件工具类
 * @Author Kosmos
 * @Date 2019.02.22 11:51
 * @Email KosmoSakura@gmail.com
 * @tip 2019.2.25：重构
 * @tip 2019.3.5：文件重命名
 * @tip 2019.3.18:优化注释，添加新函数
 * @tip 2019.5.8:优化递归删除
 * @tip 2021.1.26 文件夹遍历
 * */
object UFile {
    data class IOBean(val dir: String, val msg: String?)
    val listFiles = ArrayList<File>()
    val listDirFiles = ArrayList<IOBean>()

    fun loadFilesAll(path: String?) {
        loadFilesAll(path, null)
    }

    /**
     * @tip 递归遍历文件夹所有指定后缀文件，使用前先listFiles.clear
     * @tip 1.递归，包括子目录
     * @tip 2.非文件夹，只遍历文件
     * @tip 3.后缀传入空，则遍历全部文件
     */
    fun loadFilesAll(path: String?, suffix: String?) {
        File(path).let { out ->
            if (out.exists()) {
                out.listFiles().let { inner ->
                    if (null == inner || inner.isEmpty()) {
                        return
                    } else {
                        inner.forEach {
                            if (it.isDirectory) {
                                loadFilesAll(it.absolutePath, suffix)
                            } else {
                                if (UText.isEmpty(suffix)) {
                                    listFiles.add(it)
                                } else {
                                    if (it.name.endsWith(suffix!!)) {
                                        listFiles.add(it)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    //遍历指定文件夹下全部
    fun loadFiles(path: String?) = loadFiles(path, null)

    /**
     * @tip 遍历指定文件夹下指定后缀文件
     * @tip 1.非递归，只遍历传入目录
     * @tip 2.非文件夹，只遍历文件
     * @tip 3.后缀传入空，则遍历全部文件
     */
    fun loadFiles(path: String?, suffix: String?): ArrayList<File> {
        listFiles.clear()
        File(path).listFiles()?.forEach { out ->
            out?.let { inner ->
                if (inner.isFile) {
                    if (UText.isEmpty(suffix)) {
                        listFiles.add(inner)
                    } else {
                        if (inner.name.endsWith(suffix!!)) {
                            listFiles.add(inner)
                        }
                    }
                }
            }
        }
        return listFiles
    }

    /**
     * @tip 递归遍历文件夹所有文件，使用前先listFiles.clear
     * @tip 返回树状文件结构
     */
    fun loadFilesDir(path: String?) {
        File(path).let { out ->
            if (out.exists()) {
                val files = out.listFiles()
                if (null == files || files.isEmpty()) {
//                    listDirFiles.add(IOBean(out.absolutePath, "文件夹为空"))
                    return
                } else {
                    files.forEach {
                        if (it.isDirectory) {
                            listDirFiles.add(IOBean(it.absolutePath, "文件夹"))
                            loadFilesDir(it.absolutePath)
                        } else {
                            listDirFiles.add(IOBean(it.absolutePath, "文件"))
                        }
                    }
                }
            } else {
                listDirFiles.add(IOBean(out.absolutePath, "文件不存在"))
            }
        }
    }

    @Throws(IOException::class)
    fun createFile(dir: String): Boolean = createFile(File(dir))

    @Throws(IOException::class)
    fun createFileReplace(dir: String): Boolean = createFileReplace(File(dir))

    /**
     * @tip 新建文件
     * @return true-文件不存在，并且已经成功创建
     * @return false-文件已经存在,或创建失败
     */
    @Throws(IOException::class)
    fun createFile(file: File): Boolean {
        return if (file.exists()) {
            true
        } else file.createNewFile()
    }

    /**
     * @tip 新建或替换文件（如果文件已经存在，则删除替换）
     * @return true-成功，false-失败
     */
    @Throws(IOException::class)
    fun createFileReplace(file: File): Boolean {
        if (file.exists()) file.delete()
        return file.createNewFile()
    }

    //创建文件夹
    @Throws(IOException::class)
    fun createDir(dir: String): Boolean = createDir(File(dir))

    //创建文件夹
    @Throws(IOException::class)
    fun createDir(file: File): Boolean {
        return if (file.exists()) {
            true
        } else file.mkdirs()
    }

    //删除单文件
    @Throws(IOException::class)
    fun delete(dir: String) = delete(File(dir))

    /**
     * @param file 文件完整路径(a/b/c/aa.txt)
     * @return 是否删除成功
     * @tip 删除单文件
     */
    @Throws(IOException::class)
    fun delete(file: File): Boolean = if (file.exists()) file.delete() else false

    fun deleteDir(dir: String) = deleteDir(File(dir))

    /**
     * @param fileDir 目录
     * @return 是否成功
     * @apiNote 递归删除目录下的所有文件及子目录下所有文件
     */
    fun deleteDir(fileDir: File?): Boolean {
        if (fileDir == null) return false
        if (!fileDir.exists()) return false
        if (fileDir.isDirectory) {
            fileDir.listFiles().forEach {
                if (it.isFile) {
                    if (!it.delete()) return false
                } else if (it.isDirectory) {
                    if (!deleteDir(it)) return false
                }
            }
        } else {
            return false
        }
        return fileDir.delete() // 目录此时为空，可以删除
    }


    /**
     * @param oldName 旧文件名绝对路径(eg:a/b/c/d.txt)
     * @param newName 新文件名绝对路径(eg:a/b/c/e.txt)
     * @return 是否成功
     * @apiNote 重命名（原理和方法同Linux）
     */
    fun fileRename(oldName: String?, newName: String?): Boolean = try {
        File(oldName).renameTo(File(newName))
    } catch (e: Exception) {
        false
    }

    /**
     * @param fileName 文件名(不包含后缀名）
     * @param times    校验次数(外部调用传入0，递归调用自增）
     * @return 新名字
     * @apiNote 检查文件是否存在，存在则重命名
     */
    private fun fileRename(fileName: String, times: Int): String? {
        val file = if (times == 0) {
            File(KApp.instance().rootPath + File.separator + fileName + ".mp4")
        } else {
            File(KApp.instance().rootPath + File.separator + fileName + "_" + times + ".mp4")
        }
        return if (file.exists()) {
            fileRename(fileName, times + 1)
        } else {
            file.absolutePath
        }
    }

    /**
     * @param dir      文件路径
     * @param fileName 文件名(包含后缀名eg:aaa.txt)
     * @param times    校验次数(外部调用传入0，递归调用自增）
     * @return 新名字（包含路径）
     * @apiNote 检查文件是否存在，存在则重命名
     */
    private fun fileRename(dir: String, fileName: String, times: Int): String? {
        val file = if (times == 0) {
            File(dir + File.separator + fileName)
        } else {
            val suffix = "." + getSuffix(fileName)
            File(dir + File.separator + fileName.replace(suffix, "") + "_" + times + suffix)
        }
        return if (file.exists()) {
            fileRename(dir, fileName, times + 1)
        } else {
            file.absolutePath
        }
    }

    /**
     * @param filePath 路径（a/b/c)、文件(a/b/c/aa.txt)
     * @return 路径、文件是否存在
     */
    fun isFileExist(filePath: String): Boolean {
        return try {
            File(filePath).exists()
        } catch (e: Exception) {
            false
        }
    }

    /**
     * @return 判断 两个文件大小是否相等.
     */
    fun isEqualSize(path1: String?, path2: String?): Boolean = isEqualSize(File(path1), File(path2))

    fun isEqualSize(file1: File, file2: File): Boolean = file1.length() == file2.length()

    /**
     * @return 只有 空目录 会返回true
     */
    fun isEmptyDir(file: File): Boolean = file.exists() && file.isDirectory && file.listFiles().isEmpty()

    /**
     * @return 只有 空文件 会返回true
     */
    fun isEmptyFile(file: File): Boolean = file.exists() && !file.isDirectory && file.length() <= 0

    /**
     * @param path 完整文件名（可以加路径eg1:ss.txt,eg2:a/b/c/ss.txt）
     * @return 文件后缀
     * @apiNote 获取文件后缀名
     */
    fun getSuffix(path: String?): String {
        if (path == null) return ""
        val index = path.lastIndexOf('.')
        return if (index > -1) path.substring(index + 1).toLowerCase(Locale.ROOT) else ""
    }

    /**
     * @param dir 目录或文件(a/b/1.txt 或 a/b/c/)
     * @return 父级(a / b 或 a / b)
     * @apiNote 返回父级目录
     */
    fun getParent(dir: String?): String? = getParent(File(dir))
    fun getParent(file: File): String? = file.parent

    /**
     * @param dir 路径
     * @return 返回当前分区总空间大小
     */
    fun getFileSize(dir: String?): Long = getFileSize(File(dir))
    fun getFileSize(file: File): Long = file.totalSpace

    /**
     * @param dir 路径
     * @return 返回当前分区可用空间大小
     */
    fun getFileAvailable(dir: String?): Long = getFileAvailable(File(dir))
    fun getFileAvailable(file: File): Long = file.usableSpace
}