package cn.itcast.interrupt;

import org.junit.Test;

public class TestInterrupt2 {
	class A extends Thread {
		private A partner;
		
		public A(String name) {
			super(name);
		}

		public void setPartner(A partner) {
			this.partner = partner;
		}

		@Override
		public void run() {
			// 如果小伙伴是刘备的话，那么我们先让大哥刘备先执行完，再开始
			if(partner.getName().equals("刘备")) {
				try {
					partner.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			for (int i = 0; i < 5; i++) {
				synchronized (A.class) {
					System.out.println(Thread.currentThread().getName() + ": " + i);
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@Test
	public void testInterrupt() throws InterruptedException {
		A a1 = new A("刘备");
		A a2 = new A("关羽");
		a1.setPartner(a2);
		a2.setPartner(a1);
		a1.start();
		a2.start();
		// 开启a1 和 a2 线程以后，根据代码来看，关羽线程应该会等等刘备线程先执行完，再执行
		// 但是2秒以后，我们在主线程调用了 a2.interrupt() 方法，这个方法相当于取消 关羽线程的 join() 方法
		//  关羽不再等等 刘备线程执行完，直接就参与CPU的执行权
		Thread.sleep(2000);
		a2.interrupt();
		
		a1.join();
		a2.join();
	}
}
