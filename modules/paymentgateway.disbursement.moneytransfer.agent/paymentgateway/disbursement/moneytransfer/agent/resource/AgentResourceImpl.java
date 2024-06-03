package paymentgateway.disbursement.moneytransfer.agent;

import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

import java.nio.charset.StandardCharsets;

import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;

import java.text.SimpleDateFormat;

import paymentgateway.disbursement.DisbursementFactory;
import paymentgateway.disbursement.core.Disbursement;
import paymentgateway.disbursement.core.DisbursementResourceDecorator;
import paymentgateway.disbursement.core.DisbursementImpl;
import paymentgateway.disbursement.core.DisbursementResourceComponent;

public class AgentResourceImpl extends DisbursementResourceDecorator {

	public AgentResourceImpl(DisbursementResourceComponent record) {
		super(record);
	}

}
