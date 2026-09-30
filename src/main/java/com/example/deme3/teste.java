package com.example.deme3;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * 序列化与反序列化演示入口。
 * 通过文件流将 Test 对象写入 test.txt，再读回为独立副本，
 * 验证副本的修改不会影响原对象。
 *
 * @author kaimu
 * @date 2026-09-30
 */
@Slf4j
public class teste {

    public static void main(String[] args) throws CloneNotSupportedException {
        System.out.println("Hello, World!");
        Test test = new Test();
        Student student = new Student();
        test.setStudent(student);
        test.getStudent().setName("张三");

        // 通过对象输出流将 test 序列化写入 test.txt
        Test object;
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("test.txt"));

            objectOutputStream.writeObject(test);

            objectOutputStream.close();

            log.info("序列化成功");

            // 通过对象输入流从 test.txt 反序列化读回，得到一个全新的独立对象
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("test.txt"));

            object = (Test) objectInputStream.readObject();
            log.info("反序列化成功");

            objectInputStream.close();
            // 修改副本的 name，原对象 test 不受影响，以此证明是独立副本
            object.getStudent().setName("李四");

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


//        Test clone = test.clone();
//        System.out.println(test.getStudent());
//        System.out.println(clone.getStudent()); // 输出: true
//        System.out.println("");
//        clone.getStudent().setName("李四");
//        System.out.println(test.getStudent());
//        System.out.println(clone==test); // 输出: false


        System.out.println(test.getStudent());
        System.out.println(object.getStudent());
    }
}
