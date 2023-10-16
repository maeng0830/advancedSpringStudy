package hello.proxy.advisor;

import hello.proxy.common.advice.TimeAdvice;
import hello.proxy.common.service.ServiceImpl;
import hello.proxy.common.service.ServiceInterface;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

@Slf4j
public class AdvisorTest {

	@Test
	void advisorTest1() {
		ServiceInterface target = new ServiceImpl();
		ProxyFactory proxyFactory = new ProxyFactory(target);

		// 어드바이저 생성
		// 어드바이저는 하나의 포인트컷 + 하나의 어드바이스
		// Pointcut.TRUE: 항상 어드바이스를 적용
		DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(Pointcut.TRUE,
				new TimeAdvice());

		// 프록시팩토리에 적용할 어드바이저를 지정한다.
		// 어드바이저는 포인트컷과 어드바이스를 모두 갖고 있기 때문에, 어디에 어떤 부가기능을 적용해야할지 알 수 있다.
		// 프록시팩토리를 생성할 때 어드바이저는 필수이다.
		// 프록시팩토리에 어드바이스만 지정한다면, 내부적으로 Pointcut.TRUE의 어드바이저가 생성된다.
		proxyFactory.addAdvisor(advisor);

		ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();

		proxy.save();
		// TimeAdvice - TimeProxy 실행
		// ServiceImpl - save 호출
		// TimeAdvice - TimeProxy 종료  resultTime=0
		proxy.find();
		// TimeAdvice - TimeProxy 실행
		// ServiceImpl - find 호출
		// TimeAdvice - TimeProxy 종료  resultTime=0
	}

	@DisplayName("직접 만든 포인트컷 적용")
	@Test
	void advisorTest2() {
		ServiceInterface target = new ServiceImpl();
		ProxyFactory proxyFactory = new ProxyFactory(target);

		// 어드바이저 생성
		// 어드바이저는 하나의 포인트컷 + 하나의 어드바이스
		// 직접 구현한 포인트컷을 적용한다.
		DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(new MyPointcut(),
				new TimeAdvice());

		// 프록시팩토리에 적용할 어드바이저를 지정한다.
		// 어드바이저는 포인트컷과 어드바이스를 모두 갖고 있기 때문에, 어디에 어떤 부가기능을 적용해야할지 알 수 있다.
		// 프록시팩토리를 생성할 때 어드바이저는 필수이다.
		// 프록시팩토리에 어드바이스만 지정한다면, 내부적으로 Pointcut.TRUE의 어드바이저가 생성된다.
		proxyFactory.addAdvisor(advisor);

		ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();

		proxy.save();
		// 포인트컷 호출 method=save targetClass=class hello.proxy.common.service.ServiceImpl
		// 포인트컷 결과 result=true
		// TimeAdvice - TimeProxy 실행
		// ServiceImpl - save 호출
		// TimeAdvice - TimeProxy 종료  resultTime=0
		proxy.find();
		// 포인트컷 호출 method=find targetClass=class hello.proxy.common.service.ServiceImpl
		// 포인트컷 결과 result=false
		// ServiceImpl - find 호출
	}

	@DisplayName("스프링이 제공하는 포인트컷 적용")
	@Test
	void advisorTest3() {
		ServiceInterface target = new ServiceImpl();
		ProxyFactory proxyFactory = new ProxyFactory(target);

		// 스프링이 제공하는 포인트컷
		// 메소드명이 "save"일 때만 어드바이스 실행
		NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
		pointcut.setMappedNames("save");

		// 어드바이저 생성
		// 어드바이저는 하나의 포인트컷 + 하나의 어드바이스
		// 스프링이 제공하는 포인트컷 적용
		DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointcut, new TimeAdvice());

		// 프록시팩토리에 적용할 어드바이저를 지정한다.
		// 어드바이저는 포인트컷과 어드바이스를 모두 갖고 있기 때문에, 어디에 어떤 부가기능을 적용해야할지 알 수 있다.
		// 프록시팩토리를 생성할 때 어드바이저는 필수이다.
		// 프록시팩토리에 어드바이스만 지정한다면, 내부적으로 Pointcut.TRUE의 어드바이저가 생성된다.
		proxyFactory.addAdvisor(advisor);

		ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();

		proxy.save();
		// TimeAdvice - TimeProxy 실행
		// ServiceImpl - save 호출
		// TimeAdvice - TimeProxy 종료  resultTime=0
		proxy.find();
		// ServiceImpl - find 호출
	}

	// 직접 구현한 포인트컷
	static class MyPointcut implements Pointcut {

		// 클래스에 대한 필터링
		@Override
		public ClassFilter getClassFilter() {
			return ClassFilter.TRUE;
		}

		// 메소드에 대한 필터링
		// 직접 구현한 MethodMatcher의 구현체를 반환한다.
		@Override
		public MethodMatcher getMethodMatcher() {
			return new MyMethodMatcher();
		}
	}

	// 메소드 필터링을 위한 MethodMatcher의 구현체
	static class MyMethodMatcher implements MethodMatcher {

		private String matchName = "save";

		// true를 반환할 경우, 어드바이스(프록시가 제공하는 기능)가 적용된다.
		@Override
		public boolean matches(Method method, Class<?> targetClass) {
			boolean result = method.getName().equals(matchName);
			log.info("포인트컷 호출 method={} targetClass={}", method.getName(), targetClass);
			log.info("포인트컷 결과 result={}", result);
			return result;
		}

		@Override
		public boolean isRuntime() {
			return false;
		}

		@Override
		public boolean matches(Method method, Class<?> targetClass, Object... args) {
			return false;
		}
	}
}
