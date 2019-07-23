package org.tuzhao.umeng;

/**
 * 开关push的回调
 *
 * @author tuzhao
 */
public interface PushSetCallback {

    void onSuccess();

    void onFailure(String s, String s1);

}
