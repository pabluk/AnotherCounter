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
		display.setCurrent( canvas );
	}

	protected void pauseApp(){
    }

	protected void destroyApp( boolean unconditional ){
		notifyDestroyed();
	}

	public void exitMIDlet(){
		destroyApp(true);
	}

    public void addCounter() {
        if (counter <= 9999) {
            counter++;
        } else {
            counter = 0;
        }
    }

    public void subCounter() {
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
	private Command add;
	private Command sub;
	private Command reset;
	private Command exit;
	private AnotherCounter midlet;
    private Image image;
    private TiledLayer tiled;
    private static int value;
    private static String msgKey;
    private static String msgAction;

	public CanvasCounter(AnotherCounter midlet){
		this.midlet = midlet;
        value = -1;

        try {
            image = Image.createImage("/counter-tiled.png");
        } catch (Exception e) {}
        tiled = new TiledLayer(4, 1, image, 32, 32);

        tiled.setPosition((getWidth() - (32 * 4))/2, (getHeight() - 32)/2);

		add = new Command("Add", Command.EXIT, 0);
		sub = new Command("Subtract", Command.SCREEN, 1);
		reset = new Command("Reset", Command.SCREEN, 1);
		exit = new Command("Exit", Command.SCREEN, 2);
		addCommand(add);
		addCommand(sub);
		addCommand(reset);
		addCommand(exit);
		setCommandListener(this);

	} 
	
	protected void paint(Graphics g){
        showCounterImg(g);
        //showKeys(g);

	}

    protected void keyReleased(int keyCode) {
        if (keyCode > 0) {
            System.out.println("keyReleased " +((char)keyCode));
            msgKey = "keyReleased " +((char)keyCode);
        } else {
            System.out.println("keyReleased action " +getGameAction(keyCode));
            msgKey = "keyReleased action " +getGameAction(keyCode);
        }
    }

	public void commandAction(Command c, Displayable d){
        msgAction = c.getLabel();
		if (c == exit) {
            setCommandListener(null);
			midlet.exitMIDlet();
        } else if (c == add) {
            midlet.addCounter();
            repaint();
        } else if (c == sub) {
            midlet.subCounter();
            repaint();
        } else if (c == reset) {
            midlet.resetCounter();
            repaint();
        }
        repaint();

	}

    private void showKeys(Graphics g) {
        g.setColor(255, 255, 255);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(0);
        g.drawString(msgKey, 0, 0, g.LEFT | g.TOP);
        g.drawString(msgAction, 0, 0, g.RIGHT | g.TOP);
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
