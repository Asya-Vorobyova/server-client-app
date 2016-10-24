/**
 * Author Asya
 * Date Oct 23, 2016
 */
package ixtens.my.serverclientapp.domain;

/**
 * Enum containing bad response reasons
 *
 */
public enum EBadResponse {
    OK(""),
    SERVICENOTEXIST("There isn't such service"),
    METHODNOTEXIST("There isn't such method"),
    BADTYPESPARAMETERS("Parameters of bad types"),
    WRONGPARAMETERSCOUNT("Wrong number of parameters"),
    OTHERERRORS("Other errors");
    
    private String reason;

    /**
     * @param reason
     */
    private EBadResponse(String reason) {
        this.reason = reason;
    }

    /**
     * @return the reason
     */
    public String getReason() {
        return reason;
    }
    
}
