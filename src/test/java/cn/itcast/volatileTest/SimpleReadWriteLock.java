package cn.itcast.volatileTest;

import org.junit.Test;

public class SimpleReadWriteLock {
	class A {
		private volatile int count;
		
		// 多个线程可以同时读取
		public int getCount() throws InterruptedException {
			int count = this.count;
			Thread.sleep(1000);
			return count;
		}
		
		// 写操作的时候，一个时刻只允许一个线程去操作
		public synchronized int increase() throws InterruptedException {
			int count = ++this.count;
			Thread.sleep(1000);
			return count;
		}
	}
	
	// 测试结果：
	//   Thread-0: 1565750179887 : 0
	//   main: 1565750179887 : 0
	//   两个线程的执行时间几乎是同时执行，说明多个线程都可以同时执行此方法
	@Test
	public void testRead() throws InterruptedException {
		A a = new A();
		new Thread() {
			@Override
			public void run() {
				try {
					System.out.println(Thread.currentThread().getName() + ": " + System.currentTimeMillis() + " : " + a.getCount());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
		
		// 主线程和另一个线程同时去执行 getCount() 方法
		// 我们不确定哪个线程会先执行，这里主要看两个方法执行的时间间隔有没有 1 秒
		System.out.println(Thread.currentThread().getName() + ": " + System.currentTimeMillis() + " : " + a.getCount());
		
		Thread.sleep(2000);
	}
	
	// 测试结果：
	//   main: 156575049 9272 : 1
	//   Thread-0: 156575050 0273 : 2
	//  两个线程执行的间隔差 1 秒左右，说明有线程等待
	@Test
	public void testWrite() throws InterruptedException {
		A a = new A();
		new Thread() {
			@Override
			public void run() {
				try {
					int count = a.increase();
					System.out.println(Thread.currentThread().getName() + ": " + System.currentTimeMillis() + " : " + count);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
		
		// 主线程和另一个线程同时去执行 increase() 方法
		// 我们不确定哪个线程会先执行，这里主要看两个方法执行的时间间隔有没有 1 秒
		int count = a.increase();
		System.out.println(Thread.currentThread().getName() + ": " + System.currentTimeMillis() + " : " + count);
		
		Thread.sleep(3000);
	}
}
