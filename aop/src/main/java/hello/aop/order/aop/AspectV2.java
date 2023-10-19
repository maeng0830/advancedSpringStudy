package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Slf4j
@Aspect
public class AspectV2 {

	/**
	 * 포인트컷 표현식을 @Pointcut에 분리하여 사용할 수 있다.
	 * 포인트컷 시그니처: 메소드명(파라미터) -> 반환타입은 void, 코드 내용은 없어야 한다.
	 * 내부에서만 사용하려면 private, 외부에서도 사용하려면 public을 사용하면 된다.
	 */

	// hello.aop.order 패키지와 하위 패키지
	@Pointcut("execution(* hello.aop.order..*(..))") // 포인트컷 표현식
	private void allOrder() {} // 포인트컷 시그니처


	// @Around의 값으로 포인트컷 시그니처를 사용하여 포인트컷을 지정할 수 있다.
	@Around("allOrder()")
	public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
		log.info("[log] {}", joinPoint.getSignature()); // joinPoint 시그니처

		return joinPoint.proceed();
	}
}
