package cn.itcast.thread.stop;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

import org.junit.Test;

public class StopTheThread2 {
	class MultiThreadDownload {
		private CountDownLatch latch;
		private int count;
		private volatile boolean isCancel;
		private Thread mergeThread;
		
		public MultiThreadDownload(int count) {
			super();
			this.count = count;
			this.latch = new CountDownLatch(count);
		}

		public void start() {
			mergeThread = new Thread() {
				@Override
				public void run() {
					try {
						merge();
					} catch (InterruptedException e) {
						System.out.println(Thread.currentThread().getName() + "下载失败，合并线程关闭！！");
						//e.printStackTrace();
					}
				}
			};
			
			mergeThread.start();
			
			for (int i = 0; i < count; i++) {
				new Thread() {
					public void run() {
						try {
							downLoad();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					};
				}.start();
			}
		}
		
		private void downLoad() throws InterruptedException {
			System.out.println(Thread.currentThread().getName() + "开始下载！");
			Random r = new Random();
			for (int i = 0; i < 10; i++) {
				// 在下载的过程中，我们总是检查 isCancel 值是否是 true, 如果是 true 的话，我们直接停止下载 
				if(!isCancel) {
					if(r.nextInt(20) == 5) {
						isCancel = true;
						mergeThread.interrupt();
						System.out.println(Thread.currentThread().getName() + "停止下载！");
						return;
					}else {
						Thread.sleep(500);
					}
				}else {
					System.out.println(Thread.currentThread().getName() + "停止下载！");
					return;
				}
			}
			System.out.println(Thread.currentThread().getName() + "数据下载完成！");
			latch.countDown();
		}
		
		private void merge() throws InterruptedException {
			// 在各个下载线程执行完以前，这里我们先不执行
			latch.await();
			System.out.println("文件合并成功.....");
		}
	}
	
	@Test
	public void test() throws InterruptedException {
		MultiThreadDownload multiDownLoad = new MultiThreadDownload(4);
		multiDownLoad.start();
		Thread.sleep(20000);
	}
}
