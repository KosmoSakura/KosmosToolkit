package cos.mos.utils.net.okhttp_kotlin

/**
 * @Description:
 * @Author: Kosmos
 * @Date: 2019.05.06 20:33
 * @Email: KosmoSakura@gmail.com
 */
open class BaseBean<T>(open val code: Int, open val msg: String, open val data: T)