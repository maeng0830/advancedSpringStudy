package hello.proxy.config.v2_dynamicproxy.handler;

import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

// 동적 프록시 객체가 실행할 로직
public class LogTraceBasicHandler implements InvocationHandler {

	private final Object target; // 프록시가 호출할 대상(서버 or 프록시)
	private final LogTrace logTrace;

	public LogTraceBasicHandler(Object target, LogTrace logTrace) {
		this.target = target;
		this.logTrace = logTrace;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		TraceStatus status = null;

		try {
			String message =
					method.getDeclaringClass().getSimpleName() + "." + method.getName() + "()";
			status = logTrace.begin(message);

			// 로직 호출
			Object result = method.invoke(target, args);

			logTrace.end(status);
			return result;
		} catch (Exception e) {
			logTrace.exception(status, e);
			throw e;
		}
	}
}
