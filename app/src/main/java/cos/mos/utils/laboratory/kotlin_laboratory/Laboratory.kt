package cos.mos.utils.laboratory.kotlin_laboratory

/**
 * @Description:
 * @Author: Kosmos
 * @Date: 2019.04.29 10:09
 * @Email: KosmoSakura@gmail.com
 */
fun main(args: Array<String>) {
    test3()
}

fun test1() {
    repeat(10) {
        print(it)
    }
}

fun test2() {
    print("开始循环\n")
    jump@
    for (i in 1..3) {
        for (j in 1..3) {
            if (i == 2) {
                break@jump
            }

            print("i=$i,j=$j  ")
        }
    }
    print("\n结束循环")
}

fun test3() {
    val list = arrayListOf<String>("a", "b", "c")
    for (index in list.indices) {
        println("第$index 个是${list[index]}")
    }

}