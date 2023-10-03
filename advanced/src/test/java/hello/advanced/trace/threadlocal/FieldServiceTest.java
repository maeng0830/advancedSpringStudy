package hello.advanced.trace.threadlocal;

import hello.advanced.trace.threadlocal.code.FieldService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class FieldServiceTest {

	private FieldService fieldService = new FieldService();

	@Test
	void field() {
		log.info("main start");

		// threadA가 실행할 로직
		Runnable userA = () -> {
			fieldService.logic("userA");
		};

		// threadB가 실행할 로직
		Runnable userB = () -> {
			fieldService.logic("userB");
		};

		Thread threadA = new Thread(userA);
		threadA.setName("thread-A");
		Thread threadB = new Thread(userB);
		threadB.setName("thread-B");

		threadA.start();
//		sleep(2000); // threadA가 끝난 후, threadB 실행 -> 동시성 문제 발생 X
		sleep(100); // threadA가 끝나기 전에 threadB 실행 -> 동시성 문제 발생
		threadB.start();
		sleep(3000); // 메인 쓰레드 종료 대기

		// sleep(2000)
		// 18:05:43.250 [thread-A] - 저장 name=userA -> nameStore=null
		// 18:05:44.266 [thread-A] - 조회 nameStore=userA
		// 18:05:45.258 [thread-B] - 저장 name=userB -> nameStore=userA
		// 18:05:46.264 [thread-B] - 조회 nameStore=userB

		// sleep(100)
		// 18:11:12.056 [thread-A] - 저장 name=userA -> nameStore=null
		// 18:11:12.165 [thread-B] - 저장 name=userB -> nameStore=userA
		// 18:11:13.067 [thread-A] - 조회 nameStore=userB
		// 18:11:13.172 [thread-B] - 조회 nameStore=userB
	}

	private void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
