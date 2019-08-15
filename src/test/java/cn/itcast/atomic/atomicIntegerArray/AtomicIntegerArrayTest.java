package cn.itcast.atomic.atomicIntegerArray;

import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.function.IntBinaryOperator;

import org.junit.Test;

public class AtomicIntegerArrayTest {
	@Test
	public void test() {
		int[] arr = { 1, 2, 3, 4, 5, 6, 7 };
		AtomicIntegerArray atomicArr = new AtomicIntegerArray(arr);
		System.out.println(atomicArr);

		// 修改原来的数组 arr ，并不能影响到 atomicArr ，因为 AtomicIntegerArray 内部保存的是一个克隆的数组
		arr[0] = 1000;
		System.out.println(atomicArr);
	}

	// AtomicIntegerArray 跟 AtomicInteger 的区别无非就是多了一个索引， 然后自增、自减
	// 传入一元函数、传入二元函数等操作，都是一样的
	@Test
	public void test2() {
		int[] arr = { 1, 2, 3, 4, 5, 6, 7 };
		AtomicIntegerArray atomicArr = new AtomicIntegerArray(arr);
		int result = atomicArr.accumulateAndGet(0, 100, new IntBinaryOperator() {
			
			@Override
			public int applyAsInt(int left, int right) {
				return left + right * 2;
			}
		});
		// 打印一下返回值
		System.out.println(result);
		// 再看看数组第 0 索引处是不是更新为 result 了
		System.out.println(atomicArr);
	}
}
