package com.telappoint.admin.appt.common.model.response;

import java.util.List;
import com.telappoint.admin.appt.common.model.DynamicPledgeResult;
/**
 *  
 * @author Balaji N
 *
 */
public class DynamicPledgeResultResponse extends BaseResponse {
	private List<DynamicPledgeResult> dynamicPledgeResultList;

	public List<DynamicPledgeResult> getDynamicPledgeResultList() {
		return dynamicPledgeResultList;
	}

	public void setDynamicPledgeResultList(List<DynamicPledgeResult> dynamicPledgeResultList) {
		this.dynamicPledgeResultList = dynamicPledgeResultList;
	}
}
