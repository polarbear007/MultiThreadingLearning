package cn.itcast.failure;import org.junit.Test;

/**
 *  这个类是模仿前面那个类来实现的
 * 	原来多线程的逻辑并不一定都要写在  Thread 的子类中，但是初学者的我看着有点不习惯，尝试改成眼熟的版本
 * @author Administrator
 *
 */
public class LiveLock2 {
	class Apple {
		private String owner;

		public String getOwner() {
			return owner;
		}

		public void setOwner(String owner) {
			System.out.println(this.owner + " gives the apple to " + owner);
			this.owner = owner;
		}
		
		public void eatApple() {
			System.out.println(owner + " eats the apple!");
		}
	}
	
	class A extends Thread{
		private Apple sharedApple;
		private boolean isHungry = true;
		private A partner;
		private String name;
		
		public A(Apple sharedApple, String name) {
			super();
			this.sharedApple = sharedApple;
			this.name = name;
		}

		public void setPartner(A partner) {
			this.partner = partner;
		}

		@Override
		public void run() {
			while(isHungry) {
				synchronized (sharedApple) {
					// 如果本线程没有获得了苹果，那么我们就等待
					if(!name.equals(sharedApple.getOwner())) {
						try {
							sharedApple.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}else {
						// 如果本线程获得了苹果，那么我们先看看小伙伴是不是挨饿呢
						if(partner.isHungry) {
							// 我们把苹果让给小伙伴
							sharedApple.setOwner(partner.name);
							// 小伙伴可能不知道我们把苹果让给他了，我们需要 notify() 一下
							sharedApple.notify();
						}else {
							// 如果小伙伴不饿的话，那么我们就直接吃掉了
							sharedApple.eatApple();
							// 吃完以后，我们就把状态改成false
							isHungry = false;
						}
					}
				}
				// 加点小延迟
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@Test
	public void  testLiveLock() throws InterruptedException {
		// 创建一个共享的苹果， 随便设置拥有者是小明
		Apple apple = new Apple();
		apple.setOwner("小明");
		// 创建两个线程，分别对象小明和小黑
		A a1 = new A(apple, "小明");
		A a2 = new A(apple, "小黑");
		// 设置小明和小黑为对方的小伙伴
		a1.setPartner(a2);
		a2.setPartner(a1);
		
		a1.start();
		a2.start();
		
		// 演示个 20 秒，如果还没有人吃到苹果，我们就退出主线程，Junit 会关闭所有其他的线程
		Thread.sleep(20000);
	}
}
