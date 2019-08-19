package cn.itcast.uncaughtExceptionHandler;

import java.util.Random;

import org.junit.Test;

/**
 * 
 * @author Administrator
 *
 */
public class TestUncaughtExceptionHandler {
	@Test
	public void test1() throws InterruptedException {
		Thread t1 = new Thread() {
			@Override
			public void run() {
				System.out.println(10 / 0);
			}
		};
		
		t1.setUncaughtExceptionHandler(new MyUncaughtExceptionHandler());
		
		// 【注意】 我们应该在线程启动之前就设置好！！！
		t1.start();
		
		t1.join();
	}
	
	public static void main(String[] args){
		Thread t1 = new Thread() {
			@Override
			public void run() {
				Random r = new Random();
				int num = 0;
				for (int i = 0; i < 10; i++) {
					num = r.nextInt(10);
					if(num == 5) {
						throw new RuntimeException("运气不好，因为被5砸中，淘汰了！！");
					}
					System.out.println(getName() + " : " + num);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		
		t1.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				System.out.println("被淘汰了不要紧，我们再试一次...");
				// 这里我们想要开启另一个线程来执行相同的任务
				// 但不知道为什么，如果这个线程同样再出异常的话，就无法再开启新线程了
				try {
					t.getClass().newInstance().start();
				} catch (InstantiationException | IllegalAccessException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		t1.start();
	}
}	
