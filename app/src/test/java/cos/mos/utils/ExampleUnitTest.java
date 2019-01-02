package cos.mos.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Before
    public void doBefor() {
//        assertEquals(4, 2 + 2);
        System.out.println("doBefor");
    }

    @After
    public void doAfter() {
//        assertEquals(4, 2 + 2);
        System.out.println("doAfter");
    }

    @Test
    public void dodo() {
//        assertEquals(4, 2 + 2);
        System.out.println("dodo");
    }
}