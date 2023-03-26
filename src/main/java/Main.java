import java.io.*;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        List<String> input = readData();
        Map<String, Sub> subs = new HashMap<>();
        for (String cdrString: input) {
            String[] strArr = cdrString.replace(" ", "").split(",");
            Sub currentSub = subs.get(strArr[1]);
            if (currentSub == null) {
                System.out.println(strArr[2]);
                subs.put(strArr[1], new Sub(new CDR(strArr[0], strArr[2], strArr[3]), strArr[4]));
            } else {
                currentSub.addNewCall(new CDR(strArr[0], strArr[2], strArr[3]));
            }
        }
        
        writeData(subs);
    }

    private static List<String> readData() {
        List<String> strings = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("cdr.txt"))) {
            String curLine = br.readLine();
            while (curLine != null) {
                strings.add(curLine);
                curLine = br.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return strings;
    }
    
    private static void writeData(Map<String, Sub> subs) {
        DateFormat format = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        NumberFormat numberFormat = new DecimalFormat("#0.00");
        for (Map.Entry<String, Sub> tmp : subs.entrySet()) {
            try (BufferedWriter outputFile = new BufferedWriter(new FileWriter("reports/" + tmp.getKey() + "_report.txt"))) {
                Sub currentSub = tmp.getValue();
                StringBuilder outputData = new StringBuilder();
                outputData.append("Tariff index: ").append(currentSub.getSubscriptionRate()).append("\n");
                outputData.append("---------------------------------------------------------------------------\n");
                outputData.append("Report for phone number ").append(tmp.getKey()).append(":\n");
                outputData.append("---------------------------------------------------------------------------\n");
                outputData.append("  Call Type |   Start Time        |     End Time        | Duration | Cost \n");
                for (CDR currentCall : currentSub.getCalls()) {
                    outputData.append("     ").append(currentCall.getCallType()).append("     | ");
                    outputData.append(format.format(currentCall.getDateTimeCallStart())).append(" | ");
                    outputData.append(format.format(currentCall.getDateTimeCallEnd())).append(" | ");
                    Duration duration = Duration.between(currentCall.getDateTimeCallStart().toInstant(), currentCall.getDateTimeCallEnd().toInstant());
                    outputData.append(String.format("%02d:%02d:%02d", duration.toHoursPart(), duration.toMinutesPart(), duration.toSecondsPart())).append(" | ");
                    outputData.append(numberFormat.format(currentCall.getCost()));
                    currentSub.setCash(currentSub.getCash() + currentCall.getCost());

                    outputData.append("\n");
                }
                outputData.append("---------------------------------------------------------------------------\n");
                outputData.append("                                            Total Cost: | ").
                        append(numberFormat.format(currentSub.getCash())).append(" rubles |\n");
                outputData.append("---------------------------------------------------------------------------\n");

                outputFile.write(outputData.toString());
                outputFile.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
