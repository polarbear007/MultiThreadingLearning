package cn.itcast.threadGroup;

import org.junit.Test;

public class TestThreadGroup {
	@Test
	public void  test() {
		ThreadGroup group = Thread.currentThread().getThreadGroup();
		// 打印本线程组的全部线程
		group.list();
		
		// 统计一下线程组中活跃的线程
		System.out.println(group.activeCount());
	}
	
	// 批量设置一些常见的操作
	@Test
	public void test2() {
		ThreadGroup group = Thread.currentThread().getThreadGroup();
		// 批量设置内部全部线程为守护线程
		group.setDaemon(true);
		
		// 批量设置所有线程为中断状态（如果我们想要通过设置中断状态，来停止多个线程，可以使用这个方法）
		// 比如说，我们在多线程下载中，发现了一个线程下载失败，那么其他的线程再下载也没有意义，这个时候
		// 如果我们把所有的下载线程都设置成一个线程组，我们就可以直接通过任意线程给所有线程设置中断状态
		group.interrupt();
	}
}
