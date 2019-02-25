package cos.mos.utils.zkosmoslibrary.retrofit.file;

public interface FileProgressCallback {
    void onLoading(long total, long progress);
}