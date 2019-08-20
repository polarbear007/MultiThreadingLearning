package cn.itcast.threadFactory;

import java.util.concurrent.ThreadFactory;

import org.junit.Test;

public class TestThreadFactory {
	class MyThreadFactory implements ThreadFactory{
		private volatile int count;

		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(r);
			t.setName("HelloThread-" + (count++));
			t.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
				
				@Override
				public void uncaughtException(Thread t, Throwable e) {
					System.out.println(System.currentTimeMillis() + " : " + t.getName() + " : " + e.getMessage());
				}
			});
			return t;
		}
	}
	
	@Test
	public void test() throws InterruptedException {
		MyThreadFactory factory = new MyThreadFactory();
		for (int i = 0; i < 10; i++) {
			factory.newThread(new Runnable() {
				
				@Override
				public void run() {
					System.out.println(Thread.currentThread().getName() + " : hi~");
				}
			}).start();
		}
		Thread.sleep(2000);
	}
}
