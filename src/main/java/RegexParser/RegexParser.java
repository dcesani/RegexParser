/**
 * 
 *
 */
package RegexParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;

/**
 * @author david
 *
 */

public class RegexParser implements RegexParser_interface {
	
	/**
	 * ArrayList that contains the NODES of the graph
	 */
	ArrayList<Object> nodes;
	/**
	 * ArrayList that contains the FINAL NODES of the graph
	 */
	ArrayList<Object> final_nodes;
	/**
	 * ArrayList that contains the EDGES of the graph
	 */
	ArrayList<Object> edges;
	/**
	 * ArrayList that contains the LOOP of the graph
	 */
	ArrayList<Object> loop;
	/**
	 * ArrayList used for finding the true final nodes when the regex presents a Union symbol
	 */
	ArrayList<Integer> final_U;
	/**
	 * HashTable related to the input regex. It is translated in the graph
	 */
	Hashtable<Integer, String> table;
	/**
	 * String used for show the regex on the Result Interface
	 */
	String expr;
	/**
	 * graph components container
	 */
	Object parent;
	/**
	 * Result interface
	 */
	ResultInterface RU;
	/**
	 * regex graph
	 */
	mxGraph graph;
	/**
	 * graph state counter
	 */
	Integer num_stato=0;
	/**
	 * ArrayList used in returnBlock method that contains the content of each brackets block
	 */
	ArrayList<String> openList;
	/**
	 * boolean variable for understanding if the block is a starred block
	 */
	boolean star=true;
	/**
	 * important variable for the square brackets block. In particular, it allows to maintain the right start node for the substitution recursive process
	 */
	int primo_fac=-1;
	
	/**
	main method that run all the process to draw the graph
	
	     	@param re: input regex
	     	@param sigma: alphabet
	     	@param UI: UserInterface object that calls this method	
	*/	
	public RegexParser(String re, char[] sigma, UserInterface UI) {
		int Scanner_error=this.Scanner(re, sigma);
		int Parser_error;
		expr=re;
		if(Scanner_error==0)
			{
				Parser_error=this.Parser(re,sigma);
				if(Parser_error==0)				
						{
							this.Resolve(re,sigma);
						}
				 else
					 UI.error_popup(Parser_error);
			}
		else
			UI.error_popup(Scanner_error);
	}

