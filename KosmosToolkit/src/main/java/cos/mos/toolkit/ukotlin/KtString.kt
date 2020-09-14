package cos.mos.toolkit.ukotlin

/**
 * @Description:字符处理类
 * @Author: Kosmos
 * @Date: 2019.04.30 19:34
 * @Email: KosmoSakura@gmail.com
 */
@Deprecated("还是不用了，日后只在Uxxx.java中更新")
object KtString {
    /**
     * @param str "1967+2356-433*12/66"
     * @return [1967], [2356], [433], [12], [66]
     */
    fun split1(str: String): List<String> = str.split("+", "-", "*", "/")

    /**
     * 效果同上
     * */
    fun split2(str: String): List<String> = str.split(Regex("[+\\-*/]"))

    /**
     *@param str "5+3/2*3*0.5“
     * @return 6.0
     * */
    fun calculat(str: String): Float {
        var text = str
        var diff = 1f
        if ("+-/*".contains(str[0])) {
            if (str[0] == '-') {
                diff = -1f
            }
            text = str.substring(1, str.length)
        }
        val symbol = text.split(Regex("[0-9.]")).filter { it != "" }
        val digits = text.split(Regex("[+\\-*/]")).filter { it != "" }
        var calculat = 0f
        //5 6 2 1
        for (index in digits.indices) {
            if (index == 0) {
                calculat = digits[index].toFloat() * diff
            } else {
                when (symbol[index - 1]) {
                    "+" -> {
                        calculat += digits[index].toFloat()
                    }
                    "-" -> {
                        calculat -= digits[index].toFloat()
                    }
                    "*" -> {
                        calculat *= digits[index].toFloat()
                    }
                    "/" -> {
                        calculat /= digits[index].toFloat()
                    }
                }
            }
        }
        return calculat
    }

}