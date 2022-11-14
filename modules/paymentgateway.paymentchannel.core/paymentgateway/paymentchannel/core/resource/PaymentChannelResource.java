package paymentgateway.paymentchannel.core;
import java.util.*;

public interface PaymentChannelResource {
    public PaymentChannel createChannel(HashMap<String,Object> pgExchange);
}
