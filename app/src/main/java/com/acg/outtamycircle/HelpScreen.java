package com.acg.outtamycircle;

import android.graphics.Color;

import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.impl.AndroidButton;
import com.badlogic.androidgames.framework.impl.AndroidGame;
import com.badlogic.androidgames.framework.impl.AndroidRectangularButton;
import com.badlogic.androidgames.framework.impl.AndroidScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class HelpScreen extends AndroidScreen {
    private final AndroidButton backButton = new AndroidRectangularButton(androidGame.getGraphics(),66,550,324,124);
    private boolean unchanged = false;
    public HelpScreen(AndroidGame androidGame) {
        super(androidGame);
        backButton.setPixmap(Assets.back);
    }

    @Override
    public void update(float deltaTime) {
        boolean goMainMenuScreen = false;
        for(Input.TouchEvent event: androidGame.getInput().getTouchEvents()) {
            if(backButton.inBounds(event) && event.type == Input.TouchEvent.TOUCH_UP) {
                if(Settings.soundEnabled)
                    Assets.click.play(Settings.volume);
                goMainMenuScreen = true;
                break;
            }
        }
        if(goMainMenuScreen)
            androidGame.setScreen(new MainMenuScreen(androidGame));
    }

    /**
     * Split a string into a list of lines according to the max length of each line
     * @param longString string which must be splitted in more lines
     * @param maxLength max length of each line
     * @return the list of lines
     */
    private List<String> splitIntoLines(String longString, int maxLength) {
        longString = longString.trim().replaceAll(" +", " ");
        List<String> ret = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(longString, " ");
        StringBuilder builder = new StringBuilder(maxLength);
        while(tokenizer.hasMoreTokens()) {
            String word = tokenizer.nextToken();
            if(builder.length() + word.length() <= maxLength)
                builder.append(word + " ");
            else {
                ret.add(builder.toString().trim());
                builder.setLength(0);
                builder.append(word + " ");
            }
        }
        if(builder.length() > 0)
            ret.add(builder.toString());
        return ret;
    }

    @Override
    public void present(float deltaTime) {
        if(unchanged)
            return;
        unchanged = true;
        Graphics graphics = androidGame.getGraphics();
        List<String> lines = splitIntoLines(androidGame.getString(R.string.help), 73);
        graphics.drawEffect(Assets.backgroundTile, 0,0,graphics.getWidth(),graphics.getHeight());
        int i = 0;
        for(String line: lines)
            graphics.drawText(line,66, 66+45*i++, 35, Color.BLACK);
        // blank line
        graphics.drawText(androidGame.getString(R.string.authors),66, 66+45*++i, 35, Color.BLACK);
        backButton.draw();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void back() {
        if(Settings.soundEnabled)
            Assets.click.play(Settings.volume);
        androidGame.setScreen(new MainMenuScreen(androidGame));
    }
}
