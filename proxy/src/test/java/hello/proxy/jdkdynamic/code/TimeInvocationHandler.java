package hello.proxy.jdkdynamic.code;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;

// InvocationHandler의 구현체에 동적 프록시들의 공통 로직을 작성해줘야한다.
@Slf4j
public class TimeInvocationHandler implements InvocationHandler {

	private final Object target; // 동적 프록시가 호출할 대상(서버 or 프록시)

	public TimeInvocationHandler(Object target) {
		this.target = target;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		log.info("TimeProxy 실행");
		long startTime = System.currentTimeMillis();
		Object result = method.invoke(target, args); // target 인스터스의 메서드를 실행
		long endTime = System.currentTimeMillis();

		long resultTime = endTime - startTime;
		log.info("TimeProxy 종료  resultTime={}", resultTime);

		return result;
	}
}
