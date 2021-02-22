package senberg.portals.jaxws;

import jakarta.jws.WebMethod;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface RemoteCalculator {
    @WebMethod
    int add(int a, int b);
}
