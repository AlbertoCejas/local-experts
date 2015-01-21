package es.local.experts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import es.local.experts.categories.Category;
import es.local.experts.categories.Category.OptionType;
import es.local.experts.ui.CategorySelector;
import es.local.experts.ui.CategorySelector.CategorySelectorStyle;
import es.local.experts.ui.ListView;
import es.local.experts.utils.Utils;

public class MainScreen extends ScreenAdapter {

	LocalExperts _game;
	Stage _stage;
	CategorySelector _categorySelector;
	ListView _listView;
	
	Texture _sliderLeftArrow, _sliderRightArrow, _sliderBackground, _panelBackground;
	Texture _orderBackground, _orderItemBackground, _listViewBackground;
	
	public MainScreen (LocalExperts game) {
		_game = game;
		
		_stage = new Stage(_game._viewport, game._batch);
		//_stage.setDebugAll(true);
		Gdx.input.setInputProcessor(_stage);
		
		// Category Selector -----------------------------
		
		_sliderLeftArrow = new Texture("leftArrow.png");
		_sliderRightArrow = new Texture("rightArrow.png");
		_sliderBackground = new Texture("categoryHead.png");
		_panelBackground = new Texture("categoryOptions.png");
		
		CategorySelectorStyle css = new CategorySelectorStyle(Utils.FONT, 
			new TextureRegionDrawable(new TextureRegion(_sliderLeftArrow)), 
			new TextureRegionDrawable(new TextureRegion(_sliderRightArrow)), 
			new TextureRegionDrawable(new TextureRegion(_sliderBackground)), 
			new TextureRegionDrawable(new TextureRegion(_panelBackground)));
		
		
		// Categories
		Category fisio = new Category("Fisiotherapy");
		fisio.loadField("Session time", OptionType.value, 1);
		fisio.loadField("Session price", OptionType.value, 20);
		fisio.loadField("Home visit", OptionType.checkBox, true);
		Category teacher = new Category("Teacher");	
		teacher.loadField("Session time", OptionType.value, 1);
		teacher.loadField("Session price", OptionType.value, 20);
		teacher.loadField("Home visit", OptionType.checkBox, true);
		
		_categorySelector = new CategorySelector(fisio, Utils.APP_WIDTH, 200f, 75f, 75f, 150f, css);
		_categorySelector.addCategory(teacher);
		_categorySelector.setPosition(0, Utils.APP_HEIGHT - _categorySelector.getHeight());
		
		// ListView ---------------------------------------
		
		_orderBackground = new Texture("orderBackground.png");
		_orderItemBackground = new Texture("orderListBackground.png");
		
		// Color background
		Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();
		_listViewBackground = new Texture(pixmap);
				
		List.ListStyle ls = new List.ListStyle();
		ls.font = Utils.SMALLFONT;
		ls.fontColorSelected = Color.WHITE;
		ls.fontColorUnselected = Color.LIGHT_GRAY;
		ls.selection = new TextureRegionDrawable(new TextureRegion(_orderItemBackground));
		
		ScrollPane.ScrollPaneStyle sps = new ScrollPane.ScrollPaneStyle();
		sps.background = new TextureRegionDrawable(new TextureRegion(_orderItemBackground));
		
		SelectBox.SelectBoxStyle sbs = new SelectBox.SelectBoxStyle();
		sbs.scrollStyle = sps;
		sbs.listStyle = ls;
		sbs.font = Utils.SMALLFONT;
		sbs.fontColor = Color.WHITE;
		sbs.background = new TextureRegionDrawable(new TextureRegion(_orderBackground));
		
		ListView.ListViewStyle lvs = new ListView.ListViewStyle(Utils.SMALLFONT, 
			new TextureRegionDrawable(new TextureRegion(_orderBackground)), 
			new TextureRegionDrawable(new TextureRegion(_listViewBackground)),
			sbs);
		
		float orderBarHeight = 150f;
		
		_listView = new ListView(Utils.APP_WIDTH, Utils.APP_HEIGHT - _categorySelector.getHeight() - orderBarHeight, orderBarHeight, lvs);
		_listView.setBackground(new TextureRegionDrawable(new TextureRegion(_listViewBackground)));
		
		_categorySelector.setBackgroundTable(_listView);
		
		// Build the stage ---------------------------
		
		_stage.addActor(_listView);
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
		_orderBackground.dispose();
		_sliderLeftArrow.dispose();
		_sliderRightArrow.dispose();
		_sliderBackground.dispose();
		_panelBackground.dispose();
		_orderItemBackground.dispose();
		_listViewBackground.dispose();
		
	}

}
