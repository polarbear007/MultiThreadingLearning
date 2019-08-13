package cn.itcast.lock.reentrantLock;

import org.junit.Test;

public class TestFair {
	class A extends Thread{
		@Override
		public void run() {
			for (int i = 0; i < 5; i++) {
				synchronized (A.class) {
					System.out.println(Thread.currentThread().getName());
				}
			}
		}
	}
	
	@Test
	public void testFair() throws InterruptedException {
		A a1 = new A();
		A a2 = new A();
		A a3 = new A();
		
		a1.start();
		a2.start();
		a3.start();
		
		Thread.sleep(10000);
	}
}
