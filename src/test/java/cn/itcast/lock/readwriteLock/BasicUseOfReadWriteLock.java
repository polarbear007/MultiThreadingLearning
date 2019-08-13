package cn.itcast.lock.readwriteLock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import org.junit.Test;

public class BasicUseOfReadWriteLock {
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
			// 获取读锁
			readLock.lock();
			try {
				// 做一些只读操作, 遍历集合
				for (Integer i : sharedList) {
					System.out.println(Thread.currentThread().getName() + ": " + i);
					// 为了能看到读锁共享的效果，我们延迟个 1 秒才读取共享集合的下一个元素
					Thread.sleep(1000);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}finally {
				readLock.unlock();
			}
		}
	}
	
	@Test
	public void testReadLock() throws InterruptedException {
		// 创建读写锁对象
		ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
		// 创建共享的List 集合，因为暂时不考虑写锁，所以我们直接添加一些初始元素在里面
		ArrayList<Integer> list = new ArrayList<>(Arrays.asList(new Integer[] {1,2,3,4,5,6,7}));
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
	}
}
