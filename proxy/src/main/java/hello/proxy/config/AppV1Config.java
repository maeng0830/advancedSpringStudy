package hello.proxy.config;

import hello.proxy.app.v1.OrderControllerV1;
import hello.proxy.app.v1.OrderControllerV1Impl;
import hello.proxy.app.v1.OrderRepositoryV1;
import hello.proxy.app.v1.OrderRepositoryV1Impl;
import hello.proxy.app.v1.OrderServiceV1;
import hello.proxy.app.v1.OrderServiceV1Impl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// for app.v1
// 인터페이스가 있는 구현 클래스 - 수동으로 빈 등록
@Configuration
public class AppV1Config {

	@Bean
	public OrderControllerV1 orderControllerV1() {
		return new OrderControllerV1Impl(orderServiceV1());
	}

	@Bean
	public OrderServiceV1 orderServiceV1() {
		return new OrderServiceV1Impl(orderRepositoryV1());
	}

	@Bean
	public OrderRepositoryV1 orderRepositoryV1() {
		return new OrderRepositoryV1Impl();
	}
}
