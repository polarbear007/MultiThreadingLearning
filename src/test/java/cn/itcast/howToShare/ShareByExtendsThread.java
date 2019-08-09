package cn.itcast.howToShare;

import java.util.Vector;

import org.junit.Test;

public class ShareByExtendsThread {
	class CountThread extends Thread{
		// 如果是引用数据类型，那么可以通过构造方法在多个线程对象中传入共享的变量
		private Vector<Integer> list;

		public CountThread(Vector<Integer> list) {
			super();
			this.list = list;
		}
		
		@Override
		public void run() {
			for (int i = 0; i < 10; i++) {
				System.out.println( Thread.currentThread().getName() + "===> +1");
				list.add(i);
			}
		}
	}
	
	@Test
	public void testShare() throws InterruptedException {
		// 声明一个 list 对象
		Vector<Integer> list = new Vector<Integer>();
		// 通过构造方法，把这个list 对象传入各个线程对象中
		CountThread t1 = new CountThread(list);
		CountThread t2 = new CountThread(list);
		CountThread t3 = new CountThread(list);
		
		t1.start();
		t2.start();
		t3.start();
		
		Thread.sleep(10000);
		
		System.out.println(list.size());
	}
}
