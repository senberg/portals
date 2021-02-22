package senberg.portals.stargate.rpc;

import java.io.Serializable;

public class RPCResponse implements Serializable {
    Object object;

    public RPCResponse(Object object) {
        this.object = object;
    }
}
