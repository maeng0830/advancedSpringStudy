package hello.advanced.trace.hellotrace;

import static org.junit.jupiter.api.Assertions.*;

import hello.advanced.trace.TraceStatus;
import org.junit.jupiter.api.Test;

class HelloTraceV1Test {

	@Test
	void begin_end() {
		HelloTraceV1 trace = new HelloTraceV1();

		TraceStatus status = trace.begin("hello"); // 시작 로그
		trace.end(status); // 종료 로그
	}

	@Test
	void begin_exception() {
		HelloTraceV1 trace = new HelloTraceV1();

		TraceStatus status = trace.begin("hello"); // 시작 로그
		trace.exception(status, new IllegalStateException()); // 종료 로그 + 예외 정보
	}
}