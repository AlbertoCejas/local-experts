package es.local.experts;

import com.badlogic.gdx.graphics.Texture;

public class Person {
	
	Texture _picture;
	String _name;
	int _rating;
	
	public Person (Texture picture, String name, int rating) {
		_picture = picture;
		_name = name;
		_rating = rating;
	}

}
