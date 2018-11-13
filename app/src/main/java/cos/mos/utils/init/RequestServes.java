package cos.mos.utils.init;

import com.google.gson.JsonObject;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;


public interface RequestServes {
    //分类接口
    @GET("category")
    Observable<JsonObject> getCategory();


    @GET
    @Streaming
    Call<ResponseBody> download(@Url String fileUrl);
}

