package cn.itcast.howToShare;

import org.junit.Test;

public class ShareByExtendsThread2 {
	static class CountThread extends Thread{
		// 如果是基本数据类型，一定要使用静态变量，而不能使用成员变量
		//  因为成员变量不能在多个线程对象之间共享，必须转成 static 类变量才可以
		private static int count;
		
		@Override
		public void run() {
			for (int i = 0; i < 10; i++) {
				synchronized (CountThread.class) {
					System.out.println( Thread.currentThread().getName() + "===> +1");
					count++;
				}
			}
		}

		public static int getCount() {
			return count;
		}
	}
	
	@Test
	public void testShare() throws InterruptedException {
		CountThread t1 = new CountThread();
		CountThread t2 = new CountThread();
		CountThread t3 = new CountThread();
		t1.start();
		t2.start();
		t3.start();
		
		Thread.sleep(10000);
		
		System.out.println(CountThread.getCount());
	}
}
