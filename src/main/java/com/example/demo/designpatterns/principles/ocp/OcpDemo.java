// OcpDemo.java
package com.example.demo.designpatterns.principles.ocp;

public class OcpDemo {
    public static void main(String[] args) {
        SouGouInput input = new SouGouInput();

        input.setSkin(new DefaultSkin());
        input.display();

        input.setSkin(new HeimaSkin());
        input.display();
    }
}