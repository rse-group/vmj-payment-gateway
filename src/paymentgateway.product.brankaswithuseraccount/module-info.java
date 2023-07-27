module paymentgateway.product.brankaswithuseraccount {
    requires vmj.routing.route;
    requires vmj.hibernate.integrator;
    
    requires net.bytebuddy;
    requires java.xml.bind;
    requires com.sun.xml.bind;
    requires com.fasterxml.classmate;
    requires jdk.unsupported;

    requires paymentgateway.apiauth.core;
    requires paymentgateway.apiauth.basicauth;
    requires paymentgateway.apiauth.apikey;
    requires paymentgateway.client.core;
    requires paymentgateway.client.oy;
    requires paymentgateway.client.brankas;
    requires paymentgateway.fundtransfer.core;
    requires paymentgateway.fundtransfer.withuseraccount;
	
	requires prices.auth.vmj;
    requires prices.auth.vmj.model;
}