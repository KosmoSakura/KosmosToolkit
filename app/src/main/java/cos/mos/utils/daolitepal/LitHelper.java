package cos.mos.utils.daolitepal;

import android.database.Cursor;

import org.litepal.LitePal;

import java.util.List;

/**
 * @Description: <p>
 * @Author: Kosmos
 * @Date: 2019.03.19 18:32
 * @Email: KosmoSakura@gmail.com
 */
public class LitHelper {
    private KeepDB keep;
    private List<KeepDB> keepList;

    /**
     * @return 存储是否成功
     */
    public boolean add() {
        keep = new KeepDB();
        keep.setAppName("assasa");
        keep.setPkgName("sdsdsd");
        return keep.save();
    }

    /**
     * @param list 保存列表
     */
    public void add(List<KeepDB> list) {
        LitePal.saveAll(list);
    }

    /**
     * 删除
     */
    public void del() {
        //删除数据库中IgnoreDB表的所有记录
        LitePal.deleteAll(KeepDB.class);
        //删除数据库movie表中id为ass的记录
//        LitePal.deleteAll(KeepDB.class, "ass");
        //删除数据库movie表中size大于3500的记录
        LitePal.deleteAll(KeepDB.class, "size > ?", "3500");
    }

    /**
     * 改
     */
    public void change() {
        //方法一：
        //第一步，查找id为1的记录
        keep = LitePal.find(KeepDB.class, 1);
        //第二步，改变某个字段的值
        keep.setSize(123);
        //第三步，保存数据
        keep.save();

        //方法二：
        //第一步，查找id为1的记录
        keep = new KeepDB();
        //第二步，改变某个字段的值
        keep.setSize(123);
        //第三步，直接更新id为1的记录
        keep.update(1);

        //方法三：
        keep = new KeepDB();
        keep.setAppName("someone");
        //更新所有name为2Diots的记录,将director字段设为someone
        keep.updateAll("name = ?", "2Diots");

        keep = new KeepDB();
        keep.setAppName("someone");
        keep.setPkgName("someone");
        //将更新所有name为2Diots，director为gpf的记录name和director均改为someone
        keep.updateAll("name=? and director=?", "2Diots", "gpf");
    }

    /**
     * 查
     */
    public void search() {
        //keep表id为1的记录
        keep = LitePal.find(KeepDB.class, 1);
        //keep表总第一条数据
        keep = LitePal.findFirst(KeepDB.class);
        //keep表总最后一条数据
        keep = LitePal.findLast(KeepDB.class);

        //查找keep表的所有记录(返回值是一个泛型为Movie的List集合)
        keepList = LitePal.findAll(KeepDB.class);
        //查找keep表总id=1,3,4,5的记录
        keepList = LitePal.findAll(KeepDB.class, 1, 3, 4, 5);

        //查找name为aaaa的记录,并且以size作排序
        keepList = LitePal.where("name = ?", "aaaa")
            .order("size")
            .find(KeepDB.class);
        //查找size大于30的记录，中的 name和size字段，倒序输出
        keepList = LitePal.select("name", "size")
            .where("size > ?", "30")
            .order("size desc")
            .find(KeepDB.class);

        //查找size>30 或者 name=aaa的数据
        keepList = LitePal.where("size > ？ or name = ？", "30", "aaa")
            .find(KeepDB.class);
        //查找size>30 并且 name=aaa的数据
        keepList = LitePal.where("size > ？ and name = ？", "30", "aaa")
            .find(KeepDB.class);
        //写在一起
        keepList = LitePal.where("size > 30 and name = aaa")
            .find(KeepDB.class);

        //模糊查询name中有aaa的记录
        keepList = LitePal.where("name like ?", "%aaa%").find(KeepDB.class);
        //原生查询
        Cursor cursor = LitePal.findBySQL("select * from keep where size>?", "0");
    }
}
