package cn.itcast.atomic.atomicInteger;

import org.junit.Test;

public class AtomicIntegerTest2 {
	class Counter {
		private volatile int count = 0;
		
		public int getCount() {
			return count;
		}
		
		// volatile 并不能保证 count++ 这种复合操作的原子性，所以我们需要加同步代码块
		public synchronized void add() {
			count++;
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
	public void testSynchronized() throws InterruptedException {
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
		System.out.println("synchronized 耗时： " + (System.currentTimeMillis() - start));
		
		// 最后，我们再打印一下 counter 的值
		System.out.println("counter 的值：" + counter.getCount());
	}
}
