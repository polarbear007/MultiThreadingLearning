package cn.itcast.countDownLatch;

import java.util.concurrent.CountDownLatch;

import org.junit.Test;

public class CountDownLatchTest {
	class EatingInstantNoodlesDemo {
		private CountDownLatch cdl = new CountDownLatch(3);
		
		public void eating() throws InterruptedException {
			System.out.println(Thread.currentThread().getName() + ": 在准备工作完成以前，暂时先睡一觉.....");
			cdl.await();
			System.out.println(Thread.currentThread().getName() + ": 开始泡泡面了，并准备开吃.......");
		}
		
		public void buyingInstantNoodles() {
			System.out.println(Thread.currentThread().getName() + ": 开始去买泡面.....");
			cdl.countDown();
			System.out.println(Thread.currentThread().getName() + ": 成功买到泡面.....");
		}
		
		public void washingDishes() {
			System.out.println(Thread.currentThread().getName() + ": 开始去洗碗.....");
			cdl.countDown();
			System.out.println(Thread.currentThread().getName() + ": 洗好碗了.....");
		}
		
		public void boildingWater() {
			System.out.println(Thread.currentThread().getName() + ": 开始去烧水.....");
			cdl.countDown();
			System.out.println(Thread.currentThread().getName() + ": 洗好水了.....");
		}
	}
	
	@Test
	public void test() throws InterruptedException {
		EatingInstantNoodlesDemo eating = new EatingInstantNoodlesDemo();
		
		Thread t1 = new Thread("老王") {
			public void run() {
				try {
					eating.eating();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			};
		};
		
		Thread t2 = new Thread("小白") {
			public void run() {
				eating.boildingWater();
			};
		};
		
		Thread t3 = new Thread("小黑") {
			public void run() {
				eating.buyingInstantNoodles();
			};
		};
		
		Thread t4 = new Thread("小明") {
			public void run() {
				eating.washingDishes();
			};
		};
		
		t1.start();
		t2.start();
		t3.start();
		t4.start();
		
		t1.join();
	}
}
