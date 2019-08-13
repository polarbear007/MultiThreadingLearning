package cn.itcast.lock.reentrantLock;

import java.util.concurrent.locks.ReentrantLock;

import org.junit.Test;

public class TestTryLock2 {
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
			if(lock.tryLock()) {
				try {
					// 获取到锁对象的话，我们执行临界区的代码
					System.out.println("B 线程获取到锁对象，执行临界区的代码！");
				}finally {
					lock.unlock();
				}
			}else {
				System.out.println("B 线程没有获取到锁对象，但是他并不傻傻地等待，而是直接去干别的事儿了");
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
