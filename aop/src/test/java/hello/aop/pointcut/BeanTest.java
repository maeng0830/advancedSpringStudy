package hello.aop.pointcut;

import hello.aop.order.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@Import(BeanTest.BeanAspect.class)
@SpringBootTest
public class BeanTest {

	/**
	 * bean(beanName)은 해당 beanName을 가진 타켓을 조인 포인트로 적용한다.
	 */

	@Autowired
	OrderService orderService;

	@Test
	void success() {
		orderService.orderItem("itemA");
		// [bean] String hello.aop.order.OrderService.orderItem(String)
		// [orderService] 실행
		// [bean] String hello.aop.order.OrderRepository.save(String)
		// [orderRepository] 실행
	}

	@Aspect
	static class BeanAspect {

		@Around("bean(orderService) || bean(*Repository)")
		public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
			log.info("[bean] {}", joinPoint.getSignature());

			return joinPoint.proceed();
		}
	}
}
