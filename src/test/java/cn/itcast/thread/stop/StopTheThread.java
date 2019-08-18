package cn.itcast.thread.stop;

import org.junit.Test;

public class StopTheThread {
	class A extends Thread{
		@Override
		public void run() {
			// 我们可以在线程执行的时候，一直检查线程的状态，如果线程被标记成 中断，那我们直接退出
			while(true) {
				if(isInterrupted()) {
					System.out.println(getName() + "停止....");
					return;
				}
				// 多线程正常的执行代码
			}
		}
	}
	
	@Test
	public void testStop() throws InterruptedException {
		A a1 = new A();
		A a2 = new A();
		A a3 = new A();
		
		a1.start();
		a2.start();
		a3.start();
		
		// 如果两秒以后，这两个线程还不执行完，我们觉得已经没有必要再执行了
		Thread.sleep(2000);
		
		// 我们通过 中断线程  来间接实现停止线程的功能
		a1.interrupt();
		a2.interrupt();
		a3.interrupt();
		
		Thread.sleep(2000);
	}
}
