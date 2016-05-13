package com.rengwuxian.rxjavasamples.module.not_more_click_7;

/**
 * 通过synchronized的方式，在时间间隔内不允许多次点击
 * Created by trity on 13/5/16.
 */
public class NoMutilClickUtils {
    private static long lastClickTime;
    private static int DEFAULT_SPACE_TIME = 500;//点击的时间间隔

    /**
     * 指定间隔时间内，再次点击无效
     * @param spaceTime
     * @return
     */
    public synchronized static boolean isMutiClick(int spaceTime) {
        if(spaceTime>0)
            DEFAULT_SPACE_TIME=spaceTime;
        long currentTime = System.currentTimeMillis();
        boolean isClick2;
        if (currentTime - lastClickTime >
                DEFAULT_SPACE_TIME) {
            isClick2 = false;
        } else {
            isClick2 = true;
        }
        lastClickTime = currentTime;
        return isClick2;
    }
}
