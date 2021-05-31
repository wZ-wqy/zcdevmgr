package com.dt.module.flow.base;

import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.ProcessInstance;
import com.bstek.uflo.process.handler.ActionHandler;
import org.springframework.stereotype.Component;

@Component
public class BaseActionHandler implements ActionHandler {


    @Override
    public void handle(ProcessInstance processInstance, Context context) {
        System.out.println("###BaseActionHandler Info Print###");
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
