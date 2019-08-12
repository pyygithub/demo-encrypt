## 一、前言
现代互联网充斥着各种攻击、病毒、钓鱼、欺诈等手段，层出不穷。在目前安全形势越来越严重的形势下，我们项目的安全是必须要谨慎对待的问题。项目中的一个安全漏洞处理不好就可能给公司或个人带来严重的损失。

“配置属性加密”的应用场景：假设如果攻击者通过某些手段拿到部分敏感代码或配置，甚至是全部源代码和配置时，保障我们的基础设施账号依然不被泄漏。当然手段多种多种多样，比如以某台中毒的内网机器为肉机，对其他电脑进行ARP攻击抓去通信数据进行分析，或者获取某个账号直接拿到源代码或者配置，等等诸如此类。

## 二、思路
- 采用比较安全的对称加密算法；
- 对基础设施账号密码等敏感信息进行加密；
- 开发环境可以将密钥放置在代码中，生产环境放在在构建脚本或者启动脚本中；
- 如果自动化部署可以有专门的程序来管理这些密钥

## 三、技术架构
- Jasypt是一个优秀的加密库，支持密码、Digest认证、文本、对象加密，此外密码加密复合RFC2307标准。[http://www.jasypt.org/download.html](http://www.jasypt.org/download.html)
- ulisesbocchio/jasypt-spring-boot，集成Spring Boot，在程序引导时对属性进行解密。[https://github.com/ulisesbocchio/jasypt-spring-boot](https://github.com/ulisesbocchio/jasypt-spring-boot)

## 四、具体实现
### 4.1 添加maven
```
		<dependency>
			<groupId>com.github.ulisesbocchio</groupId>
			<artifactId>jasypt-spring-boot-starter</artifactId>
			<version>2.0.0</version>
		</dependency>
```
这里我们采用 springboot 工程，具体版本对应关系可以查看 [jasypt-spring-boot-starter](https://mvnrepository.com/artifact/com.github.ulisesbocchio/jasypt-spring-boot-starter/2.1.1)
### 4.2 生产要加密的字符串
```
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
```
加密结果：
```
username:BxnHqCf2GqFI+CxmbZXyVQ==
password:ZSSsINSR6bJ0vBIdTX47PA==
```
### 4.3 将加密后的密文添加到 application.yml 中，替换原有的明文
```
server:
  port: 8080
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/testdb?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&useSSL=true
    username: ENC(BxnHqCf2GqFI+CxmbZXyVQ==)
    password: ENC(ZSSsINSR6bJ0vBIdTX47PA==)
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
密方式PBEWithMD5AndDES,可以更改为PBEWithMD5AndTripleDES
```
注意：这里的密文需要写在`ENC(密文)`，我们也可以通过手动配置前缀后缀来修改默认配置：
```
jasypt:
  encryptor:
    password: 1Qaz0oKm1s2f37ef61#密钥
    property:
      prefix: ENC[
      suffix: ]
```

### 4.4 配置加密密钥
1. 开发环境直接将密钥写在在配置文件中：
```
jasypt:
  encryptor:
    password: 1Qaz0oKm1s2f37ef61 #密钥
```
2. 生产环境为了防止密钥泄露，反解除密码。可以在项目部署的时候使用命令传入秘密值
```
java -jar -Djasypt.encryptor.password=1Qaz0oKm1s2f37ef61 xxx.jar
```
3. 也可以在服务器的环境变量里配置，进一步提高安全性
```
打开 /etc/profile 文件
# vim /etc/profile

文件末尾插入
export JASYPT_PASSWORD = 1Qaz0oKm1s2f37ef61

编译
#source /etc/profile

运行
java -jar -Djasypt.encryptor.password=${JASYPT_PASSWORD} xxx.jar
```

### 4.5 修改加密方式

```
jasypt:
  encryptor:
    #algorithm: PBEWithMD5AndDES   # 默认加密方式PBEWithMD5AndDES,可以更改为PBEWithMD5AndTripleDES
```

加密方式对应的类为`BasicTextEncryptor`和`StrongTextEncryptor`
```
private final StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();

    public BasicTextEncryptor() {
        this.encryptor.setAlgorithm("PBEWithMD5AndDES");
    }
```
```
private final StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();

    public StrongTextEncryptor() {
        this.encryptor.setAlgorithm("PBEWithMD5AndTripleDES");
    }
```
![](https://upload-images.jianshu.io/upload_images/11464886-500528502b10ae3d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

实例代码：[https://github.com/pyygithub/demo-encrypt](https://github.com/pyygithub/demo-encrypt)


官方地址: [https://github.com/ulisesbocchio/jasypt-spring-boot](https://github.com/ulisesbocchio/jasypt-spring-boot "官方地址：https://github.com/ulisesbocchio/jasypt-spring-boot")

