package hello.proxy.cglib.code;

import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

// CGLIB 프록시의 실행 로직을 정의
@Slf4j
public class TimeMethodInterceptor implements MethodInterceptor {

	private final Object target; // 프록시의 호출 대상(서버 or 프록시)

	public TimeMethodInterceptor(Object target) {
		this.target = target;
	}

	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy)
			throws Throwable {
		log.info("TimeProxy 실행");
		long startTime = System.currentTimeMillis();

		Object result = methodProxy.invoke(target, args); // target 인스터스의 메서드를 실행

		long endTime = System.currentTimeMillis();

		long resultTime = endTime - startTime;
		log.info("TimeProxy 종료  resultTime={}", resultTime);

		return result;
	}
}
