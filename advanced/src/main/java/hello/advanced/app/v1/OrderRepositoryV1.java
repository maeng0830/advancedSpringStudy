package hello.advanced.app.v1;

import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.hellotrace.HelloTraceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryV1 {

	private final HelloTraceV1 trace;

	public void save(String itemId) {
		TraceStatus status = null;

		try {
			status = trace.begin("OrderRepository.save()");

			// 저장 로직
			if (itemId.equals("ex")) {
				throw new IllegalArgumentException("예외 발생");
			}
			sleep(1000);

			trace.end(status);
		} catch (Exception e) {
			trace.exception(status, e);
			throw e; // 로그 기능을 위해 예외를 여기서 막으면 안된다. 다시 던져줘야한다.
		}
	}

	private void sleep(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
