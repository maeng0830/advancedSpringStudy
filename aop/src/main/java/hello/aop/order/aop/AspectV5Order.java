package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;

@Slf4j
public class AspectV5Order {

	/**
	 * 어드바이스는 기본적으로 적용 순서를 보장하지 않으며, 순서를 지정하고 싶으면 @Aspect 단위로 @Order를 적용해야한다.
	 * 하나의 @Aspect에 여러 어드바이스가 있으면 순서를 지정할 수 없다.
	 * 따라서 순서를 지정하기 위한 어드바이스들을 각각의 @Asepct로 분리해야한다.
	 * @Order의 값이 작을수록 먼저 적용된다. TxAspect -> LogAspect
	 */

	@Aspect
	@Order(2)
	public static class LogAspect {
		@Around("hello.aop.order.aop.Pointcuts.allOrder()")
		public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
			log.info("[log] {}", joinPoint.getSignature()); // joinPoint 시그니처
			log.info("joinPoint.getTarget()={}", joinPoint.getTarget());

			return joinPoint.proceed();
		}
	}

	@Aspect
	@Order(1)
	public static class TxAspect {
		@Around("hello.aop.order.aop.Pointcuts.orderAndService()")
		public Object doTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
			log.info("joinPoint.getTarget()={}", joinPoint.getTarget());

			try {
				log.info("[트랜잭션 시작] {}", joinPoint.getSignature());
				Object result = joinPoint.proceed();
				log.info("[트랜잭션 커밋] {}", joinPoint.getSignature());

				return result;
			} catch (Exception e) {
				log.info("[트랜잭션 롤백] {}", joinPoint.getSignature());

				throw e;
			} finally {
				log.info("[리소스 릴리즈] {}", joinPoint.getSignature());
			}
		}
	}
}
