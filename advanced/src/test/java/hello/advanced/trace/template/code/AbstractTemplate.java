package hello.advanced.trace.template.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractTemplate {

	// 변하지 않는 부분, 즉 부가 기능이 구현되어 있는 메소드이다. 즉 템플릿이라고 할 수 있다.
	// 자식 클래스에서 핵심 기능이 구현될 call()을 내부에서 호출한다.
	// 부가기능의 변경이 필요하더라도, 해당 부분에서만 수정하면 된다.
	public void execute() {
		long startTime = System.currentTimeMillis();
		// 비즈니스 로직 실행
		call(); // 변하는 부분, 즉 핵심 기능
		// 비즈니스 로직 종료
		long endTime = System.currentTimeMillis();
		long resultTime = endTime - startTime;
		log.info("resultTime={}", resultTime);
	}

	// 변하는 부분, 즉 핵심 기능이 실행되는 메소드이다.
	// 자식 클래스에서 오버라이딩 한다.
	protected abstract void call();
}
