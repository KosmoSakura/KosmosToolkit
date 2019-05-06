package cos.mos.utils.net.okhttp.cache;


import android.text.TextUtils;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import framework.constants.CodeBase;
import framework.model.dto.ApkInfoDTO;
import framework.util.GsonUtils;
import framework.util.NetworkUtil;
import framework.util.ToastBase;
import framework.util.TxtUtilsBase;
import framework.util.db.DaoJsonResultUtils;
import framework.util.logger.Logger;
import framework.view.ResultLisenerImpl;
import okhttp3.Request;

/**
 * Description:Model模块，网络请求的实现类
 * <p>
 * Author: yi.zhang
 * Time: 2017/4/10 0010
 * E-mail: yi.zhang@rato360.com
 */
public class LogicImpl implements ILogic {
    protected static Gson gson = new Gson();

    private boolean isCache = false;//是否要缓存，默认不缓存
    private boolean isSync = false;//是否要同步，默认不同步
    private boolean withToken = true;//是否带token，默认不带token
    private boolean addSSL = false;//是否开启SSL，默认不开启
    private String url_temp;
    private String title = "";

    public LogicImpl setTitle(String title) {
        this.title = title;
        return this;
    }

    public boolean isCache() {
        return isCache;
    }

    public LogicImpl setCache(boolean cache) {
        isCache = cache;
        return this;
    }

    public boolean isSync() {
        return isSync;
    }

    public LogicImpl setSync(boolean sync) {
        isSync = sync;
        return this;
    }

    public boolean isAddSSL() {
        return addSSL;
    }

    public LogicImpl setAddSSL(boolean addSSL) {
        this.addSSL = addSSL;
        return this;
    }

    public LogicImpl setWithToken(boolean withToken) {
        this.withToken = withToken;
        return this;
    }

    public boolean isWithToken() {
        return withToken;
    }

    public String getToken() {
        return CodeBase.Flags.getToken();
    }

    public void loadNetData() {
    }

