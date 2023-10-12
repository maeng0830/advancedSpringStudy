package hello.proxy.pureproxy.proxy.code;

import lombok.extern.slf4j.Slf4j;

// 실제 요청을 처리하는 서버 객체
@Slf4j
public class RealSubject implements Subject {

	// DB에서 데이터를 호출하는데 1초가 걸리는 로직
	@Override
	public String operation() {
		log.info("실제 객체 호출");
		sleep(1000);
		return "data";
	}

	private void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
