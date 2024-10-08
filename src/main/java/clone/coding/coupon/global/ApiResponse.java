package clone.coding.coupon.global;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString(of = {"success", "data", "error"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiResponse<T> {

    private boolean success;

    private T data;

    private ApiError error;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime serverDateTime;

    private ApiResponse(boolean success, T data, ApiError error) {
        this.success = success;
        this.data = data;
        this.error = error;
        this.serverDateTime = LocalDateTime.now();
    }

    public static <T> ApiResponse<T> success() {
        return success(null);
    }

    public static <T> ApiResponse<T> success(T response) {
        return new ApiResponse<>(true, response, null);
    }

    public static ApiResponse<?> error(String code, String message) {
        return new ApiResponse<>(false, null, new ApiError(code, message));
    }


    @Getter
    @ToString(of = {"code", "message"})
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    protected static class ApiError {

        private String code;

        private String message;

        public ApiError(String code, String message) {
            this.code = code;
            this.message = message;
        }
    }
}
