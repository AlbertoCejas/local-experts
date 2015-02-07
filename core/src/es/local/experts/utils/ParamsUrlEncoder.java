package es.local.experts.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ParamsUrlEncoder {

	public static String defaultEncoding = "UTF-8";
	public static String nameValueSeparator = "=";
	public static String parameterSeparator = "&";

	public static String convertHttpParameters (ParamsHolder parameters) {

		StringBuffer convertedParameters = new StringBuffer();

		for (BasicNameValuePair pair : parameters.getArray()) {
			convertedParameters.append(encode(pair._name, defaultEncoding));
			convertedParameters.append(nameValueSeparator);
			convertedParameters.append(encode(pair._value, defaultEncoding));
			convertedParameters.append(parameterSeparator);
		}

		if (convertedParameters.length() > 0) convertedParameters.deleteCharAt(convertedParameters.length() - 1);
		return convertedParameters.toString();
	}

	private static String encode (String content, String encoding) {
		try {
			return URLEncoder.encode(content, encoding);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(e);
		}
	}

}
