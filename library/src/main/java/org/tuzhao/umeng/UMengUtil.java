package org.tuzhao.umeng;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

/**
 * @author tuzhao
 */
public final class UMengUtil {

    private static final String TAG = "UMengUtil";
    private static boolean isDebug = false;

    public static void init(Context context, String appKey, String channel, String pushKey, boolean isOpenLog) {
        try {
            UMConfigure.setLogEnabled(isOpenLog);
            //基础组件包提供的初始化函数
            UMConfigure.init(context, appKey, channel, UMConfigure.DEVICE_TYPE_PHONE, pushKey);
            //将默认Session间隔时长改为30秒。
            MobclickAgent.setSessionContinueMillis(1000 * 30);
            MobclickAgent.setCatchUncaughtExceptions(true);
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
     */
    public static void pushStart(Context context) {
        PushAgent.getInstance(context).onAppStart();
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
