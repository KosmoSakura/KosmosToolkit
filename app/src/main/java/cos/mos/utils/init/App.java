package cos.mos.utils.init;


import cos.mos.library.init.KApp;

/**
 * @Description: <p>
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
    protected String defaultTap() {
        return "Kosmos";
    }

    @Override
    protected String defaultSP() {
        return "wallpaper";
    }

    @Override
    protected String baseUrl() {
        return "https://api.qv92.com/wp/";
    }
}
