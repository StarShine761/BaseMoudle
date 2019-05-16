package component.shine.com.basemoudle.mvp;

import android.support.annotation.NonNull;


/**
 * Created by cc
 * On 2019/5/16.
 */
public interface IView {

    /**
     * 显示加载
     */
    default void showLoading() {

    }

    /**
     * 隐藏加载
     */
    default void hideLoading() {

    }

    /**
     * 显示信息
     *
     * @param message 消息内容, 不能为 {@code null}
     */
    void showMessage(@NonNull String message);


}
