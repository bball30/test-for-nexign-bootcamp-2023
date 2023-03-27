import java.util.ArrayList;
import java.util.List;

public class Sub {
    private List<CDR> calls;
    private final String subscriptionRate;
    private Long minutesInRate;
    private Double cash;

    Sub(CDR newCDR, String subscriptionRate) {
        this.subscriptionRate = subscriptionRate;
        this.cash = 0d;
        switch (this.subscriptionRate) {
            case ("06"):
                this.minutesInRate = 300L;
                this.cash += 100.0;
                break;
            case ("03"):
                break;
            case ("11"):
                this.minutesInRate = 100L;
                break;
        }
        calls = new ArrayList<>();
        this.addNewCall(newCDR);
    }

    public void addNewCall(CDR newCDR) {
        calls.add(newCDR);
        switch (this.subscriptionRate) {
            case ("03"):
                if (newCDR.getCallType().equals("02")) {
                    newCDR.setCost(0d);
                } else {
                    newCDR.setCost(newCDR.getCallTime() * 1.5);
                }
                break;
            case ("06"):
                if (newCDR.getCallType().equals("02")) {
                    newCDR.setCost(0d);
                } else {
                    newCDR.setCost(countCost(newCDR.getCallTime(), 1d, 0d));
                }
                break;
            case ("11"):
                if (newCDR.getCallType().equals("02")) {
                    newCDR.setCost(0d);
                } else {
                    newCDR.setCost(countCost(newCDR.getCallTime(), 1.5, 0.5));
                }
                break;
        }
    }

    private Double countCost(Long callTime, Double ratePrice, Double ratePriceSpecial) {
        double result = 0;
        if (minutesInRate == 0) {
            result = (double) callTime * ratePrice;
        } else if (minutesInRate > 0) {
            if (minutesInRate < callTime) {
                result = minutesInRate * ratePriceSpecial;
                callTime -= minutesInRate;
                minutesInRate = 0L;
                result += (double) callTime * ratePrice;
            } else {
                minutesInRate -= callTime;
                result = (double) callTime * ratePriceSpecial;
            }
        }
        return result;
    }

    public List<CDR> getCalls() {
        return calls;
    }

    public String getSubscriptionRate() {
        return subscriptionRate;
    }

    public Double getCash() {
        return cash;
    }

    public void setCash(Double cash) {
        this.cash = cash;
    }
}
