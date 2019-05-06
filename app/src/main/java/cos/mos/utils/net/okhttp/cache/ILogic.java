package cos.mos.utils.net.okhttp.cache;

import java.io.File;
import java.util.List;

import framework.view.ResultLisenerImpl;


/**
 * Description:Model模块，网络请求的基类，定义了几种网络请求的方式
 * <p>
 * Author: yi.zhang
 * Time: 2017/4/10 0010
 * E-mail: yi.zhang@rato360.com
 */
public interface ILogic {
    /**
     * post请求
     *
     * @param url
     * @param params
     * @param classType
     * @param lisener
     */
    void sendPostParams(String url, Object params, Class classType, ResultLisenerImpl lisener);

    /**
     * get请求
     *
     * @param url
     * @param classType
     * @param lisener
     */
    void sendGetParams(String url, Class classType, ResultLisenerImpl lisener);

    /**
     * str 文件上传
     *
     * @param url
     * @param path
     * @param classType
     * @param form
     * @param lisener
     */
    void sendFileStr(String url, String[] path, Class classType, String form, ResultLisenerImpl lisener);

    /**
     * List 文件上传
     *
     * @param url
     * @param path
     * @param classType
     * @param form
     * @param lisener
     */
    void sendFileList(String url, List<String> path, Class classType, String form, ResultLisenerImpl lisener);

    /**
     * List 文件上传(有关联文件)
     *
     * @param url
     * @param path
     * @param classType
     * @param form
     * @param lisener
     */
    void sendFileList(String url, List<String> path, final Class classType, final String form, String info, final ResultLisenerImpl lisener);

    /**
     * 文件下载
     *
     * @param url
     * @param destFileDir
     * @param lisener
     */
    void downLoadFiles(String url, File destFileDir, ResultLisenerImpl lisener);
}
