package cn.itcast.executor.executorCompletionService;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class ExecutorCompletionServiceTest {
	
	// 向线程池提交一个任务，并获取其计算结果
	@Test
	public void test1() throws InterruptedException, ExecutionException {
		ThreadPoolExecutor threadPool = new ThreadPoolExecutor(5, 10, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100));
		
		// 向线程池提交一个任务，并通过  Future 对象尝试获取最后的计算结果
		Future<Integer> result = threadPool.submit(new Callable<Integer>() {

			@Override
			public Integer call() throws Exception {
				return new Random().nextInt(10);
			}
		});
		
		// 获取计算结果，不过这是一个阻塞式方法，得任务执行完以后，才能返回
		System.out.println(result.get());
	}
	
	// 向线程池提交多个任务，并获取其计算结果
	@Test
	public void test2() throws InterruptedException, ExecutionException {
		ThreadPoolExecutor threadPool = new ThreadPoolExecutor(5, 10, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100));
		Future<Integer> result = null;
		long start = System.currentTimeMillis();
		for (int i = 0; i < 10; i++) {
			result = threadPool.submit(new Callable<Integer>() {

				@Override
				public Integer call() throws Exception {
					Thread.sleep(1000);
					return new Random().nextInt(10);
				}
			});
			
			// 最蠢的方式是直接在循环过程中，去调用 get() 方法获取计算结果
			// 这样，我们的这个 for 循环会在一个任务执行完成前一直被阻塞!!!! 无法再提交下一个任务
			System.out.println(result.get());
		}
		System.out.println("耗时：" + (System.currentTimeMillis() - start));
	}
	
	@Test
	public void test3() throws InterruptedException, ExecutionException {
		ThreadPoolExecutor threadPool = new ThreadPoolExecutor(5, 10, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100));
		Future<Integer> result = null;
		// 这一次，我们维护一个 list 集合来保存这个 Future 对象，这样就不会影响提交了
		ArrayList<Future<Integer>> list = new ArrayList<>();
		long start = System.currentTimeMillis();
		for (int i = 0; i < 10; i++) {
			result = threadPool.submit(new Callable<Integer>() {

				@Override
				public Integer call() throws Exception {
					Thread.sleep(1000);
					return new Random().nextInt(10);
				}
			});
			list.add(result);
		}
		
		// 再然后，我们在外部去遍历 list 集合，并获取计算结果
		// 因为我们不知道哪个任务事先已经完成，哪个任务还没有完成，所以理论上，我们执行 get() 方法可能还是会阻塞提交任务的线程
		// 当然，这里的阻塞并不会影响到任务的执行，因此总体来说已经比前面的方案好太多了
		// 不过，自己搞个 list 集合，总是很麻烦的
		for (Future<Integer> future : list) {
			System.out.println(future.get());
		}
		System.out.println("耗时：" + (System.currentTimeMillis() - start));
	}

	// 这一次，我们使用 ExecutorCompletionService 来处理批量的任务的计算结果
	// 这个类内部使用一个阻塞队列来保存所有已经执行完毕的 Future 对象，我们可以直接调用 get() 方法拿到结果
	// 也就是说，如果我们使用这个类的话，我们 通过阻塞队列拿到的  Future 对象就一定是执行好的，于是就不会有  get() 造成提交线程
	//  等待的问题
	@Test
	public void test4() throws InterruptedException, ExecutionException {
		ThreadPoolExecutor threadPool = new ThreadPoolExecutor(5, 10, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100));
		ExecutorCompletionService<Integer> completionService = new ExecutorCompletionService<>(threadPool);
		long start = System.currentTimeMillis();
		for (int i = 0; i < 10; i++) {
			// 我们直接使用 completionService 去提交任务，而实际上内部是使用 threadPool 去提交的
			completionService.submit(new Callable<Integer>() {

				@Override
				public Integer call() throws Exception {
					Thread.sleep(1000);
					return new Random().nextInt(10);
				}
			});
		}
		
		Future<Integer> result = null;
		
		// 全部提交完以后，我们可以开始来获取结果
		for (int i = 0; i < 10; i++) {
			result = completionService.take();
			// 这里的get() 方法是不会阻塞的，因为 result 对应的任务肯定是已经完成的
			System.out.println(result.get());
		}
		System.out.println("耗时：" + (System.currentTimeMillis() - start));
	}
}
