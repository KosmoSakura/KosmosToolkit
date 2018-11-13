package cos.mos.library.retrofit.file;

public interface FileProgressCallback {
    void onLoading(long total, long progress);
}