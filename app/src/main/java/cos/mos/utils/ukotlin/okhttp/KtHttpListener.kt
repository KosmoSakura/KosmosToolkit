package cos.mos.utils.ukotlin.okhttp

import okhttp3.Request

/**
 * @Description:
 * @Author: Kosmos
 * @Date: 2019.05.06 15:39
 * @Email: KosmoSakura@gmail.com
 */
interface KtHttpListener<T> {
    fun failure(request: Request?, e: Exception?, describe: String, code: Int)

    fun success(response: T)
}
