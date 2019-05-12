package com.naonao.grab12306ticket.version.database.backend.scheduler;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.naonao.grab12306ticket.version.database.backend.scheduler.common.AbstractScheduler;
import com.naonao.grab12306ticket.version.database.backend.scheduler.queue.QueryTrainInfoReturnResultQueue;
import com.naonao.grab12306ticket.version.database.backend.scheduler.thread.strategy.RejectExecutionHandlerBlocking;
import com.naonao.grab12306ticket.version.database.backend.scheduler.thread.runnable.pool.ProducerPool;
import com.naonao.grab12306ticket.version.database.backend.scheduler.thread.runnable.pool.ConsumerPool;
import lombok.extern.log4j.Log4j;

import java.util.concurrent.*;


/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-08 17:37
 **/
@Log4j
public class Scheduler extends AbstractScheduler {

    private void start(){
        ExecutorService pool = createThreadPool();
        QueryTrainInfoReturnResultQueue queryTrainInfoReturnResultQueue = createQueryTrainInfoReturnResultQueue();
        pool.execute(new ConsumerPool(queryTrainInfoReturnResultQueue));
        pool.execute(new ProducerPool(queryTrainInfoReturnResultQueue));
    }

    /**
     * create a thread pool for start ProducerPool and ConsumePool
     *
     * @return  ExecutorService
     */
    private ExecutorService createThreadPool(){
        int corePoolSize = 2;
        int maximumPoolSize = 2;
        long keepAliveTime = 60L;
        int queueSize = 4;
        // create thread pool
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("Scheduler [%d]")
                .build();
        return new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(queueSize),
                threadFactory,
                new RejectExecutionHandlerBlocking()
        );
    }

    /**
     * create a QueryTrainInfoReturnResultQueue for storage QueryTrainInfoReturnResult
     *
     * @return  QueryTrainInfoReturnResultQueue
     */
    private QueryTrainInfoReturnResultQueue createQueryTrainInfoReturnResultQueue(){
        return new QueryTrainInfoReturnResultQueue(128);
    }


    public static void main(String[] args) {
        Scheduler scheduler = new Scheduler();
        scheduler.start();
    }

}
