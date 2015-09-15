package eu.motogymkhana.competition.api;

public class GymkhanaRequest {

    private String customerCode;

    private String password;

    public GymkhanaRequest() {
    }

    public GymkhanaRequest(String customerCode, String password) {
        this.customerCode = customerCode;
        this.password = password;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }
}
