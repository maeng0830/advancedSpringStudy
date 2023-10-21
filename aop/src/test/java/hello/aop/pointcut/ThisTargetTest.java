package hello.aop.pointcut;

import hello.aop.member.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@Import(ThisTargetTest.ThisTargetAspect.class)
@SpringBootTest(properties = "spring.aop.proxy-target-class=false") //JDK 동적프록시
//@SpringBootTest(properties = "spring.aop.proxy-target-class=true") //CGLIB
public class ThisTargetTest {

	/**
	 * this(선언타입)은 선언타입과 일치하는 빈으로 등록된 객체(스프링 AOP 프록시)를 조인 포인트로 적용한다.
	 * target(선언타입)은 선언타입과 일치하는 타겟 객체(스프링 AOP 프록시가 참조하는 객체)를 조인 포인트로 적용한다.
	 * <p>
	 * 타입 하나를 정확하게 지정해야한다. *와 같은 패턴을 사용할 수 없다.
	 * <p>
	 * 상위 타입을 지정하면, 하위 타입도 적용된다.
	 */

	/**
	 * 스프링 AOP 프록시는 JDK 동적 프록시 또는 CGLIB를 통해 생성된다.
	 * JDK 동적 프록시는 인터페이스가 필수이고, 인터페이스를 구현한 프록시를 생성한다.
	 * CGLIB는 인터페이스가 필수적이지 않으며, 인터페이스가 있더라도 구현 클래스를 상속 받아 프록시를 생성한다.
	 *
	 * 이것 때문에 스프링 AOP 프록시를 생성한 기술(JDK 동적 프록시 or CGLIB)에 따라 this를 사용할 떄 차이가 발생한다.
	 *
	 * 인터페이스가 없이는 JDK 동적 프록시는 사용 자체를 할 수 없기 때문에,
	 * 인터페이스가 있는 구현 클래스에 스프링 AOP를 적용하는 상황에서의 차이를 말한다.
	 *
	 * JDK 동적 프록시는 인터페이스를 구현해서 프록시를 생성하기 때문에, 구현 클래스는 프록시의 상위 클래스가 아니다.
	 * 따라서 this(구현클래스 선언타입)으로 표현식을 작성하면, 프록시는 조인포인트로 적용되지 않는다.
	 *
	 * 반대로 CGLIB는 인터페이스가 있더라도 구현 클래스를 상속해서 프록시를 생성하기 때문에, 구현 클래스는 프록시의 상위 클래스이다.
	 * 따라서 this(구현클래스 선언타입)으로 표현식을 작성하면, 프록시는 조인포인트로 적용된다.
	 */

	@Autowired
	MemberService memberService;

	@Test
	void success() {
		log.info("memberService Proxy={}", memberService.getClass());
		memberService.hello("helloA");
	}

	@Slf4j
	@Aspect
	static class ThisTargetAspect {

		// 상위 타입을 지정하면, 하위 타입도 적용된다.
		@Around("this(hello.aop.member.MemberService)")
		public Object doThisInterface(ProceedingJoinPoint joinPoint) throws Throwable {
			log.info("[this-interface] {}", joinPoint.getSignature());

			return joinPoint.proceed();
		}

		// 상위 타입을 지정하면, 하위 타입도 적용된다.
		@Around("target(hello.aop.member.MemberService)")
		public Object doTargetInterface(ProceedingJoinPoint joinPoint) throws Throwable {
			log.info("[target-interface] {}", joinPoint.getSignature());

			return joinPoint.proceed();
		}

		//this: 스프링 AOP 프록시 객체 대상
		//JDK 동적 프록시는 인터페이스를 기반으로 생성되므로 구현 클래스를 알 수 없음
		//CGLIB 프록시는 구현 클래스를 기반으로 생성되므로 구현 클래스를 알 수 있음
		@Around("this(hello.aop.member.MemberServiceImpl)")
		public Object doThis(ProceedingJoinPoint joinPoint) throws Throwable {
			log.info("[this-impl] {}", joinPoint.getSignature());

			return joinPoint.proceed();
		}

		//target: 실제 target 객체 대상
		@Around("target(hello.aop.member.MemberServiceImpl)")
		public Object doTarget(ProceedingJoinPoint joinPoint) throws Throwable {
			log.info("[target-impl] {}", joinPoint.getSignature());

			return joinPoint.proceed();
		}
	}
}