	/**
	Checks the regex lexicon to avoid not allowed regex.
	
			@param re: Regex
			@param sigma: alphabet
	@return returns an integer. If the value is 0, no errors; else, there is an error. The different error codes refer to different error popups.
	*/
	@Override
	public int Scanner(String re, char[] sigma) {
		
		String Sigma = new String(sigma);
		if(re.length()==0 || Sigma.length()==0 || Sigma.contains("U"))
			return 1;
		//add particular symbols to sigma
		Sigma = Sigma + "("+ ")" + '\u002A' + '\u2219' + '\u207A'  + 'U'+	"[" + "]";
		//add apexes to sigma
		Sigma = Sigma + "\u00B9"+ "\u00B2" + '\u00B3' + '\u2074' + '\u2075'  + '\u2076'+ '\u2077' + '\u2078' + '\u2079';
		//add subscripts to sigma
		Sigma = Sigma + "\u2080"+ "\u2081" + '\u2082' + '\u2083' + '\u2084'  + '\u2085'+ '\u2086' + '\u2087' + '\u2088' + '\u2089';
		for(int i=0;i<re.length();i++) //check the regex
			if(!Sigma.contains(re.substring(i, i+1)))		
				return 2;
		
		Sigma = new String(sigma);
		int used_symbol=0;
		for(int i=0;i<Sigma.length();i++)//check the regex
			if(re.contains(Sigma.substring(i, i+1)))		
				used_symbol ++;
		if(used_symbol>0)
			return 0;
		else
			return 3;
	}
	/**
	Checks the regex sintax to avoid not allowed regex.
	
			@param re: Regex
			@param sigma: alphabet
	@return returns an integer. If the value is 0, no errors; else, there is an error. The 	different error codes refer to different error popups.
	*/
	@Override
	public int Parser(String re, char[] sigma) {
		
		String Sigma = new String(sigma);
		int roundOpenBrackets=0;
		int roundClosedBrackets=0;
		int squareOpenBrackets=0;
		int squareClosedBrackets=0;
		int j=0;
		int k=0;
		char[] s;
		
		for(int i=0;i<re.length();i++)
		{
			s=re.substring(i, i+1).toCharArray();
			switch(s[0])
			{
				case '(':
						roundOpenBrackets++;
						j=i+1;
						if(i<re.length()-1)
							{
								s=re.substring(j, j+1).toCharArray();
								if(!(s[0]=='(' || Sigma.contains(re.substring(j, j+1)) || s[0]=='[' ))
									return 4;
							}
						else
							return 5;
					break;
				
				case ')':
						if(roundOpenBrackets>roundClosedBrackets)
							roundClosedBrackets++;
						j=i-1;
						if(i>0)
							{
								s=re.substring(j, j+1).toCharArray();				
								if(!(s[0]==')' || s[0]==']' || apex(re.charAt(j))!='0' || subscript(re.charAt(j))!='N' || Sigma.contains(re.substring(j, j+1)) || s[0]=='\u002A' ||s[0]=='\u207A' ))
									return 6;
							}
						else
							return 7;
					break;
					
				case '[':
					squareOpenBrackets++;
					j=i+1;
					if(i<re.length()-1)
						{
							s=re.substring(j, j+1).toCharArray();
							if(!(s[0]=='(' || Sigma.contains(re.substring(j, j+1)) || s[0]=='[' ))
								return 8;
						}
					else
						return 9;
					break;
			
			case ']':
					if(squareOpenBrackets>squareClosedBrackets)
						squareClosedBrackets++;
					j=i-1;
					if(i>0)
						{
							s=re.substring(j, j+1).toCharArray();
							if(!(s[0]==')' || s[0]==']' || apex(re.charAt(j))!='0' || subscript(re.charAt(j))!='N' || Sigma.contains(re.substring(j, j+1)) || s[0]=='\u002A' ||s[0]=='\u207A' ))
								return 10;
							if(i<re.length()-1)
							{
								s=re.substring(i+1, i+2).toCharArray();
								if(!(s[0]==')' || s[0]=='(' || s[0]=='[' || s[0]==']' || apex(re.charAt(i+1))!='0' || Sigma.contains(re.substring(i+1, i+2)) || s[0]=='\u2219' || s[0]=='U' ))
									return 11;
							}
						}
					else
						return 12;
				break;
				
				case '\u002A':
							j=i+1;
							k=i-1;
							if(i>0)
								{
									s=re.substring(k, k+1).toCharArray();
									if(!(s[0]==')' || Sigma.contains(re.substring(k, k+1)) ))
										return 13;
									if(i<re.length()-1)
									{
										s=re.substring(j, j+1).toCharArray();
										if(!(s[0]==')' || s[0]=='(' || s[0]==']' || s[0]=='[' || Sigma.contains(re.substring(j, j+1)) || s[0]=='\u2219' || s[0]=='U' ))
											return 14;
									}
								}
							 else
								return 15;
					break;
				
				case '\u207A':
					j=i+1;
					k=i-1;					
					if(i>0)
						{
							s=re.substring(k, k+1).toCharArray();
							if(!(s[0]==')' || Sigma.contains(re.substring(k, k+1)) ))
								return 16;						
						
							if(i<re.length()-1)
							{
								s=re.substring(j, j+1).toCharArray();
								if(!(s[0]==')' || s[0]=='(' || s[0]==']' || s[0]=='[' || Sigma.contains(re.substring(j, j+1)) || s[0]=='\u2219' || s[0]=='U' ))
									return 17;
							}
						}
					 else
						return 18;
					break;
				
				case '\u2219':
					j=i+1;
					k=i-1;					
					if(i>0)
						{
							s=re.substring(k, k+1).toCharArray();
							if(!(s[0]==')' || s[0]==']' || apex(re.charAt(k))!='0' || subscript(re.charAt(k))!='N' || Sigma.contains(re.substring(k, k+1)) || s[0]=='\u207A' || s[0]=='\u002A' ))
								return 19;
							
							if(i<re.length()-1)
							{
								s=re.substring(j, j+1).toCharArray();
								if(!(s[0]=='(' || s[0]=='[' || Sigma.contains(re.substring(j, j+1)) ))
									return 20;
							}
							else
								return 21;
						}
					 else
						return 22;
					break;
				
				case 'U':
					j=i+1;
					k=i-1;
					if(i>0)
						{
							s=re.substring(k, k+1).toCharArray();
							if(!(s[0]==')' || s[0]==']' || apex(re.charAt(k))!='0' || subscript(re.charAt(k))!='N' || Sigma.contains(re.substring(k, k+1)) || s[0]=='\u207A' || s[0]=='\u002A' ))
								return 23;
							
							if(i<re.length()-1)
							{
								s=re.substring(j, j+1).toCharArray();
								if(!(s[0]=='(' || s[0]=='[' || Sigma.contains(re.substring(j, j+1)) ))
									return 24;
							}
							else
								return 25;
						}
					 else
						return 26;
					break;
					default:
						
						j=i+1;
						k=i-1;
						if(apex(re.charAt(i))=='0' && subscript(re.charAt(i))=='N')
							break;
						if(i>0)
						{
							if(apex(re.charAt(i))!='0')
							{
								s=re.substring(k, k+1).toCharArray();
								if(!(s[0]==')' || s[0]==']' || Sigma.contains(re.substring(k, k+1)) ))
									return 27;
				
								if(i<re.length()-1)
								{
									s=re.substring(j, j+1).toCharArray();
									if(!(s[0]==')' || s[0]==']' || s[0]=='(' || s[0]=='[' || subscript(re.charAt(j))!='N' || s[0]=='\u22C3' || s[0]=='\u2219' || Sigma.contains(re.substring(j, j+1)) ))
										return 28;
								}
								
							}
							else
							{
								s=re.substring(k, k+1).toCharArray();
								if(apex(re.charAt(k))=='0')
									return 29;
								else if (i>1)
								   {
										s=re.substring(k-1, k).toCharArray();
										if(!(s[0]==']'))
											return 30;
								   }
								 else
									return 30;
						
								if(i<re.length()-1)
								{
									s=re.substring(j, j+1).toCharArray();
									if(!(s[0]==')' || s[0]==']' || s[0]=='(' || s[0]=='[' || s[0]=='U' || s[0]=='\u2219' || Sigma.contains(re.substring(j, j+1)) ))
										return 31;
								}
							}
						}
						else
							return 32;
						break;
			}
			
		}
		if(roundOpenBrackets!=roundClosedBrackets || squareOpenBrackets!=squareClosedBrackets)
			return 33;
		
		return 0;
	}
	/**
	Runs the regex division process and the recursive substitution process
	
			@param regex: Regex
			@param sigma: alphabet
	*/	
	@Override
	public void Resolve(String regex, char[] sigma) {
		
		int j=0;
		int k;
		String str, value;
		String []str2;	
		String Sigma = new String(sigma);
		Sigma=Sigma+'\u03B5';
		List<String> split_U = new ArrayList<String>();
		List<String> split = new ArrayList<String>();
		table = new Hashtable<Integer, String>();	
		final_U = new ArrayList<Integer>();
		openList = new ArrayList<String>();
				
		regex=setupRegex(regex);
		split_U=splitter(regex);								
	
		while(j<split_U.size()) //create Union graph structure
		{		
			int restart=num_stato;
			if(split_U.size()!=1) // U is present
			{
				if(table.isEmpty())
					k=0;
				else
					{	
						k=table.keySet().size();
						str=table.get(k-1).toString();
						str2=str.split("_");
						str2[0]=str2[0].substring(1);
						str2[2]=str2[2].substring(1);
						if(Integer.valueOf(str2[0])<=Integer.valueOf(str2[2])) //find start node
							num_stato=Integer.valueOf(str2[2]);
						else
							num_stato=Integer.valueOf(str2[0]);
					}	
				if(num_stato<restart)
					num_stato=restart-1;
				value="q"+String.valueOf(0)+"_"+'\u03B5'+"_q"+String.valueOf(num_stato+1);
				table.putIfAbsent(k, value);
				num_stato++;
			}
			
			if(split_U.get(j).charAt(split_U.get(j).length()-1)=='\u002A' && split_U.get(j).charAt(split_U.get(j).length()-2)==')')
			{
				star=true;
				value="q"+String.valueOf(num_stato)+"_*_q"+String.valueOf(num_stato);
				table.putIfAbsent(table.keySet().size(), value);
			}
			else
			{
				star=false;
			}
			// divide general structure
			regex=split_U.get(j);
			regex=returnBlock(regex, openList);	//create block structure
			// |t0}U|t1}
		
			split=Arrays.asList(regex.split("\\|"));
			
			//create block graph structure 
			for(int i=0;i<split.size();i++)
				if(split.get(i).contains("}")) //block
					num_stato=this.create_hashTable(split.get(i),Sigma,table,table.size(),num_stato,num_stato,null,true,false);
				else// no block
					num_stato=this.create_hashTable(split.get(i),Sigma,table,table.size(),num_stato,num_stato,null,false,false);
			j++;
			
			// start recursive block substitution process
			for(int z=0;z<table.size();z++) //find the block into the table
			{
				if(table.get(z).contains("%") || table.get(z).contains("$"))
				{
					int nodo_in, nodo_end;
					String[] split_q;
					split_q=table.get(z).split("_");
					nodo_in=Integer.valueOf(split_q[0].substring(1));
					nodo_end=Integer.valueOf(split_q[2].substring(1));
					table.replace(z, split_q[0]+"_£_"+split_q[2]);	
					//replace block with its implementation
					num_stato=this.create_hashTable(split_q[1],Sigma,table,table.size(),num_stato,nodo_in,nodo_end,true,false);					
				}
			}
			openList.clear();
		} 
		
		//create graph
		this.create_graph(table);			
		com.mxgraph.swing.util.mxGraphTransferable.enableImageSupport = false;
		RU = new ResultInterface(graph, this);
					
	}
		
	/**
	core method of the application. It creates the block structure and the block implementation which after replace the block into the graph.
	
			@param regex: string to process
			@param sigma: alphabet
			@param table: HashTable which contains the graph structure
			@param key: key of the HashTable
			@param num_stato: nodes counter
			@param start: start node for the substitution process
			@param end: end node for the substitution process
			@param block: true if regex is a block
			@param sub: true if the substitution process is started
	@return returns last node number used into the recursive process. 
	*/
	private int create_hashTable(String regex, String sigma, Hashtable<Integer, String> table, Integer key, Integer num_stato, Integer start, Integer end, boolean block, boolean sub)
	{
		
		char[] c,c_prec;
		String [] split_square;
		String value=" ";
		List<String> split = new ArrayList<String>();
	
		c_prec=value.toCharArray();
		int stato_part=0;
		if(!block) 
			if(!regex.contains("U")) 
//---------------------------------------------------------------NO U SYMBOL---------------------------------------------------------------------
				for(int i=0;i<regex.length();i++)
				{
					c=regex.substring(i, i+1).toCharArray();
					if(regex.contains(String.valueOf('\u002A')) || regex.contains(String.valueOf('\u207A')))
						{
							// process char before pair symbol* (ex: bba*--> process bb)
								if(i<regex.length()-2 && !regex.substring(i+1, i+2).contains(String.valueOf('\u002A')) && !regex.substring(i+1, i+2).contains(String.valueOf('\u207A')))
								{
									if(sub)
										{
											value="q"+String.valueOf(start)+"_"+c[0]+"_q"+String.valueOf(num_stato+1);
											sub=false;
										}
									else
										value="q"+String.valueOf(num_stato)+"_"+c[0]+"_q"+String.valueOf(num_stato+1);
									c_prec[0]=c[0];
									num_stato++;
									if(table.isEmpty())
										table.putIfAbsent(key, value);
									else if(!table.get(key-1).equals(value))
												table.putIfAbsent(key, value);
										else
											key--;		
									key++;
								}
								else 
								{	
									if(i<regex.length()-1 && regex.substring(i+1, i+2).equals(String.valueOf('\u002A')))
									{
										if (sub)
											{
												num_stato=star(start, num_stato, key, table, c);
												sub=false;
											}
										else 
											num_stato=star(num_stato, num_stato, key, table, c);
										key=table.keySet().size();
										i++;	
										
									}else if(i<regex.length()-1 && regex.substring(i+1, i+2).equals(String.valueOf('\u207A')))
									{										
										if (sub)
										{
											num_stato=cross(start, num_stato, key, table, c);
											sub=false;
										}
										else
										num_stato=cross(num_stato, num_stato, key, table, c);
										key=table.keySet().size();
										i++;
										
									}
									else
									{
										if(sub)
										{
											value="q"+String.valueOf(start)+"_"+c[0]+"_q"+String.valueOf(num_stato+1);
											sub=false;
										}
									else
										value="q"+String.valueOf(num_stato)+"_"+c[0]+"_q"+String.valueOf(num_stato+1);
									c_prec[0]=c[0];
									num_stato++;
									if(table.isEmpty())
										table.putIfAbsent(key, value);
									else if(!table.get(key-1).equals(value))
												table.putIfAbsent(key, value);
										else
											key--;		
									key++;
									}
									
							}
							
						}
						else
						{
							if(sub)
								{
									value="q"+String.valueOf(start)+"_"+c[0]+"_q"+String.valueOf(num_stato+1);
									sub=false;
								}
							else
								value="q"+String.valueOf(num_stato)+"_"+c[0]+"_q"+String.valueOf(num_stato+1);
							c_prec[0]=c[0];
							num_stato++;
							if(table.isEmpty())
								table.putIfAbsent(key, value);
							else if(!table.get(key-1).equals(value))
										table.putIfAbsent(key, value);
								else
									key--;		
							key++;
							
						}
						
					}
		//------------------------------------------------------------------------------------------------------------------------------------
		//--------------------------------------------------------------- YES U SYMBOL---------------------------------------------------------------------
			else
			{
				split=Arrays.asList(regex.split("U"));
				int j=0;
				int k;
				String s;
				String []s2;
				while(j<split.size())
				{		
					if(table.isEmpty())
						k=0;
					else
						{
							k=table.keySet().size();
							s=table.get(k-1).toString();
							s2=s.split("_");
							s2[0]=s2[0].substring(1);
							s2[2]=s2[2].substring(1);
								if(Integer.valueOf(s2[0])<=Integer.valueOf(s2[2]))
									num_stato=Integer.valueOf(s2[2]);
								else
									num_stato=Integer.valueOf(s2[0]);
						}
					value="q"+String.valueOf(start)+"_"+'\u03B5'+"_q"+String.valueOf(num_stato+1);
					table.putIfAbsent(k, value);
					num_stato++;
					num_stato=create_hashTable(split.get(j), sigma, table, k+1, num_stato,num_stato,null,false,false);
					
					final_U.add(num_stato);
					
					j++;
				}
				
				
			}
		//------------------------------------------------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------------------------------------------------
		
		//---------------------------------------------------------------BLOCK---------------------------------------------------------------------
		else //if block
		{
			// |t0}U|t1}
			if(end==null) //---------------------------------------------------------------ROUND AND SQUARE BRACKETS BLOCK---------------------------------------------------------------------
			{
				if(!regex.contains("U"))
				{
						String rep_star='\u002A'+"}";
						String rep_cross='\u207A'+"}";
						
							for(int k=0;k<regex.length()-1;k++)
								{
									
									if(regex.charAt(k)=='}')
										if(regex.charAt(k+1)=='\u002A')										 
												regex=regex.substring(0, k)+'\u002A'+"}"+regex.substring(k+2, regex.length());											
										 else if (regex.charAt(k+1)=='\u207A')
											 	regex=regex.substring(0, k)+'\u207A'+"}"+regex.substring(k+2, regex.length());
								}
							split=Arrays.asList(regex.split("}"));
							for(int i=0;i<split.size();i++)
							{
								if(split.get(i).contains("%")) //round brackets
								{								
									
									if(split.get(i).contains((String.valueOf('\u002A'))))
									{
										if(sub)
											num_stato=star(start, num_stato, key, table, split.get(i).substring(0, split.get(i).length()-1));
										else
											num_stato=star(num_stato, num_stato, key, table, split.get(i).substring(0, split.get(i).length()-1));
										key=table.keySet().size();
												
									}else if(split.get(i).contains((String.valueOf('\u207A')))) 
									{
										if(sub)
											num_stato=cross(start,num_stato, key, table, split.get(i).substring(0, split.get(i).length()-1));
										else
											num_stato=cross(num_stato,num_stato, key, table, split.get(i).substring(0, split.get(i).length()-1));
										key=table.keySet().size();
									}
									else
									{
										//no star, no cross
										
										if(!split.get(i).contains("-"))
											split.set(i, split.get(i).concat("-1"));
										
										split_square=split.get(i).split("-");
										for(int j=0;j<Integer.valueOf(split_square[1]);j++)
										{
											if(sub)
											{
												value="q"+String.valueOf(start)+"_"+split_square[0]+"_q"+String.valueOf(num_stato+1);
												sub=false;
											}
											else
												value="q"+String.valueOf(num_stato)+"_"+split_square[0]+"_q"+String.valueOf(num_stato+1);
											table.putIfAbsent(key, value);
											num_stato++;
											key++;
										}
									}
								}
								else if(split.get(i).contains("$")) //square brackets
								{
									//set Apex = 1 if it isn't presen
									int stato_partenza=num_stato;
									int stato_finale;
									boolean sub_par=sub ,sub_nonob=false;
									split_square=split.get(i).split("-");
									stato_finale=num_stato+Integer.valueOf(split_square[1]);
									stato_part=start;
									for(int j=0;j<Integer.valueOf(split_square[1]);j++)
									{
										if(sub)
											{
												value="q"+String.valueOf(start)+"_"+split_square[0]+"_q"+String.valueOf(num_stato+1);
												sub=false; 
												sub_nonob=true;
											}
										else
											value="q"+String.valueOf(num_stato)+"_"+split_square[0]+"_q"+String.valueOf(num_stato+1);
										
										table.putIfAbsent(key, value);
										key++;
										
										if(j>Integer.valueOf(split_square[2])-1 && j<=Integer.valueOf(split_square[1])-1) //not mandatory case
											{							
												if(primo_fac==-1)
													primo_fac=stato_finale;
												if(sub_nonob && Integer.valueOf(split_square[2])==0)
												{
													value="q"+String.valueOf(start)+"_"+'\u03B5'+"_q"+String.valueOf(stato_finale);
													sub_nonob=false;
												}
												else
													value="q"+String.valueOf(num_stato)+"_"+'\u03B5'+"_q"+String.valueOf(stato_finale);
												table.putIfAbsent(key, value);
												key++;	
											}
										num_stato++;	
									}
									
									start=stato_part;
									sub=sub_par;
									
								}
								else //normal string
									num_stato=create_hashTable(split.get(i), sigma, table, table.size(), num_stato, num_stato,null,false,false);
							}//end for
				}
				else
				{
					split=Arrays.asList(regex.split("U"));
					int j=0;
					int k;
					String s;
					String []s2;
						
					while(j<split.size())
					{		
						if(table.isEmpty())
							k=0;
						else
							{
								k=table.keySet().size();
								s=table.get(k-1).toString();
								s2=s.split("_");
								s2[0]=s2[0].substring(1);
								s2[2]=s2[2].substring(1);
									if(Integer.valueOf(s2[0])<=Integer.valueOf(s2[2]))
										num_stato=Integer.valueOf(s2[2]);
									else
										num_stato=Integer.valueOf(s2[0]);
							}
						value="q"+String.valueOf(start)+"_"+'\u03B5'+"_q"+String.valueOf(num_stato+1);
						table.putIfAbsent(k, value);
						num_stato++;
						num_stato=create_hashTable(split.get(j), sigma, table, k+1, num_stato,num_stato,null,true,false);										
						j++;
				}
			 }
			} //------------------------------------------------------------------------------------------------------------------------------------
			//---------------------------------------------------------------BLOCK SUBSTITUTION---------------------------------------------------------------------
			else //if end
			{
				//I have to connect to the end state
				int index = Integer.valueOf(regex.substring(1));
				List<String> split_block = new ArrayList<String>();
				
				if(openList.get(index).contains("|"))
				{
					List<String> split_Unione = new ArrayList<String>();
					int z=0;
					int j=0;
					int k;
					String s;
					String []s2;
					split_Unione=Arrays.asList(openList.get(index).split("U"));	
						while(z<split_Unione.size() && !split_Unione.get(z).isEmpty())
							{
								int restart=num_stato;
								int start_app=start;
								if(split_Unione.size()!=1)
								{
									if(table.isEmpty())
											k=0;
										else
											{	
												k=table.keySet().size();
												s=table.get(k-1).toString();
												s2=s.split("_");
												s2[0]=s2[0].substring(1);
												s2[2]=s2[2].substring(1);											
												if(Integer.valueOf(s2[0])<=Integer.valueOf(s2[2]))
													num_stato=Integer.valueOf(s2[2]);
												else
													num_stato=Integer.valueOf(s2[0]);
											}	
										if(num_stato<restart)
											num_stato=restart-1;
										value="q"+String.valueOf(start)+"_"+'\u03B5'+"_q"+String.valueOf(num_stato+1);
										table.putIfAbsent(k, value);
										num_stato++;
										start_app=num_stato;
										
								}
								split_block=Arrays.asList(split_Unione.get(z).split("\\|"));																					
								for(int i=0;i<split_block.size();i++)
									{									
										if(split_block.get(i).contains("$"))
										{
											primo_fac=-1;
										}
										if(split_block.get(i).contains("}")) //if block
											num_stato=this.create_hashTable(split_block.get(i),sigma,table,table.size(),num_stato,start_app,null,true,true);
										else	
											num_stato=this.create_hashTable(split_block.get(i),sigma,table,table.size(),num_stato,start_app,null,false,true);									
										if(split_block.size()>1 && !split_block.get(0).isEmpty())
										{
											start_app=num_stato;
										}
										if(split_block.get(i).contains("$") || split_block.get(i).contains("%"))
										{
											if(split_block.get(i).indexOf("}")==split_block.get(i).length()-1)
												{
													start_app=primo_fac;
													primo_fac=-1;
												}
											else
												start_app=num_stato;
										}
																			
									}
								if(split_Unione.size()!=1)
									final_U.add(num_stato);
							z++;
							}
				}
				else
						num_stato=create_hashTable(openList.get(index), sigma, table, table.size(), num_stato, start,null,false,true);
					
				if(final_U.isEmpty()) 
					{
						//no internal U
						value="q"+String.valueOf(num_stato)+"_&_q"+String.valueOf(end);
						table.putIfAbsent(table.keySet().size(), value);
					}
				else
				{
					for(int p=0;p<final_U.size();p++)
					{
						if(star)
						{	
							value="q"+String.valueOf(end)+"_&_q"+String.valueOf(num_stato+1);
							table.putIfAbsent(table.keySet().size(), value);
							
						}
						value="q"+String.valueOf(final_U.get(p))+"_&_q"+String.valueOf(end);
						table.putIfAbsent(table.keySet().size(), value);
					}
					if(star)
						num_stato+=2;
				}
				final_U.clear();
				
				
			}//------------------------------------------------------------------------------------------------------------------------------------
		}//------------------------------------------------------------------------------------------------------------------------------------
		return num_stato;	
	}
	
