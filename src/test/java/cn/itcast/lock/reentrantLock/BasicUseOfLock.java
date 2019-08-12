package cn.itcast.lock.reentrantLock;

import java.util.concurrent.locks.ReentrantLock;

public class BasicUseOfLock {
	static class A extends Thread {
		// 创建一个共享的锁对象
		private final static ReentrantLock lock = new ReentrantLock();
		// 共享变量 id
		private static int id = 0;
		
		@Override
		public void run() {
			for (int i = 0; i < 5; i++) {
				// 先尝试获取锁对象
				lock.lock();
				// lock() 和 unlock() 方法之间的代码就是临界区代码， 不过我们一般直接把 try 区域的代码认为是临界区代码
				try {
					System.out.println(Thread.currentThread().getName() + "获取到id 值为: " + (id++));
				}finally {
					// 一定要在 finally 里面释放锁，因为显式锁并不像内部锁那样会自动释放，如果临界区代码出现异常
					// 那么其他的线程可能永远也获取不了锁，所以一定要在 finally 代码块中释放锁
					lock.unlock();
				}
			}
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		A a1 = new A();
		A a2 = new A();
		a1.start();
		a2.start();
		
		a1.join();
		a2.join();
	}
}
