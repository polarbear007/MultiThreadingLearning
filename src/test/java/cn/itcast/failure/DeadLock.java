package cn.itcast.failure;

import org.junit.Test;

public class DeadLock {
	class A extends Thread{
		@Override
		public void run() {
			synchronized (A.class) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				// A 线程占用A锁，等待其他线程释放B锁，才能往下执行
				synchronized (B.class) {
					System.out.println("Hello! I'm A thread...");
				}
			}
		}
	}
	
	class B extends Thread{
		@Override
		public void run() {
			synchronized (B.class) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				// B 线程占用B锁，等待其他线程释放A锁，才能往下执行
				synchronized (A.class) {
					System.out.println("Hello! I'm B thread...");
				}
			}
		}
	}
	
	@Test
	public void testDeadLock() throws InterruptedException {
		A a = new A();
		B b = new B();
		a.start();
		b.start();
		
		// 为了防止 Junit 切断全部的线程
		a.join();
		b.join();
	}
}
