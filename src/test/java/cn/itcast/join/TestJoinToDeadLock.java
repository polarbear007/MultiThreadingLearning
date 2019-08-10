package cn.itcast.join;

public class TestJoinToDeadLock {
	static class CountThread extends Thread{
		private static int count;
		private CountThread otherThread;
		
		// 外部传入其他的线程对象
		public void setOtherThread(CountThread otherThread) {
			this.otherThread = otherThread;
		}
		
		@Override
		public void run() {
			for (int i = 0; i < 5; i++) {
				synchronized (CountThread.class) {
					try {
						System.out.println("先等" + otherThread.getName() + "执行完....");
						otherThread.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					count++;
				}
			}
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		CountThread ct1 = new CountThread();
		CountThread ct2 = new CountThread();
		ct1.setOtherThread(ct2);
		ct2.setOtherThread(ct1);
		
		ct1.start();
		ct2.start();
		
		// 防止主线程结束，直接强制关闭 ct1 和 ct2
		ct1.join();
		ct2.join();
	}
}
