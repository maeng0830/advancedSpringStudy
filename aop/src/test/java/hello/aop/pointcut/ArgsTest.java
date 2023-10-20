package hello.aop.pointcut;

import static org.assertj.core.api.Assertions.*;

import hello.aop.member.MemberServiceImpl;
import java.lang.reflect.Method;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

public class ArgsTest {

	/**
	 * args(파라미터)
	 *
	 * args는 파라미터를 기준으로 매칭하는 포인트컷 지시자이다.
	 * execution는 파라미터의 타입을 정확히 지정해야 매칭되지만, args는 파라미터의 타입으로 상위 타입을 지정해도 매칭된다.
	 */

	Method helloMethod;

	@BeforeEach
	public void init() throws NoSuchMethodException {
		helloMethod = MemberServiceImpl.class.getMethod("hello", String.class);
	}

	private AspectJExpressionPointcut pointcut(String expression) {
		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();

		pointcut.setExpression(expression);

		return pointcut;
	}

	@Test
	void args() {
		//hello(String)과 매칭
		assertThat(pointcut("args(String)")
				.matches(helloMethod, MemberServiceImpl.class)).isTrue();

		assertThat(pointcut("args(Object)")
				.matches(helloMethod, MemberServiceImpl.class)).isTrue();

		assertThat(pointcut("args()")
				.matches(helloMethod, MemberServiceImpl.class)).isFalse();

		assertThat(pointcut("args(..)")
				.matches(helloMethod, MemberServiceImpl.class)).isTrue();

		assertThat(pointcut("args(*)")
				.matches(helloMethod, MemberServiceImpl.class)).isTrue();

		assertThat(pointcut("args(String,..)")
				.matches(helloMethod, MemberServiceImpl.class)).isTrue();
	}

	/**
	 * execution(* *(java.io.Serializable)): 메서드의 시그니처로 판단 (정적)
	 * args(java.io.Serializable): 런타임에 전달된 인수로 판단 (동적)
	 */
	@Test
	void argsVsExecution() {
		//Args
		assertThat(pointcut("args(String)")
				.matches(helloMethod, MemberServiceImpl.class)).isTrue();

		assertThat(pointcut("args(java.io.Serializable)")
				.matches(helloMethod, MemberServiceImpl.class)).isTrue();

		assertThat(pointcut("args(Object)")
				.matches(helloMethod, MemberServiceImpl.class)).isTrue();

		//Execution
		assertThat(pointcut("execution(* *(String))")
				.matches(helloMethod, MemberServiceImpl.class)).isTrue();

		assertThat(pointcut("execution(* *(java.io.Serializable))") //매칭 실패
				.matches(helloMethod, MemberServiceImpl.class)).isFalse();

		assertThat(pointcut("execution(* *(Object))") //매칭 실패
				.matches(helloMethod, MemberServiceImpl.class)).isFalse();
	}
}
