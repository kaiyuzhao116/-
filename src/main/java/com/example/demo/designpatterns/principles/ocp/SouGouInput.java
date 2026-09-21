// SouGouInput.java
package com.example.demo.designpatterns.principles.ocp;

public class SouGouInput {
    // 这是“关联”：SouGouInput 持有 AbstractSkin
    private AbstractSkin skin;

    public void setSkin(AbstractSkin skin) {
        this.skin = skin;
    }

    public void display() {
        skin.display();
    }
}