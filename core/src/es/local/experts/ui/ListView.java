package es.local.experts.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import es.local.experts.Person;

public class ListView extends Table implements Disposable {

	Array<String> _orderItems;
	
	private Table _orderBar;
	private Table _list;
	private SelectBox<String> _orderBox;
	private ListViewStyle _style;
	
	private Texture _background;
	
	private ScrollPane _panel;
	private Texture _lineTexture;
	
	public ListView(float width, float listHeight, float barHeight, ListViewStyle style) {
		super();
		
		setStyle(style);
		
		_orderItems = new Array<String>();
		_orderBar = new Table();
		_orderBar.setBackground( _style._background);
		
		_orderBox = new SelectBox<String>(style._filterBarStyle);
		_orderBox.setItems("HOLA", "ADIOS", "BMO", "FELIPE");
		
		_orderBar.row().height(barHeight);
		_orderBar.add(_orderBox);
		
		_list = new Table();
		_list.setSize(width, listHeight);
		
		Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
		pixmap.setColor(Color.BLACK);
		pixmap.fill();
		_lineTexture = new Texture(pixmap);
		
		_panel = new ScrollPane(_list);
		_panel.setSize(width,  listHeight);

		setSize(width, barHeight + listHeight);
		add(_orderBar).top();
		row();
		add( _panel).expand().top();
	}
	
	public void addOrderCriteria(String criteria) {
		_orderItems.add(criteria);
	}
	
	private void setStyle(ListViewStyle style) {
		if (style == null) throw new IllegalArgumentException("style cannot be null.");
		this._style = style;
		invalidateHierarchy();
	}
	
	public void addPerson(Person p) {
		Table personTable = new Table();
		//personTable.setDebug(true);
		personTable.setSize(getWidth(), 200f);

		
		Label.LabelStyle ls = new Label.LabelStyle();
		ls.font = _style._font;
		ls.fontColor = Color.BLACK;
		
		Table infoTable = new Table();
		infoTable.setSize(getWidth() - 150f, 200f);
		infoTable.row().expandX();
		infoTable.add(new Label(p.getName(), ls)).colspan(2);
		infoTable.row().expandX();
		infoTable.add(new Label("10e/hora", ls));
		infoTable.add(new Label("Estrellas", ls));
		
		Image line = new Image(_lineTexture);
		line.setSize(getWidth(), 5f);
		
		//personTable.setDebug(true);
		
		//
		
		personTable.add(new Image(p.getPicture())).width(150f).height(150f).pad(25f);
		personTable.add(infoTable).expand();
		personTable.row().colspan(2).fillX();
		personTable.add(line).height(5f);
		
		_list.row();
		_list.add(personTable).size(getWidth(), 200f);
			
	}
	
	public void draw(Batch batch, float parentAlpha) {
		validate();

		super.draw(batch, parentAlpha);
	}
	
	static public class ListViewStyle {

		public Drawable _background, _orderBackground;
		public SelectBox.SelectBoxStyle _filterBarStyle;
		public BitmapFont _font;

		public ListViewStyle(BitmapFont font, Drawable background, Drawable orderBackground, SelectBox.SelectBoxStyle style) {
			this._font = font;
			this._background = background;
			this._orderBackground = orderBackground;
			this._filterBarStyle = style;
		}

		public ListViewStyle( ListViewStyle style) {
			this._font = style._font;
			this._background = style._background;
			this._filterBarStyle = style._filterBarStyle;
		}

	}

	@Override
	public void dispose () {
		_lineTexture.dispose();
		
	}
	
}
