package hello.proxy.cglib;

import hello.proxy.cglib.code.TimeMethodInterceptor;
import hello.proxy.common.service.ConcreteService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.proxy.Enhancer;

@Slf4j
public class CglibTest {

	// 인터페이스 없는 구체 클래스로 CGLIB 프록시 생성
	// CGLIB 프록시는 구체 클래스를 상속 받아 생성된다.
	// 상속으로 인한 단점을 그대로 가진다.
	@Test
	void cglib() {
		ConcreteService target = new ConcreteService();

		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(ConcreteService.class); // CGLIB 프록시의 상위 클래스
		enhancer.setCallback(new TimeMethodInterceptor(target)); // CGLIB 프록시의 실행 로직
		ConcreteService proxy = (ConcreteService) enhancer.create(); // CGLIB 프록시 생성
		log.info("targetClass={}", target.getClass());
		// targetClass=class hello.proxy.common.service.ConcreteService
		log.info("proxyClass={}", proxy.getClass());
		// proxyClass=class hello.proxy.common.service.ConcreteService$$EnhancerByCGLIB$$25d6b0e3

		proxy.call();
		// TimeProxy 실행
		// ConcreteService 호출
		// TimeProxy 종료  resultTime=7
	}
}
