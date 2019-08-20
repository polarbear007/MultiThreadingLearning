package cn.itcast.threadFactory;

import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;

import org.junit.Test;

public class TestThreadFactory2 {
	class ConsumerThreadFactory implements ThreadFactory {
		private volatile int count;

		@Override
		public Thread newThread(Runnable r) {
			Thread t = new Thread(r);
			t.setName("ConsumerThread-" + (count++));
			t.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

				@Override
				public void uncaughtException(Thread t, Throwable e) {
					System.out.println(System.currentTimeMillis() + " : " + t.getName() + " : " + e.getMessage());
				}
			});
			return t;
		}
	}

	class MyRunnable implements Runnable {
		private ArrayBlockingQueue<Integer> queue;

		public MyRunnable(ArrayBlockingQueue<Integer> queue) {
			super();
			this.queue = queue;
		}

		@Override
		public void run() {
			while (true) {
				try {
					System.out.println(Thread.currentThread().getName() + "消费了：" + queue.take());
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}

	@Test
	public void test() throws InterruptedException {
		ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);
		queue.addAll(Arrays.asList(new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9 }));
		MyRunnable r = new MyRunnable(queue);
		ConsumerThreadFactory factory = new ConsumerThreadFactory();
		for (int i = 0; i < 3; i++) {
			factory.newThread(r).start();
		}
		Thread.sleep(5000);
	}
}
