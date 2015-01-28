package es.local.experts.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class RatingBar extends Table {

	private float _value = 2.5f;
	private int _numOfStars = 5;
	private Texture _holder, _star;
	private float _starWidth = 75f;
	
	public RatingBar() {
		super();
		
		_holder = new Texture("star_holder.png");
		_star = new Texture("star.png");
		setSize(_starWidth*_numOfStars, _starWidth);
		
		buildWidget();
		
		invalidateHierarchy();
	}
	
	public RatingBar(int numOfStars) {
		_numOfStars = numOfStars;
		setSize(_starWidth*_numOfStars, _starWidth);
		
		buildWidget();
		
		invalidateHierarchy();
	}
	
	public void setNumOfStars(int num) {
		_numOfStars = num;
		setWidth(_starWidth*_numOfStars);
		
		buildWidget();
		
		invalidateHierarchy();
	}
	
	private void buildWidget() {
		clearChildren();
		for(int i=0; i < _numOfStars; i++) {
			add(new Image(_holder)).width(_starWidth).height(_starWidth);
		}
	}
	@Override
	public void draw(Batch batch, float parentAlpha) {
		validate();
		
		super.draw(batch, parentAlpha);
		
		int value = (int)_value;
		float decimal = _value % 1;
		
		for(int i=0; i < value; i++) {
			// Alpha color
			if(i == value-1) {
				TextureRegion tr = new TextureRegion(_star, (int)(_star.getWidth() * decimal), _star.getHeight());
				batch.draw(tr, getX() + i*_starWidth, getY(), _starWidth * decimal, _starWidth);
			}
			else
				batch.draw(_star, getX() + i*_starWidth, getY(), _starWidth, _starWidth);
		}
	}
	
}
