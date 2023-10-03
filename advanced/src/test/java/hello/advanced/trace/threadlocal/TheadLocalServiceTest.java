package hello.advanced.trace.threadlocal;

import hello.advanced.trace.threadlocal.code.FieldService;
import hello.advanced.trace.threadlocal.code.ThreadLocalService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class TheadLocalServiceTest {

	private ThreadLocalService threadLocalService = new ThreadLocalService();

	@Test
	void field() {
		log.info("main start");

		// threadA가 실행할 로직
		Runnable userA = () -> {
			threadLocalService.logic("userA");
		};

		// threadB가 실행할 로직
		Runnable userB = () -> {
			threadLocalService.logic("userB");
		};

		Thread threadA = new Thread(userA);
		threadA.setName("thread-A");
		Thread threadB = new Thread(userB);
		threadB.setName("thread-B");

		threadA.start();
//		sleep(2000); // threadA가 끝난 후, threadB 실행 -> 동시성 문제 발생 X
		sleep(100); // threadA가 끝나기 전에 threadB 실행 -> threadLocal을 통해 쓰레드 별로 다른 저장소를 사용한다. 동시성 문제 발생 X
		threadB.start();
		sleep(3000); // 메인 쓰레드 종료 대기

		// sleep(2000)
		// 18:27:33.836 [thread-A] - 저장 name=userA -> nameStore=null
		// 18:27:34.842 [thread-A] - 조회 nameStore=userA
		// 18:27:35.849 [thread-B] - 저장 name=userB -> nameStore=null <- 쓰레드별로 별개의 저장소를 사용하기 때문에 null인 상태이다.
		// 18:27:36.856 [thread-B] - 조회 nameStore=userB

		// sleep(100)
		// 18:11:12.056 [thread-A] - 저장 name=userA -> nameStore=null
		// 18:11:12.165 [thread-B] - 저장 name=userB -> nameStore=null <- 쓰레드별로 별개의 저장소를 사용하기 때문에 null인 상태이다.
		// 18:11:13.067 [thread-A] - 조회 nameStore=userA
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
