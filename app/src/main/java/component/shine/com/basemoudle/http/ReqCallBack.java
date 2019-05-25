package component.shine.com.basemoudle.http;

/**
 * Created by cc
 * On 2019/5/24.
 * okhttp回调
 */
public interface ReqCallBack<T> {

    /**
     * 成功的回调
     * @param result
     */
    void OnSuccess(T result);


    /**
     * 失败的回调
     */
    void onError(String error);
}
