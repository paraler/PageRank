package PageRankDemo;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
//import java.util.Scanner;
import java.util.TreeMap;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

//import PageRank.focusCount;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;


class focusCount//��Ԫ���
{
		//�µ��û�ID
		private int col;
		//�µĹ�עID������ֵ
		private int row;
		//�����PRֵ
		private double prvalue;
		public focusCount(){}
		public focusCount(int col,int row,double prvalue)
		{
			this.col=col;
			this.row=row;
			this.prvalue=1/prvalue;
		} 
		public int getCol(){
			return this.col;
		} 
		public int getRow(){
			return this.row;
		} 
		public double getPrvalue(){
			return prvalue;
		}
		/*
		public String toString()//toString����������
		{
			DecimalFormat df=new DecimalFormat("0.000");
			return "[col="+col+" row="+row+" prvalue="+prvalue+"]\n";
		}
		*/
}

public class PageRankDemo
{
	//���������������������
	Frame frame=new Frame("pagerank");
	FileDialog file=new FileDialog(frame,"���ļ�",FileDialog.LOAD);
	//TextField textfield=new TextField(10);
	String str1[] = {"1", "2", "3", "4","5","6"};  
	@SuppressWarnings({ "unchecked", "rawtypes" })
	JComboBox jcb=new JComboBox(str1);
	JLabel label=new JLabel("��������");
	TextField textfield1=new TextField(15);
	Button button1=new Button("ȷ��");
	Panel p=new Panel();
	Panel p1=new Panel();
	Panel p2=new Panel();
	TextArea text=new TextArea();
	Button button=new Button("ѡ���ļ�");
	
	
	//�����������
	JTable table;
	
	//���ں�̨����ı�������
	//�������ڸ�ʽ
	SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static DecimalFormat df=new DecimalFormat("0.000");//���������ʽ
	//��������
	static int number;
	//��������ı���
	static int num=0;
	//����ʱ���м����
	static int n;
	//����һ�����������
	static int N;
	//��������ĳ�ʼֵ
	static double k;
	//�����ļ�·���ı���
	String filepath;
	//�������ݵ���Ԫ���
	@SuppressWarnings("rawtypes")
	static List<List> arrayList=new ArrayList<>();
	//����ԭʼ���ݵ�ӳ��
	TreeMap<String,Integer>tree=new TreeMap<String,Integer>();  
	//ԭʼrankֵ
	double[] rankMartix;
	//������rankֵ
	double[][] rankResult;
	String str;
	
	
	//��ȡ���ݵķ���
	public TreeMap<String,Integer> read()
	{
		//TreeMap<String,Integer>tree=new TreeMap<String,Integer>();  
		try 
		{
			/*
			@SuppressWarnings("resource")
			Scanner scanner=new Scanner(System.in);//**************������
			System.out.println("������������Ĵ�����");
			String it=scanner.next();
			number=Integer.parseInt(it);
			*/
			Workbook book=Workbook.getWorkbook(new File(filepath));
			Sheet sheet=book.getSheet(0);//0�����һ�����������
			int rows=sheet.getRows();
			String filePath = "d:\\test2.xls";   //*****************����д
            WritableWorkbook wwb;      
            OutputStream os = new FileOutputStream(filePath);   
            wwb=Workbook.createWorkbook(os);   
            WritableSheet wsheet = wwb.createSheet("sheet", 0);  // ��ӵ�һ�����������õ�һ��Sheet������        
			Label label=new Label(0,0,"ID");
			wsheet.addCell(label);
			label=new Label(1,0,"g");
			wsheet.addCell(label);
			label=new Label(2,0,"list");
			wsheet.addCell(label);  //д����
			  
			
			int i=0;
            String substr=null;
            for(int z=1;z<rows;z++)
			{
				//0����������z��������
				String ID=sheet.getCell(0,z).getContents();
				if(!tree.containsKey(ID))
				{
 					tree.put(ID,i++);
 					N++;
				}
				
			String str=Integer.toString(tree.get(ID));	
            label=new Label(0,z,str);
            wsheet.addCell(label);
              
            String list=sheet.getCell(4,z).getContents();  //��z�û����ܹ�עID
            String[] b = list.split(",");
            
            String g=sheet.getCell(3,z).getContents();
            //ɸȥ���������������ݣ���������ʱb[0].length()<9����С����ʱ<1
            if((list.contains("#"))||(b[0].length()<9))
            	g="0";
            
            label=new Label(1,z,g);
            wsheet.addCell(label);
            
            
       	   
       	    
            if(g.compareTo("0")>0)
            {
			  
				
			    for (int j = 0; j < b.length; j++) 
			    {  
			       
			    	if(!tree.containsKey(b[j]))
					{
	   					tree.put(b[j],i);
	   					N++;
	   					if(j==0)
	   					substr=Integer.toString(i)+",";
	   					else 
	   					if(j==b.length-1)
	   					substr=substr+Integer.toString(i);
	   					else
	   					substr=substr+Integer.toString(i)+",";
	   	                i++;
					}
					else
					{
						str=  Integer.toString(tree.get(b[j]));
					    if(j==0)
	   					substr=str+",";
	   					else 
	   					if(j==b.length-1)
	   					substr=substr+str;
	   					else
	   					substr=substr+str+",";
					}
				  }
			    
			    label=new Label(2,z,substr);
				wsheet.addCell(label);
				substr=null;
            }
					
		}//�������ݽ���
		
		//����ܹ����������	
		System.out.println(N);
		wwb.write(); 
		// �ر��ļ�   
	    wwb.close();   
		}catch(Exception e){
			e.printStackTrace();
		}
		return tree;
	}
	
