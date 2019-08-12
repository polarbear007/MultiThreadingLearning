package cn.itcast.interrupt;

public class TestInterrupt {
	static class A extends Thread {
		private static int count;
		
		@Override
		public void run() {
			for (int i = 0; i < 5; i++) {
				// 当线程获取到CPU的执行权时，外部的 interrupt() 方法并不会阻止线程的执行
				synchronized (A.class) {
					System.out.println(Thread.currentThread().getName() + ": " + count);
					count++;
				}
				// 只有在线程方法执行到  wait() / join() / sleep() 方法的时候，正要进入或者正处于阻塞状态时
				// 才会去检查线程的状态是否是  interrupted ， 一旦检查到线程状态为 interrupted ，就会扔异常
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// 因为我们这里catch 异常对象以后，只是随便打印一下异常信息，没有其他的处理，所以中断状态被清除后，
					// 线程会正常执行
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		A a = new A();
		a.start();
		// 两秒以后我们再去设置 a 线程的中断状态
		Thread.sleep(2000);
		a.interrupt();
	}
}
