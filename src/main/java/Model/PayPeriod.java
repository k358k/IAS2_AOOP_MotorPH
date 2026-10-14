package Model;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PayPeriod {
    private  int payPeriodId;
    private  LocalDate startDate;
    private  LocalDate endDate;

    public PayPeriod(LocalDate startDate, LocalDate endDate) {
        this.payPeriodId = payPeriodId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public  int getPayPeriodId() {
        return payPeriodId;
    }

    public  void setPayPeriodId(int payPeriodId) {
        this.payPeriodId = payPeriodId;
    }

    public  LocalDate getStartDate() {
        return startDate;
    }

    public  void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public  LocalDate getEndDate() {
        return endDate;
    }

    public  void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    
    
    
}