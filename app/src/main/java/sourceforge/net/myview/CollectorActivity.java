package sourceforge.net.myview;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created：2018/4/18 on 16:50
 * Author:gaideng on dg
 * Description:
 */

public class CollectorActivity {
    public static List<Activity> collect = new ArrayList<>();
    public static void addAct(Activity act){
        if (act != null) {
            collect.add(act);
        }
    }
    public static void removeAct(Activity act){
        if (act != null) {
            collect.remove(act);
        }
    }
    public static void finish(){
        for (Activity act :
                collect) {
            if (act != null){
                act.finish();
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
    }



}
