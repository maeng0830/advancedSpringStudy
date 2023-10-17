package hello.proxy.postprocessor;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class BeanPostProcessorTest {

	@Test
	void basicConfig() {
		// 스프링 컨테이너
		// BeanPostProcessorConfig를 토대로 스프링 컨테이너에 스프링 빈을 등록한다.
		ApplicationContext applicationContext = new AnnotationConfigApplicationContext(
				BeanPostProcessorConfig.class);

		// beanA의 객체가 A에서 B로 변경되어 빈으로 등록된다.
		B b = applicationContext.getBean("beanA", B.class);
		b.helloB(); // B - hello B

		// A는 빈으로 등록되지 않는다.
		Assertions.assertThatThrownBy(() -> applicationContext.getBean(A.class))
				.isInstanceOf(NoSuchBeanDefinitionException.class);
	}

	@Slf4j
	@Configuration
	static class BeanPostProcessorConfig {

		// A 객체를 생성한다.
		// 빈후처리기가 있으므로, 빈 이름과 A 객체를 빈후처리기에 전달한다.
		@Bean(name = "beanA")
		public A a() {
			return new A();
		}

		// 빈후처리기는 A 객체를 전달받는다.
		// 빈후처리기 로직에 따라 A 객체를 B 객체로 변경한다.
		// 빈 이름이 beanA인 B 객체를 빈으로 등록한다.
		@Bean
		public AToBPostProcessor helloPostProcessor() {
			return new AToBPostProcessor();
		}
	}

	@Slf4j
	static class A {

		public void helloA() {
			log.info("hello A");
		}
	}

	@Slf4j
	static class B {

		public void helloB() {
			log.info("hello B");
		}
	}

	// 빈후처리기
	// BeanPostProcessor의 구현체로 구현한다.
	@Slf4j
	static class AToBPostProcessor implements BeanPostProcessor {

		@Override
		public Object postProcessAfterInitialization(Object bean, String beanName)
				throws BeansException {
			log.info("beanName={} bean={}", beanName, bean);

			if (bean instanceof A) {
				return new B();
			}

			return bean;
		}
	}
}
