package cn.itcast.join;

import org.junit.Test;

public class TestJoin {
	class MyThread extends Thread {
		
		public MyThread(String name){
			super(name);
		}
		
		@Override
		public void run() {
			for (int i = 0; i < 5; i++) {
				System.out.println(Thread.currentThread().getName() + "==> 正在吃饭" + i);
			}
		}
	}
	
	@Test
	public void testJoin() throws InterruptedException {
		System.out.println("主线程开始....");
		MyThread zf = new MyThread("张飞");
		MyThread gy = new MyThread("关羽");
		MyThread lb = new MyThread("刘备");
		zf.start();
		gy.start();
		lb.start();
		// 当我们在主线程中执行了 lb.join() 方法，意味着主线程会一直等待 lb 线程执行完
		// 才开始执行下面的代码。  但是这只是 主线程跟 lb 线程之间的关系， 跟其他的两个线程没有任何关系。
		// 也就是说， zf / gy / lb 等线程仍然是正常抢夺执行权！！
		lb.join();
		System.out.println("主线程结束....");
	}
}
