package com.example.demo6;

public class Student {
    private String name;
    private int age;

    private Student(Builder builder) {
        this.name = builder.name;
        this.age = builder.age;
    }

    public static final class Builder {
        private String name;
        private int age;

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setAge(int age) {
            this.age = age;
            return this;
        }
        public Student bulid(){
            return new Student(this);
        }

    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}