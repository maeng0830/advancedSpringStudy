package hello.proxy.common.advice;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

// Advice에는 프록시가 제공할 로직이 작성된다.
// org.aopalliance.intercept.MethodInterceptor의 구현체로 Advice 생성할 수 있다.
@Slf4j
public class TimeAdvice implements MethodInterceptor {

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		log.info("TimeProxy 실행");
		long startTime = System.currentTimeMillis();

		// 프록시가 호출할 target의 정보, 호출할 메소드의 정보 등은 이미 invocation에 포함되어 있다.
		Object result = invocation.proceed(); // target 인스턴스의 메서드를 실행

		long endTime = System.currentTimeMillis();

		long resultTime = endTime - startTime;
		log.info("TimeProxy 종료  resultTime={}", resultTime);

		return result;
	}
}
