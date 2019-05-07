package cos.mos.utils.ukotlin.okhttp

/**
 * @Description:
 * @Author: Kosmos
 * @Date: 2019.05.06 15:39
 * @Email: KosmoSakura@gmail.com
 */
interface KtHttpListener {
    fun failure(describe: String, code: Int)

    fun success(response: Any?)
}
