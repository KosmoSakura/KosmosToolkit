package cos.mos.toolkit.log;

import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @Description 把字符串保存到SD卡根目录
 * @Author Kosmos
 * @Date 2019.04.19 14:24
 * @Email KosmoSakura@gmail.com
 */
public class ULogSaves {
    private static final File saveFile = new File(Environment.getExternalStorageDirectory(), "WM记录.txt");

    private static String read() {
        byte[] Buffer = new byte[1024];
        //得到文件输入流
        FileInputStream in = null;
        ByteArrayOutputStream outputStream = null;
        try {
            in = new FileInputStream(saveFile);
            //读出来的数据首先放入缓冲区，满了之后再写到字符输出流中
            int len = in.read(Buffer);
            //创建一个字节数组输出流
            outputStream = new ByteArrayOutputStream();
            outputStream.write(Buffer, 0, len);
            //把字节输出流转String
            return new String(outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    public static void write(String str) {
        ULog.commonD(str);
        try {
            //新建文件
            if (!saveFile.exists()) {
                saveFile.createNewFile();
            }
            String out = read() + "\n" + str;
            final FileOutputStream outStream = new FileOutputStream(saveFile);
            outStream.write(out.getBytes());
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
