/**
 * Author Asya
 * Date Oct 24, 2016
 */
package ixtens.my.serverclientapp;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Properties;

import ixtens.my.serverclientapp.domain.*;

/**
 * This class contains server utility methods
 *
 */
public class ServerUtils {

    private static class WrongParametersCountException extends Exception {
        private static final long serialVersionUID = 1L;
    }

    private static class NoSuchMethodNameException extends Exception {
        private static final long serialVersionUID = -492026398347367094L;
    }

    private static Properties properties;
    
    private static final String SERVICE_PACKAGE_PRFX = "ixtens.my.serverclientapp.services.";
    
    static {
        properties = new Properties();
        InputStream input = null;
        try {
            input = ServerUtils.class.getClassLoader().getResourceAsStream("server.properties");
            // load a properties file
            properties.load(input);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
    
    public static Response generateResponse(Command command) {
        Response response = new Response(command.getId());
        EBadResponse errors = EBadResponse.OK;
        Object responseData = null;
        String serviceClassName = properties.getProperty(command.getServiceName());
        if (serviceClassName == null || serviceClassName.isEmpty()) {
            errors = EBadResponse.SERVICENOTEXIST;
        } else {
            try {
                //get service class
                Class<?> serviceClass = Class.forName(SERVICE_PACKAGE_PRFX + serviceClassName);
                Class<?>[] parameterTypes = Arrays.stream(command.getParams()).map(obj -> obj.getClass()).toArray(Class[]::new);
                Method[] findByNameMethods = Arrays.stream(serviceClass.getDeclaredMethods()).filter(m -> m.getName().equals(command.getMethodName())).toArray(Method[]::new);
                if (findByNameMethods.length == 0)
                    throw new NoSuchMethodNameException();
                Arrays.stream(findByNameMethods).filter(m -> m.getParameterCount() == parameterTypes.length).findFirst().orElseThrow(() -> new WrongParametersCountException());
                Method serviceMethod = serviceClass.getMethod(command.getMethodName(), parameterTypes);
                Object serviceInstance = serviceClass.newInstance();
                responseData = serviceMethod.invoke(serviceInstance, command.getParams());
                if(serviceMethod.getReturnType().equals(Void.TYPE)) {
                    response.setReturnsVoid(true);
                }
            } catch (ClassNotFoundException e) {
                errors = EBadResponse.SERVICENOTEXIST;
            } catch (NoSuchMethodNameException e) {
                errors = EBadResponse.METHODNOTEXIST;
            } catch (WrongParametersCountException e) {
                errors = EBadResponse.WRONGPARAMETERSCOUNT;
            } catch (NoSuchMethodException e) {
                errors = EBadResponse.BADTYPESPARAMETERS;
            } catch (Exception e) {
                // other problems: cannot instantiate new service or nullary
                // constructor isn't accessible or service instance is null or
                // method cannot be accessed, and so on
                errors = EBadResponse.OTHERERRORS;
            }
        }
        
        response.setResponseData(responseData);
        response.setBadResponse(errors);
        return response;
    }
}
