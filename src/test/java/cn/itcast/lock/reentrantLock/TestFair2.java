package cn.itcast.lock.reentrantLock;

import java.util.concurrent.locks.ReentrantLock;

import org.junit.Test;

public class TestFair2 {
	class A extends Thread{
		private ReentrantLock lock;
		
		public A(ReentrantLock lock) {
			super();
			this.lock = lock;
		}

		@Override
		public void run() {
			for (int i = 0; i < 5; i++) {
				lock.lock();
				try {
					System.out.println(Thread.currentThread().getName());
				}finally {
					lock.unlock();
				}
			}
		}
	}
	
	@Test
	public void testFair() throws InterruptedException {
		// 设置显式锁的调度模式为公平模式
		ReentrantLock lock = new ReentrantLock(true);
		A a1 = new A(lock);
		A a2 = new A(lock);
		A a3 = new A(lock);
		
		a1.start();
		a2.start();
		a3.start();
		
		Thread.sleep(10000);
	}
}
