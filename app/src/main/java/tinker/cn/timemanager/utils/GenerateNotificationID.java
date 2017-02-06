package tinker.cn.timemanager.utils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by tiankui on 1/12/17.
 */

public class GenerateNotificationID {

    private static final AtomicInteger counter=new AtomicInteger();

    public static int getID(){
        return counter.getAndIncrement()+1;
    }
}
