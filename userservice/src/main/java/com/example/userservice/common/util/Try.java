package com.example.userservice.common.util;

import java.util.function.Function;

public class Try {

    /*
     * Stream 과 같은 Checked Exception 을 처리하기 위한 함수형 인터페이스
     */

    public static void excute(Subroutine subroutine) {
        excute(subroutine, RuntimeException::new);
    }

    public static <E extends RuntimeException> void excute(Subroutine subroutine, Function<Exception, E> exceptionWrapper) {
        try {
            subroutine.excute();
        } catch (Exception e) {
            throw exceptionWrapper.apply(e);
        }
    }

}
