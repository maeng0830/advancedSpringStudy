package hello.advanced.trace.strategy;

import hello.advanced.trace.strategy.code.strategy.ContextV1;
import hello.advanced.trace.strategy.code.strategy.Strategy;
import hello.advanced.trace.strategy.code.strategy.StrategyLogic1;
import hello.advanced.trace.strategy.code.strategy.StrategyLogic2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ContextV1Test {

	@Test
	void strategyV0() {
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

	// 전략 패턴 사용
	// Context의 필드에 전략을 보관
	@Test
	void strategyV1() {
		StrategyLogic1 strategyLogic1 = new StrategyLogic1();
		ContextV1 context1 = new ContextV1(strategyLogic1);
		context1.execute();
		// 비즈니스 로직1 실행
		// resultTime=4

		StrategyLogic2 strategyLogic2 = new StrategyLogic2();
		ContextV1 context2 = new ContextV1(strategyLogic2);
		context2.execute();
		// 비즈니스 로직2 실행
		// resultTime=0
	}

	// 전략 패턴 사용 - 익명 내부 클래스
	// Context의 필드에 전략을 보관
	@Test
	void strategyV2() {
		Strategy strategy1 = new Strategy() {

			@Override
			public void call() {
				log.info("비즈니스 로직1 실행");
			}
		};

		ContextV1 context1 = new ContextV1(strategy1);
		context1.execute();
		// 비즈니스 로직1 실행
		// resultTime=4

		Strategy strategy2 = new Strategy() {

			@Override
			public void call() {
				log.info("비즈니스 로직2 실행");
			}
		};

		ContextV1 context2 = new ContextV1(strategy2);
		context2.execute();
		// 비즈니스 로직2 실행
		// resultTime=0
	}

	// 전략 패턴 사용 - 익명 내부 클래스 - 인라인
	// Context의 필드에 전략을 보관
	@Test
	void strategyV3() {
		ContextV1 context1 = new ContextV1( new Strategy() {

			@Override
			public void call() {
				log.info("비즈니스 로직1 실행");
			}
		});
		context1.execute();
		// 비즈니스 로직1 실행
		// resultTime=4

		ContextV1 context2 = new ContextV1( new Strategy() {

			@Override
			public void call() {
				log.info("비즈니스 로직2 실행");
			}
		});
		context2.execute();
		// 비즈니스 로직2 실행
		// resultTime=0
	}

	// 전략 패턴 사용 - 익명 내부 클래스 - 람다
	// Context의 필드에 전략을 보관
	@Test
	void strategyV4() {
		ContextV1 context1 = new ContextV1(() -> log.info("비즈니스 로직1 실행"));
		context1.execute();
		// 비즈니스 로직1 실행
		// resultTime=4

		ContextV1 context2 = new ContextV1(() -> log.info("비즈니스 로직2 실행"));
		context2.execute();
		// 비즈니스 로직2 실행
		// resultTime=0
	}
}
