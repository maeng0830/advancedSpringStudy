package hello.proxy.config.v1_proxy;

import hello.proxy.app.v2.OrderControllerV2;
import hello.proxy.app.v2.OrderRepositoryV2;
import hello.proxy.app.v2.OrderServiceV2;
import hello.proxy.config.v1_proxy.concrete_proxy.OrderControllerConcreteProxy;
import hello.proxy.config.v1_proxy.concrete_proxy.OrderRepositoryConcreteProxy;
import hello.proxy.config.v1_proxy.concrete_proxy.OrderServiceConcreteProxy;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// 인터페이스가 없는 구체 클래스로 프록시 구현: 프록시는 서버를 상속한다.
// 서버가 아닌 프록시를 빈으로 등록한다.
// 서버를 빈으로 등록하지 않는다고 해서, 서버를 사용하지 않는 것이 아니다. 프록시가 서버를 의존하고 있다.
@Configuration
public class ConcreteProxyConfig {

	// 컨트롤러 프록시를 빈으로 등록한다.
	// 컨트롤러 프록시는 컨트롤러 서버를 의존한다.
	// 컨트롤러 서버는 서비스 프록시를 의존한다.
	@Bean
	public OrderControllerV2 orderControllerV2(LogTrace logTrace) {
		OrderControllerV2 controllerImpl = new OrderControllerV2(orderServiceV2(logTrace));
		return new OrderControllerConcreteProxy(controllerImpl, logTrace);
	}

	// 서비스 프록시를 빈으로 등록한다.
	// 서비스 프록시는 서비스 서버를 의존한다.
	// 서비스 서버는 리포지토리 프록시를 의존한다.
	@Bean
	public OrderServiceV2 orderServiceV2(LogTrace logTrace) {
		OrderServiceV2 serviceImpl = new OrderServiceV2(orderRepositoryV2(logTrace));
		return new OrderServiceConcreteProxy(serviceImpl, logTrace);
	}

	// 리포지토리 프록시를 빈으로 등록한다.
	// 리포지토리 프록시는 리포지토리 서버를 의존한다.
	@Bean
	public OrderRepositoryV2 orderRepositoryV2(LogTrace logTrace) {
		OrderRepositoryV2 repositoryImpl = new OrderRepositoryV2();
		return new OrderRepositoryConcreteProxy(repositoryImpl, logTrace);
	}
}
