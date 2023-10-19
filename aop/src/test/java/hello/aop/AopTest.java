package hello.aop;

import hello.aop.order.OrderRepository;
import hello.aop.order.OrderService;
import hello.aop.order.aop.AspectV1;
import hello.aop.order.aop.AspectV2;
import hello.aop.order.aop.AspectV3;
import hello.aop.order.aop.AspectV4Pointcut;
import hello.aop.order.aop.AspectV5Order;
import hello.aop.order.aop.AspectV6Advice;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Indexed;

//@Import(AspectV1.class)
//@Import(AspectV2.class)
//@Import(AspectV3.class)
//@Import(AspectV4Pointcut.class)
//@Import({AspectV5Order.LogAspect.class, AspectV5Order.TxAspect.class})
@Import(AspectV6Advice.class)
@Slf4j
@SpringBootTest
public class AopTest {

	@Autowired
	OrderService orderService;

	@Autowired
	OrderRepository orderRepository;

	// orderService, orderRepository가 AOP 프록시(프록시 팩토리로 생성된 프록시)인지 확인!
	@Test
	void aopInfo() {
		log.info("isAopProxy, orderService={}", AopUtils.isAopProxy(orderService));
		// isAopProxy, orderService=true
		log.info("isAopProxy, orderRepository={}", AopUtils.isAopProxy(orderRepository));
		// isAopProxy, orderRepository=true
	}

	@Test
	void success() {
		System.out.println(orderService.orderItem("itemA"));
		// [트랜잭션 시작] void hello.aop.order.OrderService.orderItem(String)
		// [log] void hello.aop.order.OrderService.orderItem(String)
		// [orderService] 실행
		// [log] String hello.aop.order.OrderRepository.save(String)
		// [orderRepository] 실행
		// [트랜잭션 커밋] void hello.aop.order.OrderService.orderItem(String)
		// [리소스 릴리즈] void hello.aop.order.OrderService.orderItem(String)
	}

	@Test
	void exception() {
		Assertions.assertThatThrownBy(() -> orderService.orderItem("ex"))
				.isInstanceOf(IllegalStateException.class);
		// [트랜잭션 시작] void hello.aop.order.OrderService.orderItem(String)
		// [log] void hello.aop.order.OrderService.orderItem(String)
		// [orderService] 실행
		// [log] String hello.aop.order.OrderRepository.save(String)
		// [orderRepository] 실행
		// [트랜잭션 롤백] void hello.aop.order.OrderService.orderItem(String)
		// [리소스 릴리즈] void hello.aop.order.OrderService.orderItem(String)
	}
}
