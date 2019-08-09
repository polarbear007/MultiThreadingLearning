package cn.itcast.deamon;

public class TestDeamon {
	static class TimeThread extends Thread {
		@Override
		public void run() {
			int seconds = 0;
			while(true) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("耗时： " + (++seconds) + " 秒");
			}
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		System.out.println("主线程开启.....");
		TimeThread timeThread = new TimeThread();
		// 一定要在 start() 方法之前设置
		timeThread.setDaemon(true);
		timeThread.start();
		
		Thread.sleep(10000);
		System.out.println("主线程结束！！");
	}
}
