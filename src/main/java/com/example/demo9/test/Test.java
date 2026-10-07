package com.example.demo9.test;

public class Test {
    public static void main(String[] args) {
        Object date = getDate();
        if (date instanceof String aa){
            //类型转换强转

            System.out.println(aa);
            System.out.println("It's a String");
        }else if(date instanceof Integer){
            System.out.println("It's an Integer");
        }

    }

    public static Object getDate(){
        return "Hello" ;
    }
}