    /**
     * Post请求
     *
     * @param url       请求地址
     * @param params    参数
     * @param classType 转换DTO类
     */
    @Override
    public void sendPostParams(String url, final Object params, final Class classType, final ResultLisenerImpl lisener) {
        url_temp = url;
        if (withToken) {
            url += getToken();
        }
        final String finalUrl = url;
        //需要同步
        if (isSync()) {
            //去数据库读取数据
            DaoJsonResultUtils.getJsonDatas(url, new DaoJsonResultUtils.GetEvent() {
                @Override
                public void getComplete(String data) {
                    //如果有数据刷新
                    if (!TxtUtilsBase.isEmpty(data)) {
                        convertDTO(finalUrl, data, classType, lisener, true);
                        Logger.Custom_i("JessieK", "getComplete: 同步---数据库");
                    } else {
                        Logger.Custom_i("JessieK", "getComplete: 同步---数据库无数据");
                    }
                }
            });
        }

        // 断网时
        if (!NetworkUtil.isConnected()) {
            //不同步,直接显示断网界面
            if (!isSync()) {
                lisener.onNoNet();
            }
            //同步，先去数据库读取数据
            else {
                final String finalUrl1 = url;
                DaoJsonResultUtils.getJsonDatas(url, new DaoJsonResultUtils.GetEvent() {
                    @Override
                    public void getComplete(String data) {
                        //没有数据,提示断网，显示断网界面
                        if (TxtUtilsBase.isEmpty(data)) {
                            ToastBase.ShortMessage("网络不可用");
                            lisener.onNoNet();
                        } else {
                            convertDTO(finalUrl, data, classType, lisener, true);
                        }
                    }
                });
            }
            return;
        }

        //发起网络请求
        final String json = gson.toJson(params);
        Logger.Custom_d("Logger", "Post开始：url:: " + url_temp);
        OkHttpClientManager.postAsyn(isAddSSL(), url, json, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e, final int code) {
                lisener.setTitle(title).onError(url_temp, CodeBase.Status.ERROR_TYPE_OKHTTP, e, code, e.getMessage());
            }

            @Override
            public void onResponse(Request request, String response) {
                Logger.d("Post结束:\n描述：" + title + "\nurl:: " + url_temp + "\n参数：" + json + "\n返回结果：" + response);
                convertDTO(finalUrl, response, classType, lisener, false);
            }
        });
    }

    /**
     * Get请求
     *
     * @param url       请求地址
     * @param classType 转换DTO类
     */
    public void sendGetParams(String url, final Class classType, final ResultLisenerImpl lisener) {
        url_temp = url;
        if (withToken) {
            url += getToken();
        }
        final String finalUrl = url;

        //需要同步
        if (isSync()) {
            //去数据库读取数据
            DaoJsonResultUtils.getJsonDatas(url, new DaoJsonResultUtils.GetEvent() {
                @Override
                public void getComplete(String data) {
                    //如果有数据刷新
                    if (!TxtUtilsBase.isEmpty(data)) {
                        convertDTO(finalUrl, data, classType, lisener, true);
                        Logger.Custom_i("JessieK", "getComplete: 同步---数据库");
                    } else {
                        Logger.Custom_i("JessieK", "getComplete: 同步---数据库无数据");
                    }
                }
            });
        }

        // 断网时
        if (!NetworkUtil.isConnected()) {
            //不同步,直接显示断网界面
            if (!isSync()) {
                lisener.onNoNet();
            }
            //同步，先去数据库读取数据
            else {
                DaoJsonResultUtils.getJsonDatas(url, new DaoJsonResultUtils.GetEvent() {
                    @Override
                    public void getComplete(String data) {
                        //没有数据,提示断网，显示断网界面
                        if (TxtUtilsBase.isEmpty(data)) {
                            lisener.onNoNet();
                        } else {
                            convertDTO(finalUrl, data, classType, lisener, true);
                        }
                    }
                });
            }
            return;
        }
        //发起网络请求
        Logger.Custom_i("Logger", "Get开始：url:" + url_temp);
        OkHttpClientManager.getAsyn(false, url, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e, final int code) {
                lisener.setTitle(title).onError(url_temp, CodeBase.Status.ERROR_TYPE_OKHTTP, e, code, e.getMessage());
            }

            @Override
            public void onResponse(Request request, String response) {
                Logger.i("Get结束：\n描述：" + title + "\nurl：" + url_temp + "\n结果：" + response);
                convertDTO(finalUrl, response, classType, lisener, false);
            }
        });
    }

    /**
     * 上传文件
     *
     * @param url       请求地址
     * @param paths     上传的文件地址数组
     * @param classType 转换DTO类
     * @param form      对应的服务器表名
     * @param lisener   文件处理回调
     */
    public void sendFileStr(String url, final String[] paths, final Class classType, final String form, final ResultLisenerImpl lisener) {
        url_temp = url;
        if (withToken) {
            url += getToken();
        }
        final String finalUrl = url;
        final File[] file = new File[paths.length];
        for (int i = 0; i < paths.length; i++) {
            file[i] = new File(paths[i]);
        }
        try {
            Logger.Custom_w("Logger", "文件上传 开始：url:" + url_temp);
            OkHttpClientManager.postFile(false, url, file, form, new OkHttpClientManager.FileResultCallback() {
                @Override
                public void onError(Request request, final Exception e, final int code) {
                    lisener.setTitle(title).onError(url_temp, CodeBase.Status.ERROR_TYPE_OKHTTP, e, code, e.getMessage());
                }

                @Override
                public void onResponse(Request request, String response) {
                    Logger.w("文件上传 结束：\n描述：" + title + "\nurl:" + url_temp + "\n文件数目：" + file.length + "\n表名：" + form + "\n结果：" + response);
                    convertDTO(response, classType, lisener, url_temp);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传文件
     *
     * @param url       请求地址
     * @param paths     上传的文件地址数组
     * @param classType 转换DTO类
     * @param form      对应的服务器表名
     * @param lisener   文件处理回调
     */
    @Override
    public void sendFileList(String url, List<String> paths, final Class classType, final String form, final ResultLisenerImpl lisener) {
        url_temp = url;
        if (withToken) {
            url += getToken();
        }
        final String finalUrl = url;
        final File[] file = new File[paths.size()];
        for (int i = 0; i < paths.size(); i++) {
            file[i] = new File(paths.get(i));
        }
        try {
            Logger.Custom_w("Logger", "文件批量 开始：url:" + url_temp);
            OkHttpClientManager.postFile(false, url, file, form, new OkHttpClientManager.FileResultCallback() {
                @Override
                public void onError(Request request, final Exception e, final int code) {
                    lisener.setTitle(title).onError(url_temp, CodeBase.Status.ERROR_TYPE_OKHTTP, e, code, e.getMessage());
                }

                @Override
                public void onResponse(Request request, String response) {
                    Logger.w("文件批量 结束：\n描述：" + title + "\nurl:" + finalUrl + "\n文件数目：" + file.length + "\n表名：" + form + "\n结果：" + response);
                    convertDTO(response, classType, lisener, url_temp);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传文件
     *
     * @param url       请求地址
     * @param paths     上传的文件地址数组
     * @param classType 转换DTO类
     * @param form      对应的服务器表名
     * @param uuid      关联数据主键
     * @param lisener   文件处理回调
     */
    @Override
    public void sendFileList(String url, List<String> paths, final Class classType, final String form, String uuid, final ResultLisenerImpl lisener) {
        url_temp = url;
        if (withToken) {
            url += getToken();
        }
        final String finalUrl = url;
        final File[] file = new File[paths.size()];
        for (int i = 0; i < paths.size(); i++) {
            file[i] = new File(paths.get(i));
        }
        try {
            Logger.Custom_w("Logger", "文件批量 开始：url:" + url_temp);
            OkHttpClientManager.postFile(false, url, file, form, uuid, new OkHttpClientManager.FileResultCallback() {
                @Override
                public void onError(Request request, final Exception e, final int code) {
                    lisener.setTitle(title).onError(url_temp, CodeBase.Status.ERROR_TYPE_OKHTTP, e, code, e.getMessage());
                }

                @Override
                public void onResponse(Request request, String response) {
                    Logger.w("文件批量 结束：\n描述：" + title + "\nurl:" + finalUrl + "\n文件数目：" + file.length + "\n表名：" + form + "\n结果：" + response);
                    convertDTO(response, classType, lisener, url_temp);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载文件
     *
     * @param url
     * @param destFileDir
     * @param lisener
     */
    @Override
    public void downLoadFiles(String url, final File destFileDir, final ResultLisenerImpl lisener) {
        url_temp = url;
        if (withToken) {
            url += getToken();
        }
        final String finalUrl = url;
        Logger.Custom_w("Logger", "文件下载 开始：url:" + url_temp);
        OkHttpClientManager.downloadAsyn(false, url, destFileDir, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, final Exception e, final int code) {
                lisener.setTitle(title).onError(url_temp, CodeBase.Status.ERROR_TYPE_OKHTTP, e, code, e.getMessage());
            }

            @Override
            public void onResponse(Request request, String response) {
                Logger.w("文件下载 结束：\n描述：" + title + "\nurl:" + url_temp + "\n文件绝对地址：" + response.toString());
//                convertDTO( (String)response, String.class, lisener, finalUrl);
                lisener.onSuccess(response);
            }
        });
    }

    /**
     * json转换Dto 文件相关
     *
     * @param response  json源
     * @param classType 转换类型
     */
    private void convertDTO(String response, Class classType, final ResultLisenerImpl lisener, String url) {
        if (!TextUtils.isEmpty(response)) {
            try {
                final JSONObject rootJsonObj = new JSONObject(response);
                try {
                    //返回成功
                    if (rootJsonObj.getInt("code") == CodeBase.Status.CODE_SUCCESS) {
                        String content = rootJsonObj.getString("content");
                        try {
                            final ArrayList list = GsonUtils.getPersons(content, classType);
                            lisener.onSuccess(list);
                        } catch (Exception e) {
                            Logger.Custom_i("JessieK", "convertDTO: 转换list错误");
                            try {
                                final Object object = GsonUtils.getPerson(content, classType);
                                lisener.onSuccess(object);
                            } catch (Exception e1) {
                                try {
                                    ApkInfoDTO apk = GsonUtils.getPerson(content, ApkInfoDTO.class);
                                    lisener.onRelease(apk);
                                } catch (Exception e2) {
                                    lisener.onDataChanged(content);
                                }
                            }
                        }
                    } else { //返回失败
                        try {
                            //状态码错误，向外抛出
                            lisener.setTitle(title).onError(url, CodeBase.Status.ERROR_TYPE_RESPONSE, null, rootJsonObj.getInt("code"), rootJsonObj.getString("message"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Logger.Custom_i("JessieK", "convertDTO: 获取不到code或content");
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
                Logger.Custom_i("JessieK", "convertDTO: 不是个json");
            }
        }
    }


    /**
     * json转换Dto 普通转换
     *
     * @param url       url地址
     * @param response  json源
     * @param classType 转换类型
     */
    private void convertDTO(String url, String response, Class classType, final ResultLisenerImpl lisener, boolean fromDatabase) {
        if (fromDatabase) {
            parseContent(url, response, classType, lisener);
            return;
        }
        if (!TextUtils.isEmpty(response)) {
            try {
                final JSONObject rootJsonObj = new JSONObject(response);
                try {
                    //返回成功
                    if (rootJsonObj.getInt("code") == CodeBase.Status.CODE_SUCCESS) {
                        String content = rootJsonObj.getString("content");
                        if (isCache()) {//需要更新缓存
                            DaoJsonResultUtils.getInstance().update(url, content);
                        }
                        //content内容为空判断
                        if (TxtUtilsBase.isEmpty(content)) {
                            lisener.onEmpty();
                        }
                        try {
                            final ArrayList list = GsonUtils.getPersons(content, classType);
                            lisener.onSuccess(list);
                        } catch (Exception e) {
                            try {
                                final Object object = GsonUtils.getPerson(content, classType);
                                lisener.onSuccess(object);
                            } catch (Exception e1) {
                                try {
                                    ApkInfoDTO apk = GsonUtils.getPerson(content, ApkInfoDTO.class);
                                    lisener.onRelease(apk);
                                } catch (Exception e2) {
                                    e2.printStackTrace();
                                    lisener.onDataChanged(content);
                                }
                            }
                        }
                    } else { //返回失败
                        //状态码错误，向外抛出
                        lisener.setTitle(title).onError(url, CodeBase.Status.ERROR_TYPE_RESPONSE, null, rootJsonObj.getInt("code"), rootJsonObj.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Logger.Custom_i("Zero", "convertDTO: 获取不到code或content");
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
                Logger.Custom_i("Zero", "convertDTO: 不是个json");
            }
        }
    }


    private void parseContent(String url, String content, Class classType, final ResultLisenerImpl lisener) {
        if (isCache()) {//需要更新缓存
            DaoJsonResultUtils.getInstance().update(url, content);
        }
        //content内容为空判断
        if (TxtUtilsBase.isEmpty(content)) {
            lisener.onEmpty();
        }

        try {
            final ArrayList list = GsonUtils.getPersons(content, classType);
            lisener.onSuccess(list);
        } catch (Exception e) {
            Logger.Custom_i("JessieK", "convertDTO: 转换list错误");
            try {
                Object object = GsonUtils.getPerson(content, classType);
                lisener.onSuccess(object);
            } catch (Exception e1) {
                e1.printStackTrace();
                try {
                    ApkInfoDTO apk = GsonUtils.getPerson(content, ApkInfoDTO.class);
                    lisener.onRelease(apk);
                } catch (Exception e2) {
                    e2.printStackTrace();
                    lisener.onDataChanged(content);
                }
            }
        }
    }


}
