package com.example.agent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);

        // ========= 测试Agent =========
        ReactLoopAgent agent = new ReactLoopAgent();
        // 发送消息：查询天气，会触发工具调用
        agent.sendMessage("查询北京天气");
        // 启动kick驱动器
        agent.kick();

        // 你可以打开下面注释，测试协作取消
        // new Thread(() -> {
        //     try {Thread.sleep(600);} catch (Exception e){}
        //     agent.cancel(true);
        // }).start();
    }
}
