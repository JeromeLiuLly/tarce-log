package com.candao.trace.gateway.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtil {
	public static Gson gson = new GsonBuilder().disableHtmlEscaping().create();
}
