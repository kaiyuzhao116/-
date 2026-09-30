package com.example.deme3;

import java.io.Serializable;

/**
 * 测试实体类，内部持有 Student 对象。
 * 实现 Serializable 用于序列化演示，实现 Cloneable 用于浅拷贝对比。
 *
 * @author kaimu
 * @date 2026-09-30
 */
public class Test implements Cloneable, Serializable {

    private Student student;

    public Student getStudent() {
        return student;
    }
    public void setStudent(Student student) {
        this.student = student;
    }

    /**
     * 克隆当前对象。
     * 注意：super.clone() 为浅拷贝，内部的 student 引用会被共享。
     *
     * @return 克隆后的新对象
     * @throws CloneNotSupportedException 当对象未实现 Cloneable 时抛出
     */
    @Override
    protected Test clone() throws CloneNotSupportedException {

        System.out.println("克隆成功！！");
        return (Test) super.clone();
    }
}
