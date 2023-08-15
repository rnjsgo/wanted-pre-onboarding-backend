package wanted.onboarding.errors.Exception;

import org.springframework.http.HttpStatus;
import wanted.onboarding.Utils.ApiUtils;

public class Exception404 extends RuntimeException{
    public Exception404(String message) {
        super(message);
    }

    public ApiUtils.ApiResult<?> body(){
        return ApiUtils.error(getMessage(), HttpStatus.NOT_FOUND);
    }

    public HttpStatus status(){
        return HttpStatus.NOT_FOUND;
    }
}
