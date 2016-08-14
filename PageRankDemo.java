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
import java.util.Scanner;
import java.util.TreeMap;

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
	TextField textfield=new TextField(10);
	TextField textfield1=new TextField(15);
	Button button1=new Button("ȷ��");
	Panel p=new Panel();
	Panel p1=new Panel();
	Panel p2=new Panel();
	TextArea text=new TextArea();
	Button button=new Button("ѡ���ļ�");
	
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
			  
			
			int i=1;
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
              
            String g=sheet.getCell(3,z).getContents();
            label=new Label(1,z,g);
            wsheet.addCell(label);
              
            if(g.compareTo("0")>0) 
            {
				String list=sheet.getCell(4,z).getContents();  //��z�û����ܹ�עID
				String[] b = list.split(",");
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
		//System.out.println(N);
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
			String ID=sheet.getCell(0,z).getContents();//IDΪrow,�û���ע��Ϊ����,�û���ע������Ϊ��ʼprvalue;//0����������z��������
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
						threeValueList.add(new focusCount(A-1,B-1,C));//��ֵ�ͽ���Ԫ����У������浽List��
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
			rankMartix[i]=k;
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
		while(true)
		{
			n = n / 2;
			for(int x=0;x<n;x++)
			{
				for(int i=x+n;i<rankResult.length;i=i+n)
				{
				double temp =rankResult[i][0];
			    int j;
			    for(j=i-n;j>=0&&rankResult[j][0]<temp;j=j-n)
			    {
			    	rankResult[j+n][0] = rankResult[j][0];
			    }
			    rankResult[j+n][0] = temp;
			    }
			 }
			  if(n == 1)
			  {
			       break;
			  }
		}
		
	}
	public void init()
	{
		button.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) 
			{	
				// TODO Auto-generated method stub
				
				file.setVisible(true);
				text.setFont(new Font("����",1,15));
				text.setForeground(Color.RED);
				filepath=file.getDirectory()+file.getFile();
				textfield1.setText(filepath);
				
				
				button1.addActionListener(new ActionListener()
				{

					@Override
					public void actionPerformed(ActionEvent e)
					{
						// TODO Auto-generated method stub
						text.append("�ļ���ȡ��� - ");
						text.append(date.format(new Date())+"\n");
						text.append(filepath+"\n");
						String value=textfield.getText();
						number=Integer.valueOf(value);
						text.append("��ʼ�������� - ");
						text.append(date.format(new Date())+"\n");
						read();
						try {
							readNew();
						} catch (BiffException | IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						initial();
						text.append("��ʼ����pagerankֵ - ");
						text.append(date.format(new Date())+"\n\n");
						rankCal();
						sort();
						/*
						for(int i=0;i<N;i++)
						{
							System.out.println(rankResult[i][0]+"  "+rankResult[i][1]+"  "+rankMartix[i]);
						}
						*/
						//ֻ���ǰ20���˵�pagerankֵ
						if(N<20)
						{
							n=N;
						}else{
							n=20;
						}
						text.append("�����Ľ����\n");
						for(int i=0;i<n;i++)
						{
							Iterator it = tree.keySet().iterator(); 
							//�������Ҷ�Ӧ����ʵID�����
							while (it.hasNext())                
							if(rankResult[i][1]==tree.get(it.next()))
							{
								text.append("��"+(i+1)+"��"+it.next()+"\n");
								text.append(String.valueOf(rankResult[i][0])+"\n");
								// text.append(String.valueOf(rankResult[i][1])+"\n");
							} 
					
						}
						text.append(date.format(new Date())+"\n");
						
					}
			
				});
				//button1 Listener END
			}
			
		});
		//button Listener END
		textfield.setText("�����������");
		frame.setSize(400,500);
		button.setSize(200, 100);
		//textfield.setSize(300,100);
		
		p1.add(textfield1);
		p1.add(button);
		
		p2.add(textfield);
		p2.add(button1);
		
		p.add(p1);
		p.add(p2);
		text.setSize(300,400);
		frame.addWindowListener(new MyListen());
		frame.add(p,BorderLayout.NORTH);
		frame.add(text,BorderLayout.CENTER);
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
