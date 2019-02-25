package cos.mos.utils.utils.media;

import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cos.mos.utils.utils.java.UText;


/**
 * @Description: 文件工具类
 * @Author: Kosmos
 * @Date: 2019.02.22 11:51
 * @Email: KosmoSakura@gmail.com
 */
public class UFile {
    /**
     * @param path 完整文件名（可以加路径）
     * @return 文件后缀
     */
    public static String getFileSuffix(String path) {
        if (path == null)
            return "";
        int index = path.lastIndexOf('.');
        if (index > -1)
            return path.substring(index + 1);
        else
            return "";
    }

    /**
     * @param path 删除指定的文件
     */
    public static void deleteFile(String path) {
        if (path != null) {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
        }
    }

    /**
     * @return 路径文件是否有效
     */
    public static boolean isFileExist(String filePath) {
        if (UText.isEmpty(filePath)) {
            return false;
        }
        return new File(filePath).exists();
    }

    /**
     * @return 判断 两个文件大小相等.
     */
    public static boolean equalSize(String path1, String path2) {
        return new File(path1).length() == new File(path2).length();
    }

    /**
     * @param path a/b/c/d/123.mp3
     * @return mp3
     * @apiNote 通过路径获取文件名
     */
    public static String getFileNameByPath(String path) {
        if (path == null)
            return "";
        int index = path.lastIndexOf('/');
        if (index > -1)
            return path.substring(index + 1);
        else
            return path;
    }

    /**
     * @param path 路径
     * @return 上一级路径
     */
    public static String getParent(String path) {
        if (TextUtils.equals("/", path))
            return path;
        String parentPath = path;
        if (parentPath.endsWith("/"))
            parentPath = parentPath.substring(0, parentPath.length() - 1);
        int index = parentPath.lastIndexOf('/');
        if (index > 0) {
            parentPath = parentPath.substring(0, index);
        } else if (index == 0)
            parentPath = "/";
        return parentPath;
    }


    /**
     * @param src 被复制的文件
     * @param dst 复制到哪里
     * @return 复制是否成功
     */
    public static boolean copyFile(String src, String dst) {
        return copyFile(new File(src), new File(dst));
    }

    public static boolean copyFile(File src, File dst) {
        boolean ret = true;
        if (src.isDirectory()) {
            File[] filesList = src.listFiles();
            dst.mkdirs();
            for (File file : filesList)
                ret &= copyFile(file, new File(dst, file.getName()));
        } else if (src.isFile()) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = new BufferedInputStream(new FileInputStream(src));
                out = new BufferedOutputStream(new FileOutputStream(dst));

                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                close(in);
                close(out);
            }
            return false;
        }
        return ret;
    }

    private static boolean close(Closeable closeable) {
        if (closeable != null)
            try {
                closeable.close();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        return false;
    }

    /**
     * @param dir 目录
     * @return 是否成功
     * 递归删除目录下的所有文件及子目录下所有文件
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }
}
