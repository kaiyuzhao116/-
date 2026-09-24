package com.example.demo.test2.test2;

public class anquanmen2 implements fanghuo, fangshui,fangdao{
    @Override
    public void fanghuo() {
        System.out.println("防盗");
    }

    @Override
    public void fangshui() {
        System.out.println("防入侵");
    }

    @Override
    public void fangdao() {
        System.out.println("防盗窃");
    }
}
