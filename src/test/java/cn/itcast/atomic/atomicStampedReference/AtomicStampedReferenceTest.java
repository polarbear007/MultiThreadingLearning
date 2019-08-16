package cn.itcast.atomic.atomicStampedReference;

import java.util.concurrent.atomic.AtomicStampedReference;

import org.junit.Test;

import cn.itcast.atomic.atomicReference.Student;

public class AtomicStampedReferenceTest {
	class AtomicStudent {
		private AtomicStampedReference<Student> asr;

		public AtomicStudent(Student stu) {
			super();
			this.asr = new AtomicStampedReference<Student>(stu, 0);
		}
		
		// 模仿其他的API，使用 CAS 算法，把一个复合的操作，变成一个类似原子性操作
		public Student getAndUpdate(Student newStu) {
			Student oldStu = null;
			int oldStamp = -1;
			do {
				oldStu = asr.getReference();
				oldStamp = asr.getStamp();
			}while(!asr.compareAndSet(oldStu, newStu, oldStamp, oldStamp + 1));
			return oldStu;
		}
		
		@Override
		public String toString() {
			return asr.getReference().toString();
		}
	}
	
	@Test
	public void test() {
		AtomicStudent atomicStu = new AtomicStudent(new Student(1, "eric", 10));
		
		Student result = atomicStu.getAndUpdate(new Student(1, "1111", 100));
		System.out.println(result);
		System.out.println(atomicStu);
	}
}
