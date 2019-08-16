package cn.itcast.atomic.atomicReference;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.UnaryOperator;

import org.junit.Test;

/**
 *   测试 AtomicReference 保证的原子性的内涵到底是保证 对引用变量地址值的复合操作的原子性。
 *   还是可以保证对引用变量成员变量的复合操作的原子性。
 * @author Administrator
 *
 */

public class AtomicReferenceTest2 {
	class A extends Thread {
		private AtomicReference<Student> atomicStu;

		public A(AtomicReference<Student> atomicStu) {
			super();
			this.atomicStu = atomicStu;
		}
		
		@Override
		public void run() {
			for (int i = 0; i < 1000000; i++) {
				atomicStu.getAndUpdate(new UnaryOperator<Student>() {
					@Override
					public Student apply(Student t) {
						t.setId(1);
						t.setAge(100);
						t.setName("1111");
						return t;
					}
				});
			}
		}
	}
	
	class B extends Thread {
		private AtomicReference<Student> atomicStu;

		public B(AtomicReference<Student> atomicStu) {
			super();
			this.atomicStu = atomicStu;
		}
		
		@Override
		public void run() {
			Student stu = new Student(2, "2222", 200);
			Student stu2 = new Student(1, "1111", 100);
			for (int i = 0; i < 1000000; i++) {
				// 这里我们怕 方法返回的对象的成员变量值在获取以后可能会变，于是就来个 clone() 方法
				Student student = null;
				try {
					student = (Student)atomicStu.updateAndGet(new UnaryOperator<Student>() {
						@Override
						public Student apply(Student t) {
							t.setId(2);
							t.setAge(200);
							t.setName("2222");
							return t;
						}
					}).clone();
				} catch (CloneNotSupportedException e1) {
					e1.printStackTrace();
				}
				
				// 我们这里想看下， student 对象的成员变量值，是否会存在   中间状态
				// 如果存在中间状态的话，那么说明 AtomicReference 只能保证   对引用变量的地址值的复合操作是原子性的
				if(!stu.equals(student) && !stu2.equals(student)) {
					System.out.println(student);
				}
			}
		}
	}
	
	@Test
	public void test() throws InterruptedException {
		AtomicReference<Student> atomicStu = new AtomicReference<>(new Student(0, "eric", 0));
		A a = new A(atomicStu);
		B b = new B(atomicStu);
		
		a.start();
		b.start();
		
		a.join();
		b.join();
		
		System.out.println(atomicStu);
	}
}
