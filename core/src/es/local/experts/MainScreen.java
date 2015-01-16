package es.local.experts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class MainScreen extends ScreenAdapter {

	LocalExperts _game;
	Stage _stage;
	
	TextSlider _textSlider;
	Texture _sliderLeftArrow, _sliderRightArrow, _sliderBackground;
	
	public MainScreen (LocalExperts game) {
		_game = game;
		
		_stage = new Stage(_game._viewport, game._batch);
		Gdx.input.setInputProcessor(_stage);
		
		
		_sliderLeftArrow = new Texture("leftArrow.png");
		_sliderRightArrow = new Texture("rightArrow.png");
		_sliderBackground = new Texture("categoryHead.png");
		
		TextSlider.TextSliderStyle tss = new TextSlider.TextSliderStyle(Utils.FONT, 
			new TextureRegionDrawable(new TextureRegion(_sliderLeftArrow)), 
			new TextureRegionDrawable(new TextureRegion(_sliderRightArrow)), 
			new TextureRegionDrawable(new TextureRegion(_sliderBackground)));
		
		_textSlider = new TextSlider("Fisiotherapy", Utils.APP_WIDTH, 200f, tss);
		
		_stage.addActor(_textSlider);
	}

	@Override
	public void render (float delta) {
		_stage.act(delta);
		_stage.draw();

	}

	@Override
	public void resize (int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause () {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume () {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose () {
		_sliderLeftArrow.dispose();
		_sliderRightArrow.dispose();
		_sliderBackground.dispose();
		_stage.dispose();
		
	}

}
