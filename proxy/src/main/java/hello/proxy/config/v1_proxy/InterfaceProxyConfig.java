package hello.proxy.config.v1_proxy;

import hello.proxy.app.v1.OrderControllerV1;
import hello.proxy.app.v1.OrderControllerV1Impl;
import hello.proxy.app.v1.OrderRepositoryV1;
import hello.proxy.app.v1.OrderRepositoryV1Impl;
import hello.proxy.app.v1.OrderServiceV1;
import hello.proxy.app.v1.OrderServiceV1Impl;
import hello.proxy.config.v1_proxy.interface_proxy.OrderControllerInterfaceProxy;
import hello.proxy.config.v1_proxy.interface_proxy.OrderRepositoryInterfaceProxy;
import hello.proxy.config.v1_proxy.interface_proxy.OrderServiceInterfaceProxy;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// 인터페이스가 있는 구현 클래스로 프록시 구현: 프록시와 서버는 동일한 인터페이스를 구현한다.
// 서버가 아닌 프록시를 빈으로 등록한다.
// 서버를 빈으로 등록하지 않는다고 해서, 서버를 사용하지 않는 것이 아니다. 프록시가 서버를 의존하고 있다.
@Configuration
public class InterfaceProxyConfig {

	// 컨트롤러 프록시를 빈으로 등록한다.
	// 컨트롤러 프록시는 컨트롤러 서버를 의존한다.
	// 컨트롤러 서버는 서비스 프록시를 의존한다.
	@Bean
	public OrderControllerV1 orderController(LogTrace logTrace) {
		OrderControllerV1Impl controllerImpl = new OrderControllerV1Impl(orderService(logTrace));
		return new OrderControllerInterfaceProxy(controllerImpl, logTrace);
	}

	// 서비스 프록시를 빈으로 등록한다.
	// 서비스 프록시는 서비스 서버를 의존한다.
	// 서비스 서버는 리포지토리 프록시를 의존한다.
	@Bean
	public OrderServiceV1 orderService(LogTrace logTrace) {
		OrderServiceV1Impl serviceImpl = new OrderServiceV1Impl(orderRepository(logTrace));
		return new OrderServiceInterfaceProxy(serviceImpl, logTrace);
	}

	// 리포지토리 프록시를 빈으로 등록한다.
	// 리포지토리 프록시는 리포지토리 서버를 의존한다.
	@Bean
	public OrderRepositoryV1 orderRepository(LogTrace logTrace) {
		OrderRepositoryV1Impl repositoryImpl = new OrderRepositoryV1Impl();
		return new OrderRepositoryInterfaceProxy(repositoryImpl, logTrace);
	}
}
