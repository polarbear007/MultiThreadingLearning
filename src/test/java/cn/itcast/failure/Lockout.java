package cn.itcast.failure;

import org.junit.Test;

/**
 *  Lockout 跟 DeadLock 最大的区别在于：
 *     DeadLock 是两个线程相互等待； 而 Lockout 只是一个线程等待另一个线程释放锁，但是另一个线程永远释放不了。
 * @author Administrator
 *
 */
public class Lockout {
	class A extends Thread{
		@Override
		public void run() {
			// 加上延迟，防止此线程先获取到锁对象
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			synchronized (B.class) {
				System.out.println("A线程能获取到锁吗？");
			}
		}
	}
	
	class B extends Thread{
		@Override
		public void run() {
			synchronized(B.class) {
				while(true) {
					// B 线程能释放锁吗？
				}
			}
		}
	}
	
	@Test
	public void testLockout() throws InterruptedException {
		A a = new A();
		B b = new B();
		a.start();
		b.start();
		a.join();
	}
}
