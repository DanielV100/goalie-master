package dhbw.on.webdev.service.helper;

import dhbw.on.webdev.model.User;
import dhbw.on.webdev.repository.UserRepository;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Base64;

/**
 * This helper class contains several helper methods for the service classes.
 * @author daniel
 */
@ApplicationScoped
public class ServiceHelper {
    @Inject
    UserRepository userRepository;

    /**
     * This is a generic method for updating every fields from existing entities.
     * It's done via reflection and by copying fields from source to target.
     * Setter/Getters have to be triggered, so that the JPA knows that the persistence has changed.
     * Setter/Getter should always be in default declaration - otherwise it won't work.
     * @param source entity from the client via put-request
     * @param target entity to override/update from db
     */
    public  <T> boolean updateEntity(T source, T target) {
        if (source == null || target == null) {
            Log.warn("Source entity or target entity is null");
            return false;
        }
        Class<?> enitityClass = source.getClass();
        final Field[] fields = enitityClass.getDeclaredFields();
        if (fields.length == 0) {
            Log.warn("No fields found in class: " + enitityClass.getSimpleName());
            return false;
        }
        for (Field field : fields) {
            final String fieldName = field.getName();
            //exclude hibernate specific fields
            if(fieldName.startsWith("$$") || fieldName.contains("hibernate")) {
                Log.info("Hibernate specific field reached");
                continue;
            }
            //Getting setter method
            final String setterMethodName = "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
            try {
                field.setAccessible(true);
                Object sourceValue = field.get(source);
                if (sourceValue == null) {
                    Log.info("Nothing to change at field: " + fieldName);
                } else {
                    Method setterMethod = enitityClass.getMethod(setterMethodName, field.getType());
                    setterMethod.invoke(target, sourceValue);
                    Log.info("Updated field: " + fieldName + " with value: " + sourceValue);
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException exception) {
                Log.error("Error updating field: " + fieldName + ": " + exception);
                return false;
            }
        }
        return true;
    }

    /**
     * Try to get user from user repo and return it.
     * @param id from user in db
     * @return User or null
     */
    public User getCurrentUser(long id) {
        User user = userRepository.findById(id);
        if(user != null) {
            Log.error("User found: " + user);
            return user;
        } else {
            Log.error("User not found");
            return null;
        }
    }

    /**
     * Method for converting a data url (e.g. from sketch) to a byte array
     * @param dataURL from client
     * @return data url as byte array
     */
    public byte[] convertDataUrlToByteArray(String dataURL) {
        if(dataURL != null) {
            return Base64.getDecoder().decode(dataURL.split(",")[1]);
        } else {
            Log.warn("Data url is null");
            return null;
        }
    }
}
