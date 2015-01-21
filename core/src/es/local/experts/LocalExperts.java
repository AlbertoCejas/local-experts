package es.local.experts;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import es.local.experts.utils.Utils;

public class LocalExperts extends Game {
	public SpriteBatch _batch;
	public Viewport _viewport;
	public OrthographicCamera _camera;
	
	@Override
	public void create () {
		_batch = new SpriteBatch();
		_camera = new OrthographicCamera();
		_viewport = new FitViewport(Utils.APP_WIDTH, Utils.APP_HEIGHT, _camera);
		_viewport.update(Utils.APP_WIDTH, Utils.APP_HEIGHT, true);
		
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Gidole-Regular.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 100;
		Utils.FONT = generator.generateFont(parameter);
		parameter.size = 75;
		Utils.SMALLFONT = generator.generateFont(parameter);
		generator.dispose();
		
		
		setScreen(new MainScreen(this));

	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		Screen screen = getScreen();
		if (screen != null) screen.hide();
		
		if (Utils.FONT != null) Utils.FONT.dispose();
	}
	
}
