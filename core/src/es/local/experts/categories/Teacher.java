package es.local.experts.categories;

import com.badlogic.gdx.utils.ObjectMap;

import es.local.experts.categories.Category.OptionType;

public class Teacher extends Category {
	
	public Teacher() {
		super();
		
		_fields = new ObjectMap<String, OptionType>();
		
		_name = "Teacher";
		
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
