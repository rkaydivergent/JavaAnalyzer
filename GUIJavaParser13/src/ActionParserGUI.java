import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.awt.Font;
import java.awt.Image;
import java.awt.Color;
import java.awt.SystemColor;
import javax.swing.UIManager;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ActionParserGUI extends JFrame {

	private JPanel contentPane;
	private JTextPane textPane ;
	static EG1 parser = null;
	static List<String> equi_textual= new ArrayList<String>();
	static List<String> errorlist= new ArrayList<String>();
	static List<String> descriptionlist= new ArrayList<String>();
	static List<String> suggessionlist= new ArrayList<String>();
	static List<String> coordinateslist= new ArrayList<String>();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ActionParserGUI frame = new ActionParserGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ActionParserGUI() {
		setTitle("MCS");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 828, 605);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Enter Code:");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel.setBounds(10, 84, 84, 20);
		contentPane.add(lblNewLabel);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 115, 792, 195);
		contentPane.add(scrollPane_1);
		
		JTextPane textPane_1 = new JTextPane();
		scrollPane_1.setViewportView(textPane_1);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 360, 792, 195);
		contentPane.add(scrollPane);
		
		textPane = new JTextPane();
		textPane.setEditorKit(new javax.swing.text.StyledEditorKit());
		scrollPane.setViewportView(textPane);
		
		
       
		
		JButton btnNewButton = new JButton("Check");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton.setForeground(SystemColor.text);
		btnNewButton.setBackground(SystemColor.textHighlight);
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String sentence = textPane_1.getText();
                // Put parens around sentence so that parser knows scope
               
                InputStream is = new ByteArrayInputStream(sentence.getBytes());
                if(parser == null) parser = new EG1(is);
                else EG1.ReInit(is);
                try
                {
                  switch (EG1.start())
                  {
                    case 0 :
                    	textPane.setText("Input Parsed ok According to Java BNF");
                    break;
                    default :
                    break;
                  }
                }
                catch (Exception e1)
                {
                	textPane.setText("Input NOT parsed according to Java EBNF.\nPlease enter code in Java or remove the following Syntax Error:\n"+
                		  				e1.getMessage());
                }
                catch (Error e2)
                {
                	textPane.setText("error in expression.\n"+
    		  						   e2.getMessage());
                }
                finally
                {
                  
                }
                if (textPane.getText().equals("Input Parsed ok According to Java BNF")) {
                    
                    equi_textual=  EG1.getWarnings();
                    int no=0;
                    String error, desc, suges,coord ="";
               	 textPane.setText("Expression Paresed ok According to Java BNF \nTotal no of Warnings: "+equi_textual.size()+" \nWarning(s):");
               	 if ( equi_textual.size()>0) {
                    for(String  gi : equi_textual) {
                    	String[] res = gi.split("[,]", 0);
                        System.out.println("ye "+ res[0]);
                    	if(res[0].equals("notify")) {
                    		no++;     
                    		error= "Detected notify().";
                    	    desc= "Calling 'notify' instead of 'notifyAll' may fail to wake up the correct thread and cannot wake up multiple threads.";
                    	    suges= "Use 'notifyAll()' to notify safely.";
                    	    coord= "("+res[1]+","+res[2]+")";
                    	    errorlist.add(error);
                    	    descriptionlist.add(desc);
                    	    suggessionlist.add(suges);
                    	    coordinateslist.add(coord);
                    		textPane.setText(textPane.getText()+"\n"+no+". "+error+" at Line: "+res[1]+" and Col: "+res[2]+ "\nDescription: "+ desc+"\nSugessios: "+ suges) ;
                    				
                    	}
                    	else if(res[0].equals("constart")) {
                    	     no++;
                    	     error="Detected a thread start inside constructor";
                    	     desc="Starting a thread within a constructor may cause the thread to start before any subclass constructor has completed its initialization, causing unexpected results.";
                    	     suges="Start the thread outside of Constructor. Starting a thread within a constructor may cause the thread to start before any subclass constructor has completed its initialization, causing unexpected results";
                    	     coord= "("+res[1]+","+res[2]+")";
                    	     errorlist.add(error);
                     	     descriptionlist.add(desc);
                     	     suggessionlist.add(suges);
                     	     coordinateslist.add(coord);
                     	    
                     	     textPane.setText(textPane.getText()+"\n"+no+". "+error+" at Line: "+res[1]+" and Col: "+res[2]+ "\nDescription: "+ desc+"\nSugessios: "+ suges) ;
                     	   }
                    	else if(res[0].equals("yield")) {
                    		no++;
                    		error="Detected Thread.yield";
                   	        desc="Calling 'Thread.yield' may have no effect, and is not a reliable way to prevent a thread from taking up too much execution time.";
                   	        suges="Some form of waiting for a notification using the wait and notifyAll methods or by using the java.util.concurrent library should be used.";
                   	        coord= "("+res[1]+","+res[2]+")";
                   	        errorlist.add(error);
              	            descriptionlist.add(desc);
              	            suggessionlist.add(suges);
              	            coordinateslist.add(coord);
              	            textPane.setText(textPane.getText()+"\n"+no+". "+error+" at Line: "+res[1]+" and Col: "+res[2]+ "\nDescription: "+ desc+"\nSugessios: "+ suges) ;
              	          }
                    	else if(res[0].equals("priority")) {
                    		no++;
                    		error="Detected setting thread priority";
                   	        desc="Setting thread priorities to control interactions between threads is not portable, and may not have the desired effect or even cause starvation.";
                   	        suges="Some form of waiting for a notification using the wait and notifyAll methods or by using the java.util.concurrent library should be used.";
                   	        coord= "("+res[1]+","+res[2]+")";
                   	        errorlist.add(error);
              	            descriptionlist.add(desc);
              	            suggessionlist.add(suges);
              	            coordinateslist.add(coord);
                   	     
              	            textPane.setText(textPane.getText()+"\n"+no+". "+error+" at Line: "+res[1]+" and Col: "+res[2]+ "\nDescription: "+ desc+"\nSugessios: "+ suges) ;
              	          }
                    	else if(res[0].equals("emptyblock")) {
                    		no++;
                    		error="Detected empty synchronized blocks";
                   	        desc="Empty synchronized blocks may indicate the presence of incomplete code or incorrect synchronization, and may lead to concurrency problems.";
                   	        suges="Any code that requires synchronization on the given lock should be placed within the synchronized block";
                   	        coord= "("+res[1]+","+res[2]+")";
                   	        errorlist.add(error);
              	            descriptionlist.add(desc);
              	            suggessionlist.add(suges);
              	            coordinateslist.add(coord);
              	          textPane.setText(textPane.getText()+"\n"+no+". "+error+" at Line: "+res[1]+" and Col: "+res[2]+ "\nDescription: "+ desc+"\nSugessios: "+ suges) ;
              	          }
                    	else if(res[0].equals("dateformat")) {
                    		no++;
                    		error="Detected static fields of type 'DateFormat'";
                   	        desc="Static fields of type java.text.DateFormat is not thread-safe ";
                   	        suges="Use instance fields instead and synchronize access where necessary";
                   	        coord= "("+res[1]+","+res[2]+")";
                   	        errorlist.add(error);
              	            descriptionlist.add(desc);
              	            suggessionlist.add(suges);
              	            coordinateslist.add(coord);
              	            textPane.setText(textPane.getText()+"\n"+no+". "+error+" at Line: "+res[1]+" and Col: "+res[2]+ "\nDescription: "+ desc+"\nSugessios: "+ suges) ;
                        
                          	}
                    	else if(res[0].equals("lockswait")) {
                    		no++;
                    		error="Detected 'Object.wait' while two locks are held";
                   	        desc="Calling Object.wait while two locks are held may cause deadlock, because only one lock is released by wait";
                   	        suges="See if one of the locks should continue to be held while waiting for a condition on the other lock. If not, release one of the locks before calling Object.wait.";
                   	        coord= "("+res[1]+","+res[2]+")";
                   	        errorlist.add(error);
              	            descriptionlist.add(desc);
              	            suggessionlist.add(suges);
              	          textPane.setText(textPane.getText()+"\n"+no+". "+error+" at Line: "+res[1]+" and Col: "+res[2]+ "\nDescription: "+ desc+"\nSugessios: "+ suges) ;
              	          }
                    	else if(res[0].equals("lockrelease")) {
                    		no++;
                    		error="Detected Unlocked Lock";
                   	        desc="If a method acquires a lock and some of the exit paths from the method do not release the lock then this may cause deadlock.";
                   	        suges=" Ensure that all exit paths of the method release the lock";
                   	        coord= "("+res[1]+","+res[2]+")";
                   	        errorlist.add(error);
              	            descriptionlist.add(desc);
              	            suggessionlist.add(suges);
              	            coordinateslist.add(coord);
                    		System.out.println("here");
                    		textPane.setText(textPane.getText()+"\n"+no+". "+error+" at Line: "+res[1]+" and Col: "+res[2]+ "\nDescription: "+ desc+"\nSugessios: "+ suges) ;
                    		
                          	}
                    	else if(res[0].equals("locksleep")) {
                    		no++;
                    		System.out.println("locksleep");
                    		error="Detected 'Thread.sleep' while a lock held";
                   	        desc="Calling Thread.sleep with a lock held may lead to very poor performance or even deadlock. This is because Thread.sleep does not cause a thread to release its locks.";
                   	        suges="Thread.sleep should be called only outside of a synchronized block. However, a better way for threads to yield execution time to other threads may be to use either of the following solutions: The java.util.concurrent library The wait and notifyAll methods";
                   	        coord= "("+res[1]+","+res[2]+")";
                   	        errorlist.add(error);
              	            descriptionlist.add(desc);
              	            suggessionlist.add(suges);
              	            coordinateslist.add(coord);
              	          textPane.setText(textPane.getText()+"\n"+no+". "+error+" at Line: "+res[1]+" and Col: "+res[2]+ "\nDescription: "+ desc+"\nSugessios: "+ suges);
              	          }
                    	else if(res[0].equals("conditionwait")) {
                    		no++;
                    		error="Detected .wait()on a Condition interface";
                   	        desc="Calling wait on an object of type java.util.concurrent.locks.Condition may result in unexpected behavior because wait is a method of the Object class, not the Condition interface itself. Such a call is probably a typographical error: typing \\\"wait\\\" instead of \\\"await\\\". ";
                   	        suges="Instead of Object.wait, use one of the Condition.await methods.";
                   	        coord= "("+res[1]+","+res[2]+")";
                   	        errorlist.add(error);
              	            descriptionlist.add(desc);
              	            suggessionlist.add(suges);
              	            coordinateslist.add(coord);
              	          textPane.setText(textPane.getText()+"\n"+no+". "+error+" at Line: "+res[1]+" and Col: "+res[2]+ "\nDescription: "+ desc+"\nSugessios: "+ suges) ;
                          	}
                    	else if(res[0].equals("confinal")) {
                    		no++;
                    		error="Detected a call to non-final method from a Constructor\"+\" at Line:";
                   	        desc="If a constructor calls a method that is overridden in a subclass, the result can be unpredictable.";
                   	        suges="Do not call a non-final method from within a constructor if that method could be overridden in a subclass";
                   	        coord= "("+res[1]+","+res[2]+")";
                   	        errorlist.add(error);
              	            descriptionlist.add(desc);
              	            suggessionlist.add(suges);
              	            coordinateslist.add(coord);
              	            textPane.setText(textPane.getText()+"\n"+no+". "+error+" at Line: "+res[1]+" and Col: "+res[2]+ "\nDescription: "+ desc+"\nSugessios: "+ suges) ;
              	          }
                    	else if(res[0].equals("downcast")) {
                    		no++;
                    		System.out.println(res[0]);
                    		error="Detected array downcast";
                   	        desc="Trying to cast an array of a particular type as an array of a subtype causes a 'ClassCastException' at runtime. Some downcasts on arrays will fail at runtime.";
                   	        suges="Ensure that the array creation expression constructs an array object of the right type.";
                   	        coord= "("+res[1]+","+res[2]+")";
                   	        errorlist.add(error);
              	            descriptionlist.add(desc);
              	            suggessionlist.add(suges);
              	            coordinateslist.add(coord);
              	            textPane.setText(textPane.getText()+"\n"+no+". "+error+" at Line: "+res[1]+" and Col: "+res[2]+ "\nDescription: "+ desc+"\nSugessios: "+ suges) ;
              	          }
                    }           
                    }  else { textPane.setText(textPane.getText()+"No Warnings Detected");}
                }
			}
		});
		btnNewButton.setBounds(520, 321, 80, 28);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Reset");
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				textPane.setText("");
				textPane_1.setText("");
			}
		});
		btnNewButton_1.setBackground(SystemColor.textHighlight);
		btnNewButton_1.setForeground(SystemColor.text);
		btnNewButton_1.setBounds(610, 321, 76, 28);
		contentPane.add(btnNewButton_1);
		
		JLabel lblWarnings = new JLabel("Warnings:");
		lblWarnings.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblWarnings.setBounds(10, 329, 84, 20);
		contentPane.add(lblWarnings);
		
		JLabel lblNewLabel_1 = new JLabel("Code Analysis Tool for Mission Critical Systems");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel_1.setBounds(231, 41, 360, 25);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/mcs.png")).getImage().getScaledInstance( 70,50,Image.SCALE_SMOOTH)));
		//ImageIcon img = new ImageIcon(this.getClass().getResource("/mcs.png").getFile().getScaledInstance( lblNewLabel_2.getWidth(),lblNewLabel_2.getHeight(),Image.SCALE_SMOOTH));
		//lblNewLabel_2.setIcon(img);
		lblNewLabel_2.setBounds(10, 11, 70, 55);
		contentPane.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("");
		lblNewLabel_3.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/eme-logo.png")).getImage().getScaledInstance( 100,65,Image.SCALE_SMOOTH)));
		lblNewLabel_3.setBounds(724, 11, 78, 62);
		contentPane.add(lblNewLabel_3);
		
	
		
		JButton btnNewButton_2 = new JButton("Save Result");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
				
				fileChooser.setDialogTitle("Specify a file to save");   
				String path ="";
				int userSelection = fileChooser.showSaveDialog(contentPane);
				 
				if (userSelection == JFileChooser.APPROVE_OPTION) {
				    File fileToSave = fileChooser.getSelectedFile();
				    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				   
				    fileChooser.setAcceptAllFileFilterUsed(false);
				    path = fileToSave.getAbsolutePath();
				    System.out.println("Save as file: " + fileToSave.getAbsolutePath());
				    
				}
			
				writeXMLFile( path);
		
			}

			private void writeXMLFile(String path) {
				// TODO Auto-generated method stub
				try {

			        DocumentBuilderFactory dFact = DocumentBuilderFactory.newInstance();
			        DocumentBuilder build = dFact.newDocumentBuilder();
			        Document doc = build.newDocument();

			        Element root = doc.createElement("Analysis");
			        doc.appendChild(root);

			       int sr=0;
			       
			        for (int r = 0; r <  errorlist.size(); r++)
					{
			        	 Element Details = doc.createElement("Warning");
			        	 root.appendChild(Details);
						 sr= r+1;
						 Element id = doc.createElement("sr");
				            id.appendChild(doc.createTextNode(Integer.toString(r+1)));				                 
				            Details.appendChild(id);
				    	  
				            Element name = doc.createElement("Error");
				            name.appendChild(doc.createTextNode(String.valueOf(errorlist.get(r)
				                    )));
				            Details.appendChild(name);
				           
				            Element coord = doc.createElement("Coordinates");
				            coord.appendChild(doc.createTextNode(String.valueOf(coordinateslist.get(r)
				                    )));
				            Details.appendChild(coord);
				           
				            Element desc = doc.createElement("Description");
				            desc.appendChild(doc.createTextNode(String.valueOf(descriptionlist.get(r)
				                    )));
				            Details.appendChild(desc);
				           
				            Element sug = doc.createElement("Sugession");
				            sug.appendChild(doc.createTextNode(String.valueOf(suggessionlist.get(r)
				                    )));
				            Details.appendChild(sug);
				            
				  	      }  
						 
			        errorlist.clear();
					
					

			        // Save the document to the disk file
			        TransformerFactory tranFactory = TransformerFactory.newInstance();
			        Transformer aTransformer = tranFactory.newTransformer();

			        // format the XML nicely
			        aTransformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");

			        aTransformer.setOutputProperty(
			                "{http://xml.apache.org/xslt}indent-amount", "4");
			        aTransformer.setOutputProperty(OutputKeys.INDENT, "yes");

			        DOMSource source = new DOMSource(doc);
			        try {
			            // location and name of XML file you can change as per need
			            FileWriter fos = new FileWriter(path+".xml");
			            StreamResult result = new StreamResult(fos);
			            aTransformer.transform(source, result);

			        } catch (IOException e) {

			            e.printStackTrace();
			        }

			    } catch (TransformerException ex) {
			        System.out.println("Error outputting document");

			    } catch (ParserConfigurationException ex) {
			        System.out.println("Error building document");
			    }
			}
			
		});
		btnNewButton_2.setBackground(SystemColor.textHighlight);
		btnNewButton_2.setForeground(SystemColor.text);
		btnNewButton_2.setBounds(696, 321, 106, 28);
		contentPane.add(btnNewButton_2);
		
		JLabel lblNewLabel_1_1 = new JLabel("College of E&ME- NUST");
		lblNewLabel_1_1.setForeground(new Color(0, 0, 205));
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel_1_1.setBounds(311, 11, 185, 35);
		contentPane.add(lblNewLabel_1_1);
		
		
	}
}
