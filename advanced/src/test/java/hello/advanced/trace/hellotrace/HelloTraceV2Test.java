package hello.advanced.trace.hellotrace;

import hello.advanced.trace.TraceStatus;
import org.junit.jupiter.api.Test;

class HelloTraceV2Test {

	@Test
	void begin_end() {
		HelloTraceV2 trace = new HelloTraceV2();

		TraceStatus status1 = trace.begin("hello1"); // 레벨 0 시작 로그
		TraceStatus status2 = trace.beginSync(status1.getTraceId(), "hello2"); // 레벨 1 시작 로그
		trace.end(status2); // 레벨 1 종료 로그
		trace.end(status1); // 레벨 0 종료 로그
	}

	@Test
	void begin_exception() {
		HelloTraceV2 trace = new HelloTraceV2();

		TraceStatus status1 = trace.begin("hello1"); // 레벨 0 시작 로그
		TraceStatus status2 = trace.beginSync(status1.getTraceId(), "hello2"); // 레벨 1 시작 로그
		trace.exception(status2, new IllegalStateException()); // 레벨 1 종료 로그 + 예외
		trace.exception(status1, new IllegalStateException()); // 레벨 0 종료 로그 + 에ㅚ
	}
}