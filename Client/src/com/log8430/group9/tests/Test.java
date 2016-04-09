package com.log8430.group9.tests;


import org.junit.Before;

import com.log8430.group9.views.FileNode;
import com.log8430.group9.views.LazyLoader;

import junit.framework.TestCase;

public class Test{
	private LazyLoader lazyLoader;
	
	@Before
	protected void setUp() throws Exception {
		lazyLoader = new LazyLoader(){
			public static FileNode load(String id, String api) {
				
			}
		};
	}

}
