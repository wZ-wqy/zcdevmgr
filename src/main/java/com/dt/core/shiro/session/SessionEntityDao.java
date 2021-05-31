package com.dt.core.shiro.session;

import com.dt.core.cache.ThreadTaskHelper;
import com.dt.core.dao.Rcd;
import com.dt.core.tool.util.ToolUtil;
import com.dt.module.base.service.ISysSessionService;
import com.dt.module.db.DB;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: lank
 * @date: 2017年11月7日 下午2:18:05
 * @Description:
 */
public class SessionEntityDao extends EnterpriseCacheSessionDAO {

    private static Logger log = LoggerFactory.getLogger(SessionEntityDao.class);
    Session session = null;
    @Autowired
    ISysSessionService SysSessionService;

    public static String serialize(Session session) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(session);
            return Base64.encodeToString(bos.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("serialize session error", e);
        }
    }

    public static Session deserialize(String sessionStr) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(Base64.decode(sessionStr));
            ObjectInputStream ois = new ObjectInputStream(bis);
            return (Session) ois.readObject();
        } catch (Exception e) {
            throw new RuntimeException("deserialize session error", e);
        }
    }

    @Override
    public Serializable create(Session session) {
        // 先保存到缓存中
        log.info("session:" + session.getId());
        Serializable cookie = super.create(session);
        log.info("cookie:" + cookie);
        // 新建一个entity保存到数据库
        SimpleSessionEntity entity = new SimpleSessionEntity();
        entity.setSession(serialize(session));
        entity.setCookie(cookie.toString());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(session.getStartTimestamp());
        entity.setStart_time(dateString);
        entity.setIp(session.getHost());
        entity.setToken(cookie.toString());
        entity.save();
        log.info("create session:" + cookie);
        return cookie;
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        super.update(session);
        // 如果会话过期,停止 没必要再更新了
        if (session instanceof ValidatingSession && !((ValidatingSession) session).isValid()) {
            log.info("会话失效,可能已过期,不需要更新");
            return;
        }
        SimpleSessionEntity entity = new SimpleSessionEntity();
        entity.setId(session.getId().toString());
        entity.setCookie(session.getId().toString());
        entity.setIp(session.getHost());
        entity.setSession(serialize(session));
        entity.update(entity);

    }

    @Override
    public Session readSession(Serializable sessionId) throws UnknownSessionException {

        try {
            session = super.readSession(sessionId);
//            log.info("session:" + session);
        } catch (Exception e) {
        }
        // 如果session已经被删除，则从数据库中查询session

//        log.info("session:" + session);
        if (session == null) {
            log.info("session:" + sessionId + "尝试恢复session");
            SimpleSessionEntity entity = getEntity(sessionId);
            if (entity != null) {
                try {
                    String msg = "session:" + sessionId + "找到";
                    session = deserialize(entity.getSession());
                    if (isExpire(session)) {
                        msg = msg + ",已过期";
                        // 后期可以判断只对app进行过期处理
                        session.touch();
                    } else {
                        msg = msg + ",未过期";
                    }
                    log.info(msg);
                    return session;
                } catch (Exception e) {
                    log.info("无法初始化,sessionId:" + sessionId);
                }
            } else {
                log.info("session:" + sessionId + "未找到保存的session");
            }
        }
//        log.info("返回session:" + session);
        return session;
    }

    private boolean isExpire(Session session) {
        long timeout = session.getTimeout();
        long lastTime = session.getLastAccessTime().getTime();
        long current = new Date().getTime();
        return (lastTime + timeout) <= current;
    }

    @Override
    public void delete(Session session) {

        super.delete(session);
        log.info("delete session,sessionId:" + session.getId());
        ThreadTaskHelper.run(new Runnable() {
            @Override
            public void run() {
                DB.instance().execute("update sys_session set dr=1 where cookie=? ", session.getId().toString());
            }
        });

    }

    private SimpleSessionEntity getEntity(Serializable sessionId) {
        System.out.println("cookie"+sessionId);
        String sql = "select * from sys_session where dr=0 and cookie=?";
        if (ToolUtil.isEmpty(sessionId)) {
            return null;
        }
        Rcd rs = DB.instance().uniqueRecord(sql, sessionId);
        if (ToolUtil.isNotEmpty(rs)) {
            SimpleSessionEntity res = new SimpleSessionEntity();
            res.setCookie(rs.getString("cookie"));
            res.setSession(rs.getString("dtsession"));
            res.setId(rs.getString("id"));
            res.setUser_id(rs.getString("user_id"));
            res.setClient(rs.getString("client"));
            System.out.println(res.getSession());
            System.out.println(res.getCookie());
            System.out.println(res.getToken());
            System.out.println(res.getUser_id());
            return res;
        } else {
            return null;
        }
    }
}
