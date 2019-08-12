package com.thtf;

import org.jasypt.util.text.BasicTextEncryptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoEncryptApplicationTests {

	@Test
	public void contextLoads() {
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		//密钥
		textEncryptor.setPassword("1Qaz0oKm1s2f37ef61");
		//要加密的数据（数据库的用户名或密码）
		String username = textEncryptor.encrypt("root");
		String password = textEncryptor.encrypt("123456");
		System.out.println("username:"+username);
		System.out.println("password:"+password);
	}

}
