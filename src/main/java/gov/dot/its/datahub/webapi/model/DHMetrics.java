package gov.dot.its.datahub.webapi.model;

import java.security.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

public class DHMetrics {

    @JsonFormat(shape = Shape.STRING)
    private Timestamp timeStamp;
    private String dhId;
    private String dhSourceName;
    private String userSegment;
    private String accessType;
    private int count;

    public DHMetrics() {
    }
    
    public DHMetrics(Timestamp timeStamp, String dhId, String dhSourceName, String userSegment, String accessType,
            int count) {
        this.timeStamp = timeStamp;
        this.dhId = dhId;
        this.dhSourceName = dhSourceName;
        this.userSegment = userSegment;
        this.accessType = accessType;
        this.count = count;
    }
    
    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getDhId() {
        return dhId;
    }

    public void setDhId(String dhId) {
        this.dhId = dhId;
    }

    public String getDhSourceName() {
        return dhSourceName;
    }

    public void setDhSourceName(String dhSourceName) {
        this.dhSourceName = dhSourceName;
    }

    public String getUserSegment() {
        return userSegment;
    }

    public void setUserSegment(String userSegment) {
        this.userSegment = userSegment;
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
