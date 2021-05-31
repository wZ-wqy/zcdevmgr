package com.dt.module.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dt.core.common.base.R;
import com.dt.core.dao.Rcd;
import com.dt.core.dao.RcdSet;
import com.dt.core.dao.sql.Update;
import com.dt.core.tool.encrypt.MD5Util;
import com.dt.core.tool.util.ConvertUtil;
import com.dt.core.tool.util.ToolUtil;
import com.dt.module.base.busenum.UserTypeEnum;
import com.dt.module.base.entity.SysMenus;
import com.dt.module.base.entity.SysUserInfo;
import com.dt.module.base.entity.SysUserReceivingaddr;
import com.dt.module.base.entity.UserShiro;
import com.dt.module.base.mapper.SysUserInfoMapper;
import com.dt.module.base.service.ISysUserInfoService;
import com.dt.module.base.service.ISysUserReceivingaddrService;
import com.dt.module.db.DB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author lank
 * @since 2018-07-24
 */
@Service
public class SysUserInfoServiceImpl extends ServiceImpl<SysUserInfoMapper, SysUserInfo> implements ISysUserInfoService {

    @Autowired
    DB db;

    @Autowired
    ISysUserReceivingaddrService SysUserReceivingaddrServiceImpl;

    private static Logger _log = LoggerFactory.getLogger(SysUserInfoServiceImpl.class);



    /**
     * @Description: 保存用户的默认菜单
     * @param user_id
     * @param id
     */
    public R setDefaultUserMenus(String user_id, String id) {
        if (ToolUtil.isOneEmpty(user_id, id)) {
            return R.FAILURE_REQ_PARAM_ERROR();
        }
        UpdateWrapper<SysUserInfo> ew = new UpdateWrapper<SysUserInfo>();
        ew.and(i -> i.eq("user_id", user_id));
        SysUserInfo user = new SysUserInfo();

        user.setSystemId(id);
        baseMapper.update(user, ew);

        return R.SUCCESS_OPER();
    }

    /**
     * @Description: 强制修改用户密码
     * @param user_id
     * @param pwd1
     * @param pwd2
     */
    public R changeUserPwdForce(String user_id, String pwd1, String pwd2) {
        if (ToolUtil.isOneEmpty(pwd1, pwd2)) {
            return R.FAILURE_REQ_PARAM_ERROR();
        }
        if (!pwd1.equals(pwd2)) {
            return R.FAILURE("密码输入不一致");
        }
        UpdateWrapper<SysUserInfo> ew = new UpdateWrapper<SysUserInfo>();
        ew.and(i -> i.eq("user_id", user_id));
        SysUserInfo user = new SysUserInfo();
        user.setPwd(pwd1);
        baseMapper.update(user, ew);
        return R.SUCCESS_OPER();
    }

    /**
     * @Description: 显示用户的菜单
     * @param user_id
     */
    public List<SysMenus> listMyMenus(String user_id) {
        return this.baseMapper.listMyMenus(user_id);
    }

    @Override
    public SysUserInfo selectOneByEmpl(String empl) {
        QueryWrapper<SysUserInfo> ew = new QueryWrapper<SysUserInfo>();
        ew.and(i -> i.eq("empl_id", empl));
        return baseMapper.selectOne(ew);

    }

    /**
     * @Description: 修改用户密码
     * @param user_id
     * @param pwd
     */
    public R modifyPassword(String user_id, String pwd) {
        if (ToolUtil.isOneEmpty(user_id, pwd)) {
            return R.FAILURE_REQ_PARAM_ERROR();
        }
        UpdateWrapper<SysUserInfo> ew = new UpdateWrapper<SysUserInfo>();
        ew.and(i -> i.eq("user_id", user_id));
        SysUserInfo user = new SysUserInfo();
        user.setPwd(pwd);
        baseMapper.update(user, ew);

        return R.SUCCESS_OPER();

    }

    /**
     * @Description: 根据open_id查询用户信息
     * @param open_id
     */
    @Override
    public R selectUserInfoByOpenId(String open_id) {
        if (ToolUtil.isEmpty(open_id)) {
            return R.FAILURE_REQ_PARAM_ERROR();
        }
        QueryWrapper<SysUserInfo> ew = new QueryWrapper<SysUserInfo>();
        ew.and(i -> i.eq("open_id", open_id));
        Integer c = baseMapper.selectCount(ew);
        if (c == 1) {
            return R.SUCCESS_OPER(baseMapper.selectOne(ew));
        } else if (c == 0) {
            return R.FAILURE("不存在");
        } else {
            return R.FAILURE("存在多个");
        }

    }

    /**
     * @Description: 查询用户角色
     * @param open_id
     */
    @Override
    public List<HashMap<String, Object>> listUserRoles(String user_id) {
        return this.baseMapper.listUserRoles(user_id);
    }

    /**
     * @Description: 增加用户
     * @param user
     */
    @Override
    public R addUser(SysUserInfo user) {
        String user_type = user.getUserType();

        // 判断用户类型
        if (ToolUtil.isEmpty(user_type)) {
            return R.FAILURE("请选择用户类型");
        }

        // 如果密码为空,则随机
        if (ToolUtil.isEmpty(user.getPwd())) {
            user.setPwd("dt123456");
        }

//		// 如果组织ID为空，则随机
//		if (ToolUtil.isEmpty(user.getEmplId())) {
//			user.setEmplId(ToolUtil.getUUID());
//		}

        // 如果用户名称为空，则随机，用户名称username字段不能重复
        if (ToolUtil.isEmpty(user.getUserName())) {
            user.setUserName(ToolUtil.getUUID());
        } else {
            QueryWrapper<SysUserInfo> queryWrapper = new QueryWrapper<SysUserInfo>();
            queryWrapper.eq("user_name", user.getUserName());
            int c = this.baseMapper.selectCount(queryWrapper);
            if (c > 0) {
                return R.FAILURE("登录名重复,请重新输入");
            }
        }
        String emplId = ToolUtil.getUUID();
        if (UserTypeEnum.SYSTEM.getValue().equals(user_type)) {
            // 无动作
        } else if (UserTypeEnum.EMPL.getValue().equals(user_type)) {
            R r = getEmplNextId();
            if (r.isFailed()) {
                return r;
            }
            emplId = r.getData().toString();
        } else if (UserTypeEnum.CRM.getValue().equals(user_type)) {
        } else if (UserTypeEnum.WX.getValue().equals(user_type)) {
        }

        user.setEmplId(emplId);
        baseMapper.insert(user);
        QueryWrapper<SysUserInfo> queryWrapper = new QueryWrapper<SysUserInfo>();
        queryWrapper.eq("empl_id", emplId);
        return R.SUCCESS_OPER(baseMapper.selectOne(queryWrapper));

    }

    /**
     * @Description: 获取下一个工号
     */
    public R getEmplNextId() {
        Rcd seqrs = db.uniqueRecord(
                "select case when value is null then '50' else value end seq from sys_params where id='sys_empl_no' and dr ='0'");
        if (ToolUtil.isEmpty(seqrs)) {
            return R.FAILURE("未获取员工编号,创建员工失败。");
        }
        String empl_id = (ConvertUtil.toInt(seqrs.getString("seq")) + 1) + "";
        Update me = new Update("sys_params");
        me.set("value", empl_id);
        me.where().and("id=?", "sys_empl_no");
        db.execute(me);
        return R.SUCCESS_OPER(ConvertUtil.formatIntToString(empl_id, 6, 100));
    }


    /**
     * @Description: 获取用户，返回Shiro
     * @param user_id
     */
    public UserShiro queryUserShiroByUserId(String user_id) {
        UserShiro user = new UserShiro();
        // 账号状态信息
        Rcd u_rs = db.uniqueRecord("select * from sys_user_info a where a.dr='0' and a.user_id=?", user_id);
        user.setUserId(u_rs.getString("user_id"));
        user.setPassword(u_rs.getString("pwd"));
        user.setAccount(u_rs.getString("user_name"));
        user.setName(u_rs.getString("name"));
        user.setUsername(u_rs.getString("user_name"));
        user.setSalt(MD5Util.encrypt(u_rs.getString("user_id")));
        if (ToolUtil.isNotEmpty(u_rs.getString("locked")) && u_rs.getString("locked").equals("N")) {
            user.setIsLocked(false);
        }

        // 获取角色信息
        RcdSet r_rs = db.query("select a.role_id,b.role_name from sys_user_role a,sys_role_info b where b.dr='0' and a.role_id=b.role_id and user_id=?", user_id);
        HashMap<String, String> rmap = new HashMap<String, String>();
        for (int i = 0; i < r_rs.size(); i++) {
            rmap.put(r_rs.getRcd(i).getString("role_id"), r_rs.getRcd(i).getString("role_name"));
        }
        user.setRolsSet(rmap);
        return user;
    }

    /**
     * @Description: 根据用户名获取用户ID
     * @param user_name
     */
    public R queryUserIdByUserName(String user_name) {
        QueryWrapper<SysUserInfo> queryWrapper = new QueryWrapper<SysUserInfo>();
        queryWrapper.eq("user_name", user_name);
        List<SysUserInfo> lists = this.baseMapper.selectList(queryWrapper);
        if (lists.size() == 0) {
            return R.FAILURE("不存在该用户");
        } else {
            if ("1".equals(lists.get(0).getIslogoff())) {
                return R.FAILURE("用户已注销");
            } else {
                return R.SUCCESS_OPER(lists.get(0).getUserId());
            }
        }
    }

    /**
     * @Description: 查询用户收获地址
     * @param user_id
     */
    public R queryUserReceivingaddr(String user_id) {
        if (user_id == null) {
            return R.FAILURE_REQ_PARAM_ERROR();
        }
        QueryWrapper<SysUserReceivingaddr> queryWrapper = new QueryWrapper<SysUserReceivingaddr>();
        queryWrapper.eq("user_id", user_id);
        return R.SUCCESS_OPER(SysUserReceivingaddrServiceImpl.list(queryWrapper));
    }


    /**
     * @Description: 删除用户收获地址
     * @param user_id
     */
    public R deleteUserReceivingaddr(String user_id, String id) {
        if (ToolUtil.isOneEmpty(user_id, id)) {
            return R.FAILURE_REQ_PARAM_ERROR();
        }
        QueryWrapper<SysUserReceivingaddr> queryWrapper = new QueryWrapper<SysUserReceivingaddr>();
        queryWrapper.eq("user_id", user_id).eq("id", id);
        return R.SUCCESS_OPER(SysUserReceivingaddrServiceImpl.remove(queryWrapper));

    }

}
