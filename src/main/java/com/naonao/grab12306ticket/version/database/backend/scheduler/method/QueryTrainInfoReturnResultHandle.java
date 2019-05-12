package com.naonao.grab12306ticket.version.database.backend.scheduler.method;

import com.naonao.grab12306ticket.version.database.backend.constants.TaskStatusName;
import com.naonao.grab12306ticket.version.database.backend.database.table.GrabTicketInformationTable;
import com.naonao.grab12306ticket.version.database.backend.database.table.NotifyInformationTable;
import com.naonao.grab12306ticket.version.database.backend.database.table.StatusInformationTable;
import com.naonao.grab12306ticket.version.database.backend.notify.Notify;
import com.naonao.grab12306ticket.version.database.backend.resultclass.ticket.login.LoginReturnResult;
import com.naonao.grab12306ticket.version.database.backend.resultclass.ticket.query.QueryTrainInfoReturnResult;
import com.naonao.grab12306ticket.version.database.backend.scheduler.common.AbstractScheduler;
import com.naonao.grab12306ticket.version.database.backend.ticket.booking.Booking;
import com.naonao.grab12306ticket.version.database.backend.resultclass.ticket.booking.BookingReturnResult;
import com.naonao.grab12306ticket.version.database.backend.ticket.login.Login;
import com.naonao.grab12306ticket.version.database.backend.tools.ComputeHash;
import com.naonao.grab12306ticket.version.database.backend.tools.GeneralTools;
import com.naonao.grab12306ticket.version.database.backend.database.Database;
import com.naonao.grab12306ticket.version.database.backend.tools.HttpTools;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.http.impl.client.CloseableHttpClient;


import java.util.Set;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-08 22:13
 **/
@Log4j
public class QueryTrainInfoReturnResultHandle extends AbstractScheduler {

    /**
     * here is QueryTrainInfoReturnResult handle method
     */
    private Database database;
    private BookingReturnResult bookingReturnResult;

    private QueryTrainInfoReturnResult queryTrainInfoReturnResult;


    /**
     * database version
     * @param queryTrainInfoReturnResult    queryTrainInfoReturnResult
     */
    public QueryTrainInfoReturnResultHandle(QueryTrainInfoReturnResult queryTrainInfoReturnResult){
        this.queryTrainInfoReturnResult = queryTrainInfoReturnResult;
        this.database = new Database();
    }


    /**
     * database version
     *
     * QueryTrainInfoReturnResult handle method
     * first get a QueryTrainInfoReturnResult object
     *
     *  1. check queryTrainInfoReturnResult is true, else update database.
     *  2. check bookingReturnResult is true, else update database.
     *  3. check send notification is true, else update database.
     *  4. booking succeed, update database.
     *
     */
    public void handle(){
        // database version
        StatusInformationTable statusInformationTable = queryTrainInfoReturnResult.getStatusInformationTable();
        // not found eligible train information
        if (!isTure(queryTrainInfoReturnResult)){
            updateDatabase(TaskStatusName.WAIT, statusInformationTable);
            return;
        }
        // booking failed
        if (!booking(queryTrainInfoReturnResult)){
            updateDatabase(TaskStatusName.BOOKING_FAILED, statusInformationTable);
            return;
        }
        // send notification failed
        if (!sendNotification(bookingReturnResult, queryTrainInfoReturnResult.getNotifyInformationTable())){
            updateDatabase(TaskStatusName.BOOKING_SUCCEED_BUT_SEND_NOTIFICATION_FAILED, statusInformationTable);
            return;
        }
        // success
        updateDatabase(TaskStatusName.COMPLETED, statusInformationTable);
    }


    /**
     * check queryTrainInfoReturnResult is true.
     * if true, then have train information.
     *
     * @param queryTrainInfoReturnResult    queryTrainInfoReturnResult
     * @return                              Boolean
     */
    private Boolean isTure(QueryTrainInfoReturnResult queryTrainInfoReturnResult){
        return queryTrainInfoReturnResult.getStatus();
    }

    /**
     * booking ticket
     *
     * @param queryTrainInfoReturnResult    queryTrainInfoReturnResult
     * @return                              Boolean
     */
    private Boolean booking(QueryTrainInfoReturnResult queryTrainInfoReturnResult){

        // // produce
        //
        // String username12306 = queryTrainInfoReturnResult.getUserInformationTable().getUsername12306();
        // String password12306 = queryTrainInfoReturnResult.getUserInformationTable().getPassword12306();
        // // Login
        // CloseableHttpClient session = HttpTools.getSession(30000);
        // Login login = new Login(session);
        // LoginReturnResult loginReturnResult = login.login(username12306, password12306);
        // if (!loginReturnResult.getStatus()){
        //     log.error(loginReturnResult.getMessage());
        //     return false;
        // }
        // session = loginReturnResult.getSession();
        // // Booking
        // Booking booking = new Booking(session);
        // BookingReturnResult bookingReturnResult = booking.booking(queryTrainInfoReturnResult);
        // if (!bookingReturnResult.getStatus()){
        //     log.error(bookingReturnResult.getMessage());
        //     return false;
        // }
        // this.bookingReturnResult = bookingReturnResult;
        // return true;

        // test
        int a = RandomUtils.nextInt(0, 100);
        if (a % 2 == 0){
            return true;
        }
        return true;
    }

    /**
     * send notification
     *
     * @param bookingReturnResult       bookingReturnResult
     * @param notifyInformationTable    notifyInformationTable
     * @return                          Boolean
     */
    private Boolean sendNotification(BookingReturnResult bookingReturnResult, NotifyInformationTable notifyInformationTable){

        // produce
        // here is send notification method
        // if (bookingReturnResult == null){
        //     log.error("bookingReturnResult is null.");
        //     return false;
        // }
        return new Notify().sendNotification(bookingReturnResult, notifyInformationTable);

        // // test
        // int a = RandomUtils.nextInt(0, 100);
        // if (a % 2 == 0){
        //     return true;
        // }
        // return false;
    }

    /**
     * update database
     *
     * @param statusName                statusName
     * @param statusInformationTable    statusInformationTable
     */
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
