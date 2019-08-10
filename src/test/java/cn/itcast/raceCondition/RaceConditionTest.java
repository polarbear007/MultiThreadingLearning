package cn.itcast.raceCondition;

import org.junit.Test;

public class RaceConditionTest {
	class IDGenerator implements Runnable{
		private int id;

		@Override
		public void run() {
			for (int i = 0; i < 5; i++) {
				System.out.println(Thread.currentThread().getName() + "获取 id = " + getId());
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		private int getId(){
			// 先使用 i 保存当前的 id
			final int i = this.id;
			// 更新 id
			this.id = i + 1;
			// 最后返回之前保存的 i
			return i;
		}
	}
	
	@Test
	public void test() throws InterruptedException {
		IDGenerator idGenerator = new IDGenerator();
		Thread t1 = new Thread(idGenerator, "张飞");
		Thread t2 = new Thread(idGenerator, "关羽");
		Thread t3 = new Thread(idGenerator, "刘备");
		
		t1.start();
		t2.start();
		t3.start();
		
		// 为了防止 Junit 在主线程结束后，直接强行关闭其他线程
		t1.join();
		t2.join();
		t3.join();
	}
}