	/**
	Translates the graph table in the graph object.
	
			@param table: table to translate 
	*/
	private void create_graph(Hashtable<Integer, String> table)
	{
	
		nodes = new ArrayList<Object>();
		final_nodes = new ArrayList<Object>();
		edges = new ArrayList<Object>();
		loop = new ArrayList<Object>();
		ArrayList<Integer> index = new ArrayList<Integer>();
		String[] node_label;
		boolean contains=false;
		int startNodePosition=0,endNodePosition=0;
		graph = new mxGraph();
		parent = graph.getDefaultParent();
		
		graph.getModel().beginUpdate();
		try 
		{	
			for(int i=0; i<table.size();i++)
			{
				node_label=table.get(i).split("_");
				if(!node_label[1].contains("*"))
				{
					mxCell node_1= new mxCell(node_label[0]); //first node of the pair
					for(int j=0;j<nodes.size();j++)
					{
						if(graph.getLabel(nodes.get(j)).equals(node_label[0]))
								{
									contains=true;
									startNodePosition=j;
								}
					}
					if(!contains)
					{
						node_1.setVertex(true);
						graph.addCell(node_1, parent);
						nodes.add(node_1);
						startNodePosition=nodes.size()-1;
					
					}	
						contains=false;
						mxCell node_2= new mxCell(node_label[2]); //second node
						for(int j=0;j<nodes.size();j++)
						{
							if(graph.getLabel(nodes.get(j)).equals(node_label[2]))
									{
										contains=true;
										endNodePosition=j;
									}
						}
						if(!contains)
						{
							node_2.setVertex(true);
							graph.addCell(node_2, parent);
							nodes.add(node_2);
							endNodePosition=nodes.size()-1;
							
						}
						//create edges
						if(!node_label[1].equals("£"))
						{
						int quanti=0;
						for(int p=0;p<=i;p++)
						{
							if(table.get(p).equals(table.get(i)))
								{
									quanti++;
								}
						}
						if (quanti==1)
							{
								Object edge = graph.insertEdge(parent, null,node_label[1], nodes.get(startNodePosition), nodes.get(endNodePosition));
								
								edges.add(edge);
								if(graph.getLabel(nodes.get(startNodePosition)).equals(graph.getLabel(nodes.get(endNodePosition))))
								{
									loop.add(edges);
								}
								if(node_label[1].equals("&"))
									index.add(edges.size()-1);	
								
							}
						
							
						}
				}
				
			}
			
			//final nodes 
			Boolean stop;
			int quanti=0;
			
			for(int k=0;k<nodes.size();k++)
			{
				
				stop=false;
				for(int i=0; i<table.size() && !stop;i++)
				{
					node_label=table.get(i).split("_");				
					if(graph.getLabel(nodes.get(k)).equals(node_label[0]))
						{
							if (!node_label[1].contains("*"))
							{
								quanti++;
								if(graph.getLabel(nodes.get(k)).equals(node_label[2]))
									quanti--;
								else if (node_label[1].equals(String.valueOf('\u03B5')))
								{
									node_label[0]=node_label[0].replace('q', '0');
									node_label[2]=node_label[2].replace('q', '0');									
									if(Integer.valueOf(node_label[0])>Integer.valueOf(node_label[2]))
										quanti--;
								}	
									else						
										stop=true;	
							}
							else
							{								
								if(!final_nodes.contains(nodes.get(k)))
									final_nodes.add(nodes.get(k));
							}
						}
				}
				if(quanti==0)
						final_nodes.add(nodes.get(k));
				quanti=0;
				//trick symbol substitution
				for(int z=0;z<index.size();z++)
				{
					graph.cellLabelChanged(edges.get(index.get(z)), '\u03B5', true);
				}
			}
		}finally
		{
			graph.getModel().endUpdate();
		}
	}
	
