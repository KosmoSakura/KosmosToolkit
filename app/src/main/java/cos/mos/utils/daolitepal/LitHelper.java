package cos.mos.utils.daolitepal;

import org.litepal.LitePal;

import java.util.List;

/**
 * @Description: <p>
 * @Author: Kosmos
 * @Date: 2019.03.19 18:32
 * @Email: KosmoSakura@gmail.com
 */
public class LitHelper {
    /**
     * @return 存储是否成功
     */
    public boolean add() {
        IgnoreDB db = new IgnoreDB();
        db.setAppName("assasa");
        db.setPkgName("sdsdsd");
        return db.save();
    }

    /**
     * @param list 保存列表
     */
    public void add(List<IgnoreDB> list) {
        LitePal.saveAll(list);
    }

    /**
     * 删除
     */
    public void del() {
        //删除数据库中IgnoreDB表的所有记录
        LitePal.deleteAll(IgnoreDB.class);
        //删除数据库movie表中id为ass的记录
//        LitePal.deleteAll(IgnoreDB.class, "ass");
        //删除数据库movie表中size大于3500的记录
        LitePal.deleteAll(IgnoreDB.class, "size > ?", "3500");
    }

    /**
     * 改
     */
    public void change() {
        //方法一：
        //第一步，查找id为1的记录
        IgnoreDB db0 = LitePal.find(IgnoreDB.class, 1);
        //第二步，改变某个字段的值
        db0.setSize(123);
        //第三步，保存数据
        db0.save();

        //方法二：
        //第一步，查找id为1的记录
        IgnoreDB db = new IgnoreDB();
        //第二步，改变某个字段的值
        db.setSize(123);
        //第三步，直接更新id为1的记录
        db.update(1);

        //方法三：
        IgnoreDB movie = new IgnoreDB();
        movie.setAppName("someone");
        //更新所有name为2Diots的记录,将director字段设为someone
        movie.updateAll("name = ?", "2Diots");

        IgnoreDB movie2 = new IgnoreDB();
        movie2.setAppName("someone");
        movie2.setPkgName("someone");
        //将更新所有name为2Diots，director为gpf的记录name和director均改为someone
        movie.updateAll("name=? and director=?", "2Diots", "gpf");
    }

    /**
     * 查
     */
    public void search() {
        //查找movie表的所有记录，返回值是一个泛型为Movie的List集合
        List<IgnoreDB> allMovies = LitePal.findAll(IgnoreDB.class);

        //查找movie表id为1的记录
        IgnoreDB movie = LitePal.find(IgnoreDB.class, 1);

        //查找name为2Diots的记录,并且以时长作排序
        List<IgnoreDB> movies = LitePal.where("name = ?", "2Diots").order("duration").find(IgnoreDB.class);

        //查找所有年龄小于25岁的人
        List<IgnoreDB> person = LitePal.where("size < ?", "25").find(IgnoreDB.class);
    }
}
