package paymentgateway.disbursement.agent;

import javax.persistence.Entity;
import javax.persistence.Table;

import java.util.HashMap;
import java.util.Date;

import paymentgateway.disbursement.core.Disbursement;
import paymentgateway.disbursement.core.DisbursementDecorator;
import paymentgateway.disbursement.core.DisbursementComponent;

@Entity(name = "agent_impl")
@Table(name = "agent_impl")
public class AgentImpl extends DisbursementDecorator {
	protected int agent_id;
	protected String direction;

	public AgentImpl(DisbursementComponent record, int agent_id, String direction) {
		super(record);
		this.agent_id = agent_id;
		this.direction = direction;
	}

	public AgentImpl() {
		super();
	}

	public int getAgentId() {
		return agent_id;
	}

	public void setAgentId(int agent_id) {
		this.agent_id = agent_id;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public HashMap<String, Object> toHashMap() {
		HashMap<String, Object> specialMap = record.toHashMap();
		specialMap.put("agent_id", getAgentId());
		specialMap.put("direction", getDirection());
		return specialMap;
	}
}
