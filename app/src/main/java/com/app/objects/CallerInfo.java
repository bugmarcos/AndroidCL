package com.app.objects;

/**
 * Created by Akash on 09-May-15.
 */
public class CallerInfo {

    public String name;
    public String number;
    public long date;
    public long duration;
    public int callType;
    public long isRead;

    public CallerInfo(String name, String number, long date, long duration, int callType, long isRead) {
        this.name = name;
        this.number = number;
        this.date = date;
        this.duration = duration;
        this.callType = callType;
        this.isRead = isRead;
    }

    public CallerInfo() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getCallType() {
        return callType;
    }

    public void setCallType(int callType) {
        this.callType = callType;
    }

    public long getIsRead() {
        return isRead;
    }

    public void setIsRead(long isRead) {
        this.isRead = isRead;
    }

    @Override
    public String toString() {
        return "CallerInfo{" +
                "name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", date=" + date +
                ", duration=" + duration +
                ", callType=" + callType +
                ", isRead=" + isRead +
                '}';
    }
}
