package HW9;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

/**
 *
 * @author Jesse
 */
public class CalculatorFunctionality implements ActionListener, KeyListener
{
	/*
	 * Show log on the screen for user readability. Show or not?
	 */
	
	public JTextField firstInput = new JTextField(); //value on top. User can't edit this
	public JTextField operationInput = new JTextField(); //holds the operation
	public JTextField secondInput = new JTextField(); //lower value for the user input
	public JTextField resultOutput = new JTextField(); //result of calculation
	
	public Font inputFont = new Font("Arial", Font.PLAIN, 20);
	public Log log;
	
	private JToggleButton altButton = new JToggleButton("2nd");
	private AbstractButton[][] buttons = //the layout of the buttons and their labels
			{{new JButton("<--"),	new JButton("Clear"),	altButton,					new JButton("E")},
			{new JButton("sin"),	new JButton("cos"),		new JButton("tan"),			new JButton("PI")},
			{new JButton("sqr"),	new JButton("x^y"),		new JButton("inv"),			new JButton("/")},
			{new JButton("7"),		new JButton("8"),		new JButton("9"),			new JButton("*")},
			{new JButton("4"),		new JButton("5"),		new JButton("6"),			new JButton("-")},
			{new JButton("1"),		new JButton("2"),		new JButton("3"),			new JButton("+")},
			{new JButton("0"),		new JButton("."),		new JButton("+/-"),			new JButton("=")}};
	private String[][] alternativeNames = //lists conversions between the original name and the alt name
			{{"sin",	"sin-1"},
			{"cos",		"cos-1"},
			{"tan",		"tan-1"},
			{"sqr",		"sqrt"},
			{"E",		"g"},
			{"PI",		"c"}};
	
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		AbstractButton button = (AbstractButton)e.getSource();
		handleInput(button.getText());
	}
	
	
	
	//returns all the buttons
	public AbstractButton[][] getButtons()
	{
		return buttons;
	}
	
	
	
	//makes sure that some of the buttons have the alternative names
	public void switchToAltButtons()
	{
		for (int j = 0; j < buttons.length; j++)
			for (int k = 0; k < buttons[j].length; k++)
				for (int m = 0; m < alternativeNames.length; m++)
					if (buttons[j][k].getText().equals(alternativeNames[m][0]))
						buttons[j][k].setText(alternativeNames[m][1]); //switch to alts
		altButton.setSelected(true);
	}
	
	
	
	//makes sure that the buttons are back to their regular names
	public void switchBackButtons()
	{
		for (int j = 0; j < buttons.length; j++)
				for (int k = 0; k < buttons[j].length; k++)
					for (int m = 0; m < alternativeNames.length; m++)
						if (buttons[j][k].getText().equals(alternativeNames[m][1]))
							buttons[j][k].setText(alternativeNames[m][0]); //undo alts
		altButton.setSelected(false);
	}
	
	
	
	//master function for handling any incoming input
	public void handleInput(String inputStr)
	{
		System.out.println("Got input \""+inputStr+"\"");
		
		//note: button selected state changes before this code is executed
		if (altButton.isSelected() && inputStr.equals(altButton.getText()))
			switchToAltButtons(); //called the alt button wants to be selected
		else
			switchBackButtons(); //called if the alt button wants to be unselect or if its another button
		
		switch (inputStr)
		{
			case "Clear":
				System.out.println("handled clear function");
				clearInputs();
				return;
				
			case "<--":
				System.out.println("handled backspace");
				if (!Character.isDigit(secondInput.getText().charAt(0)))
					secondInput.setText("0");
				else
				{
					if (secondInput.getText().length() > 0)
						secondInput.setText(secondInput.getText().substring(0, secondInput.getText().length()-1));
					if (secondInput.getText().length() == 0)
						secondInput.setText("0");
				}
				resultOutput.setText("");
				return;
		}
		
		if (handleConstant(inputStr))
			return;
		
		if (inputStr.length() == 1 && (Character.isDigit(inputStr.charAt(0)) || inputStr.equals(".")))
			handleInputChar(inputStr.charAt(0)); //handle digit, decimal point, etc
		else if (countOperationArguments(inputStr) > 0)
			handleOperationInput(inputStr); //handle an operation
		else if (inputStr.equals("+/-"))
			invertInputSign(); //process the invert command
		else if (inputStr.equals("="))
			displayResult(); //display the result
		
		System.out.println("Finished with \"" + inputStr + "\"");
	}
	
	
	
	//if the given input represents a constant,
	//handles it and returns true, otherwise does nothing and returns false
	private boolean handleConstant(String inputStr)
	{
		switch (inputStr)
		{
			case "E":
				if (!resultOutput.getText().isEmpty())
					clearInputs();
				secondInput.setText(""+Math.E);
				return true;
			
			case "PI":
				if (!resultOutput.getText().isEmpty())
					clearInputs();
				secondInput.setText(""+Math.PI);
				return true;
			
			case "g":
				if (!resultOutput.getText().isEmpty())
					clearInputs();
				secondInput.setText(""+9.808175174); //gravitational constant of Earth in m/s^2
				return true;
			
			case "c":
				if (!resultOutput.getText().isEmpty())
					clearInputs();
				secondInput.setText(""+299792458); //speed of light in m/s
				return true;
		}
		
		return false;
	}
	
	
	
	//handles a digit, decimal point, etc
	private void handleInputChar(char inputChar)
	{
		System.out.println("handling input: "+inputChar);
		if (!resultOutput.getText().isEmpty())
			clearInputs();
		
		if (secondInput.getText().length() >= 21)
		{
			System.out.println("Field has reached maximum length. Rejecting input.");
			return;
		}
		
		if (secondInput.getText().endsWith(".") && inputChar == '.')
		{
			System.out.println("Decimal point is already present. Rejecting input.");
			return; //don't do anything if the user is trying to enter two decimal points
		}
		
		System.out.println("Appending \"" + inputChar + "\"");
		resultOutput.setText("");
		secondInput.setText(secondInput.getText() + inputChar);
		if (secondInput.getText().charAt(0) == '0' && inputChar != '.')
			secondInput.setText(secondInput.getText().substring(1));
	}
	
	
	
	//handles operations; instantly executes those that take 1 argument (like sqrt or sine)
	private void handleOperationInput(String op)
	{
		System.out.println("handling operation: "+op);
		
		if (countOperationArguments(op) == 1)
		{
			System.out.println("Performed internal one-argument op \""+op+"\"");
			if (resultOutput.getText().isEmpty())
				secondInput.setText(getCalculatedResult(op));
			else
			{
				firstInput.setText("0");
				secondInput.setText(resultOutput.getText());
				secondInput.setText(getCalculatedResult(op));
			}
		}
		else
		{
			firstInput.setText(getCalculatedResult(operationInput.getText()));
			secondInput.setText("0");
			operationInput.setText(op);
			System.out.println("Changed operation to two-argument op \""+op+"\"");
		}
		
		resultOutput.setText("");
	}
	
	
	
	//displays the calculated result
	private void displayResult()
	{
		System.out.println("Displaying result");
		secondInput.setText(filterTrailingDecimalPoint(secondInput.getText())); //filter in place
		
		if (!resultOutput.getText().isEmpty())
		{ //if the result field is already filled out
			
			if (countOperationArguments(operationInput.getText()) == 1)
				secondInput.setText(filterTrailingDecimalPoint(resultOutput.getText())); //make 2nd the result
			else
				firstInput.setText(filterTrailingDecimalPoint(resultOutput.getText())); //make 1st the result
		}
		
		resultOutput.setText(getCalculatedResult(operationInput.getText())); //show the result of the calculation
	}
	
	
	
	//returns result of operation, or NaN if op failed due to math error or unknown op
	private String getCalculatedResult(String op)
	{		
		double num1 = Double.parseDouble(firstInput.getText());
		double num2 = Double.parseDouble(secondInput.getText());
		double result = Double.NaN;
		
		switch (op)
		{
			case "+":
				result = num1 + num2;
				break;
				
			case "-":
				result = num1 - num2;
				break;
				
			case "*":
				result = num1 * num2;
				break;
				
			case "/":
				if (num2 != 0)
					result = num1 / num2;
				break;
				
			case "x^y":
				result = Math.pow(num1, num2);
				break;
				
			case "x root y":
				if (num2 != 0)
					result = Math.pow(num1, 1 / num2);
				break;
				
			case "sqrt":
				result = Math.sqrt(num2);
				break;
			
			case "sqr":
				result = Math.pow(num2, 2);
				break;
				
			case "inv":
				if (num2 != 0)
					result = 1 / num2;
				break;
				
			case "sin":
				result = Math.sin(num2);
				break;
				
			case "sin-1":
				result = Math.asin(num2);
				break;
				
			case "cos":
				result = Math.cos(num2);
				break;
				
			case "cos-1":
				result = Math.acos(num2);
				break;
				
			case "tan":
				result = Math.tan(num2);
				break;
				
			case "tan-1":
				result = Math.atan(num2);
				break;
		}
		
		String calculationEvent = "";
		if (countOperationArguments(op) == 1)
			calculationEvent += op + " of " + num2 + " is " + result;
		else
			calculationEvent += num1 + " " + op + " " + num2 + " = " + result;
		System.out.println(calculationEvent);
		log.appendToLog(calculationEvent);
		
		long rounded = Math.round(result);
		if (rounded == result)
			return ""+rounded;
		else
			return ""+result;
	}
	
	
	
	//count the number of argument the given operation takes
	private int countOperationArguments(String str)
	{
		switch (str)
		{
			case "+": return 2;
			case "-": return 2;
			case "*": return 2;
			case "/": return 2;
			case "x^y": return 2;
			case "x root y": return 2;
			case "sqr": return 1;
			case "sqrt": return 1;
			case "inv": return 1;
			case "sin": return 1;
			case "sin-1": return 1;
			case "cos": return 1;
			case "cos-1": return 1;
			case "tan": return 1;
			case "tan-1": return 1;
		}
		
		return 0;
	}
	
	
	//clears all inputs, resets everything back to default
	public final void clearInputs()
	{
		System.out.println("Cleared all input fields");
		
		firstInput.setText("0");
		operationInput.setText("+");
		secondInput.setText("0");
		resultOutput.setText("");
		altButton.setSelected(false);
	}
	
	
	
	//inverts the input value
	private void invertInputSign()
	{
		System.out.println("inverted sign");
		
		String currText = secondInput.getText();
		if (currText.charAt(0) == '-')
			secondInput.setText(currText.substring(1));
		else
			secondInput.setText("-"+currText);
	}
	
	
	
	//removes a trailing decimal point from the string if there is one
	private String filterTrailingDecimalPoint(String str)
	{
		if (str.endsWith("."))
			return str.substring(0, str.length()-1);
		return str;
	}
	
	
	
	//handle main keys here
	@Override
	public void keyTyped(KeyEvent e)
	{
		char c = e.getKeyChar();
		if (Character.isDigit(c))
			triggerButton(""+c);
		else
			switch (c)
			{
				case '.':
				case '+':
				case '-':
				case '*':
				case '/':
				case '=':
				triggerButton(""+c);
			}
	}
	
	
	
	//finds and triggers the button whose text matches the given string
	public void triggerButton(String str)
	{
		for (int j = 0; j < buttons.length; j++)
		{
			for (int k = 0; k < buttons[j].length; k++)
			{
				if (buttons[j][k].getText().equals(str))
				{
					System.out.println("Button triggered: " + buttons[j][k].getText());
					buttons[j][k].doClick(); //150
					return;
				}
			}
		}
	}

	
	
	//handle some of the special keys here
	@Override
	public void keyPressed(KeyEvent e)
	{
		String keyParamStr = e.paramString();
		int keyCharIndex = keyParamStr.indexOf("keyChar");
		System.out.println("Keystroke: " + keyParamStr.substring(keyCharIndex, keyParamStr.indexOf(',', keyCharIndex)));
		
		int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_SHIFT || keyCode == KeyEvent.VK_ALT)
		{
			System.out.println("Handled shift/alt key press");
			switchToAltButtons();
		}
		else if (keyCode == KeyEvent.VK_BACK_SPACE)
			triggerButton("<--");
		else if (keyCode == KeyEvent.VK_ENTER)
			triggerButton("=");
		else if (keyCode == KeyEvent.VK_ESCAPE)
			triggerButton("Clear");	
	}

	
	
	//same as above, but just undo their function
	@Override
	public void keyReleased(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_SHIFT || e.getKeyCode() == KeyEvent.VK_ALT)
		{
			System.out.println("Handled shift/alt key release");
			switchBackButtons();
		}
	}
}
