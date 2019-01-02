package cos.mos.utils.tool;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.runner.AndroidJUnit4;
import cos.mos.library.init.KApp;
import cos.mos.library.utils.USP;

/**
 * @Description: <p>
 * @Author: Kosmos
 * @Date: 2019.01.02 17:29
 * @Email: KosmoSakura@gmail.com
 */
// @RunWith 只在混合使用 JUnit3 和 JUnit4 需要，若只使用JUnit4，可省略
@RunWith(AndroidJUnit4.class)
public class UUTextTest {
    //    public static SharedPreferences sp;
//    public static SharedPreferences.Editor editor;

    @Before
    public void setUp() {
        USP.instance().init(KApp.getInstance(), "test");
    }


    @Test
    public void runCode() {
        USP.instance().putString("haha", "csncainssicns");

        Assert.assertEquals("七的可能", USP.instance().getString("haha", "木有"));
        Assert.assertEquals("七的可能", USP.instance().getString("haha213", "木有"));
//        System.out.println(sp.getString("haha", "木有"));
//        System.out.println(sp.getString("hahass", "木有"));
    }
}