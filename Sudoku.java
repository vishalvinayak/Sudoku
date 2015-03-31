import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.Random;

class Sudoku1 extends JFrame implements ActionListener,Runnable,MouseListener
{
	JButton b1[][]=new JButton[9][9];
	JButton b2[]=new JButton[9];
	JButton c11;
	ImageIcon im=new ImageIcon("Sudoku.jpg");
	JTextField t1;

	JLabel l1=new JLabel("Time Over : ");
	JLabel l2=new JLabel(im);
	
	Thread t;
	
	MenuBar mbar;
	Menu file,view,help;
	MenuItem item1,item2,item3,item4,item5,item6,item7;

	Panel p1,p2,p3;

	FileInputStream fin;
	FileOutputStream fout;

	int i,j,k,timeover,count=0,flag=1,chk,chk1;
	int actual[][]=new int[9][9];
	int fake[][]=new int[9][9];
	String show[]=new String[81];
	int realshow[]=new int[33];
	int realcount=0;
	int x,y,c1,d1,thrdchk=0;
	long breakcount;
	String selected;

	boolean suspendthrd;	

	Sudoku1()
	{
		timeover=0;
		suspendthrd=false;

		setTitle("Sudoku By- Vishal Vinayak");
		Container c =this.getContentPane();
		c.setLayout(null);
		c.setBackground(Color.white);	
		
		c11=new JButton("Check");
		c11.setActionCommand("Check");
		t1=new JTextField(3);
		t1.setEnabled(false);
		t1.setText(String.valueOf(timeover));
		
		t=new Thread(this);
	
		mbar=new MenuBar();
		setMenuBar(mbar);
		file=new Menu("File");
		view=new Menu("View");
		help=new Menu("Help");

		file.add(item1=new MenuItem("New..."));
		file.add(item2=new MenuItem("Exit"));
		mbar.add(file);	
		
		view.add(item3=new MenuItem("Best Times"));
		mbar.add(view);
		
		help.add(item4=new MenuItem("About"));
		help.add(item5=new MenuItem("Instructions"));
		mbar.add(help);

		item1.addActionListener(this);
		item2.addActionListener(this);
		item3.addActionListener(this);
		item4.addActionListener(this);
		item5.addActionListener(this);
		c11.addActionListener(this);

		p1=new Panel(new FlowLayout());
		p1.setBackground(Color.pink);
		p1.setBounds(0,0,500,50);
		p1.add(l1);
		p1.add(t1);
		p1.add(l2);
		p1.add(c11);
		
		p2=new Panel(new GridLayout(9,1));
		p2.setBackground(Color.gray);
		p2.setBounds(450,50,50,450);
		
		p3=new Panel(new GridLayout(9,9));
		p3.setBackground(Color.gray);
		p3.setBounds(0,50,450,450);
		
		for(i=0;i<9;i++)
		{
			for(j=0;j<9;j++)
			{
				b1[i][j]=new JButton();
				b1[i][j].setActionCommand(String.valueOf(i)+String.valueOf(j));			
			}
		}
		b1[8][0].addMouseListener(this);
		for(i=0;i<9;i++)
		{
			for(j=0;j<9;j++)
			{
				if(i==0 || i==1 || i==2 || i==6 || i==7 || i==8 )
				{
					if(j==0 || j==1 || j==2 || j==6 || j==7 || j==8)
					{
						b1[i][j].setForeground(Color.white);
						b1[i][j].setBackground(Color.black);
					}
					if(j==3 || j==4 || j==5)	
					{
						b1[i][j].setForeground(Color.black);
						b1[i][j].setBackground(Color.white);
					}
				}
				if(i==3 || i==4 || i==5)
				{
					if(j==0 || j==1 || j==2 || j==6 || j==7 || j==8)
					{
						b1[i][j].setForeground(Color.black);
						b1[i][j].setBackground(Color.white);	
					}
					if(j==3 || j==4 || j==5)
					{
						b1[i][j].setForeground(Color.white);
						b1[i][j].setBackground(Color.black);
					}	
				}
			}
		}
		for(i=0;i<9;i++)
		{
			for(j=0;j<9;j++)
			{
				b1[i][j].addActionListener(this);
			}
		}
		for(i=0;i<9;i++)
		{
			for(j=0;j<9;j++)
			{
				p3.add(b1[i][j]);
			}
		}
			
		for(i=0;i<9;i++)
		{
			b2[i]=new JButton();
			b2[i].setActionCommand(String.valueOf(i+1));
			b2[i].setForeground(Color.black);
			b2[i].setText(String.valueOf(i+1));
			b2[i].setBackground(new Color(220,110,160));
		}
		for(i=0;i<9;i++)
		{
			b2[i].addActionListener(this);
		}
		for(i=0;i<9;i++)
		{
			p2.add(b2[i]);
		}
		
		
		c.add(p1);
		c.add(p2);
		c.add(p3);
		int showcount=0;
		for(i=0;i<9;i++)
		{
			for(j=0;j<9;j++)
			{
				show[showcount]=String.valueOf(i)+String.valueOf(j);
				showcount++;
			}
		}
		
		Random r1=new Random();
		do
		{
			go:
			{
				chk1=r1.nextInt(80);
				chk=chk1+1;
				for(j=0;j<realcount;j++)
				{
					if(chk==realshow[j])
					{
						flag=0;
						break;
					}
					else
					{
						flag=1;	
					}
				}
				if(flag==0)
				{
					break go;					
				}
				if(flag==1)
				{
					realshow[realcount]=chk;
					realcount++;
				}
			}
		}
		while(realcount!=33);
		
		c1=0;d1=0;
		Random r=new Random();
		for(i=0;i<9;i++)
		{
			do
			{
				go:
				{					
					if(c1==i && d1==count)
					{
						breakcount++;
						if(breakcount==50)
						{
							i--;
							continue;
						}
					}
					else
					{
						breakcount=0;
					}
					c1=i;
					d1=count;
					chk1=r.nextInt(9);
					chk=chk1+1;	
					for(j=0;j<count;j++)
					{
						if(chk==actual[i][j])
						{
							flag=0;
							break;
						}
						else
						{
							flag=1;	
						}
					}
					for(x=count,y=i-1;y>=0;y--)
					{
						if(actual[y][x]==chk)
						{
							break go;
						}
					}
					if((i==1||i==4||i==7)&&(count==0||count==3||count==6))
					{
						if(chk==actual[i-1][count+1] || chk==actual[i-1][count+2])
						{
							break go;
						}	
					}
					if((i==1||i==4||i==7)&&(count==1||count==4||count==7))
					{
						if(chk==actual[i-1][count-1] ||chk==actual[i-1][count+1] )
						{
							break go;
						}
					}
					if((i==1||i==4||i==7)&&(count==2||count==5||count==8))
					{
						if((chk==actual[i-1][count-2])||chk==actual[i-1][count-1])
						{
							break go;
						}
					}
					if((i==2||i==5||i==8)&&(count==0||count==3||count==6))
					{
						if(chk==actual[i-2][count+1] || chk==actual[i-2][count+2] || chk==actual[i-1][count+1] || chk==actual[i-1][count+2])
						{
							break go;
						}
					}
					if((i==2||i==5||i==8)&&(count==1||count==4||count==7))
					{
						if(chk==actual[i-2][count-1] || chk==actual[i-2][count+1] || chk==actual[i-1][count-1] || chk==actual[i-1][count+1])
						{
							break go;
						}
					}
					if((i==2||i==5||i==8)&&(count==2||count==5||count==8))
					{
						if(chk==actual[i-2][count-2] || chk==actual[i-2][count-1] || chk==actual[i-1][count-2] || chk==actual[i-1][count-1])
						{
							break go;
						}
					}
					if(flag==0)
					{
						break go;					
					}
					if(flag==1)
					{
						if(count==9)
						{
							continue;
						}
						actual[i][count]=chk;
						count++;
					}
				}
			}
			while(count!=9);
			count=0;
		}
		int s1=0,s2=0;	
		for(i=0;i<33;i++)
		{
			s1=Integer.parseInt(String.valueOf(show[realshow[i]].charAt(0)));
			s2=Integer.parseInt(String.valueOf(show[realshow[i]].charAt(1)));
			b1[s1][s2].setText(String.valueOf(actual[s1][s2]));
			b1[s1][s2].setEnabled(false);		
		}	
	}
	public void actionPerformed(ActionEvent ae)
	{
		String s=(String) ae.getActionCommand();
		if(s.equals("New..."))
		{
			dispose();
			Sudoku1 m=new Sudoku1();
			m.setSize(511,555);
			m.setVisible(true);
		}
		
		if(s.equals("Exit"))
		{
			System.exit(0);
		}
		if(s.equals("Best Times"))
		{
			OpenBestTime();
		}
		if(s.equals("About"))
		{
			JOptionPane.showMessageDialog(this," Sudoku \n\n\t Made By :  \n Vishal Vinayak \n C.S.E.       '03 \n S.B.S.C.E.T. Ferozepur \n Punjab, India. \n\n http://vishal.says.it \n vishalvinayak@rediffmail.com ");
	
		}
		if(s.equals("Instructions"))
		{
			JOptionPane.showMessageDialog(this,"                                      Sudoku \n                              Copyrights Protected \n Try to fill the numbers in boxes. Click on the pink buttons on  \n the right to select a number. Then click on the box to label it with \n the selected number.  \n A Number should be unique in it's Row, Column & Coloured Box.  \n When you r finished with numbers click on 'Check' Button on top. \n If U'r order was correct You Win else You Loose. \n ");
	
		}
		for(i=0;i<9;i++)
		{
			if(b2[i].getActionCommand()==s)
			{
				if(thrdchk==0)
				{
					t.start();
					thrdchk++;
				}
				if(selected!=null)
				{
					b2[Integer.parseInt(selected)-1].setBackground(new Color(220,110,160));					
				}
				selected=s;
				b2[Integer.parseInt(selected)-1].setBackground(new Color(120,110,160));
			}
		}
		for(i=0;i<9;i++)
		{
			for(j=0;j<9;j++)
			{
				if(b1[i][j].getActionCommand()==s)
				{
					if(thrdchk==0)
					{
						t.start();
						thrdchk++;
					}
					b1[i][j].setText(selected);
				}
			}
		}
		if(s.equals("Check"))
		{
			c11.setEnabled(false);
			winner();
		}
	}
	public void run()
	{
		try
		{
			do
			{
				t.sleep(1000);
				t1.setText(String.valueOf(timeover));
				timeover++;
			}
			while(timeover!=2001 && suspendthrd!=true);
			if(timeover>=2000)
			{
				for(i=0;i<9;i++)
				{
					for(j=0;j<9;j++)
					{
						b1[i][j].setEnabled(false);
					}
				}
				JOptionPane.showMessageDialog(this,"        You took a lot of time \n               YOU LOOSE");
				System.exit(0);
			}
		}
		catch(InterruptedException e1)
		{
		}
	}
	public void mouseClicked(MouseEvent me)
	{
		int key=(int) me.getButton();
		if(key==MouseEvent.BUTTON3)
		{
			String sss=JOptionPane.showInputDialog("Enter Code:");
			if(sss.equalsIgnoreCase("show"))
			{
				for(int m=0;m<9;m++)
				{
					for(int n=0;n<9;n++)
					{
						if(b1[m][n].getText()=="")
						{
							fake[m][n]=0;
						}
						else
						{
							fake[m][n]=Integer.parseInt(b1[m][n].getText());
						}
						b1[m][n].setText(String.valueOf(actual[m][n]));
					}
				}
			}
			if(sss.equalsIgnoreCase("hide"))
			{
				for(int m=0;m<9;m++)
				{
					for(int n=0;n<9;n++)
					{
						if(fake[m][n]==0)
						{
							b1[m][n].setText("");
						}
						else
						{
							b1[m][n].setText(String.valueOf(fake[m][n]));
						}
					}
				}	
			}
		
			if(sss.equalsIgnoreCase("savetime"))
			{
				timeover=0;	
			}
			if(sss.equalsIgnoreCase("exit"))
			{
				System.exit(0);	
			}
		}
	}
	public void mouseEntered(MouseEvent me)
	{
	}
	public void mouseExited(MouseEvent me)
	{
	}
	public void mousePressed(MouseEvent me)
	{
	}
	public void mouseReleased(MouseEvent me)
	{
	}
	void winner()
	{
		int win=0;
		for(i=0;i<9;i++)
		{
			for(j=0;j<9;j++)
			{
				if(b1[i][j].getText()==String.valueOf(actual[i][j]))
				{
					win++;				
				}	
			}
		}
		if(win==81)
		{
			suspendthrd=true;
			timeover++;
			JOptionPane.showMessageDialog(this,"                You Win");
			SaveBestTime(timeover);
			System.exit(0);
		}
		else
		{
			suspendthrd=true;
			JOptionPane.showMessageDialog(this,"               You Loose");
			for(int m=0;m<9;m++)
			{
				for(int n=0;n<9;n++)
				{
					b1[m][n].setEnabled(false);				
					if(b1[m][n].getText()!=String.valueOf(actual[m][n]))
					{
						b1[m][n].setBackground(Color.red);
					}
				}
			}	
			JOptionPane.showMessageDialog(this,"      Here's the Correct Order :");	
			for(int m=0;m<9;m++)
			{
				for(int n=0;n<9;n++)
				{
					b1[m][n].setText(String.valueOf(actual[m][n]));
				}
			}
		}
	}
	void SaveBestTime(int time)
	{
		FileInputStream fins;
		FileOutputStream fouts;
		int a[]=new int[8];
		int j=0,f=1,rem=0;
		String sb[]=new String[8];
		String sbf=new String();
		try
		{
			try
			{
				fins=new FileInputStream("BestTimes.vis");	
			}
			catch(FileNotFoundException e)
			{
				fouts=new FileOutputStream(new File("BestTimes.vis"));
				fouts.write(time);	
				JOptionPane.showMessageDialog(this,"      Best Time Made: "+time+" sec"); 
				f=0;
				return;
			}
			if(f==1)
			{
				do
				{
					i=fins.read();
					if(i!=-1)
					{
						a[j]=i;
						j++;
					}					
				}
				while(i!=-1);
				a[j]=9;
				j++;
				a[j]=9;
				j++;
				a[j]=9;
				j++;	
				
				for(i=0;i<5;i++)
				{
					if(a[i]==9 && a[i+1]==9 && a[i+2]==9)
					{	
						rem=i;	
						break;	
					}
				}
				
				for(i=0;i<rem;i++)
				{
					sb[i]=new String();
					sb[i]=String.valueOf(a[i]);	
					sbf=sbf+sb[i];
				}
				int m=Integer.parseInt(sbf);
				if(time<m)
				{
					try
					{
						fouts=new FileOutputStream("BestTimes.vis");
						fouts.write(time);
						JOptionPane.showMessageDialog(this,"      Best Time Made: "+time+" sec"); 
					}
					catch(FileNotFoundException fe)
					{	
					}
				}	
			}
			fins.close();
		}
		catch(IOException ioe)
		{
		}
		catch(NullPointerException ne)
		{
		}
	}	
	void OpenBestTime()
	{
		FileInputStream fins;
		FileOutputStream fouts;
		int a[]=new int[8];
		int j=0,rem=0;
		String sb[]=new String[8];
		String sbf=new String();
		try
		{
			try
			{
				fins=new FileInputStream("BestTimes.vis");	
			}
			catch(FileNotFoundException e)
			{
				JOptionPane.showMessageDialog(this,"    No Best Times Saved"); 
				return;
			}
			do
			{
				i=fins.read();
				if(i!=-1)
				{
					a[j]=i;
					j++;
				}					
			}
			while(i!=-1);
			a[j]=9;
			j++;
			a[j]=9;
			j++;
			a[j]=9;
			j++;	
					
			for(i=0;i<5;i++)
			{
				if(a[i]==9 && a[i+1]==9 && a[i+2]==9)
				{	
					rem=i;	
					break;	
				}
			}					
			for(i=0;i<rem;i++)
			{
				sb[i]=new String();
				sb[i]=String.valueOf(a[i]);	
				sbf=sbf+sb[i];
			}
			int m=Integer.parseInt(sbf);
			JOptionPane.showMessageDialog(this,"      Best Time : "+m+" sec");
			fins.close();
		}
		catch(IOException ioe)
		{
		}
		catch(NullPointerException ne)
		{
		}
	}
}
public class Sudoku
{
	public static void main(String args[])
	{
		Sudoku1 m=new Sudoku1();
		m.setSize(511,555);
		m.setVisible(true);
	}
}