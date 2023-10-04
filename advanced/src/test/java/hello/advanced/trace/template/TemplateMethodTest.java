package hello.advanced.trace.template;

import hello.advanced.trace.template.code.AbstractTemplate;
import hello.advanced.trace.template.code.SubClassLogic1;
import hello.advanced.trace.template.code.SubClassLogic2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class TemplateMethodTest {

	@Test
	void templateMethodV0() {
		logic1();
		// 비즈니스 로직1 실행
		// resultTime=4
		logic2();
		// 비즈니스 로직2 실행
		// resultTime=0
	}

	// 핵심기능인 비즈니스 로직과 부가기능인 시간측정 로직이 혼재되어있다.
	// 시간측정 로직은 변하지 않으며, logic2에도 중복 작성되어있다.
	// 또한 부가기능의 변경이 있을 경우, 부가기능이 적용된 메소드를 모두 수정해야한다.
	private void logic1() {
		long startTime = System.currentTimeMillis();
		// 비즈니스 로직 실행
		log.info("비즈니스 로직1 실행");
		// 비즈니스 로직 종료
		long endTime = System.currentTimeMillis();
		long resultTime = endTime - startTime;
		log.info("resultTime={}", resultTime);
	}

	// 핵심기능인 비즈니스 로직과 부가기능인 시간측정 로직이 혼재되어있다.
	// 시간측정 로직은 변하지 않으며, logic1에도 중복 작성되어있다.
	// 또한 부가기능의 변경이 있을 경우, 부가기능이 적용된 메소드를 모두 수정해야한다.
	private void logic2() {
		long startTime = System.currentTimeMillis();
		// 비즈니스 로직 실행
		log.info("비즈니스 로직2 실행");
		// 비즈니스 로직 종료
		long endTime = System.currentTimeMillis();
		long resultTime = endTime - startTime;
		log.info("resultTime={}", resultTime);
	}

	// 템플릿 메서드 패턴 적용
	@Test
	void templateMethodV1() {
		AbstractTemplate template1 = new SubClassLogic1();
		template1.execute();
		// 비즈니스 로직1 실행
		// resultTime=0
		AbstractTemplate template2 = new SubClassLogic2();
		template2.execute();
		// 비즈니스 로직1 실행
		// resultTime=1
	}

	// 템플릿 메서드 패턴 적용 + 익명 내부 클래스
	// 익명 내부 클래스를 통해 템플릿 메서트 패턴 적용을 위한 자식 클래스 생성을 방지할 수 있다.
	@Test
	void templateMethodV2() {
		AbstractTemplate template1 = new AbstractTemplate() {
			@Override
			protected void call() {
				log.info("비즈니스 로직1 실행");
			}
		};

		AbstractTemplate template2 = new AbstractTemplate() {
			@Override
			protected void call() {
				log.info("비즈니스 로직2 실행");
			}
		};

		template1.execute();
		// 비즈니스 로직1 실행
		// resultTime=4
		template2.execute();
		// 비즈니스 로직2 실행
		// resultTime=0
	}
}
