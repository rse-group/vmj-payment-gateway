package paymentgateway.disbursement.special;

import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLEncoder;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import java.nio.charset.StandardCharsets;

import vmj.routing.route.Route;
import vmj.routing.route.VMJExchange;
import vmj.routing.route.exceptions.*;

import java.text.SimpleDateFormat;

import paymentgateway.disbursement.DisbursementFactory;
import paymentgateway.disbursement.core.Disbursement;
import paymentgateway.disbursement.core.DisbursementResourceDecorator;
import paymentgateway.disbursement.core.DisbursementImpl;
import paymentgateway.disbursement.core.DisbursementResourceComponent;
import paymentgateway.disbursement.core.DisbursementServiceComponent;

import paymentgateway.config.core.Config;
import paymentgateway.config.ConfigFactory;

public class DisbursementResourceImpl extends DisbursementResourceDecorator {
	private static final Logger LOGGER = Logger.getLogger(DisbursementResourceImpl.class.getName());
	private final DisbursementServiceImpl disbursementServiceImpl;

	public DisbursementResourceImpl(DisbursementResourceComponent recordResource, DisbursementServiceComponent recordService) {
		super(recordResource);
		this.disbursementServiceImpl = new DisbursementServiceImpl(recordService);
	}

	@Route(url = "call/disbursement/special")
	public HashMap<String, Object> moneyTransfer(VMJExchange vmjExchange) {
		if (vmjExchange.getHttpMethod().equals("POST")) {
            Map<String, Object> requestBody = vmjExchange.getPayload(); 
			Disbursement result = disbursementServiceImpl.createDisbursement(requestBody);
			return result.toHashMap();
		}
		throw new NotFoundException("Route tidak ditemukan");
	}
}
