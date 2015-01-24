package es.local.experts.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.SizeByAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldFilter;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.reflect.ClassReflection;

import es.local.experts.categories.Category;
import es.local.experts.categories.Category.Field;
import es.local.experts.utils.Utils;

public class CategorySelector extends Table implements Disposable {

	private Array<Category> _model;
	private int _currentCategoryIndex;

	private CategorySelectorStyle _style;
	private Button _rightButton, _leftButton, _backgroundButton;
	private Label _label;

	private boolean _expanded = false, _expanding = false, _sliding = false;

	private float _minHeight, _maxHeight, _rowHeight, _rowPadding;

	private Table _textSlider;
	private Table _expandablePanel;
	private Table _backgroundTable;

	private Label.LabelStyle sls;
	private Texture _textFieldBackground, _textFieldCursor;
	private Texture _checkBoxOn, _checkBoxOff;

	private Group _filterActors;

	public CategorySelector(Category category, float width, float minHeight, float buttonWidth, float buttonHeight, float sidePad, CategorySelectorStyle css) {
		super();

		setStyle(css);

		_rowHeight = 100f;
		_rowPadding = 75f;

		// Category model initialization -----------------

		int numOfFields = category.getFields().size;
		float panelSize = numOfFields * _rowHeight + (numOfFields+1) * _rowPadding; // Panel size for this category
		_minHeight = minHeight;
		_maxHeight = minHeight + panelSize;

		_model = new Array<Category>();
		_model.add(category);
		_currentCategoryIndex=0;

		// Label -----------------------------------------
		Label.LabelStyle ls = new Label.LabelStyle();
		ls.font = _style._font;
		ls.fontColor = Color.BLACK;
		_label = new Label(category.getName(), ls);

		// Small labels
		sls = new Label.LabelStyle();
		sls.font = Utils.SMALLFONT;
		sls.fontColor = Color.BLACK;

		// TextFieldBackground
		_textFieldBackground = new Texture("numberTextField.png");
		_textFieldCursor = new Texture("cursor.png");

		// CheckBox
		_checkBoxOn = new Texture("checkBoxOn.png");
		_checkBoxOff = new Texture("checkBoxOff.png");

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
				if(!_sliding)
					prevCategory();
			}
		});

		_rightButton.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				if(!_sliding)
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

		_expandablePanel.addListener( new ActorGestureListener() {
			public void fling (InputEvent event, float velocityX, float velocityY, int button) {				
				if(!_sliding && Math.abs(velocityX)<Math.abs(velocityY)){
					if(!_expanding && _expanded && velocityY>0){
						shrink();
					}
				}
			}
		});

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

			hideBackgroundTable();

			MoveByAction expandMovePanel = Actions.moveBy(0, -(_maxHeight - _minHeight), .75f);
			SizeByAction expandSizePanel = Actions.sizeBy(0, _maxHeight - _minHeight, .75f);
			ParallelAction expandAction = Actions.parallel(expandMovePanel, expandSizePanel);
			_expandablePanel.addAction(Actions.sequence(expandAction, Actions.run(new Runnable() {

				@Override
				public void run () {

					Category currentCategory = _model.get(_currentCategoryIndex);

					setHeight(_maxHeight);
					setY(Utils.APP_HEIGHT - getHeight());
					_expanding = false;

					Array<Field> fields = currentCategory.getFields();
					_expandablePanel.row().height(_rowPadding);

					_filterActors = new Group();

					for(final Field field : fields) {
						_expandablePanel.row().height(_rowHeight).expandX().padBottom(_rowPadding);

						Label name = new Label(field._fieldName, sls);
						// Start invisible
						/*Color color = name.getColor();
						color.a = 0f;
						name.setColor(color);*/

						// Group to animate the content of the expandable panel
						_filterActors.addActor(name);

						_expandablePanel.add(name);
						switch(field._type) {
						case value :
							TextField.TextFieldStyle tfs = new TextField.TextFieldStyle();
							tfs.background = new TextureRegionDrawable(new TextureRegion(_textFieldBackground));
							tfs.font = Utils.SMALLFONT;
							tfs.fontColor = Color.BLACK;
							tfs.cursor = new TextureRegionDrawable(new TextureRegion(_textFieldCursor));
							TextField textField = new TextField("", tfs);
							textField.setText(String.valueOf(field._value));
							textField.setTextFieldFilter(new TextFieldFilter.DigitsOnlyFilter());
							textField.setAlignment(Align.center);
							textField.setMaxLength(2);
							
							// Update model if necessary
							textField.setTextFieldListener( new TextFieldListener() {
								@Override
								public void keyTyped (TextField textField, char c) {
									
									if(textField.getText().isEmpty())
										field._value = 0;
									else
										field._value = Float.valueOf(textField.getText());
								}
							});

							// Start invisible
							/*AlphaAction fadeOutTextField = Actions.fadeOut(0f);
								textField.addAction(fadeOutTextField);*/
							/*color = textField.getColor();
								color.a = 0f;
								textField.setColor(color);*/


							_filterActors.addActor(textField);

							_expandablePanel.add(textField);
							break;
						case checkBox :
							CheckBox.CheckBoxStyle cbs = new CheckBox.CheckBoxStyle();
							cbs.checkboxOff = new TextureRegionDrawable(new TextureRegion(_checkBoxOff));
							cbs.checkboxOn = new TextureRegionDrawable(new TextureRegion(_checkBoxOn));
							cbs.font = Utils.SMALLFONT;
							CheckBox checkBox = new CheckBox("", cbs);
							checkBox.setChecked((Boolean)field._value);
							checkBox.addListener( new ClickListener() {
							
								public void clicked (InputEvent event, float x, float y) {
									field._value = !(Boolean)field._value;
								}
								
							});

							// Start invisible
							/*color = checkBox.getColor();
								color.a = 0f;
								checkBox.setColor(color);*/

							_filterActors.addActor(checkBox);

							_expandablePanel.add(checkBox);

							break;
						}

					}


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

			_expandablePanel.clearChildren();
			_filterActors.clearChildren();

			_expandablePanel.addAction(Actions.sequence(shrinkAction, Actions.run(new Runnable() {

				@Override
				public void run () {
					setHeight(_minHeight);
					setY(Utils.APP_HEIGHT - getHeight());
					_expanding = false;
				}

			})));
			_expanded = false;

			showBackgroundTable();

		}
	}

	public void nextCategory() {

		_sliding = true;

		AlphaAction fadeIn = new AlphaAction();
		fadeIn.setAlpha(1f);
		fadeIn.setDuration(.75f);
		
		AlphaAction fadeOut = new AlphaAction();
		fadeOut.setAlpha(0f);
		fadeOut.setDuration(.75f);
		
		_label.addAction(Actions.sequence(fadeOut, Actions.run(new Runnable() {

			@Override
			public void run () {

				if(_expanded)
					shrink();

				if(++_currentCategoryIndex == _model.size)
					_currentCategoryIndex = 0;

				int numOfFields = _model.get(_currentCategoryIndex).getFields().size;
				_maxHeight = _minHeight + (numOfFields * _rowHeight + (numOfFields+1) * _rowPadding);

				_label.setText(_model.get(_currentCategoryIndex).getName());
			}

		}), fadeIn, Actions.run(new Runnable() {

			@Override
			public void run () {
				_sliding = false;
			}

		})));
	}

	public void prevCategory() {

		_sliding = true;

		AlphaAction fadeIn = new AlphaAction();
		fadeIn.setAlpha(1f);
		fadeIn.setDuration(.75f);
		
		AlphaAction fadeOut = new AlphaAction();
		fadeOut.setAlpha(0f);
		fadeOut.setDuration(.75f);
		
		
		_label.addAction(Actions.sequence(fadeOut, Actions.run(new Runnable() {

			@Override
			public void run () {

				if(_expanded)
					shrink();

				if(--_currentCategoryIndex == -1)
					_currentCategoryIndex = _model.size-1;

				int numOfFields = _model.get(_currentCategoryIndex).getFields().size;
				_maxHeight = _minHeight + (numOfFields * _rowHeight + (numOfFields+1) * _rowPadding);

				_label.setText(_model.get(_currentCategoryIndex).getName());


			}
		}), fadeIn, Actions.run(new Runnable() {

			@Override
			public void run () {
				_sliding = false;
			}

		})));

	}

	public void addCategory(Category category) {
		String categoryName = ClassReflection.getSimpleName(category.getClass());
		_model.add(category);
	}

	public void setBackgroundTable(Table table) {
		_backgroundTable = table;
	}

	private void hideBackgroundTable() {
		if(_backgroundTable != null) {
			AlphaAction action = new AlphaAction();
			action.setAlpha(.5f);
			action.setDuration(.75f);
			_backgroundTable.setTouchable(Touchable.disabled);
			_backgroundTable.addAction(action);
		}
	}

	private void showBackgroundTable() {
		if(_backgroundTable != null) {
			AlphaAction action = new AlphaAction();
			action.setAlpha(1f);
			action.setDuration(.75f);
			_backgroundTable.setTouchable(Touchable.enabled);
			_backgroundTable.addAction(action);
		}
	}

	public void draw(Batch batch, float parentAlpha) {
		validate();

		super.draw(batch, parentAlpha);
	}

	@Override
	public void dispose () {
		_textFieldCursor.dispose();
		_textFieldBackground.dispose();
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
