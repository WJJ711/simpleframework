package org.simpleframework.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * @author wjj
 * @version 1.0
 * @date 2020/6/22 18:51
 */
@Slf4j
public class ClassUtil {

    public static final String FILE_PROTOCOL = "file";

    /**
     * 获取包下类集合
     *  1.获取到类的加载器
     *  2.通过类加载器获取到加载的资源信息
     *  3.依据不同的资源类型，采用不同的方式获取资源的集合
     * @param packageName
     */
    public static Set<Class<?>> extractPackageClass(String packageName){
        //1.获取到类的加载器
        ClassLoader classLoader = getClassLoader();
        // 2.通过类加载器获取到加载的资源信息
        URL url = classLoader.getResource(packageName.replace(".", "/"));
        if (url==null){
            log.warn("unable to retrieve anything from package"+packageName);
            return null;
        }
        //3.依据不同的资源类型，采用不同的方式获取资源的集合
        Set<Class<?>> classSet=null;
        if (url.getProtocol().equalsIgnoreCase(FILE_PROTOCOL)){
            classSet=new HashSet<>();
            File packageDirectory = new File(url.getPath());
            extractClassFile(classSet,packageDirectory,packageName);

        }
        //todo 此处可以加入针对其它类型资源的处理
        return classSet;
    }

    /**
     * 递归获取目标package里面的所有class文件(包括子package里的class文件)
     * @param classSet 装载目标类的集合
     * @param packageDirectory  文件或者目录
     * @param packageName   包名
     */
    private static void extractClassFile(Set<Class<?>> classSet, File packageDirectory, String packageName) {
        if (!packageDirectory.isDirectory()){
            return;
        }
        //如果是一个文件夹，则调用其listFiles方法获取文件夹下的文件或文件夹(不包括子文件夹)
        File[] files = packageDirectory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.isDirectory()){
                    return true;
                }else {
                    //是文件
                    //获取文件的绝对值路径
                    String absolutePath = file.getAbsolutePath();
                    if (absolutePath.endsWith(".class")){
                        //若是class文件，则直接加载
                        addtoClassSet(absolutePath);
                    }
                }
                return false;
            }
            //根据class文件的绝对值路径，获取并生成class对象，并放入classSet中
            private void addtoClassSet(String absolutePath) {
                //1.从class文件的绝对值路径里提取出包含了package的类名
                //D:\workspace\simpleframework\target\classes\com\baixiao\entity\bo\HeadLine.class
                //需要转变成com.baixiao.entity.bo.HeadLine

                absolutePath = absolutePath.replace(File.separator, ".");
                String className=absolutePath.substring(absolutePath.indexOf(packageName));
                className=className.substring(0,className.lastIndexOf("."));

                //2.通过反射机制获取对应的Class对象，并加入到classSet里
                Class<?> targetClass = loadClass(className);
                classSet.add(targetClass);
            }
        });
        if (files!=null){
            for (File file : files) {
                //递归调用
                extractClassFile(classSet,file,packageName);
            }
        }
    }

    /**
     * 获取Class对象
     * @param className class全名=package+类全名
     * @return
     */
    public static Class<?> loadClass(String className){
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            log.error("load class error",e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取classLoader
     * @return 当前的ClassLoader
     */
    public static ClassLoader getClassLoader(){
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     *
     * @param clazz
     * @param accessible 是否支持创建出私有class对象的实例
     * @param <T>
     * @return
     */
    public static <T> T newInstance(Class<?> clazz,boolean accessible){
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(accessible);
            return (T)constructor.newInstance();
        } catch (Exception e) {
            log.error("newInstance error",e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 设置类的属性值
     * @param field 成员变量
     * @param target 类实例
     * @param value 成员变量的值
     * @param accessible 是否允许设置私有属性
     */
    public static void setField(Field field,Object target,Object value,boolean accessible){
        field.setAccessible(accessible);
        try {
            field.set(target,value);
        } catch (IllegalAccessException e) {
            log.error("setField error",e);
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        extractPackageClass("com.baixiao.entity");

    }
}
