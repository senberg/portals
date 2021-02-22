package senberg.portals.stargate.rpc;

import java.io.Serializable;
import java.util.List;


public class RPCRequest implements Serializable {
    String objectName;
    String methodName;
    List<Object> parameters;
}
