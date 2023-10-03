package hello.advanced.trace.logtrace;

import static org.junit.jupiter.api.Assertions.*;

import hello.advanced.trace.TraceStatus;
import org.junit.jupiter.api.Test;

class FieldLogTraceTest {

	FieldLogTrace trace = new FieldLogTrace();

	@Test
	void begin_end_level2() {
		TraceStatus status1 = trace.begin("hello1");
		TraceStatus status2 = trace.begin("hello2");
		trace.end(status2);
		trace.end(status1);
//		[7bc7eedf] hello1
//		[7bc7eedf] |-->hello2
//		[7bc7eedf] |<--hello2 time=2ms
//		[7bc7eedf] hello1 time=7ms
	}

	@Test
	void begin_exception_level2() {
		TraceStatus status1 = trace.begin("hello1");
		TraceStatus status2 = trace.begin("hello2");
		trace.exception(status2, new IllegalStateException());
		trace.exception(status1, new IllegalStateException());
//		[dc098ea4] hello1
//		[dc098ea4] |-->hello2
//		[dc098ea4] |<X-hello2 time=2ms ex=java.lang.IllegalStateException
//		[dc098ea4] hello1 time=7ms ex=java.lang.IllegalStateException
	}
}