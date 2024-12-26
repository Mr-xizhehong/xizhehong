package com.jingdianjichi.subject.application.controller;

import java.util.concurrent.locks.LockSupport;

public class a {
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " is going to park.");
            LockSupport.park();
            System.out.println(Thread.currentThread().getName() + " has been unparked.");
        }, "Thread-1");
        
        t1.start();
        
        try {
            Thread.sleep(3000); // 让线程运行3秒
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("Unparking Thread-1.");
        LockSupport.unpark(t1); // 唤醒线程
    }
}
