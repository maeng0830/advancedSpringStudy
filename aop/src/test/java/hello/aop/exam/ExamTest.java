package hello.aop.exam;

import hello.aop.exam.aop.RetryAspect;
import hello.aop.exam.aop.TraceAspect;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
//@Import(TraceAspect.class)
@Import({TraceAspect.class, RetryAspect.class})
@SpringBootTest
public class ExamTest {

	@Autowired
	ExamService examService;

	@Test
	void test() {
		for (int i = 0; i < 5; i++) {
			log.info("client request i={}", i);
			examService.request("data" + i);
		}
//		client request i=0
//		[trace] void hello.aop.exam.ExamService.request(String) args=[data0]
//		[trace] String hello.aop.exam.ExamRepository.save(String) args=[data0]
//		[retry] String hello.aop.exam.ExamRepository.save(String) retry=@hello.aop.exam.annotation.Retry(value=4)
//		[retry] try count=1/4
//		client request i=1
//		[trace] void hello.aop.exam.ExamService.request(String) args=[data1]
//		[trace] String hello.aop.exam.ExamRepository.save(String) args=[data1]
//		[retry] String hello.aop.exam.ExamRepository.save(String) retry=@hello.aop.exam.annotation.Retry(value=4)
//		[retry] try count=1/4
//		client request i=2
//		[trace] void hello.aop.exam.ExamService.request(String) args=[data2]
//		[trace] String hello.aop.exam.ExamRepository.save(String) args=[data2]
//		[retry] String hello.aop.exam.ExamRepository.save(String) retry=@hello.aop.exam.annotation.Retry(value=4)
//		[retry] try count=1/4
//		client request i=3
//		[trace] void hello.aop.exam.ExamService.request(String) args=[data3]
//		[trace] String hello.aop.exam.ExamRepository.save(String) args=[data3]
//		[retry] String hello.aop.exam.ExamRepository.save(String) retry=@hello.aop.exam.annotation.Retry(value=4)
//		[retry] try count=1/4
//		client request i=4
//		[trace] void hello.aop.exam.ExamService.request(String) args=[data4]
//		[trace] String hello.aop.exam.ExamRepository.save(String) args=[data4]
//		[retry] String hello.aop.exam.ExamRepository.save(String) retry=@hello.aop.exam.annotation.Retry(value=4)
//		[retry] try count=1/4
//		[retry] try count=2/4
	}
}
