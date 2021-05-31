package com.dt.core.common.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author lank
 * @version 创建时间：2017年8月1日 上午8:33:12
 */
public class CustomDateEditor extends PropertyEditorSupport {

    Logger logger = LoggerFactory.getLogger(CustomDateEditor.class);
    private final DateFormat dateFormat;

    private final boolean allowEmpty;

    private final int exactDateLength;

    public CustomDateEditor(DateFormat dateFormat, boolean allowEmpty) {
        this.dateFormat = dateFormat;
        this.allowEmpty = allowEmpty;
        this.exactDateLength = -1;
    }
    public CustomDateEditor(DateFormat dateFormat, boolean allowEmpty, int exactDateLength) {
        this.dateFormat = dateFormat;
        this.allowEmpty = allowEmpty;
        this.exactDateLength = exactDateLength;
    }

    /**
     * Parse the Date from the given text, using the specified DateFormat.
     */
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (this.allowEmpty && !StringUtils.hasText(text)) {
            // Treat empty String as null value.
            setValue(null);
        }
        else if (text != null && this.exactDateLength >= 0 && text.length() != this.exactDateLength) {
            throw new IllegalArgumentException(
                    "Could not parse date: it is not exactly" + this.exactDateLength + "characters long");
        }
        else {
            try {
                setValue(this.dateFormat.parse(text));
            } catch (ParseException e) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                try {
                    setValue(sdf.parse(text));
                } catch (Exception ex) {
                    //如果yyyy-MM-dd HH:mm:ss 不能解析 传入的字符串， 换年月日的解析方式
                    SimpleDateFormat sdfx = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        setValue(sdfx.parse(text));
                    } catch (ParseException exx) {
                        try{
                            setValue(new Date(Long.parseLong(text)));
                        } catch (IllegalArgumentException exx2) {
                            logger.error("日期不能解析 text:"+text, exx2);
                            setValue(new Date());
                            throw new IllegalArgumentException("Could not parse date: " + exx.getMessage(), exx2);
                        }
                    }
                }
            }
        }
    }

    @Override
    public String getAsText() {
        Date value = (Date) getValue();
        return (value != null ? this.dateFormat.format(value) : "");
    }
}
