package hello.advanced.trace.template.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SubClassLogic1 extends AbstractTemplate {

	// 자식 클래스의 핵심 기능에 따라 구현될 부분이다.
	// 부가기능과 관련된 코드 없이, 핵심 기능에 대한 코드만 작성할 수 있다.
	@Override
	protected void call() {
		log.info("비즈니스 로직1 실행");
	}
}