	/**
	creates internal block structure: round and square block structure and manages internal U.
	
			@param s: string to process
			@param openList: ArrayList contains the internal block implementation
	@return returns a string contains the block symbolic structure. 
	*/
	public static String returnBlock(String s,List<String> openList)
	{
		
		int j,i=0,count=0;
		char Apex='0',Subscript='0',app;
		String k;
		while(i<s.length())
		{
			
			Apex='0';
			Subscript='0';
			if(s.charAt(i)==')')
			{
				for(j=i;s.charAt(j)!='(';j--);
				openList.add(s.substring(j+1,i));
				if(i+1<s.length() && apex(s.charAt(i+1))!='0')
				{
					app=s.charAt(i+1);
					Apex=apex(app);
					if(i+2<s.length())
						s=s.substring(0, j)+"|%"+count+"-"+Apex+"}"+s.substring(i+2, s.length());
					else
						s=s.substring(0, j)+"|%"+count+"-"+Apex+"}";
				}
				else
					s=s.substring(0, j)+"|%"+count+"}"+s.substring(i+1, s.length());
				count++;
		
				for(j=s.length()-1;s.charAt(j)!='}' && j>0;j--);
				if(j>0)
					i=j;
			}
			else
			if(s.charAt(i)==']')
			{
				if(i+1<s.length())
				{
					app=s.charAt(i+1);
					Apex=apex(app);
					if(i+2<s.length())
					{
						app=s.charAt(i+2);
						Subscript=subscript(app);
						if(Subscript=='N')
							Subscript='0';
					}
				}
				for(j=i;s.charAt(j)!='[';j--);
				openList.add(s.substring(j+1,i));
				if(Apex=='0'&&Subscript=='0')
					Apex='1';
				k=s.substring(0, j)+"|$"+count+"-"+Apex+"-"+Subscript+"}";
				
				if(i+2<s.length()&& subscript(s.charAt(i+2))!='N')
					k+=s.substring(i+3, s.length());
				else
					if(i+1<s.length() && apex(s.charAt(i+1))!='0')
						k+=s.substring(i+2, s.length());
					else
						k+=s.substring(i+1, s.length());
				
				s=k;
				count++;
			
				for(j=s.length()-1;s.charAt(j)!='}' && j>0;j--);
				if(j>0)
					i=j;
			}
		i++;
		}
		return s;
	}
	/**
	Prepares the regex.
			@param s: string to process
	@return returns the regex ready to be processed 
	*/
	
