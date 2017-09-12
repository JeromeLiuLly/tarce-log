package com.candao.trace.log;

public class Test {
	public static ThreadLocal<Integer> local = new ThreadLocal<Integer>();

	public static void main(String[] args) {
		local.set(0);
		for (int i = 0; i < 10; i++) {
			new MyThread().run();
		}
	}
}

class MyThread extends Thread {
	@Override
	public void run() {
		Integer intVal = Test.local.get();
		System.out.println(intVal);
		Test.local.set(intVal + 1);
	}
}
