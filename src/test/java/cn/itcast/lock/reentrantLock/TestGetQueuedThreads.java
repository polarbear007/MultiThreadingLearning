package cn.itcast.lock.reentrantLock;

import java.util.concurrent.locks.ReentrantLock;

import org.junit.Test;

public class TestGetQueuedThreads {
	class A extends Thread {
		private ReentrantLock lock;

		public A(ReentrantLock lock) {
			super();
			this.lock = lock;
		}
		
		@Override
		public void run() {
			lock.lock();
			try {
				System.out.println(Thread.currentThread().getName() + "获取了锁对象");
			}finally {
				lock.unlock();
			}
		}
	}
	
	@Test
	public void testGetQueuedThreads() throws InterruptedException {
		ReentrantLock lock = new ReentrantLock();
		A a1 = new A(lock);
		A a2 = new A(lock);
		A a3 = new A(lock);
		// 在开启 a1 / a2 / a3 线程之前，主线程很坏地先占有了锁对象
		lock.lock();
		a1.start();
		a2.start();
		a3.start();
		
		// 这里我们加点延迟，方便确认 a1 / a2 / a3 都已经确实开启了，并等待占有锁对象
		Thread.sleep(200);
		
		// 遗憾的是， getQueuedThreads() 方法是 protected() 修饰的，我们并不能直接访问，所以我们就调用一下
		//  getQueueLength() 方法，这个方法会返回
		int len = lock.getQueueLength();
		System.out.println(len);
	}
}
