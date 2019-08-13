package cn.itcast.lock.reentrantLock;

import java.util.concurrent.locks.ReentrantLock;

import org.junit.Test;

public class TestLockInterruptibly {
	static class A extends Thread {
		private final static ReentrantLock lock = new ReentrantLock();
		
		@Override
		public void run() {
			for (int i = 0; i < 5; i++) {
				// 普通的 lock() 方法，是不会检查线程的中断状态的，就算别的线程把本线程的状态设置成中断状态
				// 这个方法也正常获取锁对象，如果获取不到锁对象，也正常进入阻塞状态
				lock.lock();
				try {
					System.out.println(Thread.currentThread().getName() + ": hello!");
				}finally {
					lock.unlock();
				}
				
				// 其他线程执行了 interrupt() 方法以后，只会在这里抛出 InterruptedException
				// 因为这里我们只是打印一下异常信息，并不做任何处理。所以之后，线程正常执行，把循环走完
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		A a = new A();
		a.start();
		Thread.sleep(2000);
		a.interrupt();
	}
}
