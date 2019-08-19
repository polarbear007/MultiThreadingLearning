package cn.itcast.uncaughtExceptionHandler;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Date;

public class MyUncaughtExceptionHandler implements UncaughtExceptionHandler {

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		System.out.println(new Date() + " : " +  t.getName() + " : " + "抛了如下异常 ：" + e.getMessage());
	}

}
