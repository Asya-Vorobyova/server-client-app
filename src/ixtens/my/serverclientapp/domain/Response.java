/**
 * Author Asya
 * Date Oct 20, 2016
 */
package ixtens.my.serverclientapp.domain;

import java.io.Serializable;

/**
 *
 * This class contains server response. It has the following parameters: id of
 * initial command, server method resulting object, enum coding the server
 * probable problems, flag to detect if server returned nothing (void method result).
 */
public class Response implements Serializable {

    private static final long serialVersionUID = 2038781952889625197L;

    private int commandId;

    private Object responseData;

    private EBadResponse badResponse;
    
    private boolean returnsVoid = false;
    
    /**
     * @param commandId
     */
    public Response(int commandId) {
        this.commandId = commandId;
    }

    
    /**
     * @param commandId
     * @param responseData
     * @param badResponse
     * @param returnsVoid
     */
    public Response(int commandId, Object responseData, EBadResponse badResponse, boolean returnsVoid) {
        this.commandId = commandId;
        this.responseData = responseData;
        this.badResponse = badResponse;
        this.returnsVoid = returnsVoid;
    }


    /**
     * Default constructor
     */
    public Response() {
    }


    /**
     * @return the commandId
     */
    public int getCommandId() {
        return commandId;
    }


    /**
     * @param commandId the commandId to set
     */
    public void setCommandId(int commandId) {
        this.commandId = commandId;
    }


    /**
     * @return the responseData
     */
    public Object getResponseData() {
        return responseData;
    }


    /**
     * @param responseData the responseData to set
     */
    public void setResponseData(Object responseData) {
        this.responseData = responseData;
    }


    /**
     * @return the badResponse
     */
    public EBadResponse getBadResponse() {
        return badResponse;
    }


    /**
     * @param badResponse the badResponse to set
     */
    public void setBadResponse(EBadResponse badResponse) {
        this.badResponse = badResponse;
    }


    /**
     * @return the returnsVoid
     */
    public boolean isReturnsVoid() {
        return returnsVoid;
    }


    /**
     * @param returnsVoid the returnsVoid to set
     */
    public void setReturnsVoid(boolean returnsVoid) {
        this.returnsVoid = returnsVoid;
    }


    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Response [commandId=" + commandId + ", responseData=" + responseData + ", badResponse=" + badResponse.getReason()
                + ", returnsVoid=" + returnsVoid + "]";
    }

}
