package hello.aop.pointcut;

import static org.assertj.core.api.Assertions.*;

import hello.aop.member.MemberServiceImpl;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

@Slf4j
public class ExecutionTest {

	/*
	 * execution(접근제어자? 반환타입 선언타입?메서드명(파라미터) 예외?)
	 *
	 * ?가 붙는 접근제어자와 선언타입은 생략할 수 있다. 선언타입 = 패키지 + 타입
	 * *은 어떤 값이든 상관 없다는 의미이다.
	 * ..는 어떤 파라미터 목록이든 상관 없다는 의미이다.
	 */

	AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
	Method helloMethod;

	@BeforeEach
	public void init() throws NoSuchMethodException {
		helloMethod = MemberServiceImpl.class.getMethod("hello", String.class);
	}

	@Test
	void printMethod() {
		// 예제에서 사용할 조인 포인트
		log.info("helloMethod={}", helloMethod);
		// helloMethod=public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
	}

	// 가장 정확한 표현식
	@Test
	void exactMatch() {
		// 접근제어자?: public
		// 반환타입: String
		// 선언타입?: hello.aop.member.MemberServiceImpl
		// 메서드명(파라미터): hello(String)
		// 예외?: 없음
		pointcut.setExpression(
				"execution(public String hello.aop.member.MemberServiceImpl.hello(String))");

		// MemberServiceImpl의 hello()가 포인트컷을 만족하는가
		assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
	}

	// 가장 많이 생략한 표현식
	void allMath() {
		// 접근제어자?: 생략
		// 반환타입: *
		// 선언타입?: 생략
		// 메서드명(파라미터): *(..)
		// 예외?: 없음
		pointcut.setExpression("execution(* *(..))");

		// MemberServiceImpl의 hello()가 포인트컷을 만족하는가
		assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
	}

	////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////       메소드명 기준 매치       ///////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////
	@Test
	void nameMatch() {
		// 접근제어자?: 생략
		// 반환타입: *
		// 선언타입?: 생략
		// 메서드명(파라미터): hello(..)
		// 예외?: 없음
		pointcut.setExpression("execution(* hello(..))");

		// MemberServiceImpl의 hello()가 포인트컷을 만족하는가
		assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
	}

	@Test
	void nameMatchStar1() {
		// 접근제어자?: 생략
		// 반환타입: *
		// 선언타입?: 생략
		// 메서드명(파라미터): hel*(..)
		// 예외?: 없음
		pointcut.setExpression("execution(* hel*(..))");

		// MemberServiceImpl의 hello()가 포인트컷을 만족하는가
		assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
	}

	@Test
	void nameMatchStar2() {
		// 접근제어자?: 생략
		// 반환타입: *
		// 선언타입?: 생략
		// 메서드명(파라미터): *el*(..)
		// 예외?: 없음
		pointcut.setExpression("execution(* *el*(..))");

		// MemberServiceImpl의 hello()가 포인트컷을 만족하는가
		assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
	}

	@Test
	void nameMatchFalse() {
		// 접근제어자?: 생략
		// 반환타입: *
		// 선언타입?: 생략
		// 메서드명(파라미터): nono(..)
		// 예외?: 없음
		pointcut.setExpression("execution(* nono(..))");

		// MemberServiceImpl의 hello()가 포인트컷을 만족하는가
		assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
	}

	////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////   선언타입+메소드명 기준 매치   ///////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////
	// 가장 정확한 표현식
	@Test
	void packageExactMatch1() {
		// 접근제어자?: 생략
		// 반환타입: *
		// 선언타입?: hello.aop.member.MemberServiceImpl
		// 메서드명(파라미터): hello(..)
		// 예외?: 없음
		pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.hello(..))");

