/**
 * Author Asya
 * Date Oct 20, 2016
 */
package ixtens.my.serverclientapp.services;

import java.util.Date;

/**
 * @author Asya
 *
 */
public class TestService {

    public void sleep(Long millis) throws InterruptedException {
        Thread.sleep(millis.longValue());
    }

    public Date getCurrentDate() {
        return new Date();
    }
    
}
