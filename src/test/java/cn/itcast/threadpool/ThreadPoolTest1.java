package cn.itcast.threadpool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

public class ThreadPoolTest1 {
	// 如何创建一个线程池对象?
	// 最简单的构造方法： public ThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,BlockingQueue<Runnable> workQueue)
	@Test
	public void test1() {
		// 核心线程数5 ， 最大线程数 10， 非核心线程数如果空闲超过 30 秒自动销毁
		// 缓存任务队列为 ArrayBlockingQueue， 这个队列最多可以缓存 10 个任务， 
		//  当这个队列满了，再添加新任务，就会创建非核心线程
		//  当总的线程数满 10 个了，再添加新任务，就会执行默认的处理策略------ 扔异常
		ThreadPoolExecutor pool = new ThreadPoolExecutor(5, 10, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10));
	
		// 我们知道 缓存队列缓存 10个， 最大线程数 10 个，所以理论上线程池最多可以保证接收 20 个任务不报错
		// 再多一些，可能不会报错，因为线程池里面的线程可以重复利用，但是如果多太多的话（比如100），就一定会报异常
		Runnable task = null;
		for (int i = 0; i < 20; i++) {
			 task = new Runnable() {
				public void run() {
					System.out.println("hi~");
				}
			};
			pool.execute(task);
		}
	}
	
	// 比上一个构造方法多指定一个 拒绝策略， 一般我们就用现成的那四个拒绝策略就好了
	//  当然，如果有特殊的需求，我们也自己自己去实现接口
	// ThreadPoolExecutor 已经帮我们实现了四个拒绝策略，我们就直接使用就好了：
	//    ThreadPoolExecutor.AbortPolicy      默认策略，丢异常
	//    ThreadPoolExecutor.DiscardPolicy    直接丢弃新任务，不报异常
	//    ThreadPoolExecutor.DiscardOldestPolicy   丢弃缓存队列中最老的任务，然后保存新任务
	//    ThreadPoolExecutor.CallerRunsPolicy      使用主线程去执行新任务
	
	// public ThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler)
	@Test
	public void test2() {
		ThreadPoolExecutor pool = new ThreadPoolExecutor(5, 10, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(10) ,new ThreadPoolExecutor.DiscardPolicy());
		Runnable task = null;
		// 因为我们是使用丢弃策略，所以就算添加 100 个任务 ，也不会报错，但是实际上执行多少个任务就不知道了
		for (int i = 0; i < 100; i++) {
			 task = new Runnable() {
				public void run() {
					System.out.println("hi~");
				}
			};
			pool.execute(task);
		}
	}
	
	// 相比上一个构造方法又多出一个 ThreadFactory 接口参数， 我们这个参数是用来创建 线程对象用的
	//   我们之前没有指定的时候，使用的是 Executors.DefaultThreadFactory  
	// 默认的线程工厂对象会给每个线程指定同一个线程组，设置优先级都为 5 ， 非守护线程， 并指定一个固定前缀的线程名
	
	// 如果我们使用使用这个构造方法，那么就需要自己去实现一个工厂对象，这个工厂对象应该像默认的线程工厂对象那么
	//  给每个线程都指定一个固定前缀的线程名，方便调试。
	//  其他的就随意发挥了，比如说，你想要给这些线程都设置一个 异常处理 机制之类的。
	
	// public ThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
	//       BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) 
	@Test
	public void test3() {
		ThreadFactory factory = new ThreadFactory() {
			private AtomicInteger threadNum = new AtomicInteger(0);
			@Override
			public Thread newThread(Runnable r) {
				Thread t = new Thread(r);
				t.setName("pool-thread-" + threadNum.getAndIncrement());
				t.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
					@Override
					public void uncaughtException(Thread t, Throwable e) {
						System.out.println(System.currentTimeMillis() + " : " + Thread.currentThread().getName() + " : " + e.getMessage());
					}
				});
				return t;
			}
		};
		
		ThreadPoolExecutor pool = new ThreadPoolExecutor(5, 10, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(10), factory, new ThreadPoolExecutor.DiscardPolicy());
		Runnable task = null;
		for (int i = 0; i < 100; i++) {
			 if(i == 5) {
				 task = new Runnable() {
					public void run() {
						System.out.println(10 / 0);
					}
				 };
			 }else {
				 task = new Runnable() {
					public void run() {
						System.out.println("hi~");
					}
				 };
			 }
			pool.execute(task);
		}
	}
	
	
	//  获取线程池的一些基本信息
	@Test
	public void test4() {
		ThreadPoolExecutor pool = new ThreadPoolExecutor(5, 10, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10));
		// 获取 corePoolSize 参数值
		System.out.println("corePoolSize: " + pool.getCorePoolSize());
		// 获取 MaximumPoolSize 参数值
		System.out.println("MaximumPoolSize: " + pool.getMaximumPoolSize());
		// 获取 KeepAliveTime 参数值
		System.out.println("KeepAliveTime: " + pool.getKeepAliveTime(TimeUnit.SECONDS));
		// 获取已经完成的任务数量
		System.out.println("已经完成的任务数: " + pool.getCompletedTaskCount());
		// 获取当前活跃的线程（处于 runnable 状态的线程数量）
		System.out.println("当前活跃的线程数: " + pool.getActiveCount());
		// 获取线程池中线程数的历史最大值（现在不一定是这么多）
		System.out.println("线程池中线程数的历史最大值: " + pool.getLargestPoolSize());
		// 获取线程池缓存的任务队列
		System.out.println("线程池缓存的任务队列: " + pool.getQueue());
	}
	
	// 动态修改线程池的一些参数
	@Test
	public void test5() {
		ThreadPoolExecutor pool = new ThreadPoolExecutor(5, 10, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10));
		pool.setCorePoolSize(20);
		pool.setKeepAliveTime(20, TimeUnit.SECONDS);
		pool.setMaximumPoolSize(20);
		pool.setThreadFactory(new ThreadFactory() {
			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r);
			}
		});
		pool.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
	}
	
	@Test
	public void test6() {
		ThreadPoolExecutor pool = new ThreadPoolExecutor(5, 10, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10));
		// 预先开启全部的核心线程（5个）
		pool.prestartAllCoreThreads();
		
		
	}
}
