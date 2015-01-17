package es.local.experts.categories;

import com.badlogic.gdx.utils.ObjectMap;

public abstract class Category {

	static public enum OptionType { checkBox, range, value };
	
	protected ObjectMap<String, OptionType> _fields;
	protected String _name;
	
	public abstract void loadField(String field, OptionType option);
	
}
