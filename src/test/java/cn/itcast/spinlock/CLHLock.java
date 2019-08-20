package cn.itcast.spinlock;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public class CLHLock {
	// 【注意】 其实这里的 isLocked 属性值是表示下一个线程是否处于锁定状态
	//        这里理解起来可能会有一点点困难
	//   我们一直说 CLH队列，其实并不是真正的链表结构，只不过在逻辑上存在先后顺序而已。
	//   我们知道一个线程尝试获取锁对象的时候就会执行 lock() 方法，这个方法做了以下几件事：
	//    1、 创建一个 node 对象
	//    2、 更新锁对象的 tail 属性值为刚才创建的 node 对象，并保存 tail 原来的属性值（也就是 preNode）
	//    3、 判断 preNode 的 isLocked 属性值是否为 true ，如果是 true ，就一直死循环等待； 
	//        一直到 preNode.isLocked 属性值为 false 的时候，表示本线程获取到锁对象，退出死循环。
	//     ====> 因此，其实 preNode.isLocked 属性其实是指明本线程是否处于锁定状态！！！
	public static class CLHNode {
		private volatile boolean isLocked = true;
	}

	// 这个 tail 结点只是保存最后一个执行 lock() 方法的线程对应的   结点对象
	private volatile CLHNode tail;
	// 通过这个 ThreadLocal 对象，可以拿到每个线程对象 ThreadLocalMap 中保存的 CLHNode 对象
	private static final ThreadLocal<CLHNode> LOCAL = new ThreadLocal<CLHNode>();
	// AtomicReferenceFieldUpdater 可以原子更新本对象的某个成员变量值（这个成员变量为引用类型）
	private static final AtomicReferenceFieldUpdater<CLHLock, CLHNode> UPDATER = 
			AtomicReferenceFieldUpdater.newUpdater(CLHLock.class, CLHNode.class, "tail");

	// 获取锁
	public void lock() {
		// 每个线程想要获取锁的时候，都先创建一个对应的 结点对象
		CLHNode node = new CLHNode();
		// 并把新建的结点对象保存到 ThreadLocalMap 中，省得作为返回值，可能会丢失或者被修改
		LOCAL.set(node);
		// 使用新创建的 node 对象去替换锁对象的 tail 属性值，并返回原来的值
		// 【注意】 在本线程获取到锁之前，就已经成功把 锁对象的 tail 属性值给更新了
		//        其实这个 tail 属性值主要是为了下面这个语句，返回 preNode , 每个线程都能拿到一个唯一的preNode 对象
		CLHNode preNode = UPDATER.getAndSet(this, node);
		// 如果 preNode 为null ，那么说明本线程是第一个执行 lock() 方法，获取到锁对象的，那么我们啥也不用操作
		if (preNode != null) {
			// 如果preNode 不为null ，那么我们必须等待 preNode 释放锁以后，才能获取到锁
			// 本线程获取到锁以后，tail 保存的并不一定是本线程对应的 node 对象，那个无关紧要
			while (preNode.isLocked) {}
			preNode = null;  // 方便GC回收
		}
	}

	// 释放锁
	public void unlock() {
		// 获取当前线程对应的节点
		CLHNode node = LOCAL.get();
		// 【注意】 这里的 compareAndSet() 方法会尝试更新 tail 属性值为 null, 并返回修改的结果
		//     1、 如果修改成功返回true，说明锁对象的 tail 属性值还是本线程， 没有其他的线程在等着本线程释放锁对象呢
		//			这个时候，我们只需要简单地让 node = null 就可以了, 不需要更新 isLocked 属性值，这个属性值只跟后面等待的线程有关
		//     2、 如果修改失败返回 false ，说明锁对象的 tail 属性值不是本线程的node, 有其他的线程等着本线程释放锁
		//          这个时候，我们必须先更新 node.isLocked = false ，让下一个线程退出死循环，获得锁对象
		//          再然后，同样是让 node = null ，虽然另一个线程还保存着本线程的 node 引用，但是很快也会置为null
		if (!UPDATER.compareAndSet(this, node, null)) {
			node.isLocked = false;
		}
		node = null; // 方便GC
	}
	
    static class MyRunnable implements Runnable{
    	private int count = 0;
		private CLHLock lock = new CLHLock();
		@Override
		public void run() {
			for (int i = 0; i < 10; i++) {
				lock.lock();
				try {
					count++;
				}finally {
					lock.unlock();
				}
			}
		}
		public int getCount() {
			return count;
		}
    }
	
	public static void main(String[] args) throws InterruptedException {
		MyRunnable r = new MyRunnable();
		
		for (int i = 0; i < 20; i++) {
			new Thread(r).start();
		}
		
		Thread.sleep(3000);
		System.out.println(r.getCount());
	}
}
