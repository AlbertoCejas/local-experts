package es.local.experts.ui;

import java.util.Comparator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;

import es.local.experts.Person;

public class ListView extends Table implements Disposable {

	private ObjectMap<String, Comparator<Person>> _orderItems;
	private Array<Person> _persons;
	private Comparator<Person> _currentCriteria;

	private Table _orderBar;
	private Table _list;
	private SelectBox<String> _orderBox;
	private ListViewStyle _style;
	
	private Texture _background;
	
	private ScrollPane _panel;
	private Texture _lineTexture;
	private PersonListener _personListener;
	
	
	public ListView(float width, float listHeight, float barHeight, ListViewStyle style) {
		super();
		
		setStyle(style);
		
		_persons = new Array<Person>();
		_currentCriteria = Person._nameComparator;
		
		_orderItems = new ObjectMap<String, Comparator<Person>>();
		_orderItems.put("NAME", Person._nameComparator);
		_orderItems.put("RATING", Person._ratingComparator);
		
		_orderBar = new Table();
		_orderBar.setBackground( _style._background);
		
		_orderBox = new SelectBox<String>(style._filterBarStyle);
		_orderBox.setItems(_orderItems.keys().toArray());
		_orderBox.addListener(new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				String selected = _orderBox.getSelected();
				_currentCriteria = _orderItems.get(selected);
				buildList();
			}
			
		});
		
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
	
	public void setPersonListener(PersonListener listener) {
		_personListener = listener;
	}
	
	public void addOrderCriteria(String criteria, Comparator<Person> comparator) {
		_orderBox.setItems(_orderItems.keys().toArray());
		_orderItems.put(criteria, comparator);
	}
	
	private void setStyle(ListViewStyle style) {
		if (style == null) throw new IllegalArgumentException("style cannot be null.");
		this._style = style;
		invalidateHierarchy();
	}
	
	public void addPerson(Person p) {
		_persons.add(p);
		buildList();
	}
	
	public void reset() {
		_persons.clear();
		buildList();
	}
	
	private void buildList() {
		_list.clearChildren();
		_persons.sort(_currentCriteria);
		
		this.setDebug(true);
		
		for(final Person person : _persons) {
						
			Table personTable = new Table();
			personTable.setDebug(true);
			personTable.setSize(getWidth(), 200f);
			
			Label.LabelStyle ls = new Label.LabelStyle();
			ls.font = _style._font;
			ls.fontColor = Color.BLACK;
			
			Table infoTable = new Table();
			infoTable.row();
			infoTable.add(new Label(person.getName(), ls)).colspan(2);
			infoTable.row();
			infoTable.add(new Label("10e/hora", ls)).padRight(100f);
			infoTable.add(new RatingBar(5, person.getRating()));
			
			Image line = new Image(_lineTexture);
			line.setSize(getWidth(), 5f);
					
			personTable.add(new Image(person.getPicture())).width(150f).height(150f).pad(25f);
			personTable.add(infoTable).expand();
			personTable.row().colspan(2).fillX();
			personTable.add(line).height(5f);
			
			WidgetGroup group = new WidgetGroup();
			group.addActor(personTable);
			
			group.addListener(new ClickListener() {
				public void clicked (InputEvent event, float x, float y) {
					if(_personListener != null)
						_personListener.clicked(person);
				}
			});
			
			_list.row();
			_list.add(group).size(getWidth(), 200f);
		}
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
