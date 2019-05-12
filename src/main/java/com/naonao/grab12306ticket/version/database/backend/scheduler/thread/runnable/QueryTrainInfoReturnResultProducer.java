package com.naonao.grab12306ticket.version.database.backend.scheduler.thread.runnable;

import com.naonao.grab12306ticket.version.database.backend.constants.TaskStatusName;
import com.naonao.grab12306ticket.version.database.backend.database.Database;
import com.naonao.grab12306ticket.version.database.backend.database.table.StatusInformationTable;
import com.naonao.grab12306ticket.version.database.backend.resultclass.ticket.query.QueryTrainInfoReturnResult;
import com.naonao.grab12306ticket.version.database.backend.scheduler.arguments.QueryTrainInfoArguments;
import com.naonao.grab12306ticket.version.database.backend.scheduler.queue.QueryTrainInfoReturnResultQueue;
import com.naonao.grab12306ticket.version.database.backend.test.CreateQueryTrainInfoReturnResult;
import com.naonao.grab12306ticket.version.database.backend.ticket.query.QueryTrainInfo;
import com.naonao.grab12306ticket.version.database.backend.tools.GeneralTools;
import com.naonao.grab12306ticket.version.database.backend.tools.HttpTools;
import lombok.extern.log4j.Log4j;


/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-07 22:06
 **/
@Log4j
public class QueryTrainInfoReturnResultProducer implements Runnable{






    private QueryTrainInfoReturnResultQueue queryTrainInfoReturnResultQueue;
    private QueryTrainInfoArguments queryTrainInfoArguments;
    private Database database;

    public QueryTrainInfoReturnResultProducer(QueryTrainInfoReturnResultQueue queryTrainInfoReturnResultQueue,
                                              QueryTrainInfoArguments queryTrainInfoArguments){
        this.queryTrainInfoReturnResultQueue = queryTrainInfoReturnResultQueue;
        this.queryTrainInfoArguments = queryTrainInfoArguments;
        database = new Database();
    }

    /**
     * database version
     * according to arguments execute queryTrainInfo method, then put result to queue.
     */
    @Override
    public void run() {
        // produce
        QueryTrainInfo queryTrainInfo = new QueryTrainInfo(HttpTools.getSession(30000));
        QueryTrainInfoReturnResult queryTrainInfoReturnResult = queryTrainInfo.queryTrainInfo(queryTrainInfoArguments);
        updateDatabase(TaskStatusName.RUNNING, queryTrainInfoReturnResult.getStatusInformationTable());
        queryTrainInfoReturnResultQueue.producer(queryTrainInfoReturnResult);
        log.info("put a information to queue, hash ---> " + queryTrainInfoArguments.getHash());
        // test
        // test();
    }

    private void test(){
        CreateQueryTrainInfoReturnResult createQueryTrainInfoReturnResult = new CreateQueryTrainInfoReturnResult(queryTrainInfoArguments);
        QueryTrainInfoReturnResult queryTrainInfoReturnResult = createQueryTrainInfoReturnResult.queryTrainInfoReturnResult();
        updateDatabase(TaskStatusName.RUNNING, queryTrainInfoReturnResult.getStatusInformationTable());
        queryTrainInfoReturnResultQueue.producer(createQueryTrainInfoReturnResult.queryTrainInfoReturnResult());
    }

    private void updateDatabase(TaskStatusName statusName, StatusInformationTable statusInformationTable){
        String status = statusName.getTaskStatusName();
        String lastRunningTime = GeneralTools.formatTime();
        String endTime = GeneralTools.formatTime();
        Long tryCount = statusInformationTable.getTryCount() + 1L;
        String message = statusName.getTaskStatusName();
        statusInformationTable.setStatus(status);
        statusInformationTable.setTaskLastRunTime(lastRunningTime);
        statusInformationTable.setTaskEndTime(endTime);
        statusInformationTable.setTryCount(tryCount);
        statusInformationTable.setMessage(message);
        database.update().statusInformation(statusInformationTable);
    }

}
