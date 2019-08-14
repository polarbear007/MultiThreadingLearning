package cn.itcast.volatileTest;

public class SovleTheVisibilityProblem {
	static class TaskRunnable implements Runnable{
		// 这里我们只需要把共享变量 toCancel 变成 volatile 修饰的就可以保证可见性了
		private volatile boolean toCancel = false;

		@Override
		public void run() {
			int i = 0;
			while(!toCancel) {
				i++;
				// 我们在循环内部加上 sleep 方法，不要让这个线程的处理器一直处于高速的运算中，
				// 这样这个处理器就有时间去同步 toCancel 这个共享变量的最新值了
//				try {
//					Thread.sleep(100);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
			}
			System.out.println(i);
		}

		public void setToCancel(boolean toCancel) {
			this.toCancel = toCancel;
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		TaskRunnable taskRunnable = new TaskRunnable();
		Thread taskThread = new Thread(taskRunnable);
		taskThread.start();
		
		// 如果过了5 秒以后，还没有结束的话，我们想通过 把 toCancel 变量的值改成 true 让任务线程停止执行
		Thread.sleep(5000);
		taskRunnable.setToCancel(true);
	}	
	
}