	//�������ɵı�������ݶ�����
	public void readNew() throws BiffException, IOException
{
		Workbook book=Workbook.getWorkbook(new File("D:\\test2.xls"));//��ȡ�µ�Excel��
		Sheet sheet=book.getSheet(0);//0�����һ�����������
		int rows=sheet.getRows();
		/* 
		//����
		int cols=sheet.getColumns();
		String colname1=sheet.getCell(0,0).getContents().trim();
		String colname2=sheet.getCell(1,0).getContents().trim();
		System.out.println(colname1+"\t"+colname2);
		*/
		 for(int z=1;z<rows;z++)
		{
			 //IDΪrow,�û���ע��Ϊ����,�û���ע������Ϊ��ʼprvalue;
			 //0����������z��������
			String ID=sheet.getCell(0,z).getContents();
			//System.out.print("ID is:  "+ID+"\t");
			int B=Integer.parseInt(ID);
			String g=sheet.getCell(1,z).getContents();
			//System.out.print("g is: "+g+" ;\t");
			if(g!=null&&!g.isEmpty())
			{
				//���ڶ���Ϊ�յ�ʱ���Թ�������
				double C=Double.parseDouble(g);
				if(C>0)//c����ڶ��е�ֵ�����ڶ���Ϊ��ʱ��Ҳ���������û�г���
				{
					//System.out.println(ID+","+g);
					String list=sheet.getCell(2,z).getContents();
					List<focusCount> threeValueList=new ArrayList<>();
					String[] b = list.split(",");
					for (int j = 0; j < b.length;j++) 
					{
						//System.out.println("list is: "+b[j]);
						int A=Integer.parseInt(b[j]);
						threeValueList.add(new focusCount(A,B,C));//��ֵ�ͽ���Ԫ����У������浽List��
					}
					arrayList.add(threeValueList);
				} 	
			}else{
				continue;
			}
				
		}
	}
	
	//��ʼ���������õ�������
	public void initial()
{
		double k=1.0/N;//��������ĳ�ʼֵ
		rankMartix=new double[N];//ԭʼrankֵ
		rankResult=new double[N][2];//������rankֵ
		//���ԭʼ�ĵ��������ֵ
		//System.out.println(k);
		for(int i=0;i<N;i++)
		{
			rankMartix[i]=1.0;
		}
	}
	
	//��ʼ��������
	@SuppressWarnings("rawtypes")
	public void rankCal()
	{
		if(num<number)
		{
			num++;
			if(num==1)
			{
				
			}else{
				for(int i=0;i<N;i++)
				{
					rankMartix[i]=rankResult[i][0];
					rankResult[i][0]=0;
				}
			}
			for(int col=0;col<=N;col++)
			{
				for(List obj: arrayList)
				{
					for(Object value: obj)
					{
						focusCount a=new focusCount();
						a=(focusCount) value;
						if(col==a.getCol())
						{
						int i=a.getRow();
						rankResult[col][0]+=a.getPrvalue()*rankMartix[i];
						rankResult[col][1]=col;
						//System.out.println(" "+df.format(rankResult[col])+"  "+df.format(a.getPrvalue())+"  "+df.format(rankMartix[i]));
						}
					}
				}
				//System.out.println("============================================");
			}
			rankCal();
		}
	}
	
	//������Ľ������
	public void sort()
	{
		n=N;
		//���������ϣ�����򣬸��Ӷ�ΪlognN
		//;;System.out.println(rankResult.length);
		while(true)
		{
			n = n / 2;
			for(int x=0;x<n;x++)
			{
				for(int i=x+n;i<rankResult.length;i=i+n)
				{
				double temp =rankResult[i][0];
				double temp1 =rankResult[i][1];
			    int j;
			    for(j=i-n;j>=0&&rankResult[j][0]<temp;j=j-n)
			    {
			    	rankResult[j+n][0] = rankResult[j][0];
			    	rankResult[j+n][1]=rankResult[j][1];
			    }
			    rankResult[j+n][0] = temp;
			    rankResult[j+n][1]=temp1;
			    }
			 }
			  if(n == 1)
			  {
			       break;
			  }
		}
		
	}
	
