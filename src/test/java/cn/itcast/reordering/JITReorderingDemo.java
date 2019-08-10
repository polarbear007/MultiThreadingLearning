package cn.itcast.reordering;

public class JITReorderingDemo {
    static class Student {
    	private static Student stu;
    	private static boolean initialized = false;
    	private Student() {}
    	public static Student getStudent() {
    		if(!initialized) {
    			// 计算机在执行时候，可能会进行指令重排，先执行  initialized = true 语句。
    			// 这种操作在单线程环境下，并不会对最终的结果造成什么影响。但是在多线程环境下，可能会
    			// 让其他的线程直接返回 null 值。
    			stu = new Student();
    			initialized = true;
    		}
    		return stu;
    	}
    }
    
   static class MyThread extends Thread{
    	@Override
    	public void run() {
    		Student stu = Student.getStudent();
    		if(stu == null) {
    			System.out.println("获取到一个 null 值");
    		}
    	}
    }
    
   // 说明： 指令重排发生的机率并不是很高，所以你可能测试半天都没有效果
    public static void main(String[] args) {
		MyThread t1 = new MyThread();
		MyThread t2 = new MyThread();
		MyThread t3 = new MyThread();
		
		t1.start();
		t2.start();
		t3.start();
	}
}
