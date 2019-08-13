package cn.itcast.lock.readwriteLock;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import org.junit.Test;

public class TestUpgraded {
	class A extends Thread{
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
			// 先获取读锁
			readLock.lock();
			try {
				// 随便打印一下集合
				System.out.println(sharedList);
				// 释放读锁之前，我们先获取写锁，让锁升级
				// 【注意】 实际上就算是本线程获取了读锁，再想获取写锁的话，也是会造成死锁问题的！！
				writeLock.lock();
			}finally {
				readLock.unlock();
			}
			
			// 获取了写锁以后，我们 尝试进行写操作
			try {
				for (int i = 0; i < 3; i++) {
					sharedList.add(i);
				}
			}finally {
				writeLock.unlock();
			}
		}
	}
	
	@Test
	public void testDowngraded() throws InterruptedException {
		// 创建读写锁对象
		ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
		// 创建共享的List 集合，因为要测试写锁，所以我们搞个空集合就可以了
		ArrayList<Integer> list = new ArrayList<>();
		
		// 这里，我们为了排除多线程的干扰，直接使用单个线程
		// 验证一下，锁的升级操作，是否真的会造成死锁问题！！
		A a1 = new A(rwLock, list);
		a1.start();
		// 防止Junit 关闭线程
		a1.join();
	}
}
