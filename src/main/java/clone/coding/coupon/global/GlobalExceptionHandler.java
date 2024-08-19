package clone.coding.coupon.global;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ApiResponse<?> handleMethodArgumentNotValidException(ConstraintViolationException ex) {

        String errorDetail = ex.getConstraintViolations().stream()
                .map(error -> {
                    String[] split = error.getPropertyPath().toString().split("\\.");
                    return String.format("%s: %s", split[split.length - 1], error.getMessage());
                })
                .collect(Collectors.joining(","));
        String errorMessage = String.format("%s", errorDetail);

        return ApiResponse.error("INVALID_INPUT_VALUE", errorMessage); // code형식 재지정 아마 ENUM 이용
    }
}
