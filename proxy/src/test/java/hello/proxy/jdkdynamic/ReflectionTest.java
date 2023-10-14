package hello.proxy.jdkdynamic;

import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ReflectionTest {

	@Test
	void reflection0() {
		Hello target = new Hello();

		// 공통 로직1 시작
		log.info("start");
		String result1 = target.callA(); // 호출하는 메서드만 다름
		log.info("result={}", result1);
		// 공통 로직1 종료

		// 공통 로직1 시작
		log.info("start");
		String result2 = target.callB(); // 호출하는 메서드만 다름
		log.info("result={}", result2);
		// 공통 로직1 종료
	}

	// 정적인 target.callA(), target.callB() 코드를 리플렉션을 사용해서 Method라는 메타정보로 추상화했다.
	// 런타임 시점에 오류가 발생할 수 있기 때문에 사용은 가급적 자제 해야한다.
	@Test
	void reflection1()
			throws Exception {
		// 클래스 정보
		Class classHello = Class.forName("hello.proxy.jdkdynamic.ReflectionTest$Hello");
		Hello target = new Hello();

		// callA 메서드 정보
		Method methodCallA = classHello.getMethod("callA");
		Object result1 = methodCallA.invoke(target); // 메서드 동적 호출
		log.info("result1={}", result1);

		// callB 메서드 정보
		Method methodCallB = classHello.getMethod("callB");
		Object result2 = methodCallB.invoke(target); // 메서드 동적 호출
		log.info("result1={}", result2);
	}

	// 정적인 target.callA(), target.callB() 코드를 리플렉션을 사용해서 Method라는 메타정보로 추상화했다.
	// 런타임 시점에 오류가 발생할 수 있기 때문에 사용은 가급적 자제 해야한다.
	@Test
	void reflection2() throws Exception {
		// 클래스 정보
		Class classHello = Class.forName("hello.proxy.jdkdynamic.ReflectionTest$Hello");
		Hello target = new Hello();

		// callA 메서드 정보 추출 -> 동적 호출
		Method methodCallA = classHello.getMethod("callA");
		dynamicCall(methodCallA, target);
		// start
		// callA
		// result=A

		// callB 메서드 정보 -> 동적 호출
		Method methodCallB = classHello.getMethod("callB");
		dynamicCall(methodCallB, target);
		// start
		// callB
		// result=B
	}

	private void dynamicCall(Method method, Object target) throws Exception {
		log.info("start");
		Object result = method.invoke(target);
		log.info("result={}", result);
	}

	@Slf4j
	static class Hello {

		public String callA() {
			log.info("callA");
			return "A";
		}

		public String callB() {
			log.info("callB");
			return "B";
		}
	}
}
