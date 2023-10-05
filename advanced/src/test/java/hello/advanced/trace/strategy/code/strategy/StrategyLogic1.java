package hello.advanced.trace.strategy.code.strategy;

import lombok.extern.slf4j.Slf4j;

// 변하는 부분이 구현되는 StrategyLogic은 Strategy만 의존하고 있다.
// 변하지 않는 부분인 Context가 변경되더라도, 전혀 영향을 받지 않는다.
@Slf4j
public class StrategyLogic1 implements Strategy {

	@Override
	public void call() {
		log.info("비즈니스 로직1 실행");
	}
}
