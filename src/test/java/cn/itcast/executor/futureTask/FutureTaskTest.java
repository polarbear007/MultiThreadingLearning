package cn.itcast.executor.futureTask;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import org.junit.Test;

/**
 * 	演示一下 FutureTask 的基本使用
 * @author Administrator
 *
 */
public class FutureTaskTest {

	@Test
	public void test1() throws InterruptedException, ExecutionException{
		// FutureTask 可以把一个 callable 对象封装成一个 runnable 对象
		// 然后，我们再把这个 futureTask 对象当成一个runnable 对象，放到 Thread 对象中去执行
		FutureTask<Integer> task = new FutureTask<>(new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				int sum = 0;
				for (int i = 1; i <= 100; i++) {
					sum += i;
				}
				return sum;
			}
		});
		
		Thread t1 = new Thread(task);
		t1.start();
		System.out.println("计算的结果： " + task.get());
	}
	
	@Test
	public void test2() throws InterruptedException, ExecutionException {
		FutureTask<Integer> task = new FutureTask<>(new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {
				int sum = 0;
				for (int i = 1; i <= 100; i++) {
					Thread.sleep(50);
					sum += i;
				}
				return sum;
			}
		});
		
		Thread t1 = new Thread(task);
		t1.start();
		Thread.sleep(200);
		// 可以提前结束任务的执行
		task.cancel(true);
		System.out.println("计算的结果： " + task.get());
	}
}
