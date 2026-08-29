package entity;

public class Billing {

    private final double billAmount;
    private final String paidDate;
    private final String paymentMethod;

    public Billing(
            double billAmount,
            String paidDate,
            String paymentMethod) {

        this.billAmount = billAmount;
        this.paidDate = paidDate;
        this.paymentMethod = paymentMethod;
    }

    public double getBillAmount() {
        return billAmount;
    }

    public String getPaidDate() {
        return paidDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    @Override
    public String toString() {

        return "\n========== BILLING =========="
                + "\nBill Amount     : RM "
                + String.format("%.2f", billAmount)
                + "\nPaid Date       : "
                + paidDate
                + "\nPayment Method  : "
                + paymentMethod
                + "\n==============================";
    }

    
}
