package es.local.experts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import es.local.experts.categories.Fisiotherapy;
import es.local.experts.categories.Teacher;
import es.local.experts.ui.CategorySelector;
import es.local.experts.ui.CategorySelector.CategorySelectorStyle;
import es.local.experts.utils.Utils;

public class MainScreen extends ScreenAdapter {

	LocalExperts _game;
	Stage _stage;
	CategorySelector _categorySelector;
	
	Texture _sliderLeftArrow, _sliderRightArrow, _sliderBackground, _panelBackground;
	
	public MainScreen (LocalExperts game) {
		_game = game;
		
		_stage = new Stage(_game._viewport, game._batch);
		_stage.setDebugAll(true);
		Gdx.input.setInputProcessor(_stage);
		
		_sliderLeftArrow = new Texture("leftArrow.png");
		_sliderRightArrow = new Texture("rightArrow.png");
		_sliderBackground = new Texture("categoryHead.png");
		_panelBackground = new Texture("categoryOptions.png");
		
		CategorySelectorStyle css = new CategorySelectorStyle(Utils.FONT, 
			new TextureRegionDrawable(new TextureRegion(_sliderLeftArrow)), 
			new TextureRegionDrawable(new TextureRegion(_sliderRightArrow)), 
			new TextureRegionDrawable(new TextureRegion(_sliderBackground)), 
			new TextureRegionDrawable(new TextureRegion(_panelBackground)));
		
		_categorySelector = new CategorySelector(new Fisiotherapy(), Utils.APP_WIDTH, 200f, 1200f, 75f, 75f, 150f, css);
		_categorySelector.addCategory(new Teacher());
		_categorySelector.setPosition(0, Utils.APP_HEIGHT - _categorySelector.getHeight());
		
		_stage.addActor(_categorySelector);
	
		
	}

	@Override
	public void render (float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		_stage.act(delta);
		_stage.draw();

	}

	@Override
	public void resize (int width, int height) {
		_stage.getViewport().update(width, height, true);

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
		_categorySelector.dispose();
		_stage.dispose();
	}

}
