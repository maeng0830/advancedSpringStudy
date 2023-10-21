package hello.aop.pointcut;

import hello.aop.member.MemberService;
import hello.aop.member.annotation.ClassAop;
import hello.aop.member.annotation.MethodAop;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@Import(ParameterTest.ParameterAspect.class)
@SpringBootTest
public class ParameterTest {

	/*
	 * this, target, args, @target, @within, @annotation, @args 포인트컷 지시자는 어드바이스에 파라미터를 전달할 수 있다.
	 *
	 * 어드바이스의 파라미터 이름과 포인트컷 표현식의 파라미터 이름을 맞춰줘야한다.
	 *
	 * 포인트컷 표현식의 파라미터 타입은 어드바이스의 파라미터 타입으로 정의된다.
	 * args(arg, ..), advice(String arg) -> args(String, ..)
	 */

	@Autowired
	MemberService memberService;

	@Test
	void success() {
		log.info("memberService proxy={}", memberService.getClass());

		memberService.hello("helloA");
	}

	@Slf4j
	@Aspect
	static class ParameterAspect {

		@Pointcut("execution(* hello.aop.member..*.*(..))")
		private void allMember() {
		}

		@Around("allMember()")
		public Object logArgs1(ProceedingJoinPoint joinPoint) throws Throwable {
			// 별다른 작업을 하지 않아도, joinPoint는 메서드 호출 시 사용한 인자를 갖고 있다.
			Object arg = joinPoint.getArgs()[0];

			log.info("[logArgs1]{}, arg={}", joinPoint.getSignature(), arg);
			// [logArgs1]String hello.aop.member.MemberServiceImpl.hello(String), arg=helloA

			return joinPoint.proceed();
		}

		@Around("allMember() && args(arg, ..)")
		public Object logArgs2(ProceedingJoinPoint joinPoint, Object arg) throws Throwable {
			// args(arg, ..)에서 arg에 해당하는 인자값이 어드바이스의 Object arg로 전달된다.

			log.info("[logArgs2]{}, arg={}", joinPoint.getSignature(), arg);
			// [logArgs1]String hello.aop.member.MemberServiceImpl.hello(String), arg=helloA

			return joinPoint.proceed();
		}

		@Before("allMember() && args(arg, ..)")
		public void logArgs3(String arg) throws Throwable {
			// args(arg, ..)에서 arg에 해당하는 인자값이 어드바이스의 String arg로 전달된다.

			log.info("[logArgs3] arg={}", arg);
			// [logArgs3] arg=helloA
		}

		@Before("allMember() && this(obj)")
		public void thisArgs(JoinPoint joinPoint, MemberService obj) {
			// this는 프록시 객체를 의미한다.
			log.info("[this]{}, obj={}", joinPoint.getSignature(), obj.getClass());
			// obj=class hello.aop.member.MemberServiceImpl$$EnhancerBySpringCGLIB$$c7e08066
		}

		@Before("allMember() && target(obj)")
		public void targetArgs(JoinPoint joinPoint, MemberService obj) {
			// target은 실제 객체(타겟)를 의미한다.
			log.info("[target]{}, obj={}", joinPoint.getSignature(), obj.getClass());
			// obj=class hello.aop.member.MemberServiceImpl
		}

		@Before("allMember() && @target(annotation)")
		public void atTarget(JoinPoint joinPoint, ClassAop annotation) {
			log.info("[@target]{}, obj={}", joinPoint.getSignature(), annotation);
			// [@target]String hello.aop.member.MemberServiceImpl.hello(String), obj=@hello.aop.member.annotation.ClassAop()
		}

		@Before("allMember() && @within(annotation)")
		public void atWithin(JoinPoint joinPoint, ClassAop annotation) {
			log.info("[@within]{}, obj={}", joinPoint.getSignature(), annotation);
			// [@within]String hello.aop.member.MemberServiceImpl.hello(String), obj=@hello.aop.member.annotation.ClassAop()
		}

		@Before("allMember() && @annotation(annotation)")
		public void atAnnotation(JoinPoint joinPoint, MethodAop annotation) {
			log.info("[@annotation]{}, annotationValue={}", joinPoint.getSignature(), annotation.value());
			// [@annotation]String hello.aop.member.MemberServiceImpl.hello(String), annotationValue=test value
		}
	}
}
