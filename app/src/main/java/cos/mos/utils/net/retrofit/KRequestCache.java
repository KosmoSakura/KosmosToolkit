package cos.mos.utils.net.retrofit;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * @Description: <p>
 * @Author: Kosmos
 * @Date: 2018.11.13 16:53
 * @Email: KosmoSakura@gmail.com
 * * @GET 表明这是get请求
 * * @POST 表明这是post请求
 * * @PUT 表明这是put请求
 * * @DELETE 表明这是delete请求
 * * @PATCH 表明这是一个patch请求，该请求是对put请求的补充，用于更新局部资源
 * * @HEAD 表明这是一个head请求
 * * @OPTIONS 表明这是一个option请求
 * * @HTTP 通用注解, 可以替换以上所有的注解，其拥有三个属性：method，path，hasBody
 * * ******* ******* ******* ******* ******* ******* ******* ******* *******
 * * @Headers 用于添加固定请求头，可以同时添加多个。通过该注解添加的请求头不会相互覆盖，而是共同存在
 * * @Header 作为方法的参数传入，用于添加不固定值的Header，该注解会更新已有的请求头
 * * * ******* ******* ******* ******* ******* ******* ******* ******* *******
 * * @Body 多用于post请求发送非表单数据, 比如想要以post方式传递json格式数据
 * * @Filed 多用于post请求中表单字段, Filed和FieldMap需要FormUrlEncoded结合使用
 * * @FiledMap 和@Filed作用一致，用于不确定表单参数
 * * @Part 用于表单字段, Part和PartMap与Multipart注解结合使用, 适合文件上传的情况
 * * @PartMap 用于表单字段, 默认接受的类型是Map<String,REquestBody>，可用于实现多文件上传
 * * <p>
 * * Part标志上文的内容可以是富媒体形势，比如上传一张图片，上传一段音乐，即它多用于字节流传输。
 * * 而Filed则相对简单些，通常是字符串键值对。
 * * <p>
 * * Part标志上文的内容可以是富媒体形势，比如上传一张图片，上传一段音乐，即它多用于字节流传输。
 * * 而Filed则相对简单些，通常是字符串键值对。
 * * @Path 用于url中的占位符,{占位符}和PATH只用在URL的path部分，url中的参数使用Query和QueryMap代替，保证接口定义的简洁
 * * @Query 用于Get中指定参数
 * * @QueryMap 和Query使用类似
 * * @Url 指定请求路径
 */
public interface KRequestCache {
    /**
     * method 表示请求的方法，区分大小写 * path表示路径 * hasBody表示是否有请求体
     */
    @HTTP(method = "GET", path = "blog/{id}", hasBody = false)
    Call<ResponseBody> getBlog(@Path("id") int id);

    /**
     * 很多情况下，我们需要上传json格式的数据。
     * 比如当我们注册新用户的时候，因为用户注册时的数据相对较多，
     * 并可能以后会变化，这时候，服务端可能要求我们上传json格式的数据。此时就要@Body注解来实现。
     * 直接传入实体,它会自行转化成Json
     */
    @POST("------")
    Observable<ResponseBody> getCategory(@Body String json);

    /**
     * 单文件上传
     * retrofit 2.0的上传和以前略有不同，需要借助@Multipart注解、@Part和MultipartBody实现。
     * 直接：@Part("url")可能导致URL被转义
     * 可以这样写，
     * {url}-->动态url
     */
    @Multipart
    @POST("/aaa/sss/{url}/ccc")
    Observable<ResponseBody> upload(@Part("url") String url, @Part MultipartBody.Part file);

    /**
     * 防止url被转义
     */
    @POST("/aaa/sss/{url}/ccc")
    Call<ResponseBody> upload(@Path(value = "url", encoded = true) String url);

    /**
     * 多文件上传
     */
    @Multipart
    @POST("upload/upload")
    Call<ResponseBody> upload(@PartMap Map<String, MultipartBody.Part> map);

    /**
     * 图文混传
     */
    @Multipart
    @POST("upload/upload")
    Call<ResponseBody> upload(@Body String post, @PartMap Map<String, MultipartBody.Part> map);


    /**
     * 文件下载
     * 这里需要注意的是如果下载的文件较大，比如在10m以上，
     * 那么强烈建议使用@Streaming 进行注解，否则将会出现IO异常.
     */
    @GET
    @Streaming
    Call<ResponseBody> download(@Url String fileUrl);
}
