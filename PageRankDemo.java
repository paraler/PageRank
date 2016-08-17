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


class focusCount//三元组表
{
		//新的用户ID
		private int col;
		//新的关注ID的索引值
		private int row;
		//分配的PR值
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
		public String toString()//toString方法的重载
		{
			DecimalFormat df=new DecimalFormat("0.000");
			return "[col="+col+" row="+row+" prvalue="+prvalue+"]\n";
		}
		*/
}

public class PageRankDemo
{
	//关于输入界面的年变量控制
	Frame frame=new Frame("pagerank");
	FileDialog file=new FileDialog(frame,"打开文件",FileDialog.LOAD);
	//TextField textfield=new TextField(10);
	String str1[] = {"1", "2", "3", "4","5","6"};  
	@SuppressWarnings({ "unchecked", "rawtypes" })
	JComboBox jcb=new JComboBox(str1);
	JLabel label=new JLabel("迭代次数");
	TextField textfield1=new TextField(15);
	Button button1=new Button("确认");
	Panel p=new Panel();
	Panel p1=new Panel();
	Panel p2=new Panel();
	TextArea text=new TextArea();
	Button button=new Button("选择文件");
	
	
	//最后输出到表格
	JTable table;
	
	//关于后台计算的变量控制
	//设置日期格式
	SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static DecimalFormat df=new DecimalFormat("0.000");//控制输出格式
	//迭代次数
	static int number;
	//迭代计算的变量
	static int num=0;
	//排序时的中间变量
	static int n;
	//数据一共计算的人数
	static int N;
	//迭代数组的初始值
	static double k;
	//保存文件路径的变量
	String filepath;
	//保存数据的三元组表
	@SuppressWarnings("rawtypes")
	static List<List> arrayList=new ArrayList<>();
	//保存原始数据的映射
	TreeMap<String,Integer>tree=new TreeMap<String,Integer>();  
	//原始rank值
	double[] rankMartix;
	//计算后的rank值
	double[][] rankResult;
	String str;
	
	
	//读取数据的方法
	public TreeMap<String,Integer> read()
	{
		//TreeMap<String,Integer>tree=new TreeMap<String,Integer>();  
		try 
		{
			/*
			@SuppressWarnings("resource")
			Scanner scanner=new Scanner(System.in);//**************创建读
			System.out.println("输入你想迭代的次数：");
			String it=scanner.next();
			number=Integer.parseInt(it);
			*/
			Workbook book=Workbook.getWorkbook(new File(filepath));
			Sheet sheet=book.getSheet(0);//0代表第一个工作表对象
			int rows=sheet.getRows();
			String filePath = "d:\\test2.xls";   //*****************创建写
            WritableWorkbook wwb;      
            OutputStream os = new FileOutputStream(filePath);   
            wwb=Workbook.createWorkbook(os);   
            WritableSheet wsheet = wwb.createSheet("sheet", 0);  // 添加第一个工作表并设置第一个Sheet的名字        
			Label label=new Label(0,0,"ID");
			wsheet.addCell(label);
			label=new Label(1,0,"g");
			wsheet.addCell(label);
			label=new Label(2,0,"list");
			wsheet.addCell(label);  //写标题
			  
			
			int i=0;
            String substr=null;
            for(int z=1;z<rows;z++)
			{
				//0代表列数，z代表行数
				String ID=sheet.getCell(0,z).getContents();
				if(!tree.containsKey(ID))
				{
 					tree.put(ID,i++);
 					N++;
				}
				
			String str=Integer.toString(tree.get(ID));	
            label=new Label(0,z,str);
            wsheet.addCell(label);
              
            String list=sheet.getCell(4,z).getContents();  //第z用户的总关注ID
            String[] b = list.split(",");
            
            String g=sheet.getCell(3,z).getContents();
            //筛去不符合条件的数据，当大数据时b[0].length()<9，当小数据时<1
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
					
		}//读入数据结束
		
		//输出总共处理的数据	
		System.out.println(N);
		wwb.write(); 
		// 关闭文件   
	    wwb.close();   
		}catch(Exception e){
			e.printStackTrace();
		}
		return tree;
	}
	
	//将新生成的表里的数据读进来
	public void readNew() throws BiffException, IOException
{
		Workbook book=Workbook.getWorkbook(new File("D:\\test2.xls"));//读取新的Excel表
		Sheet sheet=book.getSheet(0);//0代表第一个工作表对象
		int rows=sheet.getRows();
		/* 
		//测试
		int cols=sheet.getColumns();
		String colname1=sheet.getCell(0,0).getContents().trim();
		String colname2=sheet.getCell(1,0).getContents().trim();
		System.out.println(colname1+"\t"+colname2);
		*/
		 for(int z=1;z<rows;z++)
		{
			 //ID为row,用户关注数为行数,用户关注的人数为初始prvalue;
			 //0代表列数，z代表行数
			String ID=sheet.getCell(0,z).getContents();
			//System.out.print("ID is:  "+ID+"\t");
			int B=Integer.parseInt(ID);
			String g=sheet.getCell(1,z).getContents();
			//System.out.print("g is: "+g+" ;\t");
			if(g!=null&&!g.isEmpty())
			{
				//当第二列为空的时候，略过不处理
				double C=Double.parseDouble(g);
				if(C>0)//c代表第二列的值，当第二列为零时，也就是这个人没有出度
				{
					//System.out.println(ID+","+g);
					String list=sheet.getCell(2,z).getContents();
					List<focusCount> threeValueList=new ArrayList<>();
					String[] b = list.split(",");
					for (int j = 0; j < b.length;j++) 
					{
						//System.out.println("list is: "+b[j]);
						int A=Integer.parseInt(b[j]);
						threeValueList.add(new focusCount(A,B,C));//将值送进三元组表中，并保存到List中
					}
					arrayList.add(threeValueList);
				} 	
			}else{
				continue;
			}
				
		}
	}
	