	public String setupRegex(String s)
	{
		String regex="";
		Integer value=0;
		
		s=deleteUseless(s);
		s=deleteSquare(s);
		for(int i=0;i<s.length();i++)
		{
			if(apex(s.charAt(i))!='0' && s.charAt(i-1)!=')' && s.charAt(i-1)!=']')
			{
				
				value=Integer.valueOf(String.valueOf(apex(s.charAt(i))));
				for(int j=1;j<value;j++)
					regex=regex+s.substring(i-1, i);
			}
			else if(s.charAt(i)=='\u2219' || s.charAt(i)==' ')
			{
				
			}
			else
				regex= regex+ s.substring(i, i+1);
		}
		return regex;
	}
	/**
	Divides the regex by the U symbol (only if U is outside brackets pair). 
	
			@param str: string to process
	@return returns a List contains the input string parts 
	*/
	private List<String> splitter(String str)
	{
		List<String> list = new ArrayList<String>();
		if(str.contains("U"))
		{
			
			int par=0,j=0;
			for(int i=0;i<str.length();i++)
			{
				if(str.charAt(i)=='(' || str.charAt(i)=='[')
						par++;
				if(str.charAt(i)==')' || str.charAt(i)==']')
						par--;
				
				if(str.charAt(i)=='U')
				{
					if(str.substring(0, i).contains("(") || str.substring(0, i).contains("["))
					{
						if(par==0)
						{
					
							list.add(str.substring(j, i));
							j=i+1;							
						}						
					}	
					else
					{
				
						list.add(str.substring(j, i));
						j=i+1;
						
					}
						
				}
			}
			if(str.substring(j, str.length())!="")
				list.add(str.substring(j, str.length()));
		}
		else
			list.add(str);
		return list;
		
	}
	/**
	Translates the apex unicode symbol into an ASCII symbol
	
			@param k: Unicode char
	@return returns related ASCII char
	*/
	private static char apex(char k) {
		
		switch(k) {
		case '\u00B9':	
			k='1';
			break;
		case '\u00B2':	
			k='2';
			break;
		case '\u00B3':
			k='3';
			break;
		case '\u2074':	
			k='4';
			break;
		case '\u2075':	
			k='5';
			break;
		case '\u2076':	
			k='6';
			break;
		case '\u2077':	
			k='7';
			break;
		case '\u2078':	
			k='8';
			break;
		case '\u2079':	
			k='9';
			break;
		default: k='0';
		}		
		return k;
	}
	
