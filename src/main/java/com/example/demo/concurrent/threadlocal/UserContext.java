package com.example.demo.concurrent.threadlocal;

/**
 * ThreadLocal 实验台之"上下文容器"（对应实验①②③）
 * 模仿 Spring RequestContextHolder 的封装套路：private static final + set/get/clear 三件套
 */
public class UserContext {

    /** 普通 ThreadLocal：每个线程各持一份副本，线程之间互相看不见 */
    private static final ThreadLocal<String> CURRENT = new ThreadLocal<>();

    /** InheritableThreadLocal：子线程"被创建的那一刻"会复制父线程的值（实验④专门翻车用它） */
    private static final InheritableThreadLocal<String> INHERITABLE = new InheritableThreadLocal<>();

    public static void set(String user) {
        CURRENT.set(user);
    }

    public static String get() {
        return CURRENT.get();
    }

    /** 清理必须用 remove()，而不是 set(null)——前者是彻底删掉 entry */
    public static void clear() {
        CURRENT.remove();
    }

    public static void setInheritable(String user) {
        INHERITABLE.set(user);
    }

    public static String getInheritable() {
        return INHERITABLE.get();
    }

    public static void clearInheritable() {
        INHERITABLE.remove();
    }
}
