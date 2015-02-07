package es.local.experts.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;

import es.local.experts.utils.BasicNameValuePair;
import es.local.experts.utils.ParamsHolder;
import es.local.experts.utils.ParamsUrlEncoder;

public class CategorySelectorController implements HttpResponseListener {

	private CategorySelector _selector;
	private HttpRequest _httpRequest;
	private String _serverAddress;
	private final Json _json;

	public CategorySelectorController(CategorySelector selector) {
		super();
		if(selector == null)
			throw new NullPointerException("Null selector in CategorySelectorController");
		
		_selector = selector;
		_serverAddress = "http://192.168.1.41/localexperts/";
		_json = new Json(OutputType.json);
		
	}

	public void updateExperts(String category) {
		ParamsHolder params = new ParamsHolder();
				
		params.add(new BasicNameValuePair("tag", "get_experts"));
		params.add(new BasicNameValuePair("expert_type", category));
		_httpRequest = new HttpRequest(Net.HttpMethods.POST);

		String requestJson = ParamsUrlEncoder.convertHttpParameters(params);
		
		_httpRequest.setContent(requestJson);
		_httpRequest.setUrl(_serverAddress + "requestsHandler.php");
		_httpRequest.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
		_httpRequest.setTimeOut(5000);

		_selector.setSynchronizing(true);
		
		Gdx.net.sendHttpRequest(_httpRequest, CategorySelectorController.this);
	}

	@Override
	public void handleHttpResponse (HttpResponse httpResponse) {
		
		System.out.println("String: " + httpResponse.getResultAsString());
		
		ParamsHolder ph = _json.fromJson(ParamsHolder.class, httpResponse.getResultAsString());
		
		int statusCode = httpResponse.getStatus().getStatusCode();
		if(statusCode != HttpStatus.SC_OK) {
			System.out.println("Request Failed");
			return;
		}
		
		System.out.println("BUENA!");
		
		String responseJson = httpResponse.getResultAsString();
		try {

			_selector.clearItems();
			ListView list = _selector.getItemList();
			
			for(BasicNameValuePair pair : ph.getArray()) {
				System.out.println(pair._name +", " + pair._value);
			}
			
			// FILL LISTVIEW

		}
		catch(Exception exception) {
			exception.printStackTrace();
		}
		finally {
			_selector.setSynchronizing(false);
			_selector.showBackgroundTable();
		}


	}

	@Override
	public void failed (Throwable t) {
		System.out.println("Request Failed Completely");
		_selector.setSynchronizing(false);
		_selector.showBackgroundTable();
	}

	@Override
	public void cancelled () {
		System.out.println("Request Cancelled");
		_selector.setSynchronizing(false);
		_selector.showBackgroundTable();
	}

}
