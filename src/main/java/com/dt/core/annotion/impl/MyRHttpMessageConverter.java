package com.dt.core.annotion.impl;

import com.dt.core.common.base.R;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


public class MyRHttpMessageConverter {

    private static final MediaType UTF8 = new MediaType("application", "json", StandardCharsets.UTF_8);

    public MappingJackson2HttpMessageConverter init() {
        return new MappingJackson2HttpMessageConverter() {
            /**
             * 重写Jackson消息转换器的writeInternal方法
             * SpringMVC选定了具体的消息转换类型后,会调用具体类型的write方法,将Java对象转换后写入返回内容
             */
            @Override
            protected void writeInternal(Object object, Type type, HttpOutputMessage outputMessage)
                    throws IOException, HttpMessageNotWritableException {
                Charset charset = this.getContentTypeCharset(outputMessage.getHeaders().getContentType());
                if (object instanceof R) {
                    StreamUtils.copy(((R) object).asJsonStr(), charset, outputMessage.getBody());
                } else {
                    super.writeInternal(object, type, outputMessage);
                }
            }

            @Override
            @SuppressWarnings({"unchecked", "rawtypes"})
            public boolean canWrite(Type type, Class<?> clazz, MediaType mediaType) {
                if (type instanceof R) {
                    return ((Class) type).isAssignableFrom(R.class);
                } else {
                    return true;
                }

            }

            private Charset getContentTypeCharset(MediaType contentType) {
                return contentType != null && contentType.getCharset() != null ? contentType.getCharset()
                        : UTF8.getCharset();
            }
        };
    }
}
