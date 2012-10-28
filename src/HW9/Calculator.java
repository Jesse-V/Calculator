package HW9;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.*;

/**
*
* @author Jesse
*/
public class Calculator extends JFrame implements ComponentListener
{
	private CalculatorFunctionality calc = new CalculatorFunctionality();
	
	
	public static void main(String[] args)
	{
		new Calculator();
	}
	
	
	
	public Calculator()
	{
		super("Calculator FTW");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(350, 320);
		setResizable(false);
		setLocation(300, 200);
		
		calc.clearInputs();
		
		calc.log = new Log(getCalculatorLogBounds());
		addComponentListener(this);
		
		JTextField[] fieldArr = {calc.firstInput, calc.operationInput, calc.secondInput, calc.resultOutput};
		for (JTextField field : fieldArr)
		{
			field.setEditable(false);
			field.setBackground(Color.WHITE);
			field.setHorizontalAlignment(JTextField.CENTER);
			field.addKeyListener(calc);
			field.setFont(calc.inputFont);
		}
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.add(getInputs());
		panel.add(getButtons());
		getContentPane().add(panel);
		
		setVisible(true);
	}
	
	
	
	//lays out all the text inputs
	public final JPanel getInputs()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = c.weighty = 1;
		
		JLabel spacer = new JLabel();
		
		c.gridx = 0;
		c.gridy = 0;
		panel.add(spacer, c);
		
		c.gridx = 1;
		c.gridy = 0;
		panel.add(calc.firstInput, c);

		c.gridx = 0;
		c.gridy = 1;
		panel.add(calc.operationInput, c);
		c.weightx = 1;

		c.gridx = 1;
		c.gridy = 1;
		panel.add(calc.secondInput, c);
		
		c.gridx = 0;
		c.gridy = 2;
		panel.add(spacer, c);
		
		c.gridx = 1;
		c.gridy = 2;
		c.ipadx = 180;
		panel.add(calc.resultOutput, c);
		
		return panel;
	}
	
	
	
	//lays out all the buttons
	public final JPanel getButtons()
	{
		JPanel panel = new JPanel();
		AbstractButton[][] buttons = calc.getButtons();
		panel.setLayout(new GridLayout(buttons.length, buttons[0].length, 2, 2));
		
		for (int j = 0; j < buttons.length; j++)
		{
			for (int k = 0; k < buttons[j].length; k++)
			{
				if (buttons[j][k].getText().length() == 0)
					buttons[j][k].setEnabled(false);
				else
					buttons[j][k].setBackground(new Color(204, 255, 204));
				
				buttons[j][k].addActionListener(calc);
				buttons[j][k].addKeyListener(calc);
				buttons[j][k].setFocusPainted(false);
				
				panel.add(buttons[j][k]);
			}
		}
		
		return panel;
	}

	
	
	//make sure the log window lines up again
	@Override
	public void componentResized(ComponentEvent e)
	{
		Rectangle rect = getCalculatorLogBounds();
		calc.log.setLocation(rect.getLocation());
	}

	
	
	//move the log button with it
	@Override
	public void componentMoved(ComponentEvent e)
	{
		Rectangle rect = getCalculatorLogBounds();
		calc.log.setLocation(rect.getLocation());
	}

	
	
	@Override
	public void componentShown(ComponentEvent e)
	{ }

	
	
	@Override
	public void componentHidden(ComponentEvent e)
	{ }
	
	
	
	//returns where the log window should go and how tall it should be
	public final Rectangle getCalculatorLogBounds()
	{
		return new Rectangle(getX() + getWidth(), getY(), 1, getHeight());
	}
}
