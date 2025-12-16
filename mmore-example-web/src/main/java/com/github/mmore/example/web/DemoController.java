package com.github.mmore.example.web;

import com.github.mmore.common.model.BizErrorType;
import com.github.mmore.common.model.BizException;
import com.github.mmore.common.model.SystemErrorType;
import com.github.mmore.common.model.SystemException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.github.mmore.example.web.dto.OrderVO;
import com.github.mmore.web.model.ApiResult;

@RestController
@RequestMapping("/demo")
public class DemoController {

    @GetMapping("/arg-error")
    public String argError() {
        throw new SystemException(SystemErrorType.ARGUMENT_NOT_VALID);
    }

    @GetMapping("/biz-error")
    public String bizError() {
        throw new BizException(BizErrorType.COMMON);
    }

    @GetMapping("/order")
    public ApiResult<OrderVO> order() {
        OrderVO vo = new OrderVO();
        vo.setId(1L);
        vo.setStatus(1);
        return ApiResult.success(vo);
    }
}
