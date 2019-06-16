package com.chen.safe;

/**	线程之间通信，安全版本
 *
 */

public class ProductConsumerSafeOne{
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
				if(flag){
					resource.name = "张三";
					resource.sex = '男';
					flag = false;
				}else{
					resource.name = "李四";
					resource.sex = '女';
					flag = true;
				}
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
				System.out.println(resource.name + "---" + resource.sex);
			}
		}
	}
}