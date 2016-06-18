package de.riedelgames.onedpong;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class Hud extends Stage{

	
	private String leftPlayerName = "Unkown Left Player", rightPlayerName = "Unknown Right Player";
	
	private Table topTable;
	private Table bottomTable;
	
	private Label lScoreField, rScoreField, rallyLengthField;
	
	LabelStyle topRowStyle;

	
	
	Hud(Game game) {
		topTable = new Table();
		topTable.setFillParent(true);
		this.addActor(topTable);
		topTable.left().top();
		
		//topTable.setDebug(true);
		
		bottomTable = new Table();
		bottomTable.setFillParent(true);
		bottomTable.left().bottom();
		this.addActor(bottomTable);
		//bottomTable.setDebug(true);
		
		topRowStyle = new LabelStyle(new BitmapFont(Gdx.files.getFileHandle("font/square_unique.fnt", Files.FileType.Internal)), Color.WHITE);

		
		createPlayerLabels();
		createPlayerScore();
		createRallyLength();
		
	}
	
	private void createPlayerLabels() {
		
		// Prefix  l for left Player, r for right Player

		Label lName = new Label(leftPlayerName, topRowStyle);
		lName.setName("lName");
		lName.setFontScale(0.5f);
		Label rName = new Label(rightPlayerName, topRowStyle);
		rName.setName("rName");
		rName.setFontScale(0.5f);
		this.topTable.add(lName).expandX().left().pad(7);
		this.topTable.add(rName).expandX().right().pad(7);
		this.topTable.row();
		
	}
	
	private void createPlayerScore() {
		
		Label lScore = new Label("Score: ", topRowStyle);
		lScoreField = new Label("0", topRowStyle);
		Label rScore = new Label("Score: ", topRowStyle);
		rScoreField = new Label("0", topRowStyle);
		
		HorizontalGroup lScoreGroup = new HorizontalGroup();	
		HorizontalGroup rScoreGroup = new HorizontalGroup();
		
		lScore.setFontScale(0.5f);
		rScore.setFontScale(0.5f);
		lScoreField.setFontScale(0.5f);
		rScoreField.setFontScale(0.5f);
		lScoreGroup.addActor(lScore);
		lScoreGroup.addActor(lScoreField);
		rScoreGroup.addActor(rScore);
		rScoreGroup.addActor(rScoreField);
		this.topTable.add(lScoreGroup).expandX().left().pad(7);
		this.topTable.add(rScoreGroup).expandX().right().pad(7);
		this.topTable.row();
		
	}
	
	public void createRallyLength() {
		rallyLengthField = new Label("0", topRowStyle);
		
		rallyLengthField.setFontScale(1f);
		this.bottomTable.add(rallyLengthField).expandX().bottom().pad(10);
	}
	
	public void update() {	
		lScoreField.setText(Integer.toString(GameLogic.leftPlayer.getScore()));
		rScoreField.setText(Integer.toString(GameLogic.rightPlayer.getScore()));
		rallyLengthField.setText(Integer.toString(GameLogic.getRallyLength()));
		
	}
}
