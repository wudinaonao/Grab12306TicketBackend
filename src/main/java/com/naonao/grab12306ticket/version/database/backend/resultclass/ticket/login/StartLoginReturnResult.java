package com.naonao.grab12306ticket.version.database.backend.resultclass.ticket.login;

import com.naonao.grab12306ticket.version.database.backend.resultclass.IReturnResult;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-04-30 22:59
 **/
@Getter
@Setter
public class StartLoginReturnResult implements IReturnResult {

    private Boolean status;
    private String message;
    private CloseableHttpClient session;

    private String uamtk;

}
