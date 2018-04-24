package com.ldh.androidlib.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * utility class for reflection (invoke method  or set field value)
 *
 * @author stone
 * @date 16/12/2
 */
public final class ReflectUtils {

    private ReflectUtils() {
        //no instance
    }

    /**
     * 执行一个方法
     *
     * @param targetClass 目标类的class对象
     * @param methodName  要执行的方法名
     * @param paramTypes  如果是无参方法, 此参数传null
     * @param targetObj   目标对象
     * @param actualArgs  如果是无参方法, 此参数传null
     * @param <T>         返回值类型
     * @param <T>         目标类的类型
     * @param <ARGS>      要执行的方法的参数类型(arguments type)
     * @return 返回目标方法的返回值
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     */
    public static <RT, T, ARGS> RT executeMethod(Class<T> targetClass, String methodName, Class<ARGS>[] paramTypes, T targetObj, ARGS[] actualArgs) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Method method = null;
        if (paramTypes == null) { //无参方法
            method = targetClass.getDeclaredMethod(methodName);
        } else {
            method = targetClass.getDeclaredMethod(methodName, paramTypes);
        }
        boolean isAccessible = method.isAccessible();
        method.setAccessible(true);
        RT result = null;
        if (paramTypes == null) {
            result = (RT) method.invoke(targetObj);
        } else {
            result = (RT) method.invoke(targetObj, actualArgs);
        }
        method.setAccessible(isAccessible);
        return result;
    }

    /**
     * 给某个字段设置值
     *
     * @param targetClass 目标类的class对象
     * @param fieldName   要进行设值的字段名
     * @param targetObj   目标对象
     * @param actualArg   字段的值
     * @param <T>         目标类型
     * @param <ARGS>      字段的值的类型
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static <T, ARGS> void assignFieldValue(Class<T> targetClass, String fieldName, T targetObj, ARGS actualArg) throws NoSuchFieldException, IllegalAccessException {
        Field field = targetClass.getDeclaredField(fieldName);
        boolean isAccessible = field.isAccessible();
        field.setAccessible(true);
        field.set(targetObj, actualArg);
        field.setAccessible(isAccessible);
    }

    /**
     * 获取某个字段的值
     *
     * @param targetClass 目标类的class对象
     * @param fieldName   要获取值的字段名
     * @param targetObj   目标对象
     * @param <RT>        字段类型
     * @param <T>         目标类的类型
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static <RT, T> RT getFieldValue(Class<T> targetClass, String fieldName, T targetObj) throws NoSuchFieldException, IllegalAccessException {
        Field field = targetClass.getDeclaredField(fieldName);
        boolean isAccessible = field.isAccessible();
        field.setAccessible(true);
        RT fieldValue = (RT) field.get(targetObj);
        field.setAccessible(isAccessible);
        return fieldValue;
    }

    /**
     * 判断实例是否是匿名类
     *
     * @param instance
     * @param <T>
     * @return
     */
    public static <T> boolean instanceOfAnonymousClass(T instance) {
        return instance.getClass().isAnonymousClass();
    }

    /**
     * 判断实例是否是内部非静态类
     *
     * @param instance
     * @param <T>
     * @return
     */
    public static <T> boolean instanceOfInnerNonStaticClass(T instance) {
        Class<T> klass = (Class<T>) instance.getClass();
        return klass.getName().matches("^[\\w\\.\\$_]+\\$[\\w\\$_]+$") &&
                !Modifier.isStatic(klass.getModifiers()) &&
                klass.getEnclosingClass() != null;
    }

    /**
     * 判断实例是否是内部静态类
     *
     * @param instance
     * @param <T>
     * @return
     */
    public static <T> boolean instanceOfInnerAndStaticClass(T instance) {
        Class<T> klass = (Class<T>) instance.getClass();
        return klass.getName().matches("^[\\w\\.\\$_]+\\$[\\w\\$_]+$") &&
                Modifier.isStatic(klass.getModifiers()) &&
                klass.getEnclosingClass() != null &&
                !klass.isAnonymousClass(); //匿名内部类的修饰符也是static
    }
}
