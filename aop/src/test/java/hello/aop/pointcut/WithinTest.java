package hello.aop.pointcut;

import static org.assertj.core.api.Assertions.*;

import hello.aop.member.MemberServiceImpl;
import java.lang.reflect.Method;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

public class WithinTest {

	/*
	 * within(선언타입)
	 *
	 * 특정 타입 내의 조인 포인트(메서드)들로 매칭을 제한한다.
	 * within은 상위 타입으로 하위 타입까지 매칭 시킬 수 없다. 타겟 타입을 명시해줘야한다. execution과의 차이이다.
	 */

	AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
	Method helloMethod;

	@BeforeEach
	public void init() throws NoSuchMethodException {
		helloMethod = MemberServiceImpl.class.getMethod("hello", String.class);
	}

	@Test
	void withinExact() {
		pointcut.setExpression("within(hello.aop.member.MemberServiceImpl)");

		assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
	}

	@Test
	void withinStar() {
		pointcut.setExpression("within(hello.aop.member.*Service*)");

		assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
	}

	@Test
	void withinSubPackage() {
		pointcut.setExpression("within(hello.aop..*)");

		assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
	}

	@DisplayName("within은 타켓 타입을 지정해야한다. 상위 타입을 지정하면 안된다.")
	@Test
	void withinSuperTypeFalse() {
		pointcut.setExpression("within(hello.aop.member.MemberService)");

		assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
	}

	@DisplayName("execution은 타겟 타입의 상위 타입을 지정해도 된다.")
	@Test
	void executionSuperTypeTrue() {
		pointcut.setExpression("execution(* hello.aop.member.MemberService.*(..))");

		assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
	}
}

