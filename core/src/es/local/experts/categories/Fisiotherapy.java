package es.local.experts.categories;

import com.badlogic.gdx.utils.ObjectMap;

public class Fisiotherapy extends Category {
	
	public Fisiotherapy() {
		super();
		
		_fields = new ObjectMap<String, OptionType>();
		
		_name = "Fisiotherapy";
		
		loadField("sessionTime", OptionType.value);
		loadField("sessionPrice", OptionType.value);
		loadField("homeVisit", OptionType.checkBox);
	}
	
	public String getName() {
		return _name;
	}
	
	@Override
	public void loadField (String field, OptionType option) {
		_fields.put(field, option);	
	}
	
}
