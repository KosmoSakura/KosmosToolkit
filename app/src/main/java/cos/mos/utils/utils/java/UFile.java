package cos.mos.utils.utils.java;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cos.mos.utils.init.k.KApp;


/**
 * @Description: 文件工具类
 * @Author: Kosmos
 * @Date: 2019.02.22 11:51
 * @Email: KosmoSakura@gmail.com
 * @eg: 2019.2.25：重构
 * @eg: 2019.3.5：文件重命名
 * @eg: 2019.3.18:优化注释，添加新函数
 */
public class UFile {
    /**
     * @param path 文件完整路径(a/b/c/aa.txt)
     * @return 是否删除成功
     * @apiNote 删除指定的文件
     */
    public static boolean deleteFile(String path) {
        if (path == null) {
            return false;
        } else {
            File file = new File(path);
            if (file.exists()) {
                return file.delete();
            } else {
                return false;
            }
        }
    }

    /**
     * @param dir 目录
     * @return 是否成功
     * @apiNote 递归删除目录下的所有文件及子目录下所有文件
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (String aChildren : children) {
                return deleteDir(new File(dir, aChildren));
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    /**
     * @param src 被复制文件的完整（路径+文件名+后缀名）
     * @param dst 复制到哪里（路径+文件名+后缀名）
     * @return 复制是否成功
     * @apiNote 复制文件
     */
    public static boolean copyFile(String src, String dst) {
        File file = new File(src);
        if (file.exists() && file.isFile()) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = new BufferedInputStream(new FileInputStream(src));
                out = new BufferedOutputStream(new FileOutputStream(dst));
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                return true;
            } catch (Exception e) {
                return false;
            } finally {
                close(in);
                close(out);
            }
        } else {
            return false;//不是一个文件，或者文件不存在
        }
    }

    private static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param fileName 文件名(不包含后缀名）
     * @param times    校验次数(外部调用传入0，递归调用自增）
     * @return 新名字
     * @apiNote 检查文件是否存在，存在则重命名
     */
    private static String fileRename(String fileName, int times) {
        File file;
        if (times == 0) {
            file = new File(KApp.instance().getRootPath() + File.separator
                + fileName + ".mp4");
        } else {
            file = new File(KApp.instance().getRootPath() + File.separator
                + fileName + "_" + times + ".mp4");
        }
        if (file.exists()) {
            return fileRename(fileName, times + 1);
        } else {
            return file.getAbsolutePath();
        }
    }

    /**
     * @param dir      文件路径
     * @param fileName 文件名(包含后缀名eg:aaa.txt)
     * @param times    校验次数(外部调用传入0，递归调用自增）
     * @return 新名字（包含路径）
     * @apiNote 检查文件是否存在，存在则重命名
     */
    private static String fileRename(String dir, String fileName, int times) {
        File file;
        if (times == 0) {
            file = new File(dir + File.separator + fileName);
        } else {
            String suffix = "." + getSuffix(fileName);
            file = new File(dir + File.separator + fileName.replace(suffix, "") + "_" + times + suffix);
        }
        if (file.exists()) {
            return fileRename(dir, fileName, times + 1);
        } else {
            return file.getAbsolutePath();
        }
    }

    /**
     * @param filePath 路径（a/b/c)、文件(a/b/c/aa.txt)
     * @return 路径、文件是否存在
     */
    public static boolean isFileExist(String filePath) {
        if (filePath == null) {
            return false;
        }
        return new File(filePath).exists();
    }

    /**
     * @return 判断 两个文件大小是否相等.
     */
    public static boolean isEqualSize(String path1, String path2) {
        return new File(path1).length() == new File(path2).length();
    }

    /**
     * @return 只有 空目录 会返回true
     */
    public static boolean isEmptyDir(File file) {
        return file.exists() && file.isDirectory() && file.listFiles().length <= 0;
    }

    /**
     * @return 只有 空文件 会返回true
     */
    public static boolean isEmptyFile(File file) {
        return file.exists() && !file.isDirectory() && file.length() <= 0;
    }


    /**
     * @param path 完整文件名（可以加路径eg1:ss.txt,eg2:a/b/c/ss.txt）
     * @return 文件后缀
     * @apiNote 获取文件后缀名
     */
    public static String getSuffix(String path) {
        if (path == null) {
            return "";
        }
        int index = path.lastIndexOf('.');
        if (index > -1) {
            return path.substring(index + 1).toLowerCase();
        } else {
            return "";
        }
    }

    /**
     * @param dir 目录或文件(a/b/1.txt 或 a/b/c/)
     * @return 父级(a / b 或 a / b)
     * @apiNote 返回父级目录
     */
    public static String getParent(String dir) {
        return new File(dir).getParent();
    }

    /**
     * @param dir 路径
     * @return 返回当前分区总空间大小
     */
    public static long getFileSize(String dir) {
        return new File(dir).getTotalSpace();
    }

    /**
     * @param dir 路径
     * @return 返回当前分区可用空间大小
     */
    public static long getFileAvailable(String dir) {
        return new File(dir).getUsableSpace();
    }
}
