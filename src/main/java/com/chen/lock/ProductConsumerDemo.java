package com.chen.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 在多个生产者和多个消费者之间，我们可以使用Java5.0提供的Lock机制来取代Synchronized，Condition来取代同步监视器。
 * @author Chen Dian
 * @date 2019/06/16
 */

public class ProductConsumerDemo {
    public static void main(String[] args) {
        Resources resouces = new Resources();
        new Thread(new Producter(resouces)).start();
        new Thread(new Producter(resouces)).start();
        new Thread(new Consumer(resouces)).start();
        new Thread(new Consumer(resouces)).start();
    }
}

class Resources {
    private String name;
    private Integer count = 1;
    private boolean flag;
    private ReentrantLock lock = new ReentrantLock();
    private Condition productCondit = lock.newCondition(); // 生产者上的锁
    private Condition consumerCondit = lock.newCondition(); // 消费者上的锁

    public void setValue(String name) {
        lock.lock();
        try {
            while (flag) {
                try {
                    productCondit.await(); // 存在资源就等待
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            this.name = name + "---" + count++;
            flag = true;
            System.out.println(Thread.currentThread().getName() + "---生产者：" + this.name);
            consumerCondit.signal(); // 唤醒消费者中的一个
        } finally {
            lock.unlock(); // 释放锁
        }
    }

    public void out() {
        lock.lock();
        try {
            while (!flag) {
                try {
                    consumerCondit.await(); // 没有资源就等待
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(Thread.currentThread().getName() + "---消费者：" + name);
            flag = false;
            productCondit.signal();
        } finally {
            lock.unlock();
        }
    }
}

class Producter implements Runnable {

    private Resources resource;

    public Producter(Resources resource) {
        this.resource = resource;
    }

    @Override
    public void run() {
        while (true) {
            resource.setValue("商品");
        }
    }
}

class Consumer implements Runnable {

    private Resources resources;

    public Consumer(Resources resources) {
        this.resources = resources;
    }

    @Override
    public void run() {
        while (true) {
            resources.out();
        }
    }
}