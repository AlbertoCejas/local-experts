package es.local.experts;

import java.util.HashMap;

public abstract class Category {

	static public enum OptionType { checkBox, range, value };
	
	protected HashMap<String, OptionType> _fields;
	protected String _name;
	
	public abstract void loadField(String field, OptionType option);
	
}
