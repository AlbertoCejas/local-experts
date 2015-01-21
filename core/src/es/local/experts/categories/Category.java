package es.local.experts.categories;

import com.badlogic.gdx.utils.Array;

public class Category {

	public static enum OptionType { checkBox, range, value };
	
	public static class Field {
		public String _fieldName;
		public OptionType _type;
		public Object _value;
		
		public Field(String fieldName, OptionType type, Object value) {
			_fieldName = fieldName;
			_type = type;
			_value = value;
		}
	};
	
	private Array<Field> _fields;
	private String _name;
	
	public Category(String categoryName) {
		_name = categoryName;
		_fields = new Array<Field>();
	}
	
	public String getName() {
		return _name;
	}
	
	public Array<Field> getFields() {
		return _fields;
	}
	
	public void loadField (String fieldName, OptionType type, Object value) {
		_fields.add( new Field(fieldName, type, value) );	
	}
	
	public void changeField (String fieldName, Object value) {
		for(Field field : _fields) {
			if(field._fieldName.compareTo(fieldName) == 0)
				field._value = value;
		}
	}
	
}
