package cos.mos.library.retrofit;


import java.io.IOException;

import cos.mos.library.utils.ULog;
import cos.mos.library.constant.KConfig;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;


public class HeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
//        builder.header("Authorization", "bearer ");
//        originalBuilder.addHeader("Content-Type", "application/json;charset=UTF-8");
//        Request.Builder builder = originalBuilder.method(originalRequest.method(), originalRequest.body());
        Request build = builder.build();
        Response response = chain.proceed(build);
        printLog(build, response);
        return response;
    }

    private void printLog(final Request request, final Response response) {
        StringBuilder sb = new StringBuilder();
        sb.append("接口描述：").append(KConfig.Describe);
        sb.append(" <---> Method：").append(request.method());
        sb.append("\nURL：").append(request.url());
        sb.append("\n请求参数：");
        try {
            sb.append(bodyToString(request.body()));
        } catch (IOException e) {
            sb.append("请求参数解析失败");
        }
        sb.append("\n返回结果：");
        try {
            //踩坑记录1：
            //这里如果直接使用response.body().string()的方式输出日志
            //会因为response.body().string()之后，response中的流会被关闭，请求返回旧结果，
            //因此，需要创建出一个新的response给应用层处理
            //但是不知道哪里的问题，现在，这里打印的是旧的请求信息，但返回结果无误。暂时这么用。
            //踩坑记录2：
            //老叶找到了问题原因，给老叶点个赞
            //之所以记录1中的问题，请求参数打印正常，返回结果打印却是旧的数据
            //是因为之前：request复制了一份，但response却是通过本身的Chain获取的，内存引用没有改变
            //response.peekBody()创建的新对象被重复添加到了流里面
            //导致请求2次
            ResponseBody responseBody = response.peekBody(1024 * 1024);
            sb.append(responseBody.string());
        } catch (Exception e) {
            sb.append("返回结果解析失败");
        }
        ULog.commonV(sb.toString());
    }

    private String bodyToString(final RequestBody requestBody) throws IOException {
        final Buffer buffer = new Buffer();
        if (requestBody != null) {
            if (requestBody.contentLength() > 2048) {
                return "";
            }
            requestBody.writeTo(buffer);
        } else {
            return "";
        }
        return buffer.readUtf8();
    }
}
