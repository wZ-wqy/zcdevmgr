package com.dt.module.flow.base;

import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.ProcessInstance;
import com.bstek.uflo.model.task.Task;
import com.bstek.uflo.process.assign.Assignee;
import com.bstek.uflo.process.listener.TaskListener;
import com.bstek.uflo.process.node.TaskNode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BaseTaskListener implements TaskListener {

    @Override
    public boolean beforeTaskCreate(Context context, ProcessInstance processInstance, TaskNode taskNode) {
        System.out.println("###BaseTaskListener beforeTaskCreate###");
        System.out.println("processInstance.getId()" + processInstance.getId());
        System.out.println("processInstance.getProcessId()" + processInstance.getProcessId());
        System.out.println("processInstance.getBusinessId()" + processInstance.getBusinessId());
        System.out.println("processInstance.getSubject()" + processInstance.getSubject());
        System.out.println("processInstance.getCurrentNode()" + processInstance.getCurrentNode());
        System.out.println("processInstance.getCurrentTask()" + processInstance.getCurrentTask());
        System.out.println("processInstance.getTag()" + processInstance.getTag());
        System.out.println("processInstance.getState()" + processInstance.getState());

        processInstance.getMetadata("");
        System.out.println("taskNode.getProcessId()" + taskNode.getProcessId());
        System.out.println("taskNode.getLabel()" + taskNode.getLabel());
        System.out.println("taskNode.getName()" + taskNode.getName());
        System.out.println("taskNode.getType()" + taskNode.getType());
        System.out.println("taskNode.getTaskName()" + taskNode.getTaskName());
        System.out.println("taskNode.getFormTemplate()" + taskNode.getFormTemplate());
        System.out.println("taskNode.getUrl()" + taskNode.getUrl());
        System.out.println("taskNode.getAssignees().size()" + taskNode.getAssignees().size());
        List<Assignee> newdata=new ArrayList<Assignee>();
        if(taskNode.getAssignees().size()>0){
            for(int i=0;i< taskNode.getAssignees().size();i++){
                Assignee  as=taskNode.getAssignees().get(i);
                as.setId("1310090019822751746");
                as.setProviderId("ufloUserInfoAssigneeProvider");
                newdata.add(as);
                System.out.println("assignees:" + taskNode.getAssignees().get(i).getId()+" "+taskNode.getAssignees().get(i).getName()+" "+taskNode.getAssignees().get(i).getProviderId());
            }
       }
        taskNode.setAssignees(newdata);
        if(taskNode.getAssignees().size()>0){
            for(int i=0;i< taskNode.getAssignees().size();i++){
                System.out.println("assignees:" + taskNode.getAssignees().get(i).getId()+" "+taskNode.getAssignees().get(i).getName()+" "+taskNode.getAssignees().get(i).getProviderId());
            }
        }

        System.out.println("taskNode.getAssignmentType()" + taskNode.getAssignmentType());
        System.out.println("taskNode.getTaskType()" + taskNode.getTaskType());
        System.out.println("taskNode.getDescription()" + taskNode.getDescription());
        System.out.println("taskNode.getUserData()" + taskNode.getUserData().toString());
        System.out.println("---------------------------------\n\n");
        return false;
    }

    @Override
    public void onTaskCreate(Context context, Task task) {

        System.out.println("###BaseTaskListener onTaskCreate###");
        System.out.println("task.getBusinessId()" + task.getBusinessId());
        System.out.println("task.getAssignee()" + task.getAssignee());
        System.out.println("task.getOpinion()" + task.getOpinion());
        System.out.println("task.getOwner()" + task.getOwner());
        System.out.println("task.getPriority()" + task.getPriority());
        System.out.println("task.getSubject()" + task.getSubject());
        System.out.println("task.getTaskName()" + task.getTaskName());
        System.out.println("task.getUrl()" + task.getUrl());
        System.out.println("task.getDescription()" + task.getDescription());
        System.out.println("task.getNodeName()" + task.getNodeName());
        System.out.println("task.getProcessInstanceId()" + task.getProcessInstanceId());
        System.out.println("task.getType()" + task.getType());
        System.out.println("task.getId()" + task.getId());
        System.out.println("task.getProcessId()" + task.getProcessId());
        System.out.println("task.getPrevTask()" + task.getPrevTask());
        System.out.println("task.getPrevState()" + task.getPrevState());

        System.out.println("---------------------------------\n\n");
    }

    @Override
    public void onTaskComplete(Context context, Task task) {
        System.out.println("###BaseTaskListener onTaskComplete###");
        System.out.println("task.getBusinessId()" + task.getBusinessId());
        System.out.println("task.getAssignee()" + task.getAssignee());
        System.out.println("task.getOpinion()" + task.getOpinion());
        System.out.println("task.getOwner()" + task.getOwner());
        System.out.println("task.getPriority()" + task.getPriority());
        System.out.println("task.getSubject()" + task.getSubject());
        System.out.println("task.getTaskName()" + task.getTaskName());
        System.out.println("task.getUrl()" + task.getUrl());
        System.out.println("task.getDescription()" + task.getDescription());
        System.out.println("task.getNodeName()" + task.getNodeName());
        System.out.println("task.getProcessInstanceId()" + task.getProcessInstanceId());
        System.out.println("task.getType()" + task.getType());
        System.out.println("task.getId()" + task.getId());
        System.out.println("task.getProcessId()" + task.getProcessId());
        System.out.println("task.getPrevTask()" + task.getPrevTask());
        System.out.println("task.getPrevState()" + task.getPrevState());

        System.out.println("---------------------------------\n\n");
    }
}
