package cn.itcast.condition;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.Test;

public class TestCondition {
	class Producer extends Thread{
		private LinkedList<Integer> sharedList;
		private int limit;
		private ReentrantLock lock;
		private Condition produceCondition;
		private Condition consumeCondition;
		
		public Producer(LinkedList<Integer> sharedList, int limit, ReentrantLock lock, Condition produceCondition,
				Condition consumeCondition) {
			super();
			this.sharedList = sharedList;
			this.limit = limit;
			this.lock = lock;
			this.produceCondition = produceCondition;
			this.consumeCondition = consumeCondition;
		}
		
		@Override
		public void run() {
			int i = 0;
			while(true) {
				lock.lock();
				try {
					// 注意： 这里还是得使用 while 来循环判断，虽然 produceCondition.signal() 方法只会唤醒
					//       生产者线程，但是如果不循环判断的话，还是会存在问题
					while(sharedList.size() >= limit) {
						try {
							produceCondition.await();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					
					// 如果共享集合的长度没有超过限制，那么我们就正常添加元素
					i++;
					sharedList.add(i);
					System.out.println(Thread.currentThread().getName() + "添加了一个新元素： " + i + ", 现在集合长度为：" + sharedList.size());
					
					// 添加完以后，集合已经不可能为空，我们可以唤醒因为集合为空而阻塞的消费者线程来消费元素
					consumeCondition.signal();
				}finally {
					lock.unlock();
				}
			}
		}
		
	}
	
	class Consumer extends Thread{
		private LinkedList<Integer> sharedList;
		private ReentrantLock lock;
		private Condition produceCondition;
		private Condition consumeCondition;
		
		public Consumer(LinkedList<Integer> sharedList, ReentrantLock lock, Condition produceCondition,
				Condition consumeCondition) {
			super();
			this.sharedList = sharedList;
			this.lock = lock;
			this.produceCondition = produceCondition;
			this.consumeCondition = consumeCondition;
		}
		
		@Override
		public void run() {
			while(true) {
				lock.lock();
				try {
					// 注意： 这里还是得使用 while 来循环判断，虽然 consumeCondition.signal() 方法只会唤醒
					//       消费者线程，但是如果不循环判断的话，还是会存在问题
					while(sharedList.isEmpty()) {
						try {
							consumeCondition.await();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					
					// 如果集合不为空，那么我们就消费一个元素
					// 先入先出，我们删除最左边的元素
					System.out.println(Thread.currentThread().getName() + "消费了：" + sharedList.removeFirst() + ", 现在集合长度为：" + sharedList.size());
					// 生产线程可能会因为集合元素过多，而阻塞，我们删除元素以后，可能就不满足这个条件了，这里就唤醒一下
					produceCondition.signal();
				}finally {
					lock.unlock();
				}
			}
		}
		
		
	}
	
	@Test
	public void test() throws InterruptedException {
		LinkedList<Integer> sharedList = new LinkedList<Integer>();
		int limit = 10;
		ReentrantLock lock = new ReentrantLock();
		Condition produceCondition = lock.newCondition();
		Condition consumeCondition = lock.newCondition();
		Producer p1 = new Producer(sharedList, limit, lock, produceCondition, consumeCondition);
		Producer p2 = new Producer(sharedList, limit, lock, produceCondition, consumeCondition);
		Producer p3 = new Producer(sharedList, limit, lock, produceCondition, consumeCondition);
		
		Consumer c1 = new Consumer(sharedList, lock, produceCondition, consumeCondition);
		Consumer c2 = new Consumer(sharedList, lock, produceCondition, consumeCondition);
		Consumer c3 = new Consumer(sharedList, lock, produceCondition, consumeCondition);
		
		p1.start();
		p2.start();
		p3.start();
		c1.start();
		c2.start();
		c3.start();
		
		p1.join();
	}
}
