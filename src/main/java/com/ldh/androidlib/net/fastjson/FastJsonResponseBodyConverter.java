package com.ldh.androidlib.net.fastjson;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import okio.BufferedSource;
import okio.Okio;
import retrofit2.Converter;

/**
 * Created by ldh on 2016/10/20 0020.
 */

public class FastJsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private final Type type;

    public FastJsonResponseBodyConverter(Type type) {
        this.type = type;
    }


    @Override
    public T convert(ResponseBody value) throws IOException {

        BufferedSource buffer = Okio.buffer(value.source());
        String tempStr = buffer.readUtf8();
        buffer.close();
        return JSON.parseObject(tempStr, type);
    }
}
