package com.example.deme3;

import java.io.Serializable;

/**
 * 学生实体类，用于序列化与反序列化演示。
 * 实现 Serializable 接口，才能被写入对象输出流。
 *
 * @author kaimu
 * @date 2026-09-30
 */
public class Student implements Serializable {
    private String name;

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                '}';
    }
}
