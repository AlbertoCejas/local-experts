package es.local.experts;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class CategorySelector extends Table implements Disposable {
	
	Array<Category> _categories;
	boolean _show;
	
	Texture _headTexture;
	
	CategorySelector() {
	
		List.ListStyle ls = new List.ListStyle();
		ls.background = new TextureRegionDrawable(new TextureRegion(_headTexture));
		ls.font = Utils.FONT;
		ls.fontColorSelected = Color.BLACK;
		ls.fontColorUnselected = Color.GRAY;
				
		_categories = new Array<Category>();
		_categories.add( new Fisiotherapy() );
		
		
		_show = true;
	}
	
	void render(SpriteBatch batch) {
		
		
		
	}

	@Override
	public void dispose () {
		_headTexture.dispose();
	}
	
}
