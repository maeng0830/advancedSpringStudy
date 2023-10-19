package hello.proxy.config.v6_aop.aspect;

import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Slf4j
@Aspect // @Aspect를 통해 어드바이저(어드바이스 1 + 포인트컷 1)을 편리하게 정의할 수 있다.
public class LogTraceAspect {

	private final LogTrace logTrace;

	public LogTraceAspect(LogTrace logTrace) {
		this.logTrace = logTrace;
	}

	// @Around의 값은 포인트컷이며, @Around가 적용된 메소드가 어드바이스 역할을 한다.
	@Around("execution(* hello.proxy.app..*(..))")
	public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
		TraceStatus status = null;

		try {
			String message = joinPoint.getSignature().toShortString();

			status = logTrace.begin(message);

			// 호출 대상의 메소드를 호출
			Object result = joinPoint.proceed();

			logTrace.end(status);
			return result;
		} catch (Exception e) {
			logTrace.exception(status, e);
			throw e;
		}
	}
}
