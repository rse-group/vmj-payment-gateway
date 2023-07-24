package paymentgateway.fundtransfer.infofrombenef;

import paymentgateway.fundtransfer.core.InfoFromBenefDecorator;
import paymentgateway.fundtransfer.core.InfoFromBenef;
import paymentgateway.fundtransfer.core.InfoFromBenefComponent;

@Entity(name="fundtransfer_infofrombenef")
@Table(name="fundtransfer_infofrombenef")
public class InfoFromBenefImpl extends InfoFromBenefDecorator {

	protected String url;
	public InfoFromBenefImpl(InfoFromBenefComponent record, String url, FundTransferImpl fundtransferimpl) {
		super(record);
		this.url = url;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void cancelTransfer(String id) {
		// TODO: implement this method
	}

	public void sendTransfer(String id, String destinationAccountNumber, String destinationAccountHolderName, String destinationCode) {
		// TODO: implement this method
	}

	public void sendTransferLink(Real amount, String email, String reference, String description) {
		// TODO: implement this method
	}

}
