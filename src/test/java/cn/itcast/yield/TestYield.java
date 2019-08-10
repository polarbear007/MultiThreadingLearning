package cn.itcast.yield;

public class TestYield {
	static class MyThread extends Thread {
		public MyThread(String name) {
			super(name);
		}

		@Override
		public void run() {
			for (int i = 0; i < 1000; i++) {
				// 因为张飞是一个弟弟，所以当我们发现张飞的时候，总是尽量让哥哥们先吃饭
				// 不过这个 yield() 方法并不是加锁，所以执行完yield() 方法以后，一般是会继续执行下面的代码
				// 一直到本次CPU分配的时间片执行完了以后，下次再执行的时候，一般都是其他线程先执行完，再执行此线程
				if(getName().equals("张飞")) {
					yield();
				}
				System.out.println(Thread.currentThread().getName() + "==> 正在吃饭" + i);
			}
		}
	}
	
	public static void main(String[] args) {
		System.out.println("主线程开始....");
		MyThread zf = new MyThread("张飞");
		MyThread gy = new MyThread("关羽");
		MyThread lb = new MyThread("刘备");
		zf.start();
		gy.start();
		lb.start();
		System.out.println("主线程结束....");
	}
}
