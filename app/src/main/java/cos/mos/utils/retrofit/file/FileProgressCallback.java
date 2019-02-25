package cos.mos.utils.retrofit.file;

public interface FileProgressCallback {
    void onLoading(long total, long progress);
}