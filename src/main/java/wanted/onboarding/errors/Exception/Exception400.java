package wanted.onboarding.errors.Exception;

import org.springframework.http.HttpStatus;
import wanted.onboarding.Utils.ApiUtils;

public class Exception400 extends RuntimeException{
    public Exception400(String message) {
        super(message);
    }

    public ApiUtils.ApiResult<?> body(){
        return ApiUtils.error(getMessage(), HttpStatus.BAD_REQUEST);
    }

    public HttpStatus status(){
        return HttpStatus.BAD_REQUEST;
    }
}
