package es.local.experts.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;

public class ListView extends Table {

	Array<String> _orderItems;
	
	private Table _orderBar;
	private Table _list;
	private SelectBox<String> _orderBox;
	private ListViewStyle _style;
	
	private Texture _background;
	
	ScrollPane _panel;
	
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
		_list.setDebug(true);
		_list.setSize(width, listHeight);
		
		_panel = new ScrollPane(_list);
		
		this.row();
		this.add(_orderBar);
		this.row().height(listHeight).width(width);
		this.add(_panel);
		
		setSize(width, barHeight + listHeight);
		
	}
	
	public void addOrderCriteria(String criteria) {
		_orderItems.add(criteria);
	}
	
	private void setStyle(ListViewStyle style) {
		if (style == null) throw new IllegalArgumentException("style cannot be null.");
		this._style = style;
		invalidateHierarchy();
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
	
}
