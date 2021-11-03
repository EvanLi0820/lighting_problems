//放不下9 盏灯
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JFormattedTextField;
import java.awt.Color;
import javax.swing.JTextPane;
import javax.swing.JScrollBar;
import java.awt.Panel;
import javax.swing.JScrollPane;
import java.awt.Canvas;

public class LightsOn extends JFrame {
	private JPanel contentPane;
	private JTextField lightsField;
	private JTextField edgeField;
	private String lights;
	private String edges;
	
	private HashMap<Character,String> edgeMap = new HashMap<>();
	private HashMap<Character,Integer> lightMap = new HashMap<Character,Integer>();
	private JLabel []jlights = new JLabel[10];
	private JLabel []jswitchs = new JLabel[10];
	private String []lightStr;
	Map<String,Integer> solution = new HashMap<String,Integer>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LightsOn frame = new LightsOn();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void Combine(String a[], int a_pos, String rs[], int rs_pos) {
		if(rs_pos>=rs.length){
			String tmp = "";
			for(int i=0;i<rs.length;i++) {
				tmp += rs[i];
				//System.out.print(rs[i]+" ");
			}
			//System.out.println();
			solution.put(tmp, 0);
			
		}
		else {
			for(int ap=a_pos; ap<a.length; ap++){
				rs[rs_pos]=a[ap]; 
				Combine(a,ap+1,rs,rs_pos+1);
			}
		}
	}
	
	public Map<String,Integer> Solve() {
		int len = lightStr.length;
		int lightsValue[] = new int[len];
		Arrays.fill(lightsValue, 1);

		// all substring
		for (int i = 1; i <= len; i++ ) {
			String[] res=new String[i];
			Combine(lightStr,0,res,0);
		}
		
		for(Map.Entry<String, Integer> entry: solution.entrySet()) {
			String tmp = entry.getKey();
			int sum = 0;
			// one solution
			for (int i = 0; i < tmp.length(); i++) {
				char[] switchs = edgeMap.get(tmp.charAt(i)).toCharArray();
				
				for (char s: switchs) {	
					if (lightsValue[lightMap.get(s)] == 1) {
						lightsValue[lightMap.get(s)] = 0;
					} else {
						lightsValue[lightMap.get(s)] = 1;
					}
				}
				
			}
			
			for(int i = 0; i < len; i++) {
				sum+= lightsValue[i];
			}
			
			if (sum != 0) {
				solution.put(tmp, sum);
			}
			
			Arrays.fill(lightsValue, 1);
		}
		return solution;
		
	}
	
	/**
	 * Create the frame.
	 */
	public LightsOn() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 519, 581);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		lightsField = new JTextField();
		lightsField.setColumns(10);
		
		JLabel lblLights = new JLabel("Lights");
		
		edgeField = new JTextField();
		edgeField.setColumns(10);
		
		JLabel lblEdge = new JLabel("Edge");
		JPanel leftpanel = new JPanel() {
		   protected void paintComponent(Graphics g) {
		      super.paintComponent(g);
		      // draw circuits
		      if (lightStr != null && lightStr.length != 0) {
               g.setColor(Color.BLACK);
               int switchX;
               int switchY = 25;
               int lightX;
               int lightY = 100;
               for (int i = 0; i < lightStr.length; i++) {
                  switchX = (i+1)*50 + 35;
                  HashSet<Character> connectedLights = new HashSet<>();
                  char[] lights = edgeMap.get(lightStr[i].charAt(0)).toCharArray();
                  for (char light : lights) {
                     connectedLights.add(light);
                  }
                  for (char light : connectedLights) {
                     int b = lightMap.get(light);
                     lightX = (b+1)*50 + 35;
                     g.drawLine(switchX, switchY, lightX, lightY);
                  }
               }
		      }
		   }
		};
		
		JScrollPane scrollPane = new JScrollPane();
		JTextArea textArea = new JTextArea();
		
		JButton button = new JButton("Enter");
		button.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				lights = lightsField.getText();
				edges = edgeField.getText();
				//System.out.println("lights: " + lights + ";" + "edges:" + edges);
				
				lightStr = lights.split(" ");

