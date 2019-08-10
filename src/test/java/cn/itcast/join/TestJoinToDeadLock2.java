package cn.itcast.join;

public class TestJoinToDeadLock2 {
	static class CountThread extends Thread{
		private static int count;
		
		@Override
		public void run() {
			for (int i = 0; i < 5; i++) {
				try {
					// 就算没有同步代码块，在 run 方法中，通过 this 对象调用 join() 方法也会造成死锁
					// 因为 join() 方法执行以后，会阻塞等待调用者对应的线程先执行完，再接着执行
					// 而这里的 this 对象，刚好就是本线程； 
					//  于是，在这里我们阻塞等待本线程执行结束；可是本线程只有继续往下执行，才有可能结束的呀！！
					join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				count++;
			}
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		CountThread ct1 = new CountThread();
		ct1.start();
	}
}
