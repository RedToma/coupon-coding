package clone.coding.coupon.global.exception;

import clone.coding.coupon.global.ApiResponse;
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
    public ApiResponse<?> handleConstraintViolationException(ConstraintViolationException ex) {

        String errorDetail = ex.getConstraintViolations().stream()
                .map(error -> {
                    String[] field = error.getPropertyPath().toString().split("\\.");
                    return String.format("%s: %s", field[field.length - 1], error.getMessage());
                })
                .collect(Collectors.joining(","));
        String errorMessage = String.format("%s", errorDetail);

        return ApiResponse.error("INVALID_INPUT_VALUE", errorMessage);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ApiResponse<?> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ApiResponse.error(ex.getErrorCode().getCode(), ex.getMessage());
    }


}
