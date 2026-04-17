package za.ac.cput.studentenrolmentsystem.common;

import java.io.Serializable;

/**
 *
 * @author Aidan
 */
public class Request implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String action;  
    private Object payload; 

    public Request(String action, Object payload) {
        this.action = action;
        this.payload = payload;
    }

    public String getAction() {
        return action;
    }

    public Object getPayload() {
        return payload;
    }
    
    @Override
    public String toString() {
        return "Request{action='" + action + "', payload=" + payload + "}";
    }
}
