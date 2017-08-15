package com.ldh.androidlib.net;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.ldh.androidlib.BuildConfig;
import com.ldh.androidlib.net.fastjson.FastJsonConverterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import okhttp3.ConnectionPool;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by ldh on 2017/8/15.
 */

public class RetrofitFactory {

    private static final int DEFAULT_TIMEOUT = 20;
    /**
     * 同一baseUrl可复用
     */
    private static Retrofit retrofit;

    /**
     * 使用默认 retrofit
     *
     * @param service
     * @param <T>
     * @return
     */
    public static <T> T createService(final Class<T> service) {
        if (retrofit == null) {
            retrofit = retrofitAdapter(ApiUtil.getBaseUrl(), new BaseIntercepter());
        }
        return retrofit.create(service);
    }


    public static <T> T createService(final Class<T> service, String host, String version) {
        return createService(service, host, version, null, null);
    }

    public static <T> T createService(final Class<T> service, String host, String version, String publicKey, String privateKey) {
        if (TextUtils.isEmpty(host)) host = ApiUtil.getHost();
        if (TextUtils.isEmpty(version)) version = "/";//retrofit2: baseUrl must end in /

        String url = host + version;
        //可以添加header与公共参数
        Interceptor interceptor = new BaseIntercepter();

        Retrofit retrofit = retrofitAdapter(url, interceptor);

        return retrofit.create(service);
    }

    /**
     * 测试接口用，不含公共参数与header
     * 使用FastJsonConverterFactory
     */
    public static <T> T createTestService(final Class<T> service, String host, String version) {
        if (TextUtils.isEmpty(host)) host = ApiUtil.getHost();
        if (TextUtils.isEmpty(version)) version = "/";

        String url = host + version;
        Retrofit retrofit = retrofitAdapter(url, new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                return chain.proceed(chain.request());
            }
        });
        return retrofit.create(service);
    }

    @NonNull
    private static Retrofit retrofitAdapter(String url, Interceptor interceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(8, DEFAULT_TIMEOUT, TimeUnit.SECONDS))
                .addInterceptor(interceptor);

        if (BuildConfig.DEBUG) {
            //可以更换为自定义log拦截器
            builder.addNetworkInterceptor(new HttpLoggingInterceptor())
                    .addNetworkInterceptor(new StethoInterceptor());
        }
        OkHttpClient client = builder.build();

        return new Retrofit.Builder()
                .client(client)
                .addConverterFactory(FastJsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(url)
                .build();
    }

    /**
     * 可以通过 observer.unsubscribe()取消网络请求
     * 如果在Activity中使用BrokerSubscriber传入activity 自动取消回调
     *
     * @param observable
     * @param observer
     * @param <T>
     */
    public static <T> void createDatas(Observable<HttpResult<T>> observable, Observer<T> observer) {
        observable
                .compose(Transformers.<T>dataTransformer())
                .compose(Transformers.<T>switchSchedulers())
                .subscribe(observer);
    }


    /**
     * 切换api环境后调用，重新生成retrofit
     */
    public static void resetFactory() {
        retrofit = null;
    }
}
