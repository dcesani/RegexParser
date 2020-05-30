package RegexParser;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.SystemColor;
/**
Generates the User Interface used by user. 
*/
public class UserInterface extends JDialog implements UserInterface_interface {

	private static final long serialVersionUID = 1L;
	private JFrame frmRegularExpressionParser;
	private JTextField Text_Sigma;
	private JTextField Text_Re;
	private boolean editable=false;
	boolean on_apice=true;
	boolean on_pedice=true;
	String s, apice, pedice;
	int l;
	private JTextField Text_Apice;
	private JTextField Text_Pedice;
	JSplitPane BorderLeftSplit;
	RegexParser r;
	
	public UserInterface() {
		initialize(this);
		this.frmRegularExpressionParser.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 * @param UI UserInterface object
	 */
	private void initialize(UserInterface UI) {
		
		
		frmRegularExpressionParser = new JFrame();
		frmRegularExpressionParser.setTitle("REGULAR EXPRESSION PARSER");
		frmRegularExpressionParser.setBounds(100, 100, 859, 622);
		frmRegularExpressionParser.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			

		frmRegularExpressionParser.setIconImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getClassLoader().getResource("./icona7.png")));
		
		
		
		//----------------------------   Alphabet Panel   ----------------------------------------------
		
		//-----------BorderLeft split-------------
		BorderLeftSplit = new JSplitPane();
		BorderLeftSplit.setEnabled(false);
		BorderLeftSplit.setResizeWeight(0.5);
		BorderLeftSplit.setDividerSize(0);
		BorderLeftSplit.setBorder(null);
		BorderLeftSplit.setVisible(true);
		frmRegularExpressionParser.getContentPane().setLayout(new BorderLayout(0, 0));
		frmRegularExpressionParser.getContentPane().add(BorderLeftSplit);
		
		//-----------BorderRight split-------------
		JSplitPane BorderRightSplit = new JSplitPane();
		BorderRightSplit.setEnabled(false);
		BorderRightSplit.setDividerSize(0);
		BorderRightSplit.setBorder(null);
		BorderLeftSplit.setRightComponent(BorderRightSplit);
		
		JSplitPane epsStringSplit = new JSplitPane();
		epsStringSplit.setEnabled(false);
		epsStringSplit.setOrientation(JSplitPane.VERTICAL_SPLIT);
		epsStringSplit.setResizeWeight(0.4);
		epsStringSplit.setDividerSize(0);
		epsStringSplit.setBorder(null);
		BorderRightSplit.setLeftComponent(epsStringSplit);
		
		JSplitPane splitPane_3 = new JSplitPane();
		splitPane_3.setEnabled(false);
		splitPane_3.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane_3.setResizeWeight(0.7);
		splitPane_3.setDividerSize(0);
		splitPane_3.setBorder(null);
		epsStringSplit.setLeftComponent(splitPane_3);
		
		JPanel UpperBorderPanel = new JPanel();
		UpperBorderPanel.setBackground(new Color(70, 109, 128));
		splitPane_3.setLeftComponent(UpperBorderPanel);
		
