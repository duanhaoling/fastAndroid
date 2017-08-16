package com.ldh.androidlib.net;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.ldh.androidlib.BuildConfig;
import com.ldh.androidlib.net.config.ApiUtil;
import com.ldh.androidlib.net.config.HttpResult;
import com.ldh.androidlib.net.fastjson.FastJsonConverterFactory;
import com.ldh.androidlib.net.observer.HttpObserver;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
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
        String url = getBaseUrl(host, version);
        Interceptor interceptor = new BaseIntercepter();

        Retrofit retrofit = retrofitAdapter(url, interceptor);

        return retrofit.create(service);
    }

    /**
     * 测试接口用，不含公共参数与header
     * 使用FastJsonConverterFactory
     */
    public static <T> T createTestService(final Class<T> service, String host, String version) {
        String url = getBaseUrl(host, version);
        Retrofit retrofit = retrofitAdapter(url, new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                return chain.proceed(chain.request());
            }
        });
        return retrofit.create(service);
    }

    /**
     * 通过host 与 version获取baseUrl，为空时指定默认值
     *
     * @param host
     * @param version
     * @return
     */
    static String getBaseUrl(@NonNull String host, @NonNull String version) {
        if (TextUtils.isEmpty(host)) {
            host = ApiUtil.getHost();
            //默认的host需要指定version
            if (TextUtils.isEmpty(version)) {
                version = ApiUtil.getVersion();
            }
        }
        String url = host + version;
        if (!url.endsWith("/")) url = url + "/";//retrofit2: baseUrl must end in /
        return url;
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
     *
     * @param observable
     * @param observer
     * @param <T>
     */
    public static <T> void createDatas(Observable<HttpResult<T>> observable, HttpObserver<T> observer) {
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
