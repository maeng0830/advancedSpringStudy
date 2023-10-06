package hello.advanced.trace.strategy.code.template;

import lombok.extern.slf4j.Slf4j;

// 파라미터 전달 방식으로 구현된 전략 패턴은 템플릿 콜백 패턴이라고 부른다.
// 콜백은 다른 코드의 파라미터로 넘어오는 실행 가능한 코드를 말한다.
@Slf4j
public class TimeLogTemplate {

	public void execute(Callback callback) {
		long startTime = System.currentTimeMillis();
		// 비즈니스 로직 실행
		callback.call(); // 위임
		// 비즈니스 로직 종료
		long endTime = System.currentTimeMillis();
		long resultTime = endTime - startTime;
		log.info("resultTime={}", resultTime);
	}
}
