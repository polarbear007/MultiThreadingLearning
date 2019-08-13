package cn.itcast.lock.reentrantLock;

import java.util.concurrent.locks.ReentrantLock;

import org.junit.Test;

public class TestLockInterruptibly2 {

	static class B extends Thread {
		private final static ReentrantLock lock = new ReentrantLock();
		
		@Override
		public void run() {
			// 为了排除 sleep() 方法的干扰，我们不使用 sleep()， 而是把循环次数增加
			for (int i = 0; i < 10000; i++) {
				try {
					// 每次获取锁的时候，都会检查线程的状态，如果是中断状态，那么就会直接扔异常
					lock.lockInterruptibly();
				} catch (InterruptedException e1) {
					// 捕捉到异常以后，我们一定不要去执行临界区的代码，因为这个时候本线程并没有获取到锁对象
					// 也就是说，如果你执行了临界区的代码，那么就可能会出现线程安全问题
					e1.printStackTrace();
					// 因此，我们这里直接跳出循环！！！
					//break;
				}
				try {
					System.out.println(Thread.currentThread().getName() + ": hello!");
				}finally {
					lock.unlock();
				}
			}
		}
	}
	
	@Test
	public void test() throws InterruptedException {
		B b = new B();
		b.start();
		Thread.sleep(1);
		b.interrupt();
		b.join();
	}
}
