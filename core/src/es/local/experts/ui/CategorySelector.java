package es.local.experts.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.SizeByAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.reflect.ClassReflection;

import es.local.experts.categories.Category;
import es.local.experts.utils.Utils;

public class CategorySelector extends Table implements Disposable {

	private ArrayMap<String, Category> _model;
	private int _currentCategory;

	private CategorySelectorStyle _style;
	private Button _rightButton, _leftButton, _backgroundButton;
	private Label _label;

	private boolean _expanded = false, _expanding = false, _sliding = false;

	private AlphaAction fadeIn = Actions.fadeIn(0.3f);
	private AlphaAction fadeOut = Actions.fadeOut(0.3f);
	
	private float _minHeight, _maxHeight;

	private Table _textSlider;
	private Table _expandablePanel;

	public CategorySelector(Category category, float width, float minHeight, float maxHeight, float buttonWidth, float buttonHeight, float sidePad, CategorySelectorStyle css) {
		super();
		
		setStyle(css);

		_minHeight = minHeight;
		_maxHeight = maxHeight;
		
		// Category model initialization -----------------
		String categoryName = ClassReflection.getSimpleName(category.getClass());

		_model = new ArrayMap<String, Category>();
		_model.put(categoryName, category);
		_currentCategory=0;
		
		// Label -----------------------------------------
		Label.LabelStyle ls = new Label.LabelStyle();
		ls.font = _style._font;
		ls.fontColor = Color.BLACK;
		_label = new Label(categoryName, ls);

		// Buttons ---------------------------------------
		// -- Button Styles -----
		Button.ButtonStyle bbs = new Button.ButtonStyle();
		bbs.up = css._headBackground;

		Button.ButtonStyle lbs = new Button.ButtonStyle();
		lbs.up = css._leftArrow;

		Button.ButtonStyle rbs = new Button.ButtonStyle();
		rbs.up = css._rightArrow;

		// -- Buttons definition ----
		_rightButton = new Button(rbs);
		_leftButton = new Button(lbs);
		_backgroundButton = new Button(bbs);
		_backgroundButton.setSize(width, minHeight);
		
		// -- Panel definition ---------------------------
		Button.ButtonStyle pbs = new Button.ButtonStyle();
		pbs.up = css._panelBackground;
		_expandablePanel = new Button(pbs);
		_expandablePanel.setHeight(maxHeight-minHeight);

		// -- Text slider --------------------------------
		_textSlider = new Table();
		_textSlider.setHeight(minHeight);
		_textSlider.setWidth(width);
		_textSlider.setBackground(css._headBackground);
		_textSlider.row().height(minHeight).expandX();
		_textSlider.add(_leftButton).size(buttonWidth, buttonHeight);
		_textSlider.add(_label);
		_textSlider.add(_rightButton).size(buttonWidth, buttonHeight);;
		
		// -- Expandable Panel --------------------------
		_expandablePanel = new Table();
		_expandablePanel.setHeight(0);
		_expandablePanel.setBackground(css._panelBackground);

		// Build the table
		add(_textSlider);
		row();
		add(_expandablePanel).padLeft(sidePad).padRight(sidePad);

		// Set bounds
		setSize(width, minHeight);

		// Listeners ----------------------------------
		
		_leftButton.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				prevCategory();
			}
		});
		
		_rightButton.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				nextCategory();
			}
		});
		
		_textSlider.addListener( new ActorGestureListener() {
			public void fling (InputEvent event, float velocityX, float velocityY, int button) {
				
				if(!_sliding && Math.abs(velocityX)>Math.abs(velocityY)){
					if(velocityX>0){
						nextCategory();
					}else{
						prevCategory();
					}
				}
				else {
					if(!_expanding && !_sliding && !_expanded && velocityY<0){
						expand();
					}
				}
			}
		});
		
		//CHECK IF USEFUL WHEN CROWD
		/*
		_expandablePanel.addListener( new ActorGestureListener() {
			public void fling (InputEvent event, float velocityX, float velocityY, int button) {
				
				System.out.println("HOLA");
				
				if(!_sliding && Math.abs(velocityX)<Math.abs(velocityY)){
					if(!_expanding && !_expanded && velocityY>0){
						shrink();
					}
				}
			}
		});*/
		
		_label.addListener( new ActorGestureListener() {

			public boolean longPress (Actor actor, float x, float y) {
				if(_expanded)
					shrink();
				else
					expand();

				return false;
			}
		});

	}

	public void expand() {

		if(!_expanding) {
			_expanding = true;

			MoveByAction expandMovePanel = Actions.moveBy(0, -(_maxHeight - _minHeight), .75f);
			SizeByAction expandSizePanel = Actions.sizeBy(0, _maxHeight - _minHeight, .75f);
			ParallelAction expandAction = Actions.parallel(expandMovePanel, expandSizePanel);
			_expandablePanel.addAction(Actions.sequence(expandAction, Actions.run(new Runnable() {

				@Override
				public void run () {
					setHeight(_maxHeight);
					setY(Utils.APP_HEIGHT - getHeight());
					_expanding = false;
				}

			})));
			_expanded = true;
		}

	}

	public void shrink() {

		if(!_expanding) {
			_expanding = true;

			MoveByAction shrinkMovePanel = Actions.moveBy(0, _maxHeight - _minHeight, .75f);
			SizeByAction shrinkSizePanel = Actions.sizeBy(0, -(_maxHeight - _minHeight), .75f);
			ParallelAction shrinkAction = Actions.parallel(shrinkSizePanel, shrinkMovePanel);

			_expandablePanel.addAction(Actions.sequence(shrinkAction, Actions.run(new Runnable() {

				@Override
				public void run () {
					setHeight(_minHeight);
					setY(Utils.APP_HEIGHT - getHeight());
					_expanding = false;
				}

			})));
			_expanded = false;
		}
	}

	public void nextCategory() {
		
		_sliding = true;
		_label.addAction(Actions.sequence(fadeOut, Actions.run(new Runnable() {

			@Override
			public void run () {
				if(++_currentCategory == _model.size)
					_currentCategory = 0;

				_label.setText(_model.getKeyAt(_currentCategory));
				_sliding = true;
			}

		}), fadeIn, Actions.run(new Runnable() {

			@Override
			public void run () {
				_sliding = false;
				if(_expanded)
					shrink();
			}

		})));
	}

	public void prevCategory() {
		
		_sliding = true;

		_label.addAction(Actions.sequence(fadeOut, Actions.run(new Runnable() {

			@Override
			public void run () {
				if(--_currentCategory == -1)
					_currentCategory = _model.size-1;

				_label.setText(_model.getKeyAt(_currentCategory));

			}
		}), fadeIn, Actions.run(new Runnable() {

			@Override
			public void run () {
				_sliding = false;
				
				if(_expanded)
					shrink();
			}

		})));

	}

	public void addCategory(Category category) {
		String categoryName = ClassReflection.getSimpleName(category.getClass());
		_model.put(categoryName, category);
	}

	public void draw(Batch batch, float parentAlpha) {
		validate();

		super.draw(batch, parentAlpha);
	}

	@Override
	public void dispose () {

	}

	private void setStyle(CategorySelectorStyle style) {
		if (style == null) throw new IllegalArgumentException("style cannot be null.");
		this._style = style;
		//this._leftArrow = new Button(style._leftArrow);
		//this._rightArrow = new Button(style._rightArrow);
		//setBackground(style._background);
		invalidateHierarchy();
	}


	static public class CategorySelectorStyle {

		public Drawable _headBackground, _panelBackground;
		public Drawable _leftArrow, _rightArrow;
		public BitmapFont _font;

		public CategorySelectorStyle(BitmapFont font, Drawable leftArrowImg, Drawable rightArrowImg, Drawable headBackground, Drawable panelBackground) {
			this._leftArrow = leftArrowImg;
			this._rightArrow = rightArrowImg;
			this._headBackground = headBackground;
			this._panelBackground = panelBackground;
			this._font = font;
		}

		public CategorySelectorStyle( CategorySelectorStyle style) {
			this._headBackground = style._headBackground;
			this._panelBackground = style._panelBackground;
			this._leftArrow = style._leftArrow;
			this._rightArrow = style._rightArrow;
			this._font = style._font;
		}

	}


}
