package com.example.guliMall.product.exception;

import com.example.common.exception.CodeEnume;
import com.example.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * @author cc
 * @date 2023年02月27日 23:41
 */
@Slf4j
//@ResponseBody
@RestControllerAdvice(basePackages = "com.example.guliMall.product.controller")
public class ExceptionControllerAdvice {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R ValidHandler(MethodArgumentNotValidException e){
        log.error("数据校验出现问题",e.getMessage().getClass());
        BindingResult bindingResult = e.getBindingResult();
        Map<String,String> errorMap = new HashMap<>();
        bindingResult.getFieldErrors().forEach((fieldError -> {
            errorMap.put(fieldError.getField() ,fieldError.getDefaultMessage());
        }));


        return R.error(CodeEnume.VALID_EXCEPTION.getCode(),CodeEnume.VALID_EXCEPTION.getMsg()).put("data", errorMap);
    }


    @ExceptionHandler(value = Throwable.class)
    public R handleException(){
        return R.error(CodeEnume.VALID_EXCEPTION.getCode(),CodeEnume.VALID_EXCEPTION.getMsg());
    }
}
