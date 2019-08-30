package cn.itcast.executor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;

import org.junit.Test;

public class ExecutorTest {
	@Test
	public void test1() {
		Executor executor = new Executor() {
			@Override
			public void execute(Runnable command) {
				new Thread(command).start();
			}
		};
		
		executor.execute(new Runnable() {
			@Override
			public void run() {
				System.out.println(Thread.currentThread().getName());
			}
		});
	}
	
	@Test
	public void test2() {
		Executor executor = new Executor() {
			@Override
			public void execute(Runnable command) {
				// 这里我们直接调用 run 方法，也就是使用本线程去执行
				command.run();
			}
		};
		
		executor.execute(new Runnable() {
			@Override
			public void run() {
				System.out.println(Thread.currentThread().getName());
			}
		});
	}
	
	class MyExecutor implements Executor{
		private ArrayBlockingQueue<Runnable> taskQueue = new ArrayBlockingQueue<>(20);
		
		public MyExecutor() {
			new Thread("worker-thread") {
				public void run() {
					Runnable task = null;
					while(true) {
						try {
							task = taskQueue.take();
							task.run();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				};
			}.start();
		}
		
		@Override
		public void execute(Runnable command) {
			try {
				taskQueue.put(command);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	@Test
	public void test3() {
		MyExecutor executor = new MyExecutor();
		executor.execute(new Runnable() {
			@Override
			public void run() {
				System.out.println(Thread.currentThread().getName() + "执行了任务1");
			}
		});
		executor.execute(new Runnable() {
			@Override
			public void run() {
				System.out.println(Thread.currentThread().getName() + "执行了任务2");
			}
		});
	}
}
