package com.dt.module.flow.base;

import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.ProcessInstance;
import com.bstek.uflo.process.handler.ProcessEventHandler;
import org.springframework.stereotype.Component;

/**
 * @author: lank
 * @date: Sep 2, 2019 11:10:47 AM
 * @Description:
 */


@Component
public class BaseProcessEventHandler implements ProcessEventHandler {


    @Override
    public void start(ProcessInstance processInstance, Context context) {
        System.out.println("###BaseProcessEventHandler Start###");
        System.out.println("processInstance.getId()" + processInstance.getId());
        System.out.println("processInstance.getProcessId()" + processInstance.getProcessId());
        System.out.println("processInstance.getBusinessId()" + processInstance.getBusinessId());
        System.out.println("processInstance.getSubject()" + processInstance.getSubject());
        System.out.println("processInstance.getCurrentNode()" + processInstance.getCurrentNode());
        System.out.println("processInstance.getCurrentTask()" + processInstance.getCurrentTask());
        System.out.println("processInstance.getTag()" + processInstance.getTag());
        System.out.println("processInstance.getState()" + processInstance.getState());
        System.out.println("---------------------------------\n\n");

    }


    @Override
    public void end(ProcessInstance processInstance, Context context) {
        System.out.println("###BaseProcessEventHandler End###");
        System.out.println("processInstance.getId()" + processInstance.getId());
        System.out.println("processInstance.getProcessId()" + processInstance.getProcessId());
        System.out.println("processInstance.getBusinessId()" + processInstance.getBusinessId());
        System.out.println("processInstance.getSubject()" + processInstance.getSubject());
        System.out.println("processInstance.getCurrentNode()" + processInstance.getCurrentNode());
        System.out.println("processInstance.getCurrentTask()" + processInstance.getCurrentTask());
        System.out.println("processInstance.getTag()" + processInstance.getTag());
        System.out.println("processInstance.getState()" + processInstance.getState());
        System.out.println("---------------------------------\n\n");
    }

}
