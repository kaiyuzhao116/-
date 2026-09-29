package com.example.demo1;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;


public class Factory {


    private static HashMap<String, Person> beanMap = new HashMap<>();
    static {
        Properties properties = new Properties();
        InputStream resourceAsStream = Factory.class.getClassLoader().getResourceAsStream("bean.properties");

        try {
            properties.load(resourceAsStream);
            Set<Object> objects = properties.keySet();
            for (Object object : objects) {
                String property = properties.getProperty((String) object);

                Class aClass = Class.forName(property);
                Person instance = null;
                try {
                    instance = (Person) aClass.getDeclaredConstructor().newInstance();
                } catch (InstantiationException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
                beanMap.put((String) object, instance);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public static Person getBean(String beanName) {
        return beanMap.get(beanName);
    }
    public static void main(String[] args) {
        System.out.println(Factory.getBean("st"));
        System.out.println(Factory.getBean("te"));
    }
}
