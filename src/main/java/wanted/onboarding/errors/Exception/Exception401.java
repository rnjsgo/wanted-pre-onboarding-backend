package wanted.onboarding.errors.Exception;

import org.springframework.http.HttpStatus;
import wanted.onboarding.Utils.ApiUtils;

public class Exception401 extends RuntimeException{
    public Exception401(String message) {
        super(message);
    }

    public ApiUtils.ApiResult<?> body(){
        return ApiUtils.error(getMessage(), HttpStatus.UNAUTHORIZED);
    }

    public HttpStatus status(){
        return HttpStatus.UNAUTHORIZED;
    }
}
