package com.naonao.grab12306ticket.version.database.backend.resultclass.ticket.captcha;

import com.naonao.grab12306ticket.version.database.backend.resultclass.IReturnResult;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * @program: 12306grabticket_java
 * @description: CheckCaptchaReturnResult
 * @author: Wen lyuzhao
 * @create: 2019-04-29 18:31
 **/
@Getter
@Setter
public class CheckCaptchaReturnResult implements IReturnResult {

    private Boolean status;
    private String message;
    private CloseableHttpClient session;

}
