package com.dt.core.shiro;

import com.dt.core.common.base.R;
import com.dt.core.shiro.service.ShiroAuthorizationHelper;
import com.dt.core.tool.util.support.HttpKit;
import com.dt.core.tool.util.support.StrKit;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: lank
 * @date: 2017年8月31日 下午5:46:32
 * @Description:
 */
public class SystemLogoutFilter extends LogoutFilter {
    private static final Logger log = LoggerFactory.getLogger(SystemLogoutFilter.class);


    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);
        String redirectUrl = getRedirectUrl(request, response, subject);
        log.info("sessionId:" + subject.getSession().getId() + " to logout");
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        ShiroUser shiroUser = ShiroKit.getUser();
        try {
            if (shiroUser != null) {
                // ShiroAuthorizationHelper.showCache();
                ShiroAuthorizationHelper.clearAuthorizationInfo(ShiroKit.getSubject().getPrincipals());
                ShiroAuthorizationHelper.clearAuthenticationInfo(shiroUser.id);
                ShiroAuthorizationHelper.clearSession(shiroUser.id);
            }
            subject.logout();

            // ShiroAuthorizationHelper.showCache();

        } catch (SessionException ise) {
            log.debug("Encountered session exception during logout.  This can generally safely be ignored.", ise);
        }

        if (isReturnJSON(httpRequest)) {
            httpResponse.setStatus(200);
            httpResponse.setHeader("content-type", "text/html;charset=UTF-8");
            httpResponse.getWriter().print(R.SUCCESS_OPER().asJsonStr());
            httpResponse.getWriter().flush();
            httpResponse.getWriter().close();
        } else {
            issueRedirect(request, response, redirectUrl);
        }
        return false;
    }

    private Boolean isReturnJSON(HttpServletRequest httpRequest) {
        Boolean res = false;
        if (HttpKit.isAjax(httpRequest) || StrKit.endWith(httpRequest.getRequestURL() + "", ".do", true)) {
            res = true;
        }
        return res;
    }
}
