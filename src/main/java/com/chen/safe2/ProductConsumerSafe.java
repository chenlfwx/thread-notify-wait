package com.chen.safe2;

/**
 * 线程之间通信，安全版本
 */

public class ProductConsumerSafe {
    public static void main(String[] args) {
        Resource resource = new Resource();
        new Thread(new Input(resource)).start();
        new Thread(new Output(resource)).start();
    }
}

class Resource {
    private String name;
    private char sex;
    private boolean flag; // 标志位，代表资源的状态，如果为true，代表有资源，否则代表没资源

    public synchronized void setValue(String name, char sex) {
        if (flag) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.name = name;
        this.sex = sex;
        flag = true;
        notify();
    }

    public synchronized void out() {
        if (!flag) {    // 如果没有资源就等待
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("name：" + name + "，sex：" + sex);
        flag = false;
        notify();
    }
}

class Input implements Runnable {
    private Resource resource;

    public Input(Resource resource) {
        this.resource = resource;
    }

    @Override
    public void run() {
        boolean flag = true;
        while (true) {
            if (flag) {
                resource.setValue("张三", '男');
                flag = false;
            } else {
                resource.setValue("李四", '女');
                flag = true;
            }
        }
    }
}

class Output implements Runnable {

    private Resource resource;

    public Output(Resource resource) {
        this.resource = resource;
    }

    @Override
    public void run() {
        while (true) {
            resource.out();
        }
    }
}