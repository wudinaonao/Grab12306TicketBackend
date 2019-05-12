package com.naonao.grab12306ticket.version.database.backend.scheduler.thread.runnable;

import com.naonao.grab12306ticket.version.database.backend.resultclass.ticket.query.QueryTrainInfoReturnResult;
import com.naonao.grab12306ticket.version.database.backend.scheduler.method.QueryTrainInfoReturnResultHandle;
import lombok.extern.log4j.Log4j;


import java.util.Set;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-07 22:07
 **/
@Log4j
public class QueryTrainInfoReturnResultConsumer implements Runnable{





    private QueryTrainInfoReturnResult queryTrainInfoReturnResult;


    /**
     * database version
     * @param queryTrainInfoReturnResult    queryTrainInfoReturnResult
     */
    public QueryTrainInfoReturnResultConsumer(QueryTrainInfoReturnResult queryTrainInfoReturnResult){
        this.queryTrainInfoReturnResult = queryTrainInfoReturnResult;
    }

    // /**
    //  * single version
    //  * @param queryTrainInfoReturnResult    queryTrainInfoReturnResult
    //  * @param hashSet                       global hash set use to save task hash, make only one
    //  *                                      of the same tasks in progress, avoid unpredictable errors
    //  *                                      caused by concurrent bookings.
    //  */
    // public QueryTrainInfoReturnResultConsumer(QueryTrainInfoReturnResult queryTrainInfoReturnResult,
    //                                           Set<String> hashSet){
    //     this.queryTrainInfoReturnResult = queryTrainInfoReturnResult;
    //     this.hashSet = hashSet;
    // }

    /**
     * processing queryTrainInfoReturnResult
     */
    @Override
    public void run() {
        // here write queryTrainInfoReturnResult processing method after getting train information
        // database version
        if (queryTrainInfoReturnResult != null){
            log.info("get a information by queue, hash ---> " + queryTrainInfoReturnResult.getStatusInformationTable().getHash());
            new QueryTrainInfoReturnResultHandle(queryTrainInfoReturnResult).handle();
        }
        // single version
        // if (queryTrainInfoReturnResult != null){
        //     new QueryTrainInfoReturnResultHandle(queryTrainInfoReturnResult, hashSet).handle();
        // }
    }

}
