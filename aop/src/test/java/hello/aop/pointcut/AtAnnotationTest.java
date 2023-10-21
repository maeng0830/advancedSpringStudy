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
@Import(AtAnnotationTest.AtAnnotationAspect.class)
@SpringBootTest
public class AtAnnotationTest {

	/**
	 * @annotation(애노테이션)은 해당 애노테이션이 적용된 메소드를 조인 포인트로 적용한다.
	 */

	@Autowired
	MemberService memberService;

	@Test
	void success() {
		log.info("memberService Proxy={}", memberService.getClass());

		memberService.hello("helloA");
		// [@annotation] String hello.aop.member.MemberServiceImpl.hello(String)
	}

	@Slf4j
	@Aspect
	static class AtAnnotationAspect {

		@Around("@annotation(hello.aop.member.annotation.MethodAop)")
		public Object doAtAnnotation(ProceedingJoinPoint joinPoint) throws Throwable {
			log.info("[@annotation] {}", joinPoint.getSignature());

			return joinPoint.proceed();
		}
	}

}
