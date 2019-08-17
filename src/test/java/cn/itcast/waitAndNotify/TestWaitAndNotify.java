package cn.itcast.waitAndNotify;

import java.util.LinkedList;

import org.junit.Test;

public class TestWaitAndNotify {
	class Producer extends Thread{
		private LinkedList<Integer> sharedList;
		private int limit;
		
		public Producer(LinkedList<Integer> sharedList, int limit) {
			super();
			this.sharedList = sharedList;
			this.limit = limit;
		}
		
		@Override
		public void run() {
			int i = 0;
			while(true) {
				synchronized (sharedList) {
					// 注意： 这里的条件判断应该使用 while ，因为生产者和消费者线程用的是同一把锁
					//       我们的本意是让消费者消费元素以后，唤醒本线程。 但是也有可能是生产者线程
					//       调用了 notify() 方法唤醒本线程，在这种情况下，我们应该再次判断，然后进入阻塞状态。
					while(sharedList.size() >= limit) {
						try {
							sharedList.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					
					// 如果数量不超过限制，那么我们就继续添加
					i++;
					sharedList.add(i);
					System.out.println("添加了一个新元素： " + i + ", 现在集合长度为：" + sharedList.size());
					// 添加完以后，为了防止有消费线程因为集合数量不足，而阻塞，这里唤醒一下
					sharedList.notify();
					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	class Consumer extends Thread{
		private LinkedList<Integer> sharedList;

		public Consumer(LinkedList<Integer> sharedList) {
			super();
			this.sharedList = sharedList;
		}
		
		@Override
		public void run() {
			while(true) {
				synchronized (sharedList) {
					// 注意： 这里的条件判断应该使用 while ，因为生产者和消费者线程用的是同一把锁
					//       我们的本意是让生产者生产元素以后，唤醒本线程。 但是也有可能是消费者线程
					//       调用了 notify() 方法唤醒本线程，在这种情况下，我们应该再次判断，然后进入阻塞状态。
					while(sharedList.size() <= 0) {
						try {
							sharedList.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					
					// 如果集合数量没有小于等于0，那么我们就消费一个元素
					// 先入先出，我们删除最左边的元素
					System.out.println("消费了：" + sharedList.removeFirst() + ", 现在集合长度为：" + sharedList.size());
					// 生产线程可能会因为集合元素过多，而阻塞，我们删除元素以后，可能就不满足这个条件了，这里就唤醒一下
					sharedList.notify();
					
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	@Test
	public void test() throws InterruptedException {
		LinkedList<Integer> sharedList = new LinkedList<Integer>();
		int limit = 10;
		Producer p1 = new Producer(sharedList, limit);
		Producer p2 = new Producer(sharedList, limit);
		Producer p3 = new Producer(sharedList, limit);
		
		Consumer c1 = new Consumer(sharedList);
		Consumer c2 = new Consumer(sharedList);
		Consumer c3 = new Consumer(sharedList);
		
		p1.start();
		p2.start();
		p3.start();
		c1.start();
		c2.start();
		c3.start();
		
		p1.join();
	}
}
