package com.dt.module.flow.base;

import com.bstek.uflo.env.Context;
import com.bstek.uflo.expr.ExpressionContext;
import com.bstek.uflo.model.ProcessDefinition;
import com.bstek.uflo.model.ProcessInstance;
import com.bstek.uflo.model.variable.Variable;
import com.bstek.uflo.process.handler.NodeEventHandler;
import com.bstek.uflo.process.node.Node;
import com.bstek.uflo.service.ProcessService;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

/**
 * @author: lank
 * @date: Nov 30, 2019 9:04:15 AM
 * @Description:
 */


@Component
public class BaseNodeEventHandler implements NodeEventHandler {


    @Override
    public void enter(Node node, ProcessInstance processInstance, Context context) {
        System.out.println("###BaseNodeEventHandler Enter Node###");
        System.out.println("processInstance.getId()" + processInstance.getId());
        System.out.println("processInstance.getProcessId()" + processInstance.getProcessId());
        System.out.println("processInstance.getBusinessId()" + processInstance.getBusinessId());
        System.out.println("processInstance.getSubject()" + processInstance.getSubject());
        System.out.println("processInstance.getCurrentNode()" + processInstance.getCurrentNode());
        System.out.println("processInstance.getCurrentTask()" + processInstance.getCurrentTask());
        System.out.println("processInstance.getTag()" + processInstance.getTag());
        System.out.println("processInstance.getState()" + processInstance.getState());
        System.out.println("processInstance.getParentId()" + processInstance.getParentId());

        System.out.println("node.getProcessId()" + node.getProcessId());
        System.out.println("node.getName()" + node.getName());
        System.out.println("node.getType()" + node.getType());
        System.out.println("node.getLabel()" + node.getLabel());
        System.out.println("node.getDescription()" + node.getDescription());
        processInstance.getCurrentTask();
        System.out.println("---------------------------------\n\n");
       //        ProcessService processService = context.getProcessService();
//        ExpressionContext expressionContext = context.getExpressionContext();
//        Iterator var14 = noneCompleteProcessInstances.iterator();
//        ProcessDefinition pd = processService.getProcessById(processInstance.getProcessId());
//        while(var14.hasNext()) {
//            ProcessInstance pi = (ProcessInstance)var14.next();
//            Node node2 = pd.getNode(pi.getCurrentNode());
//            node2.cancel(context, processInstance);
//            session.createQuery("delete " + Variable.class.getName() + " where processInstanceId=:piId").setLong("piId", pi.getId()).executeUpdate();
//            processService.deleteProcessInstance(pi);
//            node2.completeActivityHistory(context, pi, (String)null);
//            expressionContext.removeContext(pi);
//        }

    }


    @Override
    public void leave(Node node, ProcessInstance processInstance, Context context) {
        System.out.println("###BaseNodeEventHandler Leave Node###");
        System.out.println("processInstance.getId()" + processInstance.getId());
        System.out.println("processInstance.getProcessId()" + processInstance.getProcessId());
        System.out.println("processInstance.getBusinessId()" + processInstance.getBusinessId());
        System.out.println("processInstance.getSubject()" + processInstance.getSubject());
        System.out.println("processInstance.getCurrentNode()" + processInstance.getCurrentNode());
        System.out.println("processInstance.getCurrentTask()" + processInstance.getCurrentTask());
        System.out.println("processInstance.getTag()" + processInstance.getTag());
        System.out.println("processInstance.getState()" + processInstance.getState());

        System.out.println("node.getProcessId()" + node.getProcessId());
        System.out.println("node.getName()" + node.getName());
        System.out.println("node.getType()" + node.getType());
        System.out.println("node.getLabel()" + node.getLabel());
        System.out.println("node.getDescription()" + node.getDescription());
        System.out.println("---------------------------------\n\n");
    }

}
