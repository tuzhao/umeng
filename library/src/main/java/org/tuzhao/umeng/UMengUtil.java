package org.tuzhao.umeng;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

import java.util.List;
import java.util.Map;

/**
 * @author tuzhao
 */
public final class UMengUtil {

    private static final String TAG = "UMengUtil";
    private static boolean isDebug = false;

    /**
     * @param context          Context
     * @param appKey           当前友盟app使用的key
     * @param channel          当前apk包使用的渠道号
     * @param pushKey          友盟推送key
     * @param isOpenLog        是否打开调试log
     * @param isCatchException 是否错误统计功能
     * @param isEncrypt        是否对日志进行加密
     */
    public static void init(Context context, String appKey, String channel, String pushKey,
                            boolean isOpenLog, boolean isCatchException, boolean isEncrypt) {
        try {
            /*
             * 是否打开调试log
             */
            UMConfigure.setLogEnabled(isOpenLog);
            /*
             * 如果参数为true，SDK会对日志进行加密。加密模式可以有效防止网络攻击，提高数据安全性。
             */
            UMConfigure.setEncryptEnabled(isEncrypt);
            //基础组件包提供的初始化函数
            UMConfigure.init(context, appKey, channel, UMConfigure.DEVICE_TYPE_PHONE, pushKey);
            //将默认Session间隔时长改为30秒。
            MobclickAgent.setSessionContinueMillis(1000 * 30);
            // isEnable: false-关闭错误统计功能；true-打开错误统计功能（默认打开）
            MobclickAgent.setCatchUncaughtExceptions(isCatchException);
            //选用LEGACY_AUTO页面采集模式
            MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.LEGACY_MANUAL);
            //支持在子进程中统计自定义事件
            UMConfigure.setProcessEvent(true);
            //获取消息推送代理示例
            PushAgent mPushAgent = PushAgent.getInstance(context);
            //注册推送服务，每次调用register方法都会回调该接口
            mPushAgent.register(new IUmengRegisterCallback() {
                @Override
                public void onSuccess(String deviceToken) {
                    //注册成功会返回deviceToken deviceToken是推送消息的唯一标志
                    log("push register success,device token: " + deviceToken);
                }

                @Override
                public void onFailure(String s, String s1) {
                    logE("push register failure," + "s:" + s + ",s1:" + s1);
                }
            });
        } catch (Exception e) {
            Log.w(TAG, "init umeng error", e);
        }
    }

    public static void onResume(Context context) {
        MobclickAgent.onResume(context);
    }

    public static void onPause(Context context) {
        MobclickAgent.onPause(context);
    }

    public static void onResume(Fragment fragment) {
        MobclickAgent.onPageStart(fragment.getClass().getName());
    }

    public static void onPause(Fragment fragment) {
        MobclickAgent.onPageEnd(fragment.getClass().getName());
    }

    public static void userLogin(String loginInfo) {
        MobclickAgent.onProfileSignIn(loginInfo);
    }

    public static void userLogout() {
        MobclickAgent.onProfileSignOff();
    }

    public static void onKillProcess(Context context) {
        MobclickAgent.onKillProcess(context);
    }

    /**
     * 该方法是【友盟+】Push后台进行日活统计及多维度推送的必调用方法，请务必调用！
     *
     * @param context Context
     */
    public static void pushStart(Context context) {
        PushAgent.getInstance(context).onAppStart();
    }

    /**
     * 计数事件
     *
     * @param context Context
     * @param eventId 当前统计的事件ID。
     */
    public static void onEvent(Context context, String eventId) {
        MobclickAgent.onEvent(context, eventId);
    }

    /**
     * 计数事件
     *
     * @param context Context
     * @param eventId 当前统计的事件ID。
     * @param label   事件的标签参数。
     */
    public static void onEvent(Context context, String eventId, String label) {
        MobclickAgent.onEvent(context, eventId, label);
    }

    /**
     * 计数事件
     *
     * @param context Context
     * @param eventId 当前统计的事件ID。
     * @param map     当前事件的参数和取值（Key-Value键值对）。
     */
    public static void onEvent(Context context, String eventId, Map<String, String> map) {
        MobclickAgent.onEvent(context, eventId, map);
    }

    /**
     * 计算事件
     *
     * @param context Context
     * @param eventID 当前统计的事件ID。
     * @param map     当前事件的参数和取值（Key-Value键值对）。
     * @param du      当前事件的数值，取值范围是-2,147,483,648 到 +2,147,483,647 之间的
     *                有符号整数，即int 32类型，如果数据超出了该范围，会造成数据丢包，
     *                影响数据统计的准确性。
     */
    public static void onEventValue(Context context, String eventID, Map<String, String> map, int du) {
        MobclickAgent.onEventValue(context, eventID, map, du);
    }

    /**
     * 设置关注首次触发自定义事件接口
     * <p>
     * 如果用户需要统计特定自定义事件首次触发时机，可以通过setFirstLaunchEvent接口设置
     * 监听首次触发事件列表，所有在此列表中的自定义事件，服务器都能对其首次触发进行统计。
     *
     * @param context     Context
     * @param eventIdList 需要监听首次触发时机的自定义事件列表。
     */
    public static void setFirstLaunchEvent(Context context, List<String> eventIdList) {
        MobclickAgent.setFirstLaunchEvent(context, eventIdList);
    }

    /**
     * 如果开发者自己捕获了错误，需要手动上传到【友盟+】服务器
     *
     * @param context Context
     * @param error   错误内容字符串。
     */
    public static void reportError(Context context, String error) {
        MobclickAgent.reportError(context, error);
    }

    /**
     * 如果开发者自己捕获了错误，需要手动上传到【友盟+】服务器
     *
     * @param context Context
     * @param e       错误发生时抛出的异常对象。
     */
    public static void reportError(Context context, Throwable e) {
        MobclickAgent.reportError(context, e);
    }

    private static void log(String msg) {
        if (isDebug) {
            Log.d(TAG, msg);
        }
    }

    private static void logE(String msg) {
        if (isDebug) {
            Log.e(TAG, msg);
        }
    }

    public static void setIsDebug(boolean isDebug) {
        UMengUtil.isDebug = isDebug;
    }
}
