package org.tuzhao.umeng;

import android.content.Context;
import android.util.Log;

import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengCallback;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

import java.util.List;
import java.util.Map;

/**
 * 自己封装的简易友盟相关操作工具类
 *
 * @author tuzhao
 */
public final class UMengUtil {

    private static final String TAG = "UMengUtil";
    private static boolean isDebug = false;

    /**
     * 这个方法主要是不初始化推送
     *
     * @param context          Context
     * @param appKey           当前友盟app使用的key
     * @param channel          当前apk包使用的渠道号
     * @param isOpenLog        是否打开调试log
     * @param isCatchException 是否错误统计功能
     * @param isEncrypt        是否对日志进行加密
     */
    public static void init(Context context, String appKey, String channel, boolean isOpenLog,
                            boolean isCatchException, boolean isEncrypt) {
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
            UMConfigure.init(context, appKey, channel, UMConfigure.DEVICE_TYPE_PHONE, null);
            //将默认Session间隔时长改为30秒。
            MobclickAgent.setSessionContinueMillis(1000 * 30);
            // isEnable: false-关闭错误统计功能；true-打开错误统计功能（默认打开）
            MobclickAgent.setCatchUncaughtExceptions(isCatchException);
            /*
             * LEGACY_AUTO：
             * SDK默认情况下使用此模式，对于多数老版本【友盟+】统计SDK的开发者，如果在您的App中之前没有使
             * 用MobclickAgent.onPageStart/MobclickAgent.onPageEnd这两个接口对非Activity页面
             * (如:Fragment)进行埋点统计。则请选择此模式，这样您的App埋点代码不需要做任何修改，SDK
             * 即可正常工作。(需确保您应用中所有Activity中都已经手动
             * 调用MobclickAgent.onResume/MobclickAgent.onPause接口)
             *
             * LEGACY_MANUAL：
             * 对于已经在App中使用MobclickAgent.onPageStart/MobclickAgent.onPageEnd这两个接口对非
             * Activity页面(如:Fragment)进行埋点统计的SDK老用户，则请选择LEGACY_MANUAL模式，这样您的
             * App埋点代码不需要做任何修改，SDK即可正常工作。(需确保您应用中所有Activity中都已经手动调
             * 用MobclickAgent.onResume/MobclickAgent.onPause接口)
             */
            MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.LEGACY_MANUAL);
            //支持在子进程中统计自定义事件
            UMConfigure.setProcessEvent(true);
        } catch (Exception e) {
            Log.w(TAG, "init umeng error", e);
        }
    }

    /**
     * @param context          Context
     * @param appKey           当前友盟app使用的key
     * @param channel          当前apk包使用的渠道号
     * @param pushKey          友盟推送key Push推送业务的secret，需要集成Push功能时必须传入Push的secret，否则传空。
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
            /*
             * LEGACY_AUTO：
             * SDK默认情况下使用此模式，对于多数老版本【友盟+】统计SDK的开发者，如果在您的App中之前没有使
             * 用MobclickAgent.onPageStart/MobclickAgent.onPageEnd这两个接口对非Activity页面
             * (如:Fragment)进行埋点统计。则请选择此模式，这样您的App埋点代码不需要做任何修改，SDK
             * 即可正常工作。(需确保您应用中所有Activity中都已经手动
             * 调用MobclickAgent.onResume/MobclickAgent.onPause接口)
             *
             * LEGACY_MANUAL：
             * 对于已经在App中使用MobclickAgent.onPageStart/MobclickAgent.onPageEnd这两个接口对非
             * Activity页面(如:Fragment)进行埋点统计的SDK老用户，则请选择LEGACY_MANUAL模式，这样您的
             * App埋点代码不需要做任何修改，SDK即可正常工作。(需确保您应用中所有Activity中都已经手动调
             * 用MobclickAgent.onResume/MobclickAgent.onPause接口)
             */
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

    /**
     * 在AUTO或MANUAL模式下，如果需要对非Activity页面，如Fragment、自定义View等非标准页面进行统计。
     * 需要通过MobclickAgent.onPageStart/MobclickAgent.onPageEnd接口在合适的时机进行页面统计。
     * 一次成对的 onPageStart to onPageEnd 调用，对应一次非Activity页面(如：Fragment)生命周期统计。
     * 在fragment生命周期onResume()里面调用
     *
     * @param fragment 这里我们做一个封装，只针对fragment
     */
    public static void onResumeOfFragment(Object fragment) {
        String className = fragment.getClass().getName();
        if (isDebug) {
            if (className.equals("android.support.v4.app.Fragment") || className.equals("androidx.fragment.app.Fragment")) {
                //...ignore...
            } else {
                logW("you should use fragment for onResumeOfFragment()");
            }
        }
        MobclickAgent.onPageStart(className);
    }

    /**
     * 在fragment生命周期onPause()里面调用
     *
     * @param fragment 这里我们做一个封装，只针对fragment
     */
    public static void onPauseOfFragment(Object fragment) {
        String className = fragment.getClass().getName();
        if (isDebug) {
            if (className.equals("android.support.v4.app.Fragment") || className.equals("androidx.fragment.app.Fragment")) {
                //...ignore...
            } else {
                logW("you should use fragment for onPauseOfFragment()");
            }
        }
        MobclickAgent.onPageEnd(className);
    }

    /**
     * @param loginInfo 用户账号ID，长度小于64字节
     */
    public static void userLogin(String loginInfo) {
        MobclickAgent.onProfileSignIn(loginInfo);
    }

    /**
     * @param Provider  账号来源。如果用户通过第三方账号登陆，可以调用此接口进行统计。支持自定义，
     *                  不能以下划线”_”开头，使用大写字母和数字标识，长度小于32 字节; 如果是上市
     *                  公司，建议使用股票代码。
     * @param loginInfo 用户账号ID，长度小于64字节
     */
    public static void userLogin(String Provider, String loginInfo) {
        MobclickAgent.onProfileSignIn(Provider, loginInfo);
    }

    /**
     * 账号登出时需调用此接口，调用之后不再发送账号相关内容。
     */
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

    /**
     * 为了便于开发者更好的集成配置文件，我们提供了对于AndroidManifest配置文件的检查工具，可以
     * 自行检查开发者的配置问题。SDK默认是不检查集成配置文件的。
     *
     * @param context Context
     * @param check   true 启用检查集成配置文件
     */
    public static void setPushCheck(Context context, boolean check) {
        PushAgent.getInstance(context).setPushCheck(check);
    }

    /**
     * 推送打开 or 关闭设置
     *
     * @param context  Context
     * @param enable   true 打开推送 false 关闭推送
     * @param callback PushSetCallback
     */
    public static void setPushEnabled(Context context, boolean enable, final PushSetCallback callback) {
        PushAgent agent = null;
        try {
            agent = PushAgent.getInstance(context);
        } catch (Exception e) {
            Log.e(TAG, "get push agent error", e);
        }
        if (null != agent) {
            IUmengCallback uc = new IUmengCallback() {

                @Override
                public void onSuccess() {
                    callback.onSuccess();
                }

                @Override
                public void onFailure(String s, String s1) {
                    callback.onFailure(s, s1);
                }
            };
            if (enable) {
                agent.enable(uc);
            } else {
                agent.disable(uc);
            }
        }
    }

    private static void log(String msg) {
        if (isDebug) {
            Log.d(TAG, msg);
        }
    }

    private static void logW(String msg) {
        if (isDebug) {
            Log.w(TAG, msg);
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
