import java.util.ArrayList;
import java.util.List;

public class Sub {
    private List<CDR> calls;
    private final String subscriptionRate;
    private Long minutesInRate;
    private Double cash;

    Sub(CDR newCDR, String subscriptionRate) {
        this.subscriptionRate = subscriptionRate;
        switch (this.subscriptionRate) {
            case ("06"):
                this.minutesInRate = (long) (300 * 60 * 1000);
                this.cash += 100d;
                break;
            case ("03"):
                break;
            case ("11"):
                this.minutesInRate = (long) 100 * 60 * 1000;
                break;
        }
        calls = new ArrayList<>();
        this.addNewCall(newCDR);
    }

    public void addNewCall(CDR newCDR) {
        calls.add(newCDR);
        switch (this.subscriptionRate) {
            case ("03"):
                newCDR.setCost(newCDR.getCallTime() / 1000 / 60 * 1.5);
                break;
            case ("06"):
                newCDR.setCost(countCost(newCDR.getCallTime(), 1d, 0d));
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
            long minutes = callTime / 1000 / 60 + 1;
            result = (double) minutes * ratePrice;
        } else if (minutesInRate > 0) {
            if (minutesInRate < callTime) {
                minutesInRate = 0L;
                result = (double) (Math.abs(minutesInRate - callTime) / 1000 / 60) * ratePrice;
            } else {
                minutesInRate -= callTime;
                result = (double) callTime / 1000 / 60 * ratePriceSpecial;
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
