package cn.itcast.unsafe;

import java.lang.reflect.Field;

import org.junit.Test;

import sun.misc.Unsafe;

public class TestUnsafe {
	@SuppressWarnings("restriction")
	@Test
	public void testGetUnsafe() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		// 获取到属性对象
		Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
		// 设置跳过权限检查
		theUnsafe.setAccessible(true);
		// 获取这个属性值（就是单例的 Unsafe 对象）
		Unsafe unsafe = (Unsafe)theUnsafe.get(null);
		// 打印一下 unsafe 对象
		System.out.println(unsafe);
	}
}