	/**
	Translates the subscript unicode symbol into an ASCII symbol
	
			@param k: Unicode char
	@return returns related ASCII char
	*/
	private static char subscript(char k) {
		
		switch(k) {
		
		case '\u2080':	
			k='0';
			break;
		case '\u2081':	
			k='1';
			break;
		case '\u2082':	
			k='2';
			break;
		case '\u2083':	
			k='3';
			break;
		case '\u2084':	
			k='4';
			break;
		case '\u2085':	
			k='5';
			break;
		case '\u2086':	
			k='6';
			break;
		case '\u2087':
			k='7';
			break;
		case '\u2088':
			k='8';
			break;
		case '\u2089':	
			k='9';
			break;
		default: k='N';
		}		
		return k;
	}
	/**
	deletes the useless round Brackets to avoid to create useless table
	
			@param s: string to process
	@return returns string without useless brackets
	*/
	public static String deleteUseless(String s)
	{
		int j,i=0;
		boolean aum=true,find;
		int open=0,closed=0;
		char k,w;
		find=false;
		//delete useless bracket or double ones
		while(i<s.length())
		{
			k=s.charAt(i);
			if(s.charAt(i)==')')
			{
				j=i;
				while(!find)
				{
					w=s.charAt(j);
					if(s.charAt(j)==')')
						closed++;
					if(s.charAt(j)=='(')
						open++;
					if(s.charAt(j)=='('&&closed-open==0)
						find=true;
					else
						j--;
				}
				find=false;
				open=0;
				closed=0;
				if(j==i-1)
				{
					if(i!=0 && i!=s.length()-1)
					{
						s=s.substring(0, i-1)+s.substring(i+1, s.length());
						i=i-1;
					}
					else
					if(i==s.length()-1)
						s=s.substring(0, i);
					aum=false;
				}
				else
				if(i<s.length()-1 && j>=0 && s.charAt(i+1)==')' && s.charAt(j-1)=='(')
				{
					s=s.substring(0, j)+s.substring(j+1,i)+s.substring(i+1, s.length());
					i=i-1;
					aum=false;
				}
					else
						if(j+1==i-1)
						{
							s=s.substring(0, j)+s.substring(j+1,i)+s.substring(i+1, s.length());
							i=i-1;
							aum=false;
						}
			}
			if(aum)
			{
				i++;
			}
			else
				aum=true;
		}
		//delete bracket without U, star, cross or other bracket near them
		i=0;
		aum=true;
		while(i<s.length())
		{
			k=s.charAt(i);
			if(s.charAt(i)==')')
			{
				j=i;
				while(!find)
				{
					w=s.charAt(j);
					if(s.charAt(j)==')')
						closed++;
					if(s.charAt(j)=='(')
						open++;
					if(s.charAt(j)=='('&&closed-open==0)
						find=true;
					else
						j--;
				}
				find=false;
				open=0;
				closed=0;

				if(j==0&&i==s.length()-1)
				{
					s=s.substring(1, s.length()-1);
					aum=false;
				}
				else
					if(i<s.length()-1  && s.charAt(i+1)!='\u002A' && s.charAt(i+1)!='\u207A'&& s.charAt(i+1)!='U' && apex(s.charAt(i+1))=='0')
					{	
						s=s.substring(0, j)+s.substring(j+1,i)+s.substring(i+1, s.length());
						aum=false;
						i=i-2;
					}
					else if(i==s.length()-1)
					{
						s=s.substring(0, j)+s.substring(j+1,i);
						aum=false;
						i=i-2;
					}
			}
			if(aum)
			{
				i++;
			}
			else
				aum=true;
		}
		return s;
	}
	/**
	deletes the useless square Brackets to avoid to create useless table
	
			@param s: string to process
	@return returns string without useless brackets
	*/
	public static String deleteSquare(String s)
	{
		int j,i=0;
		boolean aum=true,find;
		int open=0,closed=0;
		char k;
		find=false;
		//delete useless bracket or double ones
		while(i<s.length())
		{
			if(s.charAt(i)==']')
			{
				j=i;
				while(!find)
				{
					if(s.charAt(j)==']')
						closed++;
					if(s.charAt(j)=='[')
						open++;
					if(s.charAt(j)=='['&&closed-open==0)
						find=true;
					else
						j--;
				}
				find=false;
				open=0;
				closed=0;
				if(j==i-1)
				{
					if(i!=0 && i!=s.length()-1)
					{
						s=s.substring(0, i-1)+s.substring(i+1, s.length());
						i=i-1;
					}
					else
					if(i==s.length()-1)
						s=s.substring(0, i);
					aum=false;
				}
				else
				if(i<s.length()-1 && j>=0 && s.charAt(i+1)==']' && s.charAt(j-1)=='[')
				{
					if(i<s.length()-2 && apex(s.charAt(i+2))=='0')
					{
						s=s.substring(0, j)+s.substring(j+1,i)+s.substring(i+1, s.length());
						i=i-1;
						aum=false;
					}
					else if(i+1==s.length()-1 && s.charAt(i+1)==']')
						{
							s=s.substring(0, j)+s.substring(j+1,i)+s.substring(i+1, s.length());
							i=i-1;
							aum=false;
						}
				}
			}
			if(aum)
			{
				i++;
			}
			else
				aum=true;
		}
		return s;
	}

