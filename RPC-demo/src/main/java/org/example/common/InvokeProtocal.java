package org.example.common;

import java.io.Serializable;

public class InvokeProtocal implements Serializable {

    public String requestId;
    public String target;

    public String methodName;

    public String[] paramTypes;

    public Object[] args;

    public Object result;

    public InvokeProtocal(String requestId, String target, String methodName, String[] paramTypes, Object[] args) {
        this.requestId = requestId;
        this.target = target;
        this.methodName = methodName;
        this.paramTypes = paramTypes;
        this.args = args;
    }
}
