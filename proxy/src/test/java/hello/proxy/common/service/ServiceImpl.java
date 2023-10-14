package hello.proxy.common.service;

import lombok.extern.slf4j.Slf4j;

// 인터페이스가 있는 구현 클래스
@Slf4j
public class ServiceImpl implements ServiceInterface {

	@Override
	public void save() {
		log.info("save 호출");
	}

	@Override
	public void find() {
		log.info("find 호출");
	}
}