	/**
	creates star graph structure
	
			@param start: start node
			@param num_stato: node number
			@param key: table key
			@param table: table where insert the structure
			@param c: starred symbol (char)
	@return returns last node number
	*/
	private int star(int start, int num_stato, int key, Hashtable<Integer, String> table, char[] c) 
	{
		String value="q"+String.valueOf(start)+"_"+'\u03B5'+"_q"+String.valueOf(num_stato+1);
		table.putIfAbsent(key, value);
		num_stato++;
		key++;
		value="q"+String.valueOf(num_stato)+"_"+c[0]+"_q"+String.valueOf(num_stato);
		table.putIfAbsent(key, value);
		key++;
		value="q"+String.valueOf(num_stato)+"_"+'\u03B5'+"_q"+String.valueOf(start);
		table.putIfAbsent(key, value);
		key++;
		return num_stato;
	}
	
	/**
	creates star graph structure
	
			@param start: start node
			@param num_stato: node number
			@param key: table key
			@param table: table where insert the structure
			@param c: starred symbol (string)
	@return returns last node number
	*/
	private int star(int start, int num_stato, int key, Hashtable<Integer, String> table, String c) 
	{
		
		String value="q"+String.valueOf(start)+"_"+'\u03B5'+"_q"+String.valueOf(num_stato+1);
		table.putIfAbsent(key, value);
		num_stato++;
		key++;
		value="q"+String.valueOf(num_stato)+"_"+c+"_q"+String.valueOf(num_stato);
		table.putIfAbsent(key, value);
		key++;
		value="q"+String.valueOf(num_stato)+"_"+'\u03B5'+"_q"+String.valueOf(start);
		table.putIfAbsent(key, value);
		key++;
		return num_stato;
	}
	
	/**
	creates cross graph structure
	
			@param start: start node
			@param num_stato: node number
			@param key: table key
			@param table: table where insert the structure
			@param c: crossed symbol (char)
	@return returns last node number
	*/
	private int cross(int start, int num_stato, int key, Hashtable<Integer, String> table, char[] c)
	{
		
		String value="q"+String.valueOf(start)+"_"+'\u03B5'+"_q"+String.valueOf(num_stato+1);
		table.putIfAbsent(key, value);
		num_stato++;
		key++;
		value="q"+String.valueOf(num_stato)+"_"+c[0]+"_q"+String.valueOf(num_stato+1);
		table.putIfAbsent(key, value);
		num_stato++;
		key++;
		value="q"+String.valueOf(num_stato)+"_"+'\u03B5'+"_q"+String.valueOf(start);
		table.putIfAbsent(key, value);
		key++;
		return num_stato;
	}
	/**
	creates cross graph structure
	
			@param start: start node
			@param num_stato: node number
			@param key: table key
			@param table: table where insert the structure
			@param c: crossed symbol (string)
	@return returns last node number
	*/
	private int cross(int start, int num_stato, int key, Hashtable<Integer, String> table, String c)
	{
		
		String value="q"+String.valueOf(start)+"_"+'\u03B5'+"_q"+String.valueOf(num_stato+1);
		table.putIfAbsent(key, value);
		num_stato++;
		key++;
		value="q"+String.valueOf(num_stato)+"_"+c+"_q"+String.valueOf(num_stato+1);
		table.putIfAbsent(key, value);
		num_stato++;
		key++;
		value="q"+String.valueOf(num_stato)+"_"+'\u03B5'+"_q"+String.valueOf(start);
		table.putIfAbsent(key, value);
		key++;
		return num_stato;
	}
} 
