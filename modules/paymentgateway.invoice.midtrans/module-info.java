module paymentgateway.invoice.midtrans {
    exports paymentgateway.invoice.midtrans;
    requires paymentgateway.invoice.core;

    requires java.naming;
    requires vmj.hibernate.integrator;
    requires vmj.routing.route;
    requires prices.auth.vmj;

    requires com.midtrans;
    uses com.midtrans.Midtrans;
    uses com.midtrans.httpclient.SnapApi;
    uses com.midtrans.httpclient.error.MidtransError;
}