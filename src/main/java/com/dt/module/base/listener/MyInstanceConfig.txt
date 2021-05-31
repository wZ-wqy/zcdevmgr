package com.dt.module.base.listener;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.netflix.appinfo.MyDataCenterInstanceConfig;

public class MyInstanceConfig extends MyDataCenterInstanceConfig {
	@Override
	public String getHostName(boolean refresh) {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			return super.getHostName(refresh);
		}
	}
}