package com.dt.core.common.base;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import java.util.Date;


/**
 * @author lank
 * @version 创建时间：2017年8月1日 上午8:33:12
 */
@SuppressWarnings("rawtypes")
public class BaseTModel<T> extends Model implements Serializable {

    private static final long serialVersionUID = 1L;

}