	//�����ǳƵĺ���
	public String searchname(Object k) 
	{    String name=null;
		// TODO Auto-generated method stub
    try{
		Workbook book=Workbook.getWorkbook(new File(filepath));
		Sheet sheet=book.getSheet(0);//0�����һ�����������
		int rows=sheet.getRows();
		 for(int z=1;z<rows;z++)
			{
			 String ID=sheet.getCell(0,z).getContents();
			  if (ID.equals((String)k))
			  {
			  name=sheet.getCell(1,z).getContents();
			//  System.out.println("!!!");
			  }
			}
     }catch(Exception e){
			e.printStackTrace();
		}
		return name;
	}
	
	
	public void outPut(){
		//�����Ա����ʽ���
			String str;
			Object[][] tableData=new Object[n][4];
			for(int i=0;i<n;i++)
			{
				@SuppressWarnings("rawtypes")
				Iterator it = tree.keySet().iterator(); 
				//�������Ҷ�Ӧ����ʵID
				while (it.hasNext())
				{
					Object k=it.next();
					if(rankResult[i][1]==tree.get(k))
					{
						//�������
						//text.append("��"+(i+1)+"��"+it.next()+"\n");
						//text.append(String.valueOf(rankResult[i][0])+"\n");
						tableData[i][0]=i+1;
						tableData[i][1]=rankResult[i][0];
						tableData[i][2]=k;
						str=searchname(k);
						if (str!=null)
							tableData[i][3]=str;
						else
							tableData[i][3]="��";
					} 
				}
			
			}
			Object[] columnTitle={"����","PRֵ","�û�ID","�ǳ�"};
			table=new JTable(tableData,columnTitle);
			//�����еĿ��
			TableColumn rankwidth=table.getColumn(columnTitle[0]);
			rankwidth.setMaxWidth(50);
			//PRֵ�еĿ��
			TableColumn prwidth=table.getColumn(columnTitle[1]);
			prwidth.setMinWidth(150);
			//�û�ID�Ŀ��
			TableColumn idwidth=table.getColumn(columnTitle[2]);
			idwidth.setPreferredWidth(80);
			//y�û��ǳƵĿ��
			TableColumn nickwidth=table.getColumn(columnTitle[3]);
			nickwidth.setPreferredWidth(120);
			
			Frame tf=new Frame("PageRank����ǰ20");
			tf.add(new JScrollPane(table));
			tf.setVisible(true);
			tf.pack();
			tf.setSize(400,500);
			tf.setResizable(true);
			tf.setLocation(900, 200);
			//�˴�������Ĺر�ʱ������ͼ�ο򶼻�ر�
			tf.addWindowListener(new MyListen());
	}
	
	
	public void init()
	{
		button.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) 
			{	
				// TODO Auto-generated method stub
				text.setFont(new Font("����",1,12));
				text.setForeground(Color.black);
				file.setVisible(true);
				filepath=file.getDirectory()+file.getFile();
				textfield1.setText(filepath);
				text.append("�ļ���ȡ��� - ");
				text.append(date.format(new Date())+"\n");
				text.append(filepath+"\n");

				
				       button1.addActionListener(new ActionListener()
				       {

					    @Override
					    public void actionPerformed(ActionEvent e)
				      	{
						// TODO Auto-generated method stub
						String value=jcb.getSelectedItem().toString();
						number=Integer.valueOf(value);
						text.append("��ʼ�������� - ");
						text.append(date.format(new Date())+"\n");
						
						read();
						try {
							readNew();
						} 
						catch (BiffException e1)
						{
						    e1.printStackTrace();
						}
						catch (IOException  e2) 
						{
						    e2.printStackTrace();
						}
						
						
						initial();
						text.append("��ʼ����pagerankֵ - ");
						text.append(date.format(new Date())+"\n\n");
						rankCal();
						sort();
						
						//ֻ���ǰ20���˵�pagerankֵ
						
						if(N<20)
						{
							n=N;
						}else{
							n=20;
						}
						
						for(int i=0;i<n;i++){
							System.out.println(rankResult[i][0]+"  "+rankResult[i][1]);
						}
						outPut();
						text.append("�����Ľ����\n");
						
						for(int i=0;i<n;i++)
						{
							@SuppressWarnings("rawtypes")
							Iterator it = tree.keySet().iterator(); 
							//�������Ҷ�Ӧ����ʵID
							while (it.hasNext())    
							{
								Object k=it.next();
								if(rankResult[i][1]==tree.get(k))
								{
									//�������
									String str=searchname(k);
								   if (str!=null)
									text.append("��"+(i+1)+"��"+str+"\n");
								   else
									   text.append("��"+(i+1)+"��"+k+"\n");
								   
									text.append(String.valueOf(rankResult[i][0])+"\n");
									// text.append(String.valueOf(rankResult[i][1])+"\n");
								} 
							}
						}

						text.append(date.format(new Date())+"\n");
						JOptionPane.showMessageDialog(null, "������ϣ�");
					}
			
				});
				//Button1 Listener END
			}
			

		});
		//Button END
		
		
		
		//	textfield.setText("�����������");
		frame.setSize(400,500);
		button.setSize(200, 100);
		//textfield.setSize(300,100);
		
		p1.add(textfield1);
		p1.add(button);
		
		p2.add(label);
		p2.add(jcb);
		p2.add(button1);
		
		p.add(p1);
		p.add(p2);
		
		//���ô�������
		text.setSize(300,400);
		frame.addWindowListener(new MyListen());
		frame.add(p,BorderLayout.NORTH);
		frame.add(text,BorderLayout.CENTER);
		//frame.add(table,BorderLayout.SOUTH);
		frame.setLocation(500, 200);
		frame.setVisible(true);
	}
	
	class MyListen extends WindowAdapter
	{
		public void windowClosing(WindowEvent e)
		{
			System.exit(0);
		}
	}
	public static void main(String[] args)
	{
		
		PageRankDemo pagerank=new PageRankDemo();
		pagerank.init();
	}
}