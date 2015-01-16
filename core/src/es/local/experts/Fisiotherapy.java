package es.local.experts;

public class Fisiotherapy extends Category {

	Fisiotherapy() {
		super();
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
