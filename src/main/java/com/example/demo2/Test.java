package com.example.demo2;

import java.util.HashMap;

public class Test implements Cloneable{

    private Student student;

    public Student getStudent() {
        return student;
    }
    public void setStudent(Student student) {
        this.student = student;
    }

    @Override
    protected Test clone() throws CloneNotSupportedException {

        System.out.println("克隆成功！！");
        return (Test) super.clone();
    }
}
