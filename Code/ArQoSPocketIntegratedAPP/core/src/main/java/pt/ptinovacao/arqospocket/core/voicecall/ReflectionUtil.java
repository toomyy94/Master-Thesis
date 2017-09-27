package pt.ptinovacao.arqospocket.core.voicecall;

import android.text.TextUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by pedro on 12/04/2017.
 */
public class ReflectionUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionUtil.class);

    public static Class<?> getClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static Method getMethod(Class<?> targetClass, String name, Class<?>... parameterTypes) {
        Method method = null;
        if (!(targetClass == null || TextUtils.isEmpty(name))) {
            try {
                method = targetClass.getMethod(name, parameterTypes);
            } catch (SecurityException e) {
                LOGGER.error(e.toString());
            } catch (NoSuchMethodException e2) {
                LOGGER.error(e2.toString());
            }
        }
        return method;
    }

    public static Field getField(Class<?> targetClass, String name) {
        Field field = null;
        if (!(targetClass == null || TextUtils.isEmpty(name))) {
            try {
                field = targetClass.getField(name);
            } catch (SecurityException e) {
                LOGGER.error(e.toString());
            } catch (NoSuchFieldException e2) {
                LOGGER.error(e2.toString());
            }
        }
        return field;
    }

    public static Constructor<?> getConstructor(Class<?> targetClass, Class<?>... parameterTypes) {
        Constructor<?> constructor = null;
        if (targetClass != null) {
            try {
                constructor = targetClass.getConstructor(parameterTypes);
            } catch (SecurityException e) {
                LOGGER.error(e.toString());
            } catch (NoSuchMethodException e2) {
                LOGGER.error(e2.toString());
            }
        }
        return constructor;
    }

    public static Object newInstance(Constructor<?> constructor, Object... args) {
        Object obj = null;
        if (constructor != null) {
            try {
                obj = constructor.newInstance(args);
            } catch (Exception e) {
                LOGGER.error("Exception in newInstance: " + e.getClass().getSimpleName());
            }
        }
        return obj;
    }

    public static Object invoke(Object receiver, Object defaultValue, Method method, Object... args) {
        if (method != null) {
            try {
                defaultValue = method.invoke(receiver, args);
            } catch (Exception e) {
                LOGGER.error("Exception in invoke: " + e.getClass().getSimpleName());
            }
        }
        return defaultValue;
    }

    public static Object getFieldValue(Object receiver, Object defaultValue, Field field) {
        if (field != null) {
            try {
                defaultValue = field.get(receiver);
            } catch (Exception e) {
                LOGGER.error("Exception in getFieldValue: " + e.getClass().getSimpleName());
            }
        }
        return defaultValue;
    }

    public static void setFieldValue(Object receiver, Field field, Object value) {
        if (field != null) {
            try {
                field.set(receiver, value);
            } catch (Exception e) {
                LOGGER.error("Exception in setFieldValue: " + e.getClass().getSimpleName());
            }
        }
    }

    public static Object getDeclaredField(Class<?> targetClass, String fieldName, Object defaultValue,
            Object returnType) {
        if (fieldName == null || fieldName.length() == 0) {
            return Integer.valueOf(0);
        }
        try {
            return targetClass.getDeclaredField(fieldName).get(returnType);
        } catch (Exception e) {
            LOGGER.error("Exception in getDeclaredField: " + e.getClass().getSimpleName() + " = " + e.toString());
            return defaultValue;
        }
    }
}