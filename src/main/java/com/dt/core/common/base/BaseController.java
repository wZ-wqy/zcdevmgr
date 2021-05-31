package com.dt.core.common.base;

import com.dt.core.tool.util.support.HttpKit;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.session.InvalidSessionException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author lank
 * @version 创建时间：2017年8月1日 上午8:33:12
 */
public class BaseController extends BaseSC {

    /**
     * 返回jsp视图
     * @param path
     * @return
     */
    public static String jsp(String path) {
        return path.concat("");
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        datetimeFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(datetimeFormat, true));
    }
    /**
     * 统一异常处理
     *
     * @param request
     * @param response
     * @param exception
     */
    @ExceptionHandler
    public String exceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception exception) {
        String msg = ExceptionUtils.getRootCauseMessage(exception) == null ? ""
                : ExceptionUtils.getRootCauseMessage(exception);
        exception.printStackTrace();
        request.setAttribute("ex", exception);
        if (null != request.getHeader("X-Requested-With")
                && "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"))) {
            request.setAttribute("requestHeader", "ajax");
        }

        if (isReturnJSON(request)) {
            try {
                response.setCharacterEncoding("UTF-8");
                response.setHeader("content-type", "text/html;charset=UTF-8");
                response.getWriter().print(R.FAILURE(msg));
                response.getWriter().flush();
                response.getWriter().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        // shiro没有权限异常
        if (exception instanceof UnauthorizedException) {
            return "/403.jsp";
        }
        // shiro会话已过期异常
        if (exception instanceof InvalidSessionException) {
            return "/error.jsp";
        }
        return "/error.jsp";
    }

    private Boolean isReturnJSON(HttpServletRequest httpRequest) {
        Boolean res = false;
        if (HttpKit.isAjax(httpRequest) || (httpRequest.getRequestURL() + "").endsWith("do")) {
            res = true;
        }
        return res;
    }

    protected HttpServletRequest getHttpServletRequest() {
        return HttpKit.getRequest();
    }

    protected HttpServletResponse getHttpServletResponse() {
        return HttpKit.getResponse();
    }

    protected HttpSession getSession() {
        return HttpKit.getRequest().getSession();
    }

    protected HttpSession getSession(Boolean flag) {
        return HttpKit.getRequest().getSession(flag);
    }

    protected String getPara(String name) {
        return HttpKit.getRequest().getParameter(name);
    }

    protected void setAttr(String name, Object value) {
        HttpKit.getRequest().setAttribute(name, value);
    }

    protected Integer getSystemInvokCount() {
        return (Integer) this.getHttpServletRequest().getServletContext().getAttribute("systemCount");
    }

    /**
     * 删除cookie
     */
    protected void deleteCookieByName(String cookieName) {
        Cookie[] cookies = this.getHttpServletRequest().getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(cookieName)) {
                Cookie temp = new Cookie(cookie.getName(), "");
                temp.setMaxAge(0);
                this.getHttpServletResponse().addCookie(temp);
            }
        }
    }

    public String warpObject(Object o) {
        if (o instanceof R) {
            return ((R) o).asJsonStr();
        } else {
            return o.toString();
        }
    }

}
