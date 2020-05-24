package com.riversoft.weixin.app.template;

import com.riversoft.weixin.app.AppWxClientFactory;
import com.riversoft.weixin.app.base.AppSetting;
import com.riversoft.weixin.app.base.WxEndpoint;
import com.riversoft.weixin.common.WxClient;
import com.riversoft.weixin.common.util.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * Created by Borball on 11/07/2016.
 */
public class Subscribes {

	private static Logger logger = LoggerFactory.getLogger(Subscribes.class);

	private WxClient wxClient;

	public static Subscribes defaultTemplates() {
		return with(AppSetting.defaultSettings());
	}

	public static Subscribes with(AppSetting appSetting) {
		Subscribes templates = new Subscribes();
		templates.setWxClient(AppWxClientFactory.getInstance().with(appSetting));
		return templates;
	}

	public void setWxClient(WxClient wxClient) {
		this.wxClient = wxClient;
	}

	public void send(SubMessage message) {
		String sendUrl = WxEndpoint.get("url.subscribe.send");
		String json = JsonMapper.defaultMapper().toJson(message);

		logger.debug("subscribe message, send message: {}", json);
		String response = wxClient.post(sendUrl, json);
		JsonMapper.defaultMapper().json2Map(response);
	}

}