		JLabel lblNewLabel_2 = new JLabel("REGULAR EXPRESSION PARSER");
		lblNewLabel_2.setForeground(new Color(140, 218, 255));
		lblNewLabel_2.setFont(new Font("MV Boli", Font.BOLD, 23));
		UpperBorderPanel.add(lblNewLabel_2);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(70, 109, 128));
		panel.setToolTipText("∑ = {abc...}");
		panel.setBorder(null);
		splitPane_3.setRightComponent(panel);
		
		JLabel Sigma = new JLabel("Alphabet ∑ = {");
		Sigma.setForeground(new Color(140, 218, 255));
		Sigma.setFont(new Font("Tahoma", Font.BOLD, 16));
		panel.add(Sigma);
		
		Text_Sigma = new JTextField();
		Text_Sigma.setForeground(SystemColor.text);
		Text_Sigma.setBackground(new Color(70, 109, 128));
		Text_Sigma.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				s= Text_Sigma.getText().replace("U", "");
				Text_Sigma.setText(s);
				l=s.length();
			
				if (l>0)
					{
						Text_Re.setEditable(true);
						editable=true;
					}
				else
				{
					Text_Re.setEditable(false);
					editable=false;
				}
			}
		});
		Text_Sigma.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent arg0) {
				s= Text_Sigma.getText().replace("U", "");
				Text_Sigma.setText(s);
				l=s.length();
			
				if (l>0)
					{
						Text_Re.setEditable(true);
						editable=true;
					}
				else
				{
					Text_Re.setEditable(false);
					editable=false;
				}
			}
		});
		Text_Sigma.setFont(new Font("Tahoma", Font.PLAIN, 16));
		Text_Sigma.setToolTipText("∑ = {abc}");
		Text_Sigma.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				s= Text_Sigma.getText().replace("U", "");
				Text_Sigma.setText(s);
				l=s.length();
			
				if (l>0)
				{
						Text_Re.setEditable(true);
						editable=true;
					}
				else
				{
					Text_Re.setEditable(false);
					editable=false;
				}
			}
		});
		panel.add(Text_Sigma);
		Text_Sigma.setColumns(15);
		
		JLabel lblNewLabel_1 = new JLabel("}");
		lblNewLabel_1.setForeground(new Color(140, 218, 255));
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 16));
		panel.add(lblNewLabel_1);
		
		JSplitPane splitPane_4 = new JSplitPane();
		splitPane_4.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane_4.setEnabled(false);
		splitPane_4.setDividerSize(0);
		splitPane_4.setResizeWeight(0.9);
		splitPane_4.setBorder(null);
		epsStringSplit.setRightComponent(splitPane_4);
		
		JSplitPane splitPane_5 = new JSplitPane();
		splitPane_5.setResizeWeight(0.2);
		splitPane_5.setEnabled(false);
		splitPane_5.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane_5.setDividerSize(0);
		splitPane_5.setBorder(null);
		splitPane_4.setLeftComponent(splitPane_5);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(70, 109, 128));
		splitPane_5.setLeftComponent(panel_1);
		
		JLabel lblNewLabel = new JLabel("        Regex : ");
		lblNewLabel.setForeground(new Color(140, 218, 255));
		
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		panel_1.add(lblNewLabel);
		
		Text_Re = new JTextField();
		Text_Re.setBackground(new Color(70, 109, 128));
		Text_Re.setForeground(new Color(255, 255, 255));
		Text_Re.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				if(!on_apice)
					on_apice=true;
				if(!on_pedice)
					on_pedice=true;
			}
		});
		Text_Re.setFont(new Font("Tahoma", Font.PLAIN, 16));
		Text_Re.setEditable(false);
		panel_1.add(Text_Re);
		Text_Re.setColumns(15);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.1);
		splitPane.setEnabled(false);
		splitPane.setDividerSize(0);
		splitPane.setBorder(null);
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane_5.setRightComponent(splitPane);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(new Color(70, 109, 128));
		splitPane.setLeftComponent(panel_2);
		
		JButton Button_1 = new JButton("*");
		Button_1.setForeground(new Color(140, 218, 255));
		Button_1.setBackground(new Color(70, 109, 128));
		Button_1.setFont(new Font("Tahoma", Font.BOLD, 16));
		Button_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(editable)
					{
						Text_Re.setText(Text_Re.getText()+'*');
						if(!on_apice)
							on_apice=true;
						if(!on_pedice)
							on_pedice=true;
					}
				
			}
		});
		panel_2.add(Button_1);
		
		JButton Button_2 = new JButton("+");
		Button_2.setForeground(new Color(140, 218, 255));
		Button_2.setBackground(new Color(70, 109, 128));
		Button_2.setFont(new Font("Tahoma", Font.BOLD, 16));
		Button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(editable)
				{
					Text_Re.setText(Text_Re.getText()+'\u207A');
					if(!on_apice)
						on_apice=true;
					if(!on_pedice)
						on_pedice=true;
				}
			}
		});
		panel_2.add(Button_2);
		
		JButton btnu = new JButton("U");
		btnu.setForeground(new Color(140, 218, 255));
		btnu.setBackground(new Color(70, 109, 128));
		btnu.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(editable)
				{
					Text_Re.setText(Text_Re.getText()+'U');
					if(!on_apice)
						on_apice=true;
					if(!on_pedice)
						on_pedice=true;
				}
			}
		});
		panel_2.add(btnu);
		
		JButton Button_4 = new JButton("∙");
		Button_4.setForeground(new Color(140, 218, 255));
		Button_4.setBackground(new Color(70, 109, 128));
		Button_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(editable)
				{
					Text_Re.setText(Text_Re.getText()+'\u2219');
					if(!on_apice)
						on_apice=true;
					if(!on_pedice)
						on_pedice=true;
				}
			}
		});
		panel_2.add(Button_4);
		Button_4.setFont(new Font("Tahoma", Font.BOLD, 16));
		
		JSplitPane splitPane_1 = new JSplitPane();
		splitPane_1.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setRightComponent(splitPane_1);
		splitPane.setBorder(null);
		splitPane_1.setBorder(null);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBackground(new Color(70, 109, 128));
		panel_4.setBorder(null);
		splitPane_1.setLeftComponent(panel_4);
		
		JButton Button_5 = new JButton(" (");
		Button_5.setForeground(new Color(140, 218, 255));
		Button_5.setBackground(new Color(70, 109, 128));
		Button_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(editable)
				{
					Text_Re.setText(Text_Re.getText()+"(");
					if(!on_apice)
						on_apice=true;
					if(!on_pedice)
						on_pedice=true;
				}
			}
		});
		Button_5.setFont(new Font("Tahoma", Font.BOLD, 16));
		panel_4.add(Button_5);
		
		JButton Button_6 = new JButton(" )");
		Button_6.setForeground(new Color(140, 218, 255));
		Button_6.setBackground(new Color(70, 109, 128));
		Button_6.setFont(new Font("Tahoma", Font.BOLD, 16));
		Button_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(editable)
				{
					Text_Re.setText(Text_Re.getText()+")");
					if(!on_apice)
						on_apice=true;
					if(!on_pedice)
						on_pedice=true;
				}
			}
		});
		panel_4.add(Button_6);
		
		JButton Button_7 = new JButton("[ ");
		Button_7.setForeground(new Color(140, 218, 255));
		Button_7.setBackground(new Color(70, 109, 128));
		Button_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(editable)
				{
					Text_Re.setText(Text_Re.getText()+'[');
					if(!on_apice)
						on_apice=true;
					if(!on_pedice)
						on_pedice=true;
				}
			}
		});
		Button_7.setFont(new Font("Tahoma", Font.BOLD, 16));
		panel_4.add(Button_7);
		
		JButton Button_8 = new JButton("]");
		Button_8.setForeground(new Color(140, 218, 255));
		Button_8.setBackground(new Color(70, 109, 128));
		Button_8.setFont(new Font("Tahoma", Font.BOLD, 16));
		Button_8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(editable)
				{
					Text_Re.setText(Text_Re.getText()+']');
					if(!on_apice)
						on_apice=true;
					if(!on_pedice)
						on_pedice=true;
				}
			}
		});
		panel_4.add(Button_8);
		
		JSplitPane splitPane_2 = new JSplitPane();
		splitPane_2.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane_1.setRightComponent(splitPane_2);
		splitPane_2.setBorder(null);
		JPanel panel_7 = new JPanel();
		panel_7.setBackground(new Color(70, 109, 128));
		splitPane_2.setLeftComponent(panel_7);
		panel_7.setBorder(null);
		splitPane_2.setDividerSize(0);
		splitPane_2.setEnabled(false);
		splitPane_1.setDividerSize(0);
		splitPane_1.setEnabled(false);
		
		JButton btnApice = new JButton("   Apex    ");
		btnApice.setForeground(new Color(140, 218, 255));
		btnApice.setBackground(new Color(70, 109, 128));
		btnApice.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnApice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(on_apice && Text_Re.isEditable())
				{
		
					if(btnApice.getText()=="   Apex    ")
						{	
							btnApice.setText("Insert");
							Text_Apice.setText(null);
							Text_Apice.setVisible(true);
						}
					else
						{	
							btnApice.setText("   Apex    ");
							apice=Text_Apice.getText();
							
				
							if(apice.isEmpty())
								{
									apice="1";
									Text_Apice.setText("   Apex    ");
								}
							String numeri="1-2-3-4-5-6-7-8-9";
							if(apice.contains("0") || !numeri.contains(apice))
								error_popup(34);
							else
								{
									int ap;
									ap=Integer.parseInt(apice);
									if(ap>9)
										{
										
										}
										else if(apice.compareTo(Text_Pedice.getText())<0)
												{
											
												}
											else 
												{
													char[] apiceU = apice.toCharArray();
													switch(apiceU[0]) {
													case '1':	apiceU[0]='\u00B9';
														break;
													case '2':	apiceU[0]='\u00B2';
														break;
													case '3':	apiceU[0]='\u00B3';
														break;
													case '4':	apiceU[0]='\u2074';
														break;
													case '5':	apiceU[0]='\u2075';
														break;
													case '6':	apiceU[0]='\u2076';
														break;
													case '7':	apiceU[0]='\u2077';
														break;
													case '8':	apiceU[0]='\u2078';
														break;
													case '9':	apiceU[0]='\u2079';
														break;
													}									
													Text_Re.setText(Text_Re.getText() + apiceU[0] );
													Text_Apice.setVisible(false);
													on_apice=false;
												}
								}
							
							
						}
				
					}
			}
		});
		panel_7.add(btnApice);
		
		Text_Apice = new JTextField();
		Text_Apice.setForeground(new Color(255, 255, 255));
		Text_Apice.setBackground(new Color(70, 109, 128));
		Text_Apice.setFont(new Font("Tahoma", Font.PLAIN, 14));
		Text_Apice.setVisible(false);
		panel_7.add(Text_Apice);
		Text_Apice.setColumns(1);
		
		JPanel panel_7_1 = new JPanel();
		panel_7_1.setBackground(new Color(70, 109, 128));
		splitPane_2.setRightComponent(panel_7_1);
		panel_7_1.setBorder(null);
		
		JButton btnPedice = new JButton("Subscript");
		btnPedice.setForeground(new Color(140, 218, 255));
		btnPedice.setBackground(new Color(70, 109, 128));
		btnPedice.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnPedice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(on_pedice && Text_Re.isEditable())
				{
					if(btnPedice.getText()=="Subscript")
						{	
							btnPedice.setText("Insert");
							Text_Pedice.setText(null);
							Text_Pedice.setVisible(true);
						}
					else
						{	
							btnPedice.setText("Subscript");
							pedice=Text_Pedice.getText();
							apice=Text_Apice.getText();
							String numeri="1-2-3-4-5-6-7-8-9-0";
							if (apice.isEmpty())							
							{
								
								error_popup(35);
							}
							else if(pedice.compareTo(apice)>0 || !numeri.contains(pedice))
								{
								
									error_popup(36);
								}
								 else
								 {
									 if(pedice.isEmpty())
										 pedice="0";
									 char[] pediceU = pedice.toCharArray();
									 
										switch(pediceU[0]) {
										case '1':	pediceU[0]='\u2081';
											break;
										case '2':	pediceU[0]='\u2082';
											break;
										case '3':	pediceU[0]='\u2083';
											break;
										case '4':	pediceU[0]='\u2084';
											break;
										case '5':	pediceU[0]='\u2085';
											break;
										case '6':	pediceU[0]='\u2086';
											break;
										case '7':	pediceU[0]='\u2087';
											break;
										case '8':	pediceU[0]='\u2088';
											break;
										case '9':	pediceU[0]='\u2089';
											break;
										default: pediceU[0]='\u2080';
										}									
										Text_Re.setText(Text_Re.getText() + pediceU[0] );
										  Text_Pedice.setVisible(false);
										  on_pedice=false;
								 }  
							
						}
				}
				
			}
		});
		panel_7_1.add(btnPedice);
		
		Text_Pedice = new JTextField();
		Text_Pedice.setForeground(new Color(255, 255, 255));
		Text_Pedice.setBackground(new Color(70, 109, 128));
		Text_Pedice.setFont(new Font("Tahoma", Font.PLAIN, 14));
		Text_Pedice.setVisible(false);
		Text_Pedice.setColumns(1);
		panel_7_1.add(Text_Pedice);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBackground(new Color(70, 109, 128));
		panel_3.setBorder(null);
		splitPane_4.setRightComponent(panel_3);
		
		
		JButton btnResolve = new JButton("RESOLVE");
		btnResolve.setForeground(new Color(140, 218, 255));
		btnResolve.setBorder(null);
		btnResolve.setBackground(new Color(70, 109, 128));
		btnResolve.setFont(new Font("MV Boli", Font.BOLD, 17));
		btnResolve.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String re = Text_Re.getText();
				char[] sigma = Text_Sigma.getText().toCharArray();
				if(Text_Sigma.getText().contains("(") || Text_Sigma.getText().contains(")") || Text_Sigma.getText().contains("[") 
						|| Text_Sigma.getText().contains("]") || Text_Sigma.getText().contains("*") 
						|| Text_Sigma.getText().contains("U") || Text_Sigma.getText().contains(String.valueOf('\u207A')) 
						|| Text_Sigma.getText().contains(String.valueOf('\u2219')) || Text_Sigma.getText().contains(String.valueOf('\u03B5')) 
						|| Text_Sigma.getText().contains("$") || Text_Sigma.getText().contains("%") || Text_Sigma.getText().contains("|")
						|| Text_Sigma.getText().contains("&") || Text_Sigma.getText().contains("$")  )
				{
					//error
					error_popup(37);
				}else
					r= new RegexParser(re, sigma, UI);
				
			}
		});
		panel_3.add(btnResolve);
		
		JPanel panel_6 = new JPanel();
		panel_6.setBackground(new Color(70, 109, 128));
		panel_6.setBorder(null);
		BorderRightSplit.setRightComponent(panel_6);
		
		JPanel panel_5 = new JPanel();
		panel_5.setBackground(new Color(70, 109, 128));
		panel_5.setBorder(null);
		BorderLeftSplit.setLeftComponent(panel_5);
		
		
	}

	@Override
	public void error_popup(int error_code) {
		String error_label="";
		String sigma = Text_Sigma.getText();
		switch(error_code) {
		case 1: error_label="The Alphabet and the Regular Expression must not be empty ";
				break;
		case 2: error_label="Symbols not permitted in Regular Expression";
				break;
		case 3:error_label="the Regular expression must contain al least one alphabet symbol: "+sigma;
		break;
		case 4:error_label="After a round bracket (, there could only be symbols: (, [, "+sigma;
		break;
		case 5:error_label="The round bracket ( couldn't be the last symbol of the Regular Expression";
		break;
		case 6:error_label="Before a round bracket ), there could only be symbols: ), ], *, "+'\u207A'+", one apex, one subscript, "+sigma;
		break;
		case 7:error_label="The round bracket ) couldn't be the first symbol of the Regular Expression";
		break;
		case 8:error_label="After a square bracket [, there could only be symbols: (, [, "+sigma;
		break;
		case 9:error_label="The square bracket [ couldn't be the last symbol of the Regular Expression";
		break;
		case 10:error_label="Before a square bracket ], there could only be symbols: ), ], *, "+'\u207A'+" one apex, one subscript, "+sigma;
		break;
		case 11:error_label="After a square bracket ], there could only be symbols: (, ), [, ], one apex, U, "+'\u2219'+", "+sigma;
		break;
		case 12:error_label="The square bracket ] couldn't be the first symbol of the Regular Expression";
		break;
		case 13:error_label="Before a star *, there could only be symbols: )"+sigma;
		break;
		case 14:error_label="After a star *, there could only be symbols: (, ), [, ], one apex, U, "+'\u2219'+", "+sigma;
		break;
		case 15:error_label="The star * couldn't be the first symbol of the Regular Expression";
		break;
		case 16:error_label="Before a cross "+'\u207A'+", there could only be symbols: ), "+sigma;
		break;
		case 17:error_label="After a cross "+'\u207A'+", there could only be symbols: (, ), [, ], one apex, U, "+'\u2219'+", "+sigma;
		break;
		case 18:error_label="The cross "+'\u207A'+" couldn't be the first symbol of the Regular Expression";
		break;
		case 19:error_label="Before a point "+'\u2219'+", there could only be symbols: ), ], *, "+'\u207A'+", "+sigma;
		break;
		case 20:error_label="After a point "+'\u2219'+", there could only be symbols: (, [, "+sigma;
		break;
		case 21:error_label="The point "+'\u2219'+" couldn't be the first symbol of the Regular Expression";
		break;
		case 22:error_label="The point "+'\u2219'+" couldn't be the last symbol of the Regular Expression";
		break;
		case 23:error_label="Before an Union U, there could only be symbols: ), ], *, "+'\u207A'+", "+sigma;
		break;
		case 24:error_label="After an Union U, there could only be symbols: (, [, "+sigma;
		break;
		case 25:error_label="The Union U couldn't be the first symbol of the Regular Expression";
		break;
		case 26:error_label="The Union U couldn't be the last symbol of the Regular Expression";
		break;
		case 27:error_label="Before an Apex, there could only be symbols: ), ], "+sigma;
		break;
		case 28:error_label="After an Apex, there could only be symbols: (, ), [, ], one subscript, U, "+'\u2219'+", "+sigma;
		break;
		case 29:error_label="Before a Subscript, there could only be an apex";
		break;
		case 30:error_label="Before an Apex-Subscript pair, there must be a square bracket ]";
		break;
		case 31:error_label="After a Subscript, there could only be symbols: (, ), [, ], U, "+'\u2219'+", "+sigma;
		break;
		case 32:error_label="Apex and Subscript couldn't be the first symbols of the Regular Expression";
		break;
		case 33:error_label="the number of ( and [ must be the same of ), ], respectively";
		break;
		case 34:error_label="the Apex must be a positive one-digit numer"; 
		break;
		case 35:error_label="there cannot be a Subscript if there's not an Apex"; 
		break;
		case 36:error_label="the Subscript must be a positive one-digit numer smaller than the Apex";
		break;
		case 37: error_label="(, ), [, ], *, U, "+'\u207A'+", "+'\u2219'+", "+'\u03B5'+" are not allowed like alphabet symbols";
		break;
		}
		JOptionPane.showMessageDialog(null, error_label,"ERROR CODE "+ error_code,JOptionPane.ERROR_MESSAGE);
	}

}
