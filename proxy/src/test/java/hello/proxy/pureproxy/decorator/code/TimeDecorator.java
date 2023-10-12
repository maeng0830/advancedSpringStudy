package hello.proxy.pureproxy.decorator.code;

import lombok.extern.slf4j.Slf4j;

// 부가 기능을 위한 프록시 객체
@Slf4j
public class TimeDecorator implements Component {

	private Component component;

	public TimeDecorator(Component component) {
		this.component = component;
	}

	@Override
	public String operation() {
		log.info("TimeDecorator 실행");
		long startTime = System.currentTimeMillis();

		String result = component.operation(); // 프록시 객체(MessageDecorator) 호출

		long endTime = System.currentTimeMillis();
		long resultTime = endTime - startTime;

		log.info("TimeDecorator 종료 resultTIme={}ms", resultTime);

		return result;
	}
}
