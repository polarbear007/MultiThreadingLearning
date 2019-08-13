package cn.itcast.lock.readwriteLock;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import org.junit.Test;

public class BasicUseOfReadWriteLock2 {
	class A extends Thread {
		private ReentrantReadWriteLock rwLock;
		private ReadLock readLock;
		private WriteLock writeLock;
		private ArrayList<Integer> sharedList;
		
		// 使用构造方法传入 读写锁对象 和 共享集合
		public A(ReentrantReadWriteLock rwLock, ArrayList<Integer> sharedList) {
			super();
			if(rwLock == null) {
				throw new RuntimeException("锁对象不能为null");
			}
			this.rwLock = rwLock;
			this.readLock = rwLock.readLock();
			this.writeLock = rwLock.writeLock();
			
			this.sharedList = sharedList;
		}
		
		@Override
		public void run() {
			// 获取写锁
			writeLock.lock();
			try {
				// 往共享集合里面写入元素
				for (int i = 0; i < 3; i++) {
					System.out.println(Thread.currentThread().getName() + "写入:  " + i);
					sharedList.add(i);
					// 这里我们故意每写一个元素停1秒钟，看看不同的线程能不能同时写入
					Thread.sleep(1000);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}finally {
				writeLock.unlock();
			}
		}
	}
	
	@Test
	public void testWriteLock() throws InterruptedException {
		// 创建读写锁对象
		ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
		// 创建共享的List 集合，因为要测试写锁，所以我们搞个空集合就可以了
		ArrayList<Integer> list = new ArrayList<>();
		A a1 = new A(rwLock, list);
		A a2 = new A(rwLock, list);
		A a3 = new A(rwLock, list);
		a1.start();
		a2.start();
		a3.start();
		// 防止Junit 关闭线程
		a1.join();
		a2.join();
		a3.join();
		// 最后输出一下保存的元素
		System.out.println(list);
	}
}
