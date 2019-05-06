package cos.mos.utils.net.retrofit.file;

public interface FileProgressCallback {
    void onLoading(long total, long progress);
}