package cn.itcast.creation;

import org.junit.Test;

public class ByExtendsThread {
	class HelloThread extends Thread {
		@Override
		public void run() {
			System.out.println(Thread.currentThread().getName() + ": hello ...");
		}
	}
	
	class HiThread extends Thread {
		@Override
		public void run() {
			System.out.println(Thread.currentThread().getName() + ": hi ...");
		}
	}
	
	@Test
	public void test() throws InterruptedException {
		HelloThread helloThread = new HelloThread();
		HiThread hiThread = new HiThread();
		
		// 启动线程必须使用 start 方法，而不是我们重写的 run 方法
		helloThread.start();
		hiThread.start();
		
		// 为了防止Junit 切断其他线程，我们让主线程多停留一会再结束
		Thread.sleep(1000);
	}
	
	@Test
	public void testStart() {
		HelloThread helloThread = new HelloThread();
		helloThread.start();
		helloThread.start();
	}
	
	
}
