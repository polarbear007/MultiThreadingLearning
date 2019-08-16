package cn.itcast.atomic.atomicReference;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

public class AtomicReferenceTest {
	@Test
	public void test() {
		Student stu = new Student(1, "eric", 10);
		AtomicReference<Student> atomicStu = new AtomicReference<>(stu);
		System.out.println(atomicStu);
		// 在外部通过 stu 修改成员变量值，看是否会影响到原子对象
		stu.setName("jack");
		// 会影响原子变量，因为构造方法是直接传的地址值
		System.out.println(atomicStu);
	}
	
	@Test
	public void test2() {
		Student stu = new Student(1, "eric", 10);
		AtomicReference<Student> atomicStu = new AtomicReference<>(stu);
		
		Student stu2 = new Student(1, "eric", 10);
		// 【注意】 compareAndSet 方法比较的是地址值！！！ 
		//        就算 stu2 的成员变量值跟 stu1 完全一样， 如果期望值不是 stu1 ，这个方法总是返回 false 
		//        相反， 就算其他线程修改了成员变量值，但是地址值不变，这个方法还是会返回 true
		System.out.println(atomicStu.compareAndSet(stu2, stu2));
		
		System.out.println(atomicStu);
	}
}
