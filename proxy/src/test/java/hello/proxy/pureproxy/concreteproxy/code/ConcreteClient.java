package hello.proxy.pureproxy.concreteproxy.code;

public class ConcreteClient {

	private ConcreteLogic concreteLogic; // 서버 객체(ConcreteLogic), 프록시 객체(TimeProxy) 모두 주입 가능

	public ConcreteClient(ConcreteLogic concreteLogic) {
		this.concreteLogic = concreteLogic;
	}

	public void execute() {
		concreteLogic.operation();
	}
}
