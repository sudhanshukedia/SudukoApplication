package com.example.sudukoApplication;

import com.example.sudukoApplication.demo.SudukoApp;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.springframework.boot.SpringApplication.run;

@SpringBootApplication
public class SudukoApplication {

	public static void main(String[] args)
	{
		run(SudukoApplication.class, args);
		SudukoApp sudukoApp = new SudukoApp();
		sudukoApp.sudokuMethods();


	}


}
