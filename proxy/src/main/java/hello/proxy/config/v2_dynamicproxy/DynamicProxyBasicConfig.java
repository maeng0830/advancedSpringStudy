package hello.proxy.config.v2_dynamicproxy;

import hello.proxy.app.v1.OrderControllerV1;
import hello.proxy.app.v1.OrderControllerV1Impl;
import hello.proxy.app.v1.OrderRepositoryV1;
import hello.proxy.app.v1.OrderRepositoryV1Impl;
import hello.proxy.app.v1.OrderServiceV1;
import hello.proxy.app.v1.OrderServiceV1Impl;
import hello.proxy.config.v2_dynamicproxy.handler.LogTraceBasicHandler;
import hello.proxy.trace.logtrace.LogTrace;
import java.lang.reflect.Proxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// for app.v1
// 인터페이스가 있는 구현 클래스 - jdk 동적 프록시(동적 프록시는 인터페이스 기반으로 생성된다)
@Configuration
public class DynamicProxyBasicConfig {

	@Bean
	public OrderControllerV1 orderControllerV1(LogTrace logTrace) {
		// 서버 객체(동적 프록시의 호출 대상)
		OrderControllerV1Impl orderController = new OrderControllerV1Impl(orderServiceV1(logTrace));

		// 동적 프록시
		OrderControllerV1 proxy = (OrderControllerV1) Proxy.newProxyInstance(
				OrderControllerV1.class.getClassLoader(), new Class[]{OrderControllerV1.class},
				new LogTraceBasicHandler(orderController, logTrace));

		return proxy;
	}

	// 동적 프록시 객체를 빈으로 등록
	@Bean
	public OrderServiceV1 orderServiceV1(LogTrace logTrace) {
		// 서버 객체(동적 프록시의 호출 대상)
		OrderServiceV1Impl orderService = new OrderServiceV1Impl(orderRepositoryV1(logTrace));

		// 동적 프록시
		OrderServiceV1 proxy = (OrderServiceV1) Proxy.newProxyInstance(
				OrderServiceV1.class.getClassLoader(), new Class[]{OrderServiceV1.class},
				new LogTraceBasicHandler(orderService, logTrace));

		return proxy;
	}

	// 동적 프록시 객체를 빈으로 등록
	@Bean
	public OrderRepositoryV1 orderRepositoryV1(LogTrace logTrace) {
		// 서버 객체(동적 프록시의 호출 대상)
		OrderRepositoryV1Impl orderRepository = new OrderRepositoryV1Impl();

		// 동적 프록시
		OrderRepositoryV1 proxy = (OrderRepositoryV1) Proxy.newProxyInstance(OrderRepositoryV1.class.getClassLoader(),
				new Class[]{OrderRepositoryV1.class},
				new LogTraceBasicHandler(orderRepository, logTrace));

		return proxy;
	}
}
