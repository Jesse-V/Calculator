package HW9;

import com.sun.awt.AWTUtilities;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 *
 * @author Jesse
 */
public class Log extends JFrame implements MouseListener, ActionListener, Runnable
{
	private final float TRANSPARENCY_LEVEL = 0.75f;
	private RingBuffer<String> buffer = new RingBuffer<>(20);
	private Font f = new Font("Times New Roman", Font.BOLD, 16);
	private boolean rectangular = true, keepFadingOut = true;
	private Timer timer = new Timer(5000, this);
	
	
	public Log(Rectangle bounds)
	{
		super();
		setBounds(bounds);
		setUndecorated(true);
		makeWindowSpecial();
		addMouseListener(this);
		setVisible(true);
	}
	
	
	
	public void setTransparencyLevel(float level)
	{
		if (AWTUtilities.isTranslucencySupported(AWTUtilities.Translucency.TRANSLUCENT))
			setOpacity(level);
	}
	
	
	
	//attempts to round and make the window transparent
	public final void makeWindowSpecial()
	{
		System.out.println("Made window special");
		shapeWindow();
		setTransparencyLevel(TRANSPARENCY_LEVEL);
	}
	
	
	
	//attempts to round the window if supported
	public void shapeWindow()
	{
		System.out.println("Rounded the window corners");
		Shape shape = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 60, 60);
		if (AWTUtilities.isWindowShapingSupported())
			setShape(shape);
		rectangular = false;
	}
	
	
	
	//puts the window back to regular rectangular shape
	public void unShapeWindow()
	{
		System.out.println("Unrounded window corners");
		setShape(null);
		rectangular = true;
	}
	
	
	
	//appends the given String to the log buffer
	public void appendToLog(String str)
	{
		System.out.println("Appended to log");
		buffer.enqueue(str);
		resetTimer();
		repaint();
	}
	
	
	
	@Override
	public void paint(Graphics g)
	{
		g.setColor(new Color(100, 149, 237)); //"Cornflower Blue" from http://www.tayloredmktg.com/rgb/
		g.fillRect(0, 0, getWidth(), getHeight());
		
		if (buffer.isEmpty())
			return;
		
		//this hack is needed since the draw function tries to resize the window,
			//which apparently can't be done if the window isn't rectangular
		if (rectangular)
		{
			draw(g);
			shapeWindow();
		}
		else
		{
			unShapeWindow();
			draw(g);
		}
	}
	
	
	
	//draws the log buffer on the screen
	public void draw(Graphics g)
	{
		System.out.println("redrawing buffer");
		
		g.setColor(Color.BLACK);
		g.setFont(f);
		
		int minWidth = 1;
		
		FontMetrics fm = g.getFontMetrics();
		int dH = fm.getHeight();
		int y = getHeight() - buffer.size() * dH + 10;
		
		for (int j = 0; j < buffer.size(); j++, y += dH)
		{
			if (y > 10)
			{
				String str = buffer.peek(j);
				int strWidth = SwingUtilities.computeStringWidth(fm, str);
				minWidth = Math.max(strWidth, minWidth);
				int x = (getWidth() - strWidth) / 2; //center
				
				g.drawString(str, x, y);
			}
		}
		
		setSize(minWidth + 50, getHeight());
	}

	
	
	//called by the Timer
	@Override
	public void actionPerformed(ActionEvent e)
	{
		System.out.println("timer fired");
		new Thread(this).start();
	}
	
	
	
	//resets the timer, so starts it if its stopped
	public void resetTimer()
	{
		System.out.println("Timer reset");
		setTransparencyLevel(TRANSPARENCY_LEVEL);
		timer.restart();
		keepFadingOut = true;
	}

	
	
	//thread function to slowly turn the screen to totally transparent if inactive
	@Override
	public void run()
	{
		try
		{
			timer.stop();
			long totalTime = 2000;
			long resolution = 50;
			
			for (int j = 1; j <= resolution && keepFadingOut; j++)
			{
				setTransparencyLevel(TRANSPARENCY_LEVEL - TRANSPARENCY_LEVEL * j / resolution);
				Thread.sleep(totalTime / resolution);
			}
			
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
	}

	
	
	@Override
	public void mouseClicked(MouseEvent e)
	{ }

	
	
	@Override
	public void mousePressed(MouseEvent e)
	{ }

	
	
	@Override
	public void mouseReleased(MouseEvent e)
	{ }

	
	
	@Override
	public void mouseEntered(MouseEvent e)
	{
		keepFadingOut = false;
		timer.stop();
		setTransparencyLevel(TRANSPARENCY_LEVEL);
	}

	
	
	@Override
	public void mouseExited(MouseEvent e)
	{
		resetTimer();
		repaint();
	}
}
