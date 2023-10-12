package hello.proxy.pureproxy.proxy.code;

import lombok.extern.slf4j.Slf4j;

// 캐시(접근 제어)를 위한 프록시 객체
@Slf4j
public class CacheProxy implements Subject {

	private Subject target; // 프록시 객체가 대신 호출할 서버 객체
	private String cacheValue;

	public CacheProxy(Subject target) {
		this.target = target;
	}

	// 캐시값이 없다면, 서버 객체를 통해 데이터를 조회한다.
	// 그렇지 않은 경우, 저장된 캐시값을 그대로 반환한다.
	@Override
	public String operation() {
		log.info("프록시 호출");

		if (cacheValue == null) {
			cacheValue = target.operation();
		}

		return cacheValue;
	}
}
