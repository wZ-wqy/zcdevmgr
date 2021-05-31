package com.dt.core.tool.util.exception;

import com.dt.core.tool.util.support.StrKit;

/**
 * 工具类初始化异常
 */
public class ToolBoxException extends RuntimeException {
    private static final long serialVersionUID = 8247610319171014183L;

    public ToolBoxException() {
        super();
    }

    public ToolBoxException(Throwable e) {
        super(e.getMessage(), e);
    }

    public ToolBoxException(String message) {
        super(message);
    }

    public ToolBoxException(String messageTemplate, Object... params) {
        super(StrKit.format(messageTemplate, params));
    }

    public ToolBoxException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public ToolBoxException(Throwable throwable, String messageTemplate, Object... params) {
        super(StrKit.format(messageTemplate, params), throwable);
    }

}
