package es.local.experts;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;

public class TextSlider extends Table {
	
	TextSliderStyle _style;
	Stack _panel;
	Array<String> _options;
	Label _label;
	Image _leftArrow, _rightArrow;
	
	TextSlider(String text, float width, float height, TextSliderStyle style) {
		super();
		
		_options = new Array<String>();
		_options.add(text);
		
		setStyle(style);
		
		//Text
		Label.LabelStyle ls = new Label.LabelStyle();
		ls.font = _style._font;
		_label = new Label(text, ls);
		
		//Arrows
		_leftArrow = new Image(style._leftArrow);
		_rightArrow = new Image(style._rightArrow);
		//_leftArrow.setSize(25f, 25f);
		//_rightArrow.setSize(25f, 25f);
		
		//Table
		row().height(height).expandX();
		
		add(_leftArrow).size(50f).padLeft(100f);
		add(_label);
		add(_rightArrow).size(50f).padRight(100f);
		
		setSize(width, height);
		
	}
	
	public void setStyle(TextSliderStyle style) {
		if (style == null) throw new IllegalArgumentException("style cannot be null.");
		this._style = style;
		this._leftArrow = new Image(style._leftArrow);
		this._rightArrow = new Image(style._rightArrow);
		setBackground(style._background);
		invalidateHierarchy();
	}
	
	public void draw(Batch batch, float parentAlpha) {
		validate();

		super.draw(batch, parentAlpha);
	}
	
	static public class TextSliderStyle {
		
		public Drawable _background;
		public Drawable _leftArrow, _rightArrow;
		public BitmapFont _font;

		public TextSliderStyle(BitmapFont font, Drawable leftArrowImg, Drawable rightArrowImg, Drawable background) {
			this._leftArrow = leftArrowImg;
			this._rightArrow = rightArrowImg;
			this._background = background;
			this._font = font;
		}

		public TextSliderStyle( TextSliderStyle style) {
			this._background = style._background;
			this._leftArrow = style._leftArrow;
			this._rightArrow = style._rightArrow;
			this._font = style._font;
		}
		
	}

}