	//初始化矩阵中用到的数据
	public void initial()
{
		double k=1.0/N;//迭代数组的初始值
		rankMartix=new double[N];//原始rank值
		rankResult=new double[N][2];//计算后的rank值
		//输出原始的迭代数组的值
		//System.out.println(k);
		for(int i=0;i<N;i++)
		{
			rankMartix[i]=1.0;
		}
	}
	
	//开始迭代计算
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
	
	//迭代完的结果排序
	public void sort()
	{
		n=N;
		//排序输出，希尔排序，复杂度为lognN
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
	
	//搜索昵称的函数
	public String searchname(Object k) 
	{    String name=null;
		// TODO Auto-generated method stub
    try{
		Workbook book=Workbook.getWorkbook(new File(filepath));
		Sheet sheet=book.getSheet(0);//0代表第一个工作表对象
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
		//数据以表格形式输出
			String str;
			Object[][] tableData=new Object[n][4];
			for(int i=0;i<n;i++)
			{
				@SuppressWarnings("rawtypes")
				Iterator it = tree.keySet().iterator(); 
				//遍历查找对应的真实ID
				while (it.hasNext())
				{
					Object k=it.next();
					if(rankResult[i][1]==tree.get(k))
					{
						//输出排名
						//text.append("第"+(i+1)+"名"+it.next()+"\n");
						//text.append(String.valueOf(rankResult[i][0])+"\n");
						tableData[i][0]=i+1;
						tableData[i][1]=rankResult[i][0];
						tableData[i][2]=k;
						str=searchname(k);
						if (str!=null)
							tableData[i][3]=str;
						else
							tableData[i][3]="无";
					} 
				}
			
			}
			Object[] columnTitle={"名次","PR值","用户ID","昵称"};
			table=new JTable(tableData,columnTitle);
			//名次列的宽度
			TableColumn rankwidth=table.getColumn(columnTitle[0]);
			rankwidth.setMaxWidth(50);
			//PR值列的宽度
			TableColumn prwidth=table.getColumn(columnTitle[1]);
			prwidth.setMinWidth(150);
			//用户ID的宽度
			TableColumn idwidth=table.getColumn(columnTitle[2]);
			idwidth.setPreferredWidth(80);
			//y用户昵称的宽度
			TableColumn nickwidth=table.getColumn(columnTitle[3]);
			nickwidth.setPreferredWidth(120);
			
			Frame tf=new Frame("PageRank排名前20");
			tf.add(new JScrollPane(table));
			tf.setVisible(true);
			tf.pack();
			tf.setSize(400,500);
			tf.setResizable(true);
			tf.setLocation(900, 200);
			//此处点击表格的关闭时，两个图形框都会关闭
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
				text.setFont(new Font("黑体",1,12));
				text.setForeground(Color.black);
				file.setVisible(true);
				filepath=file.getDirectory()+file.getFile();
				textfield1.setText(filepath);
				text.append("文件读取完毕 - ");
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
						text.append("开始读入数据 - ");
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
						text.append("开始计算pagerank值 - ");
						text.append(date.format(new Date())+"\n\n");
						rankCal();
						sort();
						
						//只输出前20个人的pagerank值
						
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
						text.append("计算后的结果：\n");
						
						for(int i=0;i<n;i++)
						{
							@SuppressWarnings("rawtypes")
							Iterator it = tree.keySet().iterator(); 
							//遍历查找对应的真实ID
							while (it.hasNext())    
							{
								Object k=it.next();
								if(rankResult[i][1]==tree.get(k))
								{
									//输出排名
									String str=searchname(k);
								   if (str!=null)
									text.append("第"+(i+1)+"名"+str+"\n");
								   else
									   text.append("第"+(i+1)+"名"+k+"\n");
								   
									text.append(String.valueOf(rankResult[i][0])+"\n");
									// text.append(String.valueOf(rankResult[i][1])+"\n");
								} 
							}
						}

						text.append(date.format(new Date())+"\n");
						JOptionPane.showMessageDialog(null, "计算完毕！");
					}
			
				});
				//Button1 Listener END
			}
			

		});
		//Button END
		
		
		
		//	textfield.setText("输入迭代次数");
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
		
		//设置窗口属性
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