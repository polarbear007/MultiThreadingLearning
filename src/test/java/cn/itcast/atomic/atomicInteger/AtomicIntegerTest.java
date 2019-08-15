package cn.itcast.atomic.atomicInteger;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

public class AtomicIntegerTest {
	class Counter {
		private AtomicInteger count = new AtomicInteger();
		
		public int getCount() {
			return count.get();
		}
		
		// 因为我们使用的是原子变量类，所以这种自增的复合操作，可以保证是原子性操作
		// 因此，我们不需要加锁或者加同步代码块
		public void add() {
			count.incrementAndGet();
		}
	}
	
	class MyThread extends Thread {
		private Counter counter;
		
		public MyThread(Counter counter) {
			super();
			this.counter = counter;
		}

		@Override
		public void run() {
			for (int i = 0; i < 10000000; i++) {
				counter.add();
			}
		}
	}
	
	@Test
	public void testAtomicInteger() throws InterruptedException {
		Counter counter = new Counter();
		MyThread t1 = new MyThread(counter);
		MyThread t2 = new MyThread(counter);
		MyThread t3 = new MyThread(counter);
		MyThread t4 = new MyThread(counter);
		MyThread t5 = new MyThread(counter);
		
		long start = System.currentTimeMillis();
		
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		t5.start();
		
		// 防止 Junit 关闭其他线程
		t1.join();
		t2.join();
		t3.join();
		t4.join();
		t5.join();
		System.out.println("AtomicInteger 耗时： " + (System.currentTimeMillis() - start));
		
		// 最后，我们再打印一下 counter 的值
		System.out.println("counter 的值：" + counter.getCount());
	}
}
