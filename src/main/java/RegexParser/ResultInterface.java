package RegexParser;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;

public class ResultInterface extends JDialog {

	private JFrame frame;

	private int x;

	private int y;

	private double vertex_width;

	private final JPanel contentPanel;


	public ResultInterface(mxGraph graph, RegexParser RegexParser) {
		setAlwaysOnTop(false);
		frame = new JFrame();
		double scale=1;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		x=(int) (screenSize.getWidth()/1.83);
		y=(int) (screenSize.getHeight()/1.25);
		vertex_width = (screenSize.getWidth()/1.9)/20;//vertices
		frame.setBounds((int)(screenSize.getWidth()*0.25), 10, 900, 600);	// window bounds
		frame.setResizable(true);
		frame.setTitle("RESULTS: " + RegexParser.expr);
		
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(this.getClass().getClassLoader().getResource("./icona7.png")));
		
		contentPanel = new JPanel();
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setBorder(null);
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new GridLayout(1, 0, 0, 0));
		
		if(screenSize.getWidth()<1500)
		{
			mxConstants.DEFAULT_FONTSIZE=14;
			vertex_width = (screenSize.getWidth()/1.9)/15;
			scale=0.55;
		}
		else
		mxConstants.DEFAULT_FONTSIZE=18;
		
		mxGeometry g=new mxGeometry(0, 0, vertex_width, vertex_width);
		for(int i=0;i<RegexParser.nodes.size();i++)
			RegexParser.graph.getModel().setGeometry(RegexParser.nodes.get(i), g);
	
		this.resetGraphStyle(RegexParser, true);
		
		graph.setCellsEditable(false);
		graph.setCellsBendable(false);
		graph.setAllowDanglingEdges(false);
		
		mxGraphComponent graphComponent = new mxGraphComponent(graph);
		graphComponent.getViewport().setOpaque(true);
		graphComponent.getViewport().setBackground(Color.white);
		graphComponent.setBorder(null);
		mxHierarchicalLayout layout = new mxHierarchicalLayout(graph,SwingConstants.NORTH);
		
		layout.setIntraCellSpacing(120*scale);
		if(screenSize.getWidth()<1500)
			layout.setInterRankCellSpacing(150*scale);
		else
			layout.setInterRankCellSpacing(70*scale);
		layout.setParallelEdgeSpacing(120*scale);
		
		layout.execute(graph.getDefaultParent());
		
		
		JSplitPane split_generico = new JSplitPane();
		split_generico.setEnabled(false);
		split_generico.setResizeWeight(0.01);
		split_generico.setDividerSize(0);
		split_generico.setBorder(null);
		split_generico.setBackground(SystemColor.WHITE);
		
		contentPanel.add(split_generico);
		split_generico.setOrientation(JSplitPane.VERTICAL_SPLIT);
		split_generico.setRightComponent(graphComponent);
		
		JPanel panel_Regex = new JPanel();
		panel_Regex.setBackground(SystemColor.WHITE);
		
		split_generico.setLeftComponent(panel_Regex);
		
		
		JLabel Regex_Label = new JLabel("New label");
		Regex_Label.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panel_Regex.add(Regex_Label);
		Regex_Label.setText("Regex: "+RegexParser.expr);
		
		frame.add(contentPanel);
		this.frame.setVisible(true);
	}

	
	public void resetGraphStyle(RegexParser RegexParser, boolean resetAll)
	{
		String style; 
		for(int i=0;i<RegexParser.nodes.size() && resetAll;i++)
			{
				if(RegexParser.final_nodes.contains(RegexParser.nodes.get(i)))
					style="shape=doubleEllipse;strokeWidth=3;strokeColor=black;fillColor=white;fontColor=black";
				else
					style="shape=ellipse;strokeWidth=3;strokeColor=black;fillColor=white;fontColor=black";
				
				if(RegexParser.graph.getLabel(RegexParser.nodes.get(i)).equals("q0"))
				{
					if(RegexParser.final_nodes.contains(RegexParser.nodes.get(i)))			
						style="shape=doubleEllipse;strokeWidth=3;strokeColor=orange;fillColor=white;fontColor=black";
					else
						style="shape=ellipse;strokeWidth=3;strokeColor=orange;fillColor=white;fontColor=black";
				}
				
				RegexParser.graph.getModel().setStyle(RegexParser.nodes.get(i), style);	
			}			
		style="sourcePerimeterSpacing=3.5;targetPerimeterSpacing=3.5;strokeWidth=2;rounded=1;labelBackgroundColor=white;strokeColor=black;fontColor=red";
		for(int i=0;i<RegexParser.edges.size();i++)
			RegexParser.graph.getModel().setStyle(RegexParser.edges.get(i), style);
	}
	
	public void setGraphEdgeLabelPosition(ArrayList<Object> loop, int offset, mxGraph graph)
	{
		
		for(int i=0;i<loop.size();i++)
		{
			mxGeometry g=graph.getModel().getGeometry(loop.get(i));
			g.setY(g.getY()+offset);
			graph.getModel().setGeometry(loop.get(i), g);
		}
		
	}

}
