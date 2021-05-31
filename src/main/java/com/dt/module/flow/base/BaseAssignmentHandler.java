package com.dt.module.flow.base;

import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.ProcessInstance;
import com.bstek.uflo.process.handler.AssignmentHandler;
import com.bstek.uflo.process.node.TaskNode;

import java.util.Collection;

public class BaseAssignmentHandler implements AssignmentHandler {


    @Override
    public Collection<String> handle(TaskNode taskNode, ProcessInstance processInstance, Context context) {
        System.out.println("###BaseAssignmentHandler Info Print###");
        System.out.println("processInstance.getId()" + processInstance.getId());
        System.out.println("processInstance.getProcessId()" + processInstance.getProcessId());
        System.out.println("processInstance.getBusinessId()" + processInstance.getBusinessId());
        System.out.println("processInstance.getSubject()" + processInstance.getSubject());
        System.out.println("processInstance.getCurrentNode()" + processInstance.getCurrentNode());
        System.out.println("processInstance.getCurrentTask()" + processInstance.getCurrentTask());
        System.out.println("processInstance.getTag()" + processInstance.getTag());
        System.out.println("processInstance.getState()" + processInstance.getState());
        System.out.println("taskNode.getProcessId()" + taskNode.getProcessId());
        System.out.println("taskNode.getNodeType()" + taskNode.getType());
        System.out.println("taskNode.getTaskType()" + taskNode.getTaskType());
        System.out.println("taskNode.getAssignmentType()" + taskNode.getAssignmentType());
        System.out.println("taskNode.getName()" + taskNode.getName());
        System.out.println("taskNode.getLabel()" + taskNode.getLabel());
        System.out.println("taskNode.getUrl()" + taskNode.getUrl());
        System.out.println("taskNode.getTaskName()" + taskNode.getTaskName());
        System.out.println("taskNode.getDescription()" + taskNode.getDescription());
        System.out.println("---------------------------------\n\n");
        return null;
    }
}
