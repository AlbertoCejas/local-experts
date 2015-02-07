package es.local.experts;

import java.util.Comparator;

import com.badlogic.gdx.graphics.Texture;

public class Person {
	
	//int _id = -1; // -1 means no ID
	private int _id;
	private Texture _picture;
	private String _name;
	private float _rating;
	
	public static Comparator<Person> _nameComparator = new Comparator<Person>(){

		@Override
		public int compare (Person p1, Person p2) {
			//Default by name
			return p1.getName().compareTo(p2.getName());
		}
		
	};
	public static Comparator<Person> _ratingComparator = new Comparator<Person>(){

		@Override
		public int compare (Person p1, Person p2) {
			return Float.compare(p1._rating, p2._rating) * -1;
		}
		
	};;
	
	public Person (int id, Texture picture, String name, float rating) {
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
	
	public float getRating() {
		return _rating;
	}

}
