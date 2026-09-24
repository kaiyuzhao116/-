package com.example.demo.designpatterns.principles.ocp1;

public class Student extends Preson {
    @Override
    public void display() {
        System.out.println("Displaying Student");
    }
}

class Teacher extends Preson {
    @Override
    public void display() {
        System.out.println("Displaying Teacher  information");
    }
}
