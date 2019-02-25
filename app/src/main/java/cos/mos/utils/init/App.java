package cos.mos.utils.init;


import cos.mos.utils.init.k.KApp;

/**
 * @Description: <p> Flatten Package Abbreviate qualified
 * @Author: Kosmos
 * @Date: 2018.11.12 15:44
 * @Email: KosmoSakura@gmail.com
 */
public class App extends KApp {

    @Override
    protected void initApp() {

    }

    @Override
    protected boolean debugState() {
        return true;
    }

    @Override
    protected String logTag() {
        return "Kosmos";
    }

    @Override
    protected String defaultSP() {
        return "Kosmos";
    }

    @Override
    protected String baseUrl() {
        return "-----";
    }
}
