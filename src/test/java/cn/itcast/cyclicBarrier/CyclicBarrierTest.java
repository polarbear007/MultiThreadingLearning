package cn.itcast.cyclicBarrier;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import org.junit.Test;

public class CyclicBarrierTest {
	class  GatheringDragonBalls {
		private CyclicBarrier barrier = new CyclicBarrier(7, new Runnable() {
			@Override
			public void run() {
				System.out.println("龙珠集齐！！！");
				System.out.println(Thread.currentThread().getName() + ": 召唤神龙，并许愿......");
			}
		});
		
		public void gathering() throws InterruptedException, BrokenBarrierException {
			System.out.println(Thread.currentThread().getName() + ": 出发去收集龙珠.....");
			Thread.sleep(new Random().nextInt(10) * 1000);
			System.out.println(Thread.currentThread().getName() + ": 收集到一颗龙珠了！！");
			barrier.await();
		}
	}
	
	@Test
	public void test() throws InterruptedException {
		GatheringDragonBalls gdb = new GatheringDragonBalls();
		String[] nameArr = {"悟空", "贝吉塔", "孙悟饭", "小林", "短笛", "天津饭", "龟仙人"};
		for (int i = 0; i < nameArr.length; i++) {
			new Thread(nameArr[i]) {
				public void run() {
					try {
						gdb.gathering();
					} catch (InterruptedException | BrokenBarrierException e) {
						e.printStackTrace();
					}
				};
			}.start();
		}
		
		Thread.sleep(20000);
//		System.out.println("许愿以后，龙珠又散落到各地去了.......");
//		for (int i = 0; i < nameArr.length; i++) {
//			new Thread(nameArr[i]) {
//				public void run() {
//					try {
//						gdb.gathering();
//					} catch (InterruptedException | BrokenBarrierException e) {
//						e.printStackTrace();
//					}
//				};
//			}.start();
//		}
//		Thread.sleep(20000);
	}
}
