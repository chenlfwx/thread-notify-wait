package com.chen.multi;

/** 在多个生产者和多个消费者之间，我们应该使用while来进行线程之间的通信，并唤醒同一个锁的所有线程(包括对方线程)，防止虚假唤醒
 * @author Chen Dian
 * @date 2019/06/16
 */

public class ProductConsumerDemo {
    public static void main(String[] args){
        Resources resouces = new Resources();
        new Thread(new Producter(resouces)).start();
        new Thread(new Producter(resouces)).start();
        new Thread(new Consumer(resouces)).start();
        new Thread(new Consumer(resouces)).start();
    }
}

class Resources{
    private String name;
    private Integer count = 1;
    private boolean flag;

    public synchronized void setValue(String name){
        while(flag){
            try {
                wait(); // 存在资源就等待
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.name = name + "---" + count++;
        flag = true;
        System.out.println(Thread.currentThread().getName() + "---生产者：" + this.name);
        notifyAll();
    }

    public synchronized void out(){
        while(!flag){
            try {
                wait(); // 没有资源就等待
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(Thread.currentThread().getName() + "---消费者：" + name);
        flag = false;
        notifyAll();
    }
}

class Producter implements Runnable{

    private Resources resource;

    public Producter(Resources resource) {
        this.resource = resource;
    }

    @Override
    public void run() {
        while(true){
            resource.setValue("商品");
        }
    }
}

class Consumer implements Runnable{

    private Resources resources;

    public Consumer(Resources resources) {
        this.resources = resources;
    }

    @Override
    public void run() {
        while(true){
            resources.out();
        }
    }
}