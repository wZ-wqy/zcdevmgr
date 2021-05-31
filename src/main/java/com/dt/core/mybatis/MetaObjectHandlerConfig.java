package com.dt.core.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.dt.core.tool.util.support.HttpKit;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author: lank
 * @date: 2020年7月22日 下午4:50:31
 * @Description:
 */
@Component
public class MetaObjectHandlerConfig implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        String user_id = HttpKit.getRequest().getSession().getAttribute("user_id") == null ? "null"
                : HttpKit.getRequest().getSession().getAttribute("user_id").toString();
        Object createTime = getFieldValByName("createTime", metaObject);
        if (createTime==null){
            setFieldValByName("createTime", new Date(), metaObject);
        }
        setFieldValByName("createBy", user_id, metaObject);
        Object updateTime = getFieldValByName("updateTime", metaObject);
        if (updateTime==null){
            setFieldValByName("updateTime", new Date(), metaObject);
        }
        setFieldValByName("updateBy", user_id, metaObject);
        setFieldValByName("dr", "0", metaObject);

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        String user_id = HttpKit.getRequest().getSession().getAttribute("user_id") == null ? "null"
                : HttpKit.getRequest().getSession().getAttribute("user_id").toString();
        setFieldValByName("updateTime", new Date(), metaObject);
        setFieldValByName("updateBy", user_id, metaObject);
    }
}
