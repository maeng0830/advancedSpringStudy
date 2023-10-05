package hello.advanced.trace.strategy.code.strategy;

import lombok.extern.slf4j.Slf4j;

// 전략 패턴은 변하지 않는 부분인 Context와 변하는 부분인 Strategy의 구현체가 Strategy에만 의존하고 있다.
// 변하는 부분을 구현하기 위해, 변하지 않는 부분을 의존해야 했던 템플릿 메소드 패턴과의 차이이다.
// 전략 패턴 - 필드에 전략을 보관하는 방식
@Slf4j
public class ContextV1 {

	private Strategy strategy; // 변하는 부분인 strategy의 구현체를 주입해주면 된다.

	public ContextV1(Strategy strategy) {
		this.strategy = strategy;
	}

	// 변하지 않는 템플릿
	public void execute() {
		long startTime = System.currentTimeMillis();
		// 비즈니스 로직 실행
		strategy.call(); // 위임
		// 비즈니스 로직 종료
		long endTime = System.currentTimeMillis();
		long resultTime = endTime - startTime;
		log.info("resultTime={}", resultTime);
	}
}
