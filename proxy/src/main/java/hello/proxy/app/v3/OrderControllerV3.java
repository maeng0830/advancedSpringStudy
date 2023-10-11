package hello.proxy.app.v3;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController // 컴포넌트 스캔으로 빈 자동 등록
public class OrderControllerV3 {

	private final OrderServiceV3 orderService;

	public OrderControllerV3(OrderServiceV3 orderService) {
		this.orderService = orderService;
	}

	@GetMapping("/v3/request")
	public String request(@RequestParam String itemId) {
		orderService.orderItem(itemId);
		return "ok";
	}

	@GetMapping("/v3/no-log")
	public String noLog() {
		return "ok";
	}
}
