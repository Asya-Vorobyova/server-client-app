/**
 * Author Asya
 * Date Oct 24, 2016
 */
package ixtens.my.serverclientapp;

/**
 * @author Asya
 *
 */
public class ResponseException extends Exception {
    private String reason;

    /**
     * @param reason
     */
    public ResponseException(String reason) {
        this.reason = reason;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ResponseException [reason=" + reason + "]";
    }

}
