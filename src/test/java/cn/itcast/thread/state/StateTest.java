package cn.itcast.thread.state;

import org.junit.Test;

public class StateTest {
	class A extends Thread{
		@Override
		public void run() {
			while(true) {
				synchronized (A.class) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	/**
	 *   Blocked 是本来 ready 状态的线程获取不到锁，而进入的状态。
	 *   Waiting 是执行了 wait() / sleep() / join() 方法而进入等待的状态。
	 * @throws InterruptedException
	 */
	@Test
	public void test() throws InterruptedException {
		A a1 = new A();
		A a2 = new A();
		a1.start();
		a2.start();
		
		for (int i = 0; i < 10; i++) {
			Thread.sleep(1000);
			System.out.println(a1.getState());
			System.out.println(a2.getState());
			System.out.println("***************");
		}
		
		a1.join();
		a2.join();
	}
}
