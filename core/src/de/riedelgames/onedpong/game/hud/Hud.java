package de.riedelgames.onedpong.game.hud;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import de.riedelgames.onedpong.OneDPong;
import de.riedelgames.onedpong.game.GameStatus;

public class Hud extends Stage {

    private String leftPlayerName = "Unkown Left Player";

    private String rightPlayerName = "Unkown RightPlayer";

    private Table topTable;
    private Table bottomTable;

    private Label leftScoreField;

    private Label rightScoreField;

    private Label rallyLengthField;

    LabelStyle topRowStyle;

    /**
     * Hud Constructor.
     * 
     * @param gameStatus
     *            Reference to the game status that will be displayed.
     */
    public Hud(GameStatus gameStatus) {
        topTable = new Table();
        topTable.setFillParent(true);
        this.addActor(topTable);
        topTable.left().top();

        // topTable.setDebug(true);

        bottomTable = new Table();
        bottomTable.setFillParent(true);
        bottomTable.left().bottom();
        this.addActor(bottomTable);
        // bottomTable.setDebug(true);

        topRowStyle = new LabelStyle(
                new BitmapFont(Gdx.files.getFileHandle("font/square_unique.fnt", Files.FileType.Internal)),
                Color.WHITE);

        createPlayerLabels();
        createPlayerScore();
        createRallyLength();

    }

    private void createPlayerLabels() {

        // Prefix l for left Player, r for right Player

        Label leftName = new Label(leftPlayerName, topRowStyle);
        leftName.setName("lName");
        leftName.setFontScale(0.5f);
        Label rightName = new Label(rightPlayerName, topRowStyle);
        rightName.setName("rName");
        rightName.setFontScale(0.5f);
        this.topTable.add(leftName).expandX().left().pad(7);
        this.topTable.add(rightName).expandX().right().pad(7);
        this.topTable.row();

    }

    private void createPlayerScore() {

        Label leftScore = new Label("Score: ", topRowStyle);
        leftScoreField = new Label("0", topRowStyle);
        Label rightScore = new Label("Score: ", topRowStyle);
        rightScoreField = new Label("0", topRowStyle);

        leftScore.setFontScale(0.5f);
        rightScore.setFontScale(0.5f);
        leftScoreField.setFontScale(0.5f);
        rightScoreField.setFontScale(0.5f);

        HorizontalGroup leftScoreGroup = new HorizontalGroup();
        HorizontalGroup rightScoreGroup = new HorizontalGroup();
        leftScoreGroup.addActor(leftScore);
        leftScoreGroup.addActor(leftScoreField);
        rightScoreGroup.addActor(rightScore);
        rightScoreGroup.addActor(rightScoreField);
        this.topTable.add(leftScoreGroup).expandX().left().pad(7);
        this.topTable.add(rightScoreGroup).expandX().right().pad(7);
        this.topTable.row();

    }

    private void createRallyLength() {
        rallyLengthField = new Label("0", topRowStyle);

        rallyLengthField.setFontScale(1f);
        this.bottomTable.add(rallyLengthField).expandX().bottom().pad(10);
    }

    /**
     * Renders the HUD displaying the {@link GameStatus} provided.
     * 
     * @param gameStatus
     *            current status
     */
    public void update(GameStatus gameStatus) {
        leftScoreField.setText(Integer.toString(OneDPong.leftPlayer.getScore()));
        rightScoreField.setText(Integer.toString(OneDPong.rightPlayer.getScore()));
        rallyLengthField.setText(Integer.toString(gameStatus.getRallyLength()));

    }
}
