package cn.itcast.lock.reentrantLock;

import java.util.concurrent.locks.ReentrantLock;

import org.junit.Test;

public class TestTryLock {
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
				// 这里我们啥事不干，就是想要让A线程一直占用锁
				while(true) {
					
				}
			}finally {
				lock.unlock();
			}
		}
	}
	
	class B extends Thread{
		private ReentrantLock lock;

		public B(ReentrantLock lock) {
			super();
			this.lock = lock;
		}
		
		@Override
		public void run() {
			// b 线程是拿不到锁对象的，那么 lock() 方法就会一直阻塞下去
			lock.lock();
			try {
				System.out.println("b 线程拿不到锁，能让程序往下走吗？");
			}finally {
				lock.unlock();
			}
		}
	}
	
	@Test
	public void test() throws InterruptedException {
		ReentrantLock lock = new ReentrantLock();
		A a = new A(lock);
		B b = new B(lock);
		
		a.start();
		// 我们故意在 200 毫秒以后，再去开启 b 线程，这样子b 线程一般是获取不到锁对象的
		Thread.sleep(200);
		b.start();
		
		a.join();
	}
}
