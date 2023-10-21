package hello.aop.pointcut;

import hello.aop.member.annotation.ClassAop;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Slf4j
@Import({AtTargetAtWithinTest.Config.class})
@SpringBootTest
public class AtTargetAtWithinTest {

	/**
	 * @target(애노테이션)은 해당 애노테이션이 적용된 타입의 인스턴스를 기준으로 모든 메서드를 조인 포인트로 적용한다.
	 * 즉 상속 받은 메서드도 조인 포인트가 적용된다.
	 *
	 * @within(애노테이션)은 해당 애노테이션이 적용된 타입 내에서 정의된 메스드를 조인 포인트로 적용한다.
	 * 즉 상속 받은 메서드는 조인 포인트가 적용되지 않는다.
	 */

	@Autowired
	Child child;

	@Test
	void success() {
		log.info("child Proxy={}", child.getClass());
		child.childMethod(); // 상속 받은 메서드도 조인 포인트 적용
		// [@target] void hello.aop.pointcut.AtTargetAtWithinTest$Child.childMethod()
		// @within] void hello.aop.pointcut.AtTargetAtWithinTest$Child.childMethod()

		child.parentMethod(); // 상속 받은 메서드는 조인 포인트가 적용되지 않음
		// [@target] void hello.aop.pointcut.AtTargetAtWithinTest$Parent.parentMethod()
	}

	static class Config {

		@Bean
		public Parent parent() {
			return new Parent();
		}

		@Bean
		public Child child() {
			return new Child();
		}

		@Bean
		public AtTargetAtWithinAspect atTargetAtWithinAspect() {
			return new AtTargetAtWithinAspect();
		}
	}

	static class Parent {

		public void parentMethod() {
		} //부모에만 있는 메서드
	}

	@ClassAop
	static class Child extends Parent {

		public void childMethod() {
		}
	}

	@Slf4j
	@Aspect
	static class AtTargetAtWithinAspect {

		//@target: 인스턴스 기준으로 모든 메서드의 조인 포인트를 선정 = 상위 타입의 메서드도 적용
		@Around("execution(* hello.aop..*(..)) && @target(hello.aop.member.annotation.ClassAop)")
		public Object atTarget(ProceedingJoinPoint joinPoint) throws Throwable {
			log.info("[@target] {}", joinPoint.getSignature());

			return joinPoint.proceed();
		}

		//@within: 선택된 클래스 내부에 있는 메서드만 조인 포인트로 선정 = 상위 타입의 메서드는 적용되지 않음
		@Around("execution(* hello.aop..*(..)) && @within(hello.aop.member.annotation.ClassAop) ")
		public Object atWithin(ProceedingJoinPoint joinPoint) throws Throwable {
			log.info("[@within] {}", joinPoint.getSignature());

			return joinPoint.proceed();
		}
	}
}
