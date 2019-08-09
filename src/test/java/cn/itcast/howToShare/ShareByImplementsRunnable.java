package cn.itcast.howToShare;

import org.junit.Test;

public class ShareByImplementsRunnable {
	class CountRunnable implements Runnable{
		// 只需要一个普通的成员变量就可以共享了，因为本身 runnable 对象就是被多个线程共享的
		private int count;

		@Override
		public void run() {
			for (int i = 0; i < 10; i++) {
				synchronized (CountRunnable.class) {
					count++;
				}
			}
		}

		public int getCount() {
			return count;
		}
	}
	
	@Test
	public void test() throws InterruptedException {
		CountRunnable cr = new CountRunnable();
		new Thread(cr).start();
		new Thread(cr).start();
		new Thread(cr).start();
		
		Thread.sleep(5000);
		
		System.out.println(cr.getCount());
	}
}
