package com.database;

import java.util.LinkedList;

public class Test {

	private LinkedList<String> list;
	
	public Test() {
		list = new LinkedList<String>();
		
		list.add("meow");
		list.add("Woof");
		
		System.out.println(list.get(0));
		list.remove(0);
		System.out.println(list.get(0));
	}
	
	public static void main(String[] args) {
		new Test();
	}

}
