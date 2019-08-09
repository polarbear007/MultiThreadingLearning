package cn.itcast.creation;

import org.junit.Test;

public class ByImplementRunnable {
	class HelloRunnable implements Runnable{
		@Override
		public void run() {
			System.out.println(Thread.currentThread().getName() + ": hello...");
		}
	}
	
	class HiRunnable implements Runnable{
		@Override
		public void run() {
			System.out.println(Thread.currentThread().getName() + ": hi...");
		}
	}
	
	@Test
	public void test() throws InterruptedException {
		Thread helloThread = new Thread(new HelloRunnable());
		Thread hiThread = new Thread(new HiRunnable());
		
		helloThread.start();
		hiThread.start();
		
		Thread.sleep(1000);
	}
}
