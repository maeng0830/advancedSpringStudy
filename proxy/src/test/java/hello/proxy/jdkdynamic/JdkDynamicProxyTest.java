package hello.proxy.jdkdynamic;

import hello.proxy.jdkdynamic.code.AImpl;
import hello.proxy.jdkdynamic.code.AInterface;
import hello.proxy.jdkdynamic.code.BImpl;
import hello.proxy.jdkdynamic.code.BInterface;
import hello.proxy.jdkdynamic.code.TimeInvocationHandler;
import java.lang.reflect.Proxy;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class JdkDynamicProxyTest {

	@Test
	void dynamicA() {
		// 서버 객체(프록시가 호출할 대상)
		AImpl target = new AImpl();

		TimeInvocationHandler handler = new TimeInvocationHandler(target);

		// 프록시 동적 생성
		AInterface proxy = (AInterface) Proxy.newProxyInstance(AInterface.class.getClassLoader(),
				new Class[]{AInterface.class}, handler);

		// handler.invoke() 실행, args 인수로 call()이 사용됨.
		proxy.call();
		// TimeProxy 실행
		// A 호출
		// TimeProxy 종료  resultTime=0
		log.info("targetClass={}", target.getClass());
		// targetClass=class hello.proxy.jdkdynamic.code.AImpl
		log.info("proxyClass={}", proxy.getClass());
		// proxyClass=class com.sun.proxy.$Proxy12
	}

	@Test
	void dynamicB() {
		// 서버 객체(프록시가 호출할 대상)
		BImpl target = new BImpl();

		// 동적 프록시에 적용할 핸들러 로직
		TimeInvocationHandler handler = new TimeInvocationHandler(target);

		// 프록시 동적 생성
		BInterface proxy = (BInterface) Proxy.newProxyInstance(BInterface.class.getClassLoader(),
				new Class[]{BInterface.class}, handler);

		// handler.invoke() 실행, args 인수로 call()이 사용됨.
		proxy.call();
		// TimeProxy 실행
		// B 호출
		// TimeProxy 종료  resultTime=0
		log.info("targetClass={}", target.getClass());
		// targetClass=class hello.proxy.jdkdynamic.code.BImpl
		log.info("proxyClass={}", proxy.getClass());
		// proxyClass=class com.sun.proxy.$Proxy12
	}
}
