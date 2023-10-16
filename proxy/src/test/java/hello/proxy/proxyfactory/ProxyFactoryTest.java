package hello.proxy.proxyfactory;

import static org.assertj.core.api.Assertions.assertThat;

import hello.proxy.common.advice.TimeAdvice;
import hello.proxy.common.service.ConcreteService;
import hello.proxy.common.service.ServiceImpl;
import hello.proxy.common.service.ServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;

@Slf4j
public class ProxyFactoryTest {

	@DisplayName("인터페이스가 있으면, JDK 동적 프록시를 통해 인터페이스 기반 프록시를 생성한다.")
	@Test
	void interfaceProxy() {
		// 인터페이스가 있는 구현 클래스
		ServiceImpl target = new ServiceImpl();

		// 프록시 팩토리 생성
		// 프록시팩토리에는 프록시가 호출할 target, target이 구현한 인터페이스 정보 등이 포함된다.
		// target이 인터페이스가 있는 구현 클래스라면 JDK 동적 프록시를 통해 인터페이스 기반 프록시를 생성한다.
		// target이 인터페이스가 없는 구체 클래스라면 CGLIB를 통해 클래스 기반 프록시를 생성한다.
		ProxyFactory proxyFactory = new ProxyFactory(target);

		// 프록시팩토리로 생성될 동적 프록시가 실행할 로직(=Advice)를 추가
		proxyFactory.addAdvice(new TimeAdvice());

		// 동적 프록시 생성
		ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();

		log.info("targetClass={}", target.getClass());
		// targetClass=class hello.proxy.common.service.ServiceImpl
		log.info("proxyClass={}", proxy.getClass());
		// proxyClass=class com.sun.proxy.$Proxy13

		proxy.save();
		// TimeAdvice - TimeProxy 실행
		// ServiceImpl - save 호출
		// TimeAdvice - TimeProxy 종료  resultTime=0

		// 프록시팩토리로 생성된 동적 프록시인지 확인한다.
		assertThat(AopUtils.isAopProxy(proxy)).isTrue();
		// 프록시팩토리로 생성된 동적 프록시이고, JDK 동적 프록시인지 확인한다.
		assertThat(AopUtils.isJdkDynamicProxy(proxy)).isTrue();
		// 프록시팩토리로 생성된 동적 프록시기오, CGLIB 동적 프록시인지 확인한다.
		assertThat(AopUtils.isCglibProxy(proxy)).isFalse();
	}

	@DisplayName("인터페이스가 없으면, CGLIB를 통해 클래스 기반 프록시를 생성한다.")
	@Test
	void concreteProxy() {
		// 인터페이스가 있는 구현 클래스
		ConcreteService target = new ConcreteService();

		// 프록시 팩토리 생성
		// 프록시팩토리에는 프록시가 호출할 target, target이 구현한 인터페이스 정보 등이 포함된다.
		// target이 인터페이스가 있는 구현 클래스라면 JDK 동적 프록시를 통해 인터페이스 기반 프록시를 생성한다.
		// target이 인터페이스가 없는 구체 클래스라면 CGLIB를 통해 클래스 기반 프록시를 생성한다.
		ProxyFactory proxyFactory = new ProxyFactory(target);

		// 프록시팩토리로 생성될 동적 프록시가 실행할 로직(=Advice)를 추가
		proxyFactory.addAdvice(new TimeAdvice());

		// 동적 프록시 생성
		ConcreteService proxy = (ConcreteService) proxyFactory.getProxy();

		log.info("targetClass={}", target.getClass());
		// targetClass=class hello.proxy.common.service.ConcreteService
		log.info("proxyClass={}", proxy.getClass());
		// proxyClass=class hello.proxy.common.service.ConcreteService$$EnhancerBySpringCGLIB$$9f45c72b

		proxy.call();
		// TimeAdvice - TimeProxy 실행
		// ConcreteService - ConcreteService 호출
		// TimeAdvice - TimeProxy 종료  resultTime=7

		// 프록시팩토리로 생성된 동적 프록시인지 확인한다.
		assertThat(AopUtils.isAopProxy(proxy)).isTrue();
		// 프록시팩토리로 생성된 동적 프록시이고, JDK 동적 프록시인지 확인한다.
		assertThat(AopUtils.isJdkDynamicProxy(proxy)).isFalse();
		// 프록시팩토리로 생성된 동적 프록시기오, CGLIB 동적 프록시인지 확인한다.
		assertThat(AopUtils.isCglibProxy(proxy)).isTrue();
	}

	@DisplayName("ProxyTargetClass 옵션을 사용하면, 인터페이스가 있어도 CGLIB를 통해 클래스 기반 프록시를 생성한다.")
	@Test
	void proxyTargetClass() {
		// 인터페이스가 있는 구현 클래스
		ServiceImpl target = new ServiceImpl();

		// 프록시 팩토리 생성
		// 프록시팩토리에는 프록시가 호출할 target, target이 구현한 인터페이스 정보 등이 포함된다.
		// target이 인터페이스가 있는 구현 클래스라면 JDK 동적 프록시를 통해 인터페이스 기반 프록시를 생성한다.
		// target이 인터페이스가 없는 구체 클래스라면 CGLIB를 통해 클래스 기반 프록시를 생성한다.
		ProxyFactory proxyFactory = new ProxyFactory(target);

		// 인터페이스가 있어도, target 클래스를 상속하여 동적 프록시를 생성한다. 즉 CGLIB 동적 프록시를 생성한다.
		proxyFactory.setProxyTargetClass(true);

		// 프록시팩토리로 생성될 동적 프록시가 실행할 로직(=Advice)를 추가
		proxyFactory.addAdvice(new TimeAdvice());

		// 동적 프록시 생성
		ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();

		log.info("targetClass={}", target.getClass());
		// targetClass=class hello.proxy.common.service.ServiceImpl
		log.info("proxyClass={}", proxy.getClass());
		// proxyClass=class hello.proxy.common.service.ServiceImpl$$EnhancerBySpringCGLIB$$4d673546

		proxy.save();
		// TimeAdvice - TimeProxy 실행
		// ServiceImpl - save 호출
		// TimeAdvice - TimeProxy 종료  resultTime=0

		// 프록시팩토리로 생성된 동적 프록시인지 확인한다.
		assertThat(AopUtils.isAopProxy(proxy)).isTrue();
		// 프록시팩토리로 생성된 동적 프록시이고, JDK 동적 프록시인지 확인한다.
		assertThat(AopUtils.isJdkDynamicProxy(proxy)).isFalse();
		// 프록시팩토리로 생성된 동적 프록시이고, CGLIB 동적 프록시인지 확인한다.
		assertThat(AopUtils.isCglibProxy(proxy)).isTrue();
	}
}
