package cn.itcast.countDownLatch;

import java.util.concurrent.CountDownLatch;

import org.junit.Test;

public class CountDownLatchTest3 {
	@Test
	public void test1() {
		CountDownLatch latch = new CountDownLatch(0);
		System.out.println(latch.getCount()); // 0
		
		// 我们执行一次 countDown() 方法，再次 getCount() , 会发现返回的还是0
		latch.countDown();
		System.out.println(latch.getCount()); // 0
	}
	
	@Test
	public void test2() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(0);
		latch.await();
		
		System.out.println("这个线程会被阻塞吗？");
	}
}
