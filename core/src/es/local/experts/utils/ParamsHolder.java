package es.local.experts.utils;

import com.badlogic.gdx.net.HttpParametersUtils;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;

public class ParamsHolder implements Json.Serializable {
	private Array<BasicNameValuePair> _array;
	
	public ParamsHolder() {
		 _array = new Array<BasicNameValuePair>();
		 HttpParametersUtils a;
	}
	
	public void add(BasicNameValuePair pair) {
		_array.add(pair);
	}

	public Array<BasicNameValuePair> getArray() {
		return _array;
	}
	
	@Override
	public void write (Json json) {
		for(BasicNameValuePair pair : _array) {
			json.writeValue(pair._name, pair._value);
		}
		
	}

	@Override
	public void read (Json json, JsonValue jsonData) {
		for(JsonValue child = jsonData.child; child != null; child = child.next.next) {
			BasicNameValuePair pair = new BasicNameValuePair(child.asString(), child.next.asString());
			add(pair);
		}
	}
	
}
