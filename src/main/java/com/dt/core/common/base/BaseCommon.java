package com.dt.core.common.base;

import com.dt.core.tool.encrypt.MD5Util;
import com.dt.core.tool.util.ToolUtil;

import java.util.Date;

/**
 * @author: lank
 * @date: 2017年11月9日 下午12:28:39
 */
public class BaseCommon {

    public static boolean isSuperAdmin(String id) {
        return ToolUtil.isNotEmpty(id) && id.equals(getSuperAdmin());
    }
    public static String getSuperAdmin() {
        return ToolUtil.isEmpty(BaseConstants.superadmin) ? MD5Util.encrypt(new Date().getTime() + "")
                : BaseConstants.superadmin;
    }


}
