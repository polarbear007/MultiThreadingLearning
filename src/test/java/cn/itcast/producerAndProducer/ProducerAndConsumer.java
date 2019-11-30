package cn.itcast.producerAndProducer;

import java.util.concurrent.ArrayBlockingQueue;

import org.junit.Test;

public class ProducerAndProducer {
	class Producer extends Thread{
		private ArrayBlockingQueue<Integer> queue;
		
		public Producer(ArrayBlockingQueue<Integer> queue) {
			super();
			this.queue = queue;
		}

		@Override
		public void run() {
			int i = 0;
			while(true) {
				try {
					i++;
					queue.put(i);
					System.out.println("生产了：" + i);
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	class Consumer extends Thread{
		private ArrayBlockingQueue<Integer> queue;

		public Consumer(ArrayBlockingQueue<Integer> queue) {
			super();
			this.queue = queue;
		}
		
		@Override
		public void run() {
			while(true) {
				try {
					System.out.println("消费了：" + queue.take());
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	@Test
	public void test() throws InterruptedException {
		ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(10);
		Producer producer = new Producer(queue);
		Consumer consumer = new Consumer(queue);
		
		producer.start();
		consumer.start();
		
		producer.join();
	}
}
