package hello.proxy.pureproxy.decorator.code;

import lombok.extern.slf4j.Slf4j;

// 부가 기능을 위한 프록시 객체
@Slf4j
public class MessageDecorator implements Component {

	private Component component; // 프록시 객체가 대신 호출할 서버 객체

	public MessageDecorator(Component component) {
		this.component = component;
	}

	@Override
	public String operation() {
		log.info("MessageDecorator 실행");

		// 서버 객체로부터 핵심 기능 호출
		String result = component.operation();

		// 프록시 객체의 부가 기능
		String decoResult = "******" + result + "******";

		log.info("MessageDecorator 적용 전={}, 적용 후={}", result, decoResult);

		return decoResult;
	}
}
