/*
 * Another Counter - Copyright (c) 2009 Pablo Seminario
 * This software is distributed under the terms of the GNU General
 * Public License
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import javax.microedition.midlet.*;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.game.TiledLayer;

public class AnotherCounter extends MIDlet{
	private Display  display;
	private CanvasCounter canvas;
    private static int counter;

	public AnotherCounter(){
		display = Display.getDisplay(this);
		canvas  = new CanvasCounter(this);
	}

	protected void startApp(){
		display.setCurrent(canvas);
	}

	protected void pauseApp(){
    }

	protected void destroyApp( boolean unconditional ){
		notifyDestroyed();
	}

	public void exitMIDlet(){
		destroyApp(true);
	}

    public void incCounter() {
        if (counter <= 9999) {
            counter++;
        } else {
            counter = 0;
        }
    }

    public void decCounter() {
        if (counter > 0) {
            counter--;
        }
    }

    public void resetCounter() {
        counter = 0;
    }

    public int getCounter() {
        return counter;
    }

}

class CanvasCounter extends Canvas implements CommandListener{
	private Command inc;
	private Command dec;
	private Command reset;
	private Command exit;
	private AnotherCounter midlet;
    private Image image;
    private TiledLayer tiled;
    private static int value;

	public CanvasCounter(AnotherCounter midlet){
		this.midlet = midlet;
        value = -1;

        try {
            image = Image.createImage("/counter-tiled.png");
        } catch (Exception e) {}
        tiled = new TiledLayer(4, 1, image, 32, 32);

        tiled.setPosition((getWidth() - (32 * 4))/2, (getHeight() - 32)/2);

		inc = new Command("Add", Command.EXIT, 0);
		dec = new Command("Subtract", Command.SCREEN, 1);
		reset = new Command("Reset", Command.SCREEN, 1);
		exit = new Command("Exit", Command.SCREEN, 2);
		addCommand(inc);
		addCommand(dec);
		addCommand(reset);
		addCommand(exit);
		setCommandListener(this);
	} 
	
	protected void paint(Graphics g){
        showCounterImg(g);
	}

	public void commandAction(Command c, Displayable d){
		if (c == exit) {
            setCommandListener(null);
			midlet.exitMIDlet();
        } else if (c == inc) {
            midlet.incCounter();
            repaint();
        } else if (c == dec) {
            midlet.decCounter();
            repaint();
        } else if (c == reset) {
            midlet.resetCounter();
            repaint();
        }
        repaint();

	}

    private void showCounterImg(Graphics g) {
        int [] value_array;
        int units, tens, hundreds, thousands;

        g.setColor(0);
        g.fillRect(0, 0, getWidth(), getHeight());

        if (value != midlet.getCounter()) {
            value = midlet.getCounter();
            thousands = value / 1000;
            hundreds = (value / 100) - thousands * 10;
            tens = (value / 10) - thousands * 100 - hundreds * 10;
            units = (value / 1) - thousands * 1000 - hundreds * 100 - tens * 10;

            value_array = new int[4];

            value_array[0] = thousands;
            value_array[1] = hundreds;
            value_array[2] = tens;
            value_array[3] = units;

            for (int i = 0; i < 4; i++) {
                if (value_array[i] == 0) { value_array[i] = 10; }
                if (tiled.getCell(i, 0) != value_array[i]) {
                    tiled.setCell(i, 0, value_array[i]);
                }
            }
        }
        tiled.paint(g);
    }

}
