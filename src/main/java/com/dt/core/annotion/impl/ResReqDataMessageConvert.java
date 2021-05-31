package com.dt.core.annotion.impl;

import com.dt.core.common.base.R;
import com.dt.core.common.base.ReqData;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ResReqDataMessageConvert extends AbstractGenericHttpMessageConverter<Object> {
    // 当返回是json是,自动转换成
    private static final MediaType UTF8 = new MediaType("application", "json", StandardCharsets.UTF_8);
    private boolean writeAcceptCharset = true;

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
        return ((Class) type).isAssignableFrom(ReqData.class);
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public boolean canWrite(Type type, Class<?> clazz, MediaType mediaType) {

        return ((Class) type).isAssignableFrom(R.class);
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return Collections.singletonList(MediaType.ALL);
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(R.class);
    }

    @Override
    public Object readInternal(Class<?> clazz, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {
        return readMap(inputMessage);
    }

    private Object readMap(HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        Charset cs = StandardCharsets.UTF_8;
        StringBuilder stringBuilder = new StringBuilder();
        InputStream inputStream = inputMessage.getBody();
        byte[] b = new byte[1024];
        int length;
        while ((length = inputStream.read(b)) != -1) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(length);
            byteBuffer.put(b, 0, length);
            byteBuffer.flip();
            stringBuilder.append(cs.decode(byteBuffer).array());
        }
        String[] list = stringBuilder.toString().split(";");
        Map<String, String> map = new HashMap<String, String>(list.length);
        for (String entry : list) {
            String[] keyValue = entry.split(",");
            map.put(keyValue[0], keyValue[1]);
        }
        ReqData requestData = new ReqData();
        requestData.setData(map);
        return requestData;
    }

    @Override
    public void writeInternal(Object o, Type type, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {
        if (o instanceof R) {
            R res = (R) o;
            Charset charset = UTF8.getCharset();
            String str = "";
            if (writeAcceptCharset) {
                outputMessage.getHeaders().setAcceptCharset(getAcceptedCharsets());
                if (res.type.equals(R.TYPE_JSON)) {
                    str = res.asJsonStr();
                } else {
                    str = "now not support.";
                }
                StreamUtils.copy(str, charset, outputMessage.getBody());
            } else {
                StreamUtils.copy("now not supprt", charset, outputMessage.getBody());
            }
        } else {
            super.writeInternal(o, outputMessage);
        }

    }

    protected List<Charset> getAcceptedCharsets() {
        return Arrays.asList(UTF8.getCharset());
    }

    public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {
        return readMap(inputMessage);
    }
}
