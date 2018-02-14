package com.tjstudy.wifidemo.base;

import android.app.Activity;
import android.os.Process;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity的管理类
 * 单例模式---懒汉式
 */

public class ActivityCollector {
    //2、定义实例
    private static ActivityCollector activityCollector = null;
    private List<Activity> activities = new ArrayList<>();

    //私有化构造方法
    private ActivityCollector() {
    }

    /**
     * 添加activity到activity管理器
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    /**
     * 从activity管理器中移除activity
     *
     * @param activity
     */
    public void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    //对外提供实例
    public synchronized static ActivityCollector getInstance() {
        if (activityCollector == null) {
            activityCollector = new ActivityCollector();
        }
        return activityCollector;
    }

    /**
     * 退出app
     */
    public void existApp() {
        for (Activity activity : activities) {
            activity.finish();
        }
        Process.killProcess(Process.myPid());//关掉当前进程
    }

    /**
     * 获取栈顶activity
     *
     * @return
     */
    public Activity getTopActivity() {
        if (activities == null || activities.size() == 0) {
            return null;
        }
        return activities.get(activities.size() - 1);
    }

    /**
     * 获取activity的大小
     * @return
     */
    public int getActivitySize(){
        return activities.size();
    }
}
