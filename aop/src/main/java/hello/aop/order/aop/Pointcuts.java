package hello.aop.order.aop;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {

	/**
	 * 포인트컷들을 외부에 모아 놓고, 필요할 때 참조하여 사용할 수 있다.
	 * 다만 접근 제한자를 public으로 설정해줘야한다.
	 */

	// hello.aop.order 패키지와 하위 패키지
	@Pointcut("execution(* hello.aop.order..*(..))") // 포인트컷 표현식
	public void allOrder() {} // 포인트컷 시그니처

	// 타입 패턴이 *Service
	@Pointcut("execution(* *..*Service.*(..))") // 포인트컷 표현식
	public void allService() {} // 포인트컷 시그니처

	// hello.aop.order 패키지와 하위 패키지 && 타입 패턴이 *Service
	@Pointcut("allOrder() && allService()") // 포인트컷 표현식을 포인트컷 시그니처를 조합해서 만들 수도 있다.
	public void orderAndService() {}
}
