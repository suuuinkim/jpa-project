package jpabook.jpashop;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JpashopApplication {

	public static void main(String[] args) {

		Hello hello = new Hello();
		hello.setData("hello");
		String data = hello.getData();

		// System.out.println(data);
		SpringApplication.run(JpashopApplication.class, args);
	}

	/**
	 * 사용지양
	 */
	@Bean
	Hibernate5Module hibernate5Module(){
		Hibernate5Module hibernate5Module = new Hibernate5Module();
		// hibernate5Module.configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING, true); // 강제 lazy 로딩
		return hibernate5Module;
	}

}
