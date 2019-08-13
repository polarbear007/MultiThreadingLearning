package cn.itcast.lock.reentrantLock;

import java.util.concurrent.locks.ReentrantLock;

public class TestGetHoldCount {
	static class A extends Thread {
		private static ReentrantLock lock = new ReentrantLock();
		
		@Override
		public void run() {
			while(true) {
				System.out.println("获取锁以前: " + lock.getHoldCount());
				lock.lock();
				try {
					// 假如，我们在这里再获取一次锁，因为前面已经获取到锁了，你再执行一次，其实并不会有什么影响
					// 但是，我们再看 getHoldCount() 的返回值，会发现这个方法的返回值是又在前面的基础上加2，
					// 因为我们执行了两次 lock() 方法
					lock.lock();
					System.out.println("获取锁的时候: " + lock.getHoldCount());
				}finally {
					lock.unlock();
				}
				System.out.println("释放锁以后: " + lock.getHoldCount());
				System.out.println("****************");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args) {
		A a1 = new A();
		a1.start();
	}
}