//check for wrong lights inputs
				for(int i=0;i<lightStr.length;i++){
					textArea.setText("");
					if(lightStr[i].length()>1 || (65>lightStr[i].charAt(0) || lightStr[i].charAt(0)>90)){
						textArea.append("Wrong input format for lights please retry again");
						return;
					}
				}
				
				
				String[] edgeStr = edges.split(" ");

				
				edgeMap.clear();
				lightMap.clear();
				solution.clear();
				
				//add edge to map
				for (int i = 0; i < edgeStr.length; i++) {
					char [] arr = edgeStr[i].toCharArray();
					if (edgeMap.get(arr[0]) != null) {
						String tmp = edgeMap.get(arr[0]);
						tmp += Character.toString(arr[1]);
						edgeMap.put(arr[0], tmp);
						//System.out.println(tmp);
					} else {
						edgeMap.put(arr[0],String.valueOf(arr));
						//System.out.println("value:"+value);
					}
				}
				
				for (int i = 0; i < lightStr.length; i++) {
					if (edgeMap.get(lightStr[i].charAt(0)) == null) {
						edgeMap.put(lightStr[i].charAt(0),lightStr[i]);
					}
					lightMap.put(lightStr[i].charAt(0),i);
				}
				// check for wrong edge inputs
				String edge_withno_space="";
				for(int i=0;i<edgeStr.length;i++){
					edge_withno_space+=edgeStr[i];
				}
				for(int i=0;i<edge_withno_space.length();i++) {
					if (lightMap.get(edge_withno_space.charAt(i)) == null) {
						textArea.append("Wrong input format for edges please retry again");
						return;
					}
				}
				for(int i=0;i<edgeStr.length;i++){
					if(edgeStr[i].length()>2){
						textArea.append("Wrong input format for edges please retry again");
						return;
					}
				}
				
				leftpanel.removeAll();
				leftpanel.repaint();
				
				//add labels
				leftpanel.setLayout(null);
				JLabel switchesLabel = new JLabel("switches: ");
				switchesLabel.setSize(60, 30);
				switchesLabel.setLocation(0, 10);
				JLabel lightsLabel = new JLabel("lights: ");
				lightsLabel.setSize(60, 30);
				lightsLabel.setLocation(0, 80);
				leftpanel.add(switchesLabel);
            leftpanel.add(lightsLabel);
				for (int i = 0; i < lightStr.length; i++) {
					//String tmpStr = str[i] + "off";
					jswitchs[i] = new JLabel(lightStr[i], JLabel.CENTER);
					jswitchs[i].setSize(30, 30);
					jswitchs[i].setLocation((i+1)*50 + 20, 10);
					jswitchs[i].setBackground(Color.WHITE);
					jswitchs[i].setOpaque(true);
					
					jlights[i] = new JLabel(lightStr[i], JLabel.CENTER);
					jlights[i].setBackground(Color.RED);
					jlights[i].setOpaque(true);
					jlights[i].setSize(40, 40);
					jlights[i].setLocation((i+1)*50 + 15, 80);
					
					leftpanel.add(jlights[i]);
					leftpanel.add(jswitchs[i]);
				}
				leftpanel.revalidate();
				
				//add buttons listener
				for (int i = 0; i < lightStr.length; i++) {
					int a = i;
					jswitchs[a].addMouseListener(new MouseListener() {
						@Override
						public void mouseClicked(MouseEvent e) {
							//System.out.println("switchs pressed");
							HashSet<Character> connectedLights = new HashSet<>();
	                  char[] lights = edgeMap.get(lightStr[a].charAt(0)).toCharArray();
	                  for (char light : lights) {
	                     connectedLights.add(light);
	                  }
							for(char light : connectedLights) {
								int b = lightMap.get(light);
								if(jlights[b].getBackground() == Color.RED) {
									jlights[b].setBackground(Color.WHITE);
									jlights[b].setOpaque(true);
								} else {
									jlights[b].setBackground(Color.RED);
									jlights[b].setOpaque(true);
									jlights[b].setText(lightStr[b]);		
								}
							}
							
						}

						@Override
						public void mousePressed(MouseEvent e) {
						}

						@Override
						public void mouseReleased(MouseEvent e) {
						}

						@Override
						public void mouseEntered(MouseEvent e) {
						}

						@Override
						public void mouseExited(MouseEvent e) {
						}
					});
				}		
				
			}
		});
		

		textArea.setColumns(38);	
		JButton btnA = new JButton("VISUAL");
		btnA.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.setText("");
				for (Map.Entry<Character, String> entry : edgeMap.entrySet()) {
					char[] chr = entry.getValue().toCharArray();
					
					textArea.append("Edge: ");
					for (int i =0; i < chr.length - 1; i++) {
						textArea.append(chr[i] + "--->");
					}
					textArea.append(chr[chr.length-1] + "\n");
				}
			}
		});	
		
		JButton btnGen = new JButton("GEN");
		btnGen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Map<String,Integer> solution = Solve();
				int sum = Integer.MAX_VALUE;
				String str = "";
				
				for (Map.Entry<String, Integer> entry: solution.entrySet()) {
					//System.out.println(entry.getKey() + ":" + entry.getValue());
					if (entry.getValue() < sum) {
						sum = entry.getValue();
						str = entry.getKey();
					}
				}	
				textArea.setText("");
				textArea.append("push button as follow \n");
				//System.out.println(str);
				for(int i = 0; i < str.length(); i++) {
					textArea.append("push button " + str.charAt(i) + " \n");
				}
				textArea.append(sum + " lights still on\n");
			}
		});
		
		JButton btnB = new JButton("SOLVE");
		btnB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textArea.setText("");
				Map<String,Integer> solution = Solve();
				int sum = Integer.MAX_VALUE,b=0;
				String str = "";
				for (Map.Entry<String, Integer> entry: solution.entrySet()) {
					//System.out.println(entry.getKey() + ":" + entry.getValue());
					if (entry.getValue() < sum) {
						sum = entry.getValue();
						str = entry.getKey();
					}
				}
				
				for(int i = 0; i < lightStr.length; i++) {
					b = lightMap.get(lightStr[i].charAt(0));
					jlights[i].setBackground(Color.RED);
					jlights[i].setOpaque(true);
					jlights[i].setText(lightStr[b]);
				}
				
				for(int i =0; i < str.length(); i++) {
					Timer timer = new Timer();
					int a = lightMap.get(str.charAt(i));
					timer.schedule(new TimerTask() {
						@Override
						public void run() {
							char[] lights = edgeMap.get(lightStr[a].charAt(0)).toCharArray();
							textArea.append("push button " +lightStr[a] +" \n");
							for(char light : lights) {
								int b = lightMap.get(light);
								if(jlights[b].getBackground() == Color.RED) {
									jlights[b].setBackground(Color.WHITE);
									jlights[b].setOpaque(true);
									jlights[b].setText(lightStr[b]);
								} else {
									jlights[b].setBackground(Color.RED);
									jlights[b].setOpaque(true);
									jlights[b].setText(lightStr[b]);		
								}
							}				
							timer.cancel();
						}
					},1000*(i+1));
				}	
			}
		});
		
		
		scrollPane.setRowHeaderView(textArea);
		JLabel lblLights_1 = new JLabel("Lights Visual");
		JLabel lblMessage = new JLabel("Message");
		

		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addComponent(lblLights)
										.addComponent(lblEdge, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE))
									.addGap(12)
									.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
										.addComponent(edgeField, GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)
										.addComponent(lightsField, GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(button, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE)
									.addGap(43))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addComponent(btnA)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(btnB, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnGen, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
									.addGap(120))
								.addGroup(gl_contentPane.createSequentialGroup()
									.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
										.addComponent(leftpanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(scrollPane, Alignment.LEADING))
									.addGap(50))))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(209)
							.addComponent(lblMessage, GroupLayout.PREFERRED_SIZE, 69, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(204)
							.addComponent(lblLights_1, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblLights)
						.addComponent(button)
						.addComponent(lightsField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblEdge)
						.addComponent(edgeField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblLights_1)
					.addGap(5)
					.addComponent(leftpanel, GroupLayout.PREFERRED_SIZE, 198, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
					.addComponent(lblMessage)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 131, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnA)
						.addComponent(btnB)
						.addComponent(btnGen))
					.addGap(19))
		);
		contentPane.setLayout(gl_contentPane);
	}
}
