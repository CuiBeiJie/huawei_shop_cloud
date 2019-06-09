/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.aliyuncs.dysmsapi.model.v20170525;

import com.aliyuncs.RpcAcsRequest;

/**
 * @author auto create
 * @version 
 */
public class SendBatchSmsRequest extends RpcAcsRequest<SendBatchSmsResponse> {
	
	public SendBatchSmsRequest() {
		super("Dysmsapi", "2017-05-25", "SendBatchSms");
	}

	private String templateCode;

	private String templateParamJson;

	private String resourceOwnerAccount;

	private String smsUpExtendCodeJson;

	private Long resourceOwnerId;

	private String signNameJson;

	private Long ownerId;

	private String phoneNumberJson;

	public String getTemplateCode() {
		return this.templateCode;
	}

	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
		if(templateCode != null){
			putQueryParameter("TemplateCode", templateCode);
		}
	}

	public String getTemplateParamJson() {
		return this.templateParamJson;
	}

	public void setTemplateParamJson(String templateParamJson) {
		this.templateParamJson = templateParamJson;
		if(templateParamJson != null){
			putQueryParameter("TemplateParamJson", templateParamJson);
		}
	}

	public String getResourceOwnerAccount() {
		return this.resourceOwnerAccount;
	}

	public void setResourceOwnerAccount(String resourceOwnerAccount) {
		this.resourceOwnerAccount = resourceOwnerAccount;
		if(resourceOwnerAccount != null){
			putQueryParameter("ResourceOwnerAccount", resourceOwnerAccount);
		}
	}

	public String getSmsUpExtendCodeJson() {
		return this.smsUpExtendCodeJson;
	}

	public void setSmsUpExtendCodeJson(String smsUpExtendCodeJson) {
		this.smsUpExtendCodeJson = smsUpExtendCodeJson;
		if(smsUpExtendCodeJson != null){
			putQueryParameter("SmsUpExtendCodeJson", smsUpExtendCodeJson);
		}
	}

	public Long getResourceOwnerId() {
		return this.resourceOwnerId;
	}

	public void setResourceOwnerId(Long resourceOwnerId) {
		this.resourceOwnerId = resourceOwnerId;
		if(resourceOwnerId != null){
			putQueryParameter("ResourceOwnerId", resourceOwnerId.toString());
		}
	}

	public String getSignNameJson() {
		return this.signNameJson;
	}

	public void setSignNameJson(String signNameJson) {
		this.signNameJson = signNameJson;
		if(signNameJson != null){
			putQueryParameter("SignNameJson", signNameJson);
		}
	}

	public Long getOwnerId() {
		return this.ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
		if(ownerId != null){
			putQueryParameter("OwnerId", ownerId.toString());
		}
	}

	public String getPhoneNumberJson() {
		return this.phoneNumberJson;
	}

	public void setPhoneNumberJson(String phoneNumberJson) {
		this.phoneNumberJson = phoneNumberJson;
		if(phoneNumberJson != null){
			putQueryParameter("PhoneNumberJson", phoneNumberJson);
		}
	}

	@Override
	public Class<SendBatchSmsResponse> getResponseClass() {
		return SendBatchSmsResponse.class;
	}

}
