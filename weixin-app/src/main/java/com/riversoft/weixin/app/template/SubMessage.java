package com.riversoft.weixin.app.template;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 2020年1月9日14:01:39
 * 
 * @author hyn
 */
public class SubMessage {

	@JsonProperty("touser")
	private String toUser;

	@JsonProperty("template_id")
	private String templateId;

	private String page;

	private Map<String, Data> data;

	public String getToUser() {
		return toUser;
	}

	public void setToUser(String toUser) {
		this.toUser = toUser;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public Map<String, Data> getData() {
		return data;
	}

	public void setData(Map<String, Data> data) {
		this.data = data;
	}
}