		// MemberServiceImpl의 hello()가 포인트컷을 만족하는가
		assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
	}

	@Test
	void packageExactMatch2() {
		// 접근제어자?: 생략
		// 반환타입: *
		// 선언타입?: hello.aop.member.*
		// 메서드명(파라미터): *(..)
		// 예외?: 없음
		pointcut.setExpression("execution(* hello.aop.member.*.*(..))");

		// MemberServiceImpl의 hello()가 포인트컷을 만족하는가
		assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
	}

	// 주의! . 으로는 하위 패키지까지 포함할 수 없다.
	@Test
	void packageExactFalse() {
		// 접근제어자?: 생략
		// 반환타입: *
		// 선언타입?: hello.aop.*
		// 메서드명(파라미터): *(..)
		// 예외?: 없음
		pointcut.setExpression("execution(* hello.aop.*.*(..))");

		// MemberServiceImpl의 hello()가 포인트컷을 만족하는가
		assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
	}

	// 주의! .. 으로 하위 패키지까지 포함할 수 있다.
	@Test
	void packageMatchSubPackage1() {
		// 접근제어자?: 생략
		// 반환타입: *
		// 선언타입?: hello.aop..*
		// 메서드명(파라미터): *(..)
		// 예외?: 없음
		pointcut.setExpression("execution(* hello.aop..*.*(..))");

		// MemberServiceImpl의 hello()가 포인트컷을 만족하는가
		assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
	}

	////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////        타입 기준 매치         ///////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////
	@Test
	void typeExactMatch() {
		// 접근제어자?: 생략
		// 반환타입: *
		// 선언타입?: hello.aop.member.MemberServiceImpl
		// 메서드명(파라미터): *(..)
		// 예외?: 없음
		pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.*(..))");

		// MemberServiceImpl의 hello()가 포인트컷을 만족하는가
		assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
	}

	// 상위 타입을 기준으로 포인트컷을 구현하면, 하위 타입들도 매칭된다.
	@Test
	void typeMatchSuperType() {
		// 접근제어자?: 생략
		// 반환타입: *
		// 선언타입?: hello.aop.member.MemberService
		// 메서드명(파라미터): *(..)
		// 예외?: 없음
		pointcut.setExpression("execution(* hello.aop.member.MemberService.*(..))");

		// MemberServiceImpl의 hello()가 포인트컷을 만족하는가
		assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
	}

	// 상위 타입을 기준으로 포인트컷을 구현하면, 하위 타입들도 매칭된다.
	// 단 하위 타입에만 존재하는 메소드에 대해서는 매칭되지 않는다.
	@Test
	void typeMatchNoSuperTypeMethodFalse() throws NoSuchMethodException {
		// 접근제어자?: 생략
		// 반환타입: *
		// 선언타입?: hello.aop.member.MemberService
		// 메서드명(파라미터): *(..)
		// 예외?: 없음
		pointcut.setExpression("execution(* hello.aop.member.MemberService.*(..))");

		// MemberServiceImpl에서 정의된 internal()
		Method internalMethod = MemberServiceImpl.class.getMethod("internal", String.class);

		// MemberServiceImpl의 internal()이 포인트컷을 만족하는가
		assertThat(pointcut.matches(internalMethod, MemberServiceImpl.class)).isFalse();
	}

	////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////       파라미터 기준 매치       ///////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////
	// String 타입의 파라미터 허용
	// (String)
	@Test
	void argsMatch() {
		// 접근제어자?: 생략
		// 반환타입: *
		// 선언타입?: 생략
		// 메서드명(파라미터): *(String)
		// 예외?: 없음
		pointcut.setExpression("execution(* *(String))");

		// MemberServiceImpl의 hello()가 포인트컷을 만족하는가
		assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
	}

	// 파라미터가 없어야함
	// ()
	@Test
	void argsMatchNoArgs() {
		// 접근제어자?: 생략
		// 반환타입: *
		// 선언타입?: 생략
		// 메서드명(파라미터): *()
		// 예외?: 없음
		pointcut.setExpression("execution(* *())");

		// MemberServiceImpl의 hello()가 포인트컷을 만족하는가
		assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
	}

	// 정확히 하나의 파라미터 허용, 모든 타입 허용
	// (*)
	@Test
	void argsMatchStar() {
		// 접근제어자?: 생략
		// 반환타입: *
		// 선언타입?: 생략
		// 메서드명(파라미터): *(*)
		// 예외?: 없음
		pointcut.setExpression("execution(* *(*))");

		// MemberServiceImpl의 hello()가 포인트컷을 만족하는가
		assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
	}

	// 모든 파라미터 목록 허용. 파라미터가 없어도 됨.
	// (..)
	@Test
	void argsMatchAll() {
		// 접근제어자?: 생략
		// 반환타입: *
		// 선언타입?: 생략
		// 메서드명(파라미터): *(..)
		// 예외?: 없음
		pointcut.setExpression("execution(* *(..))");

		// MemberServiceImpl의 hello()가 포인트컷을 만족하는가
		assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
	}

	// 첫 번째 파라미터가 String인 모든 파라미터 목록 허용
	// (String, ..)
	@Test
	void argsMatchComplex() {
		// 접근제어자?: 생략
		// 반환타입: *
		// 선언타입?: 생략
		// 메서드명(파라미터): *(..)
		// 예외?: 없음
		pointcut.setExpression("execution(* *(String, ..))");

		// MemberServiceImpl의 hello()가 포인트컷을 만족하는가
		assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
	}
}
