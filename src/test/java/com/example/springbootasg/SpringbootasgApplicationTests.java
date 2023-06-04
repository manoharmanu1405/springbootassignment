package com.example.springbootasg;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;



@SpringBootTest
class SpringbootasgApplicationTests {

	@Test
	void testMainClassLoading() {

		boolean isLoaded = false;


		try {
			SpringbootApplication.main(new String[]{});
			isLoaded = true;
		} catch (Exception e) {

			e.printStackTrace();
		}


		Assertions.assertTrue(isLoaded, "Main class should load without any exceptions");
	}

}
