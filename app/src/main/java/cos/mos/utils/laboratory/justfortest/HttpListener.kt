package cos.mos.utils.laboratory.justfortest

/**
 * @Description 我是一个虚假的回调
 * @Author Kosmos
 * @Date 2019.10.18 16:22
 * @Email KosmoSakura@gmail.com
 * */
interface HttpListener<T> {
    fun httpSucceed(out: T)
    fun httpFail(describe: String)
}