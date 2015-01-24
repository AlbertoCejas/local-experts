package es.local.experts;

import com.badlogic.gdx.graphics.Texture;

public class Person {
	
	//int _id = -1; // -1 means no ID
	private int _id;
	private Texture _picture;
	private String _name;
	private int _rating;
	
	public Person (int id, Texture picture, String name, int rating) {
		_id = id;
		_picture = picture;
		_name = name;
		_rating = rating;
	}
	
	public String getName() {
		return _name;
	}
	
	public Texture getPicture(){
		return _picture;
	}

}
