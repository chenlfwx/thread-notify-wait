package com.chen.safe;

/**	线程之间通信，安全版本
 *
 */

public class ProductConsumerSafe{
	public static void main(String[] args){
		Resource resource = new Resource();
		Input input = new Input(resource);
		Output output = new Output(resource);
		Thread product = new Thread(input);
		Thread consumer = new Thread(output);
		product.start();
		consumer.start();
	}
}

class Resource{
	String name; // 没有私有化，简便访问。
	char sex;
	boolean flag; // 标志位，代表资源的状态，如果为true，代表有资源，否则代表没资源
}

class Input implements Runnable{
	private Resource resource;


	public Input(Resource resource){
		this.resource = resource;
	}
	@Override
	public void run(){
		boolean flag = true;
		while(true){
			synchronized (resource){
				if(resource.flag){ // 有资源就等待
					try {
						resource.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				if(flag){
					resource.name = "张三";
					resource.sex = '男';
					flag = false;
				}else{
					resource.name = "李四";
					resource.sex = '女';
					flag = true;
				}
				resource.flag = true;
				resource.notify();
			}
		}
	}
}

class Output implements Runnable{
	
	private Resource resource;
	
	public Output(Resource resource){
		this.resource = resource;
	}
	@Override
	public void run(){
		while(true){
			synchronized (resource){
				if(!resource.flag){
					try {
						resource.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				System.out.println("name：" + resource.name + "，sex：" + resource.sex);
				resource.flag = false;
				resource.notify();
			}
		}
	}
}