package com.aviv_pos.olgats.avivitemquery.beans;

/**
 * Created by olgats on 08/12/2015.
 */
public class Session {
    public static final int SUCCESS = 0;
    public static final int STOCKTAKING_DOESNT_EXIST = 1;
    public static final int ACCESS_DENIED = 2;
    public static final int DB_ERROR = 3;
    public static final int SESSION_ALREADY_EXISTS = 4;
    public static final int CLIENT_CONNECTION_ERROR = 5;
    public static final int EMPTY_PWD = 6;
    public static final int OVER_LIMIT = 7;
    public static final int PRIVATE_WS_NOT_REACHABLE=8;
    private int id;
    private int status;
    private int accountId;
    private int counterId;
    private String counterName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getCounterId() {
        return counterId;
    }

    public void setCounterId(int counterId) {
        this.counterId = counterId;
    }

    public String getCounterName() {
        return counterName;
    }

    public void setCounterName(String counterName) {
        this.counterName = counterName;
    }

    @Override
    public String toString() {
        return "Session{" +
                "id=" + id +
                ", status=" + status +
                ", accountId=" + accountId +
                ", counterId=" + counterId +
                ", counterName='" + counterName + '\'' +
                '}';
    }
}
