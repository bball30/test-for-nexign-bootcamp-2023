import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

public class CDR {
    private String callType;
    private Date dateTimeCallStart;
    private Date dateTimeCallEnd;

    private Double cost;
    private Long callTime;

    public CDR(String callType, String dateTimeCallStart, String dateTimeCallEnd) {
        this.callType = callType;

        DateFormat format = new SimpleDateFormat( "yyyyMMddHHmmss" );
        try {
            this.dateTimeCallStart = format.parse(dateTimeCallStart);
            this.dateTimeCallEnd = format.parse(dateTimeCallEnd);
            this.callTime = (this.dateTimeCallEnd.getTime() - this.dateTimeCallStart.getTime()) / 1000 / 60 + 1;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public String getCallType() {
        return callType;
    }

    public Date getDateTimeCallStart() {
        return dateTimeCallStart;
    }

    public Date getDateTimeCallEnd() {
        return dateTimeCallEnd;
    }

    public Double getCost() {
        return cost;
    }

    public Long getCallTime() {
        return callTime;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public void setDateTimeCallStart(Date dateTimeCallStart) {
        this.dateTimeCallStart = dateTimeCallStart;
    }

    public void setDateTimeCallEnd(Date dateTimeCallEnd) {
        this.dateTimeCallEnd = dateTimeCallEnd;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public void setCallTime(Long callTime) {
        this.callTime = callTime;
    }
}
