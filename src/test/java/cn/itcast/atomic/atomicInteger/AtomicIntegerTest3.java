package cn.itcast.atomic.atomicInteger;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntBinaryOperator;
import java.util.function.IntUnaryOperator;

import org.junit.Test;

public class AtomicIntegerTest3 {
	@Test
	public void test() {
		AtomicInteger i = new AtomicInteger(10);
		// 这个方法可以传入一个 IntUnaryOperator(Unary 一元的) 接口的实现类对象， 一般我们都是使用 匿名内部类
		// 或者直接使用 lambda 表达式
		// 这个接口里面有一个  applyAsInt() 方法，传入的值是AtomicInteger 内部的value 值
		//  我们在这个方法里面可以做任何计算，加减乘除，位移等等各种操作
		//  最后的返回值就是要更新的值
		int result = i.updateAndGet(new IntUnaryOperator() {
			@Override
			public int applyAsInt(int operand) {
				if(operand % 2 == 0) {
					return operand + 1;
				}else {
					return operand + 2;
				}
			}
		});
		System.out.println(result);  // 11
	}
	
	
	@Test
	public void test2() {
		AtomicInteger i = new AtomicInteger(10);
		// 这个方法支持添加两个参数，其中的一个参数是 int  类型，是要用于跟  AtomicInteger 内部的  value 计算的参数
		// 第二个参数是 IntBinaryOperator （Binary 二元的） 接口的一个实现类对象，一般我们都是写匿名内部类或者 lambda 表达式
		//  这个接口内部的 applyAsInt 方法可以接收两个参数: 
		//    left 参数就是  AtomicInteger 内部的  value 
		//    right 参数就是 本方法传入的参数
		//  我们可以在 applyAsInt 方法内部，对这两个参数进行各种形式的计算，最终返回一个 int 值就可以了
		//  这个返回的 int 值就是要更新的值
		int result = i.accumulateAndGet(20, new IntBinaryOperator() {
			@Override
			public int applyAsInt(int left, int right) {
				return left + right * 2;
			}
		});
		System.out.println(result);  // 50
	}
}
