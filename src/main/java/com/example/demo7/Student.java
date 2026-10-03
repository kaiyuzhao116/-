package com.example.demo7;

public class Student {
    private String name;
    private int age;

    public Student(Builder builder) {
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
        public Student build() {
            return new Student(this);
        }
    }



    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
