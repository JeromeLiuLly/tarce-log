package com.candao.trace.framework.bean.request;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.candao.trace.framework.exception.ServiceException;
import com.candao.trace.framework.util.FastDateUtils;
import com.candao.trace.framework.util.ObjectUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

/**
 * 请求数据
 * 
 */
public class ReqData {
	/**
	 * 消息内容-json串
	 */
	public Object content;

	/**
	 * 语言类型 ：0=简体；1=繁体，2=英语
	 */
	public byte langType;
	/**
	 * 客户端类型-渠道{link ClientTypes}
	 */
	public int clientType;
	/**
	 * api版本号
	 */
	public String version;

	// --------------json解析帮助方法----------------
	private static Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
				return FastDateUtils.getDateFormat().parse(json.getAsJsonPrimitive().getAsString());
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
        }
    }).create();
	
	private static Gson _gson = new GsonBuilder().registerTypeAdapter(Double.class, new JsonSerializer<Double>() {

		@Override
		public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
			if (src == src.longValue())
				return new JsonPrimitive(src.longValue());
			return new JsonPrimitive(src);
		}
	}).create();

	private JsonObject jsonObject;
	// 抛异常信息及code
	private static final int EXCEPTION_CODE = 3;
	private static final String EXCEPTION_MSG = "ParameterError";

	/**
	 * 从data中获取指定key的值
	 * @param key
	 * @return
	 */
	public JsonElement get(String key) {
		return json().get(key);
	}

	public JsonObject json() {
		if (jsonObject == null) {
			if(content instanceof String){
				jsonObject = new JsonParser().parse((String) content).getAsJsonObject();
			}else{
				jsonObject = new JsonParser().parse(gson.toJson(content)).getAsJsonObject();
			}
		}
		return jsonObject;
	}

	/**
	 * 是否有指定的key
	 * @param key
	 * @return
	 */
	public boolean has(String key) {
		return json().has(key);
	}

	/**
	 * 获取一个int值，不存在key则抛异常
	 * @param key
	 * @return
	 */
	public int getAsInt(String key) {
		try {
			return get(key).getAsInt();
		} catch (Exception e) {
			throw new ServiceException(EXCEPTION_CODE, EXCEPTION_MSG);
		}
	}

	/**
	 * 获取一个int值,没有则返回默认的defaults
	 * @param key
	 * @param defaults-key不存在时的默认值
	 * @return
	 */
	public int getAsInt(String key, int defaults) {
		if (has(key)) {
			return getAsInt(key);
		}
		return defaults;
	}

	/**
	 * 获取一个byte值，不存在key则抛异常
	 * @param key
	 * @return
	 */
	public byte getAsByte(String key) {
		try {
			return get(key).getAsByte();
		} catch (Exception e) {
			throw new ServiceException(EXCEPTION_CODE, EXCEPTION_MSG);
		}
	}

	public byte getAsByte(String key, byte defaults) {
		if (has(key)) {
			return getAsByte(key);
		}
		return defaults;
	}

	/**
	 * 获取一个Float值，不存在key则抛异常
	 * @param key
	 * @return
	 */
	public Float getAsFloat(String key) {
		try {
			return get(key).getAsFloat();
		} catch (Exception e) {
			throw new ServiceException(EXCEPTION_CODE, EXCEPTION_MSG);
		}
	}

	public Float getAsFloat(String key, Float defaults) {
		if (has(key)) {
			return getAsFloat(key);
		}
		return defaults;
	}

	/**
	 * 获取一个Double值，不存在key则抛异常
	 * @param key
	 * @return
	 */
	public Double getAsDouble(String key) {
		try {
			return get(key).getAsDouble();
		} catch (Exception e) {
			throw new ServiceException(EXCEPTION_CODE, EXCEPTION_MSG);
		}
	}

	public Double getAsDouble(String key, Double defaults) {
		if (has(key)) {
			return getAsDouble(key);
		}
		return defaults;
	}

	/**
	 * 获取一个Long值，不存在key则抛异常
	 * @param key
	 * @return
	 */
	public Long getAsLong(String key) {
		try {
			return get(key).getAsLong();
		} catch (Exception e) {
			throw new ServiceException(EXCEPTION_CODE, EXCEPTION_MSG);
		}
	}

	public Long getAsLong(String key, Long defaults) {
		if (has(key)) {
			return getAsLong(key);
		}
		return defaults;
	}

	/**
	 * 获取一个Boolean值，不存在key则抛异常
	 * @param key
	 * @return
	 */
	public Boolean getAsBoolean(String key) {
		try {
			return get(key).getAsBoolean();
		} catch (Exception e) {
			throw new ServiceException(EXCEPTION_CODE, EXCEPTION_MSG);
		}
	}

	public Boolean getAsBoolean(String key, Boolean defaults) {
		if (has(key)) {
			return getAsBoolean(key);
		}
		return defaults;
	}

	/**
	 * 获取一个String值，不存在key则抛异常
	 * @param key
	 * @return
	 */
	public String getAsString(String key) {
		try {
			return get(key).getAsString();
		} catch (Exception e) {
			throw new ServiceException(EXCEPTION_CODE, EXCEPTION_MSG);
		}
	}

	public String getAsString(String key, String defaults) {
		if (has(key)) {
			return getAsString(key);
		}
		return defaults;
	}

	/**
	 * 获取一个key并转化为指定Object, 不存在key则返回null
	 * @param key-key名称
	 * @param clazz-类类型
	 * @return
	 */
	public <T> T getAsObject(String key, Class<T> clazz) {
		if (has(key)) {
			JsonElement jsonElement = get(key);
			return gson.fromJson(jsonElement, clazz);
		}
		return null;
	}

	/**
	 * 获取一个key并转化为指定Object的List, 不存在key则返回null
	 * @param key-key名称
	 * @param clazz-类类型
	 * @return
	 */
	public <T> List<T> getAsList(String key, Class<T> clazz) {
		if (has(key)) {
			JsonElement jsonElement = get(key);
			if (clazz == Integer.class) {
				return _gson.fromJson(jsonElement, new TypeToken<List<Integer>>() {
				}.getType());
			}
			return gson.fromJson(jsonElement, new TypeToken<List<T>>() {
			}.getType());
		}
		return null;
	}

	/**
	 * 将整个data转化成T对象
	 * @param clazz-类类型
	 * @return
	 */
	public <T> T toObject(Class<T> clazz) {
		String jsonString = JSON.toJSONString(content);
		return JSON.parseObject(jsonString, clazz);
	}

	/**
	 * 将整个data转化成List<T>
	 * @param clazz-类类型
	 * @return
	 */
	public <T> List<T> toList(Class<T> clazz) {
		
		return JSON.parseArray(JSON.toJSONString(content), clazz);
	}

	@Override
	public String toString() {
		return ObjectUtil.toString(this);
	}
}
