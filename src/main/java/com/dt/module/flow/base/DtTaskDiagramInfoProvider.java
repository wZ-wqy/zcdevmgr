package com.dt.module.flow.base;

import com.bstek.uflo.diagram.TaskDiagramInfoProvider;
import com.bstek.uflo.diagram.TaskInfo;
import com.dt.module.base.entity.SysUserInfo;
import com.dt.module.base.service.ISysUserInfoService;
import com.dt.module.zc.service.IResApprovalnodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.List;

@Component
public class DtTaskDiagramInfoProvider implements TaskDiagramInfoProvider {
    private boolean disableDefaultTaskDiagramInfoProvider;

    @Autowired
    ISysUserInfoService SysUserInfoServiceImpl;

    public DtTaskDiagramInfoProvider() {
    }

    public boolean disable() {
        return false;
    }

    public String getInfo(String nodeName, List<TaskInfo> tasks) {

        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuffer sb = null;

        if (tasks != null && tasks.size() > 0) {
            sb = new StringBuffer();
            if (tasks.size() > 1) {
                for (int i = 0; i < tasks.size(); ++i) {
                    TaskInfo task = (TaskInfo) tasks.get(i);
                    SysUserInfo owner = SysUserInfoServiceImpl.getById(task.getOwner());
                    SysUserInfo assignee = SysUserInfoServiceImpl.getById(task.getAssignee());
                    sb.append("任务" + (i + 1) + ":\r");
                    sb.append("所有人：" + (owner == null ? task.getOwner() : owner.getName()) + "\r");
                    sb.append("处理人：" + (assignee == null ? task.getAssignee() : assignee.getName()) + "\r");
                    sb.append("  创建时间：" + sd.format(task.getCreateDate()) + "\r");
                    if (task.getEndDate() != null) {
                        sb.append("  完成时间：" + sd.format(task.getEndDate()) + "\r");
                    }
                }
            } else {
//                TaskInfo task = (TaskInfo)tasks.get(0);
//                sb.append("所有人：" + task.getOwner() + "\r");
//                sb.append("处理人：" + task.getAssignee() + "\r");
//                sb.append("创建时间：" + sd.format(task.getCreateDate()) + "\r");
//                if (task.getEndDate() != null) {
//                    sb.append("完成时间：" + sd.format(task.getEndDate()) + "\r");
//                }
                TaskInfo task = (TaskInfo) tasks.get(0);
                SysUserInfo owner = SysUserInfoServiceImpl.getById(task.getOwner());
                SysUserInfo assignee = SysUserInfoServiceImpl.getById(task.getAssignee());
                sb.append("所有人：" + (owner == null ? task.getOwner() : owner.getName()) + "\r");
                sb.append("处理人：" + (assignee == null ? task.getAssignee() : assignee.getName()) + "\r");
                sb.append("创建时间：" + sd.format(task.getCreateDate()) + "\r");
                if (task.getEndDate() != null) {
                    sb.append("完成时间：" + sd.format(task.getEndDate()) + "\r");
                }
            }
        }

        return sb != null ? sb.toString() : null;
    }

    public void setDisableDefaultTaskDiagramInfoProvider(boolean disableDefaultTaskDiagramInfoProvider) {
        this.disableDefaultTaskDiagramInfoProvider = false;
    }
}

