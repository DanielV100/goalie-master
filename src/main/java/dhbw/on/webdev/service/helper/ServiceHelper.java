package dhbw.on.webdev.service.helper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ServiceHelper {
    public static  <T> void updateEntity(T source, T target) {
        if (source == null || target == null) {
            System.out.println("Source or target is null");
            return;
        }
        Class<?> enitityClass = source.getClass();
        Field[] fields = enitityClass.getDeclaredFields();
        if (fields.length == 0) {
            System.out.println("No fields found in class: " + enitityClass.getSimpleName());
            return;
        }
        for (Field field : fields) {
            String fieldName = field.getName();
            if(fieldName.startsWith("$$") || fieldName.contains("hibernate")) {
                return;
            }
            String setterMethodName = "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
            try {
                field.setAccessible(true);
                Object sourceValue = field.get(source);
                if (sourceValue == null) {
                    System.out.println("Nothing to change at field: " + fieldName);
                } else {
                    Method setterMethod = enitityClass.getMethod(setterMethodName, field.getType());
                    setterMethod.invoke(target, sourceValue);
                    System.out.println("Updated field: " + fieldName + " with value: " + sourceValue);
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException exception) {
                System.out.println("Error updating field: " + fieldName);
                exception.printStackTrace();
            }
        }
    }
}
