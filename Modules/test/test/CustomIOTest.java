package test;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Before;
import org.junit.Test;
import org.zzm.CustomIO;


public class CustomIOTest {

	private CustomIO cio;
	
	@Before
	public void init(){
		this.cio = new CustomIO();
	}
	
	@Test
	public void readTest() {
		String str = cio.readFile(null, "|");
		System.out.println(str);
	}
	
	@Test 
	public void writeTest(){
		String str = "affewfsafewrsadfsafs";
		cio.writeFile(str);
	}

}
