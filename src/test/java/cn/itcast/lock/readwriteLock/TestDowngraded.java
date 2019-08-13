package cn.itcast.lock.readwriteLock;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import org.junit.Test;

public class TestDowngraded {
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
			// 先获取写锁
			writeLock.lock();
			try {
				for (int i = 0; i < 3; i++) {
					sharedList.add(i);
				}
				// 修改完共享集合以后，在释放写锁前，我们再获取读锁（java 是支持的）
				readLock.lock();
			}finally {
				writeLock.unlock();
			}
			
			try {
				System.out.println(sharedList);
			}finally {
				readLock.unlock();
			}
		}
	}
	
	@Test
	public void testDowngraded() throws InterruptedException {
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
	}
}
