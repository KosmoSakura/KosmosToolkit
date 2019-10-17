package cos.mos.utils.java_laboratory;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @Description 关于Java提供的一些排序api
 * @Author Kosmos
 * @Date 2018.12.10 17:53
 * @Email KosmoSakura@gmail.com
 */
public class NoteSort {
//    public static void main(String[] args) {
//        sortInt();//int排序
//        sortBoolean();//boolean排序
//        sortStr();//String排序
//    }


    /**
     * String排序
     */
    private static void sortStr() {
        //原数组
        List<String> intList = Arrays.asList("菜单", "casc", "查水表", "bsae", "12dsa");
        point(intList);//菜单,casc,查水表,bsae,12dsa,
        //正序排列
        Collections.sort(intList);
        point(intList);//12dsa,bsae,casc,查水表,菜单,
        //倒序排列
        Collections.sort(intList, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                // 返回值为int类型，大于0表示正序，小于0表示逆序
                return o2.compareTo(o1);
            }
        });
        point(intList);//菜单,查水表,casc,bsae,12dsa,
    }

    /**
     * boolean排序
     */
    private static void sortBoolean() {
        //原数组
        List<Boolean> intList = Arrays.asList(true, false, true, true, false);
        point(intList);//true,false,true,true,false,
        //正序排列
        Collections.sort(intList);
        point(intList);//false,false,true,true,true,
        //倒序排列
        Collections.sort(intList, new Comparator<Boolean>() {
            @Override
            public int compare(Boolean o1, Boolean o2) {
                // 返回值为int类型，大于0表示正序，小于0表示逆序
                return -1;
            }
        });
        point(intList);//true,true,true,false,false
    }

    /**
     * int排序
     */
    private static void sortInt() {
        //原数组
        List<Integer> intList = Arrays.asList(2, 3, 1, 5, 9);
        point(intList);//2,3,1,5,9,
        //正序排列
        Collections.sort(intList);
        point(intList);//1,2,3,5,9,
        //倒序排列
        Collections.sort(intList, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                // 返回值为int类型，大于0表示正序，小于0表示逆序
                return o2 - o1;
            }
        });
        point(intList);//9,5,3,2,1,
    }

    private static void point(List list) {
        for (Object o : list) {
            System.out.print(o.toString() + ",");
        }
        System.out.println();
        System.out.println("-----------------------------------");
    }
}
