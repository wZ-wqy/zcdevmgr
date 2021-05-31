package com.dt.module.flow.base;

import com.bstek.uflo.env.Context;
import com.bstek.uflo.model.ProcessInstance;
import com.bstek.uflo.process.handler.AssignmentHandler;
import com.bstek.uflo.process.node.TaskNode;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @author: lank
 * @date: Nov 30, 2019 9:04:15 AM
 * @Description:
 */

//流程处理人
@Component
public class UfloAssignmentHandler implements AssignmentHandler {

    @Override
    public Collection<String> handle(TaskNode taskNode, ProcessInstance processInstance, Context context) {
        return null;
    }

}
