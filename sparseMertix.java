package sprsemartix;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

class focusCount//��Ԫ���
{
		private int col;//�µ��û�ID
		private int row;//�µĹ�עID������ֵ
		private double prvalue;//�����PRֵ
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


public class sparseMartix 
{
	//static DecimalFormat df=new DecimalFormat("0.000");//���������ʽ
	int num=0;
	static int N=150000;
	static int n=N;
	static double k=1.0/N;
	@SuppressWarnings("rawtypes")
	static List<List> arrayList=new ArrayList<>();
	
	
	
	void read()
	{
		try {
			Workbook book=Workbook.getWorkbook(new File("D:\\test.xls")); //*************������
			Sheet sheet=book.getSheet(0);//0�����һ����������
			int rows=sheet.getRows();
			TreeMap<String,Integer>tree=new TreeMap<String,Integer>();
			
			
			String filePath = "d:\\test2.xls";//*****************����д   
			WritableWorkbook wwb;      
			OutputStream os = new FileOutputStream(filePath);   
          	wwb=Workbook.createWorkbook(os);    
         
          	WritableSheet wsheet = wwb.createSheet("sheet", 0); // ��ӵ�һ�����������õ�һ��Sheet������      
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
					String ID=sheet.getCell(0,z).getContents();//0����������z��������
					if(!tree.containsKey(ID))
					{
						tree.put(ID,i++);
					}
				
					String str=  Integer.toString(tree.get(ID));	
					label=new Label(0,z,str);
					wsheet.addCell(label);
              
					String g=sheet.getCell(3,z).getContents();
					label=new Label(1,z,g);
					wsheet.addCell(label);
              
					if (g.compareTo("0")>0) 
					{
						String list=sheet.getCell(4,z).getContents();  //��z�û����ܹ�עID
						String[] b = list.split(",");
						for (int j = 0; j < b.length; j++) 
						{
							if(!tree.containsKey(b[j]))
							{
								tree.put(b[j],i);
								if(j==0)
									substr=Integer.toString(i)+",";
								else 
									if(j==b.length-1)
										substr=substr+Integer.toString(i);
									else
										substr=substr+Integer.toString(i)+",";
								i++;
							}else{
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
				wwb.write(); 
				wwb.close();// �ر��ļ�
			}catch(Exception e){
				e.printStackTrace();
			}
	}	
	
@SuppressWarnings("rawtypes")
void rankCal(double[] rankMartix,double[][] rankResult)//��������
	{
		if(num<1)
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
			rankCal(rankMartix,rankResult);
		}
	}
	


	public static void main(String[] args) throws BiffException, IOException
	{
		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//�������ڸ�ʽ
		System.out.println(date.format(new Date()));// new Date()Ϊ��ȡ��ǰϵͳʱ��
		sparseMartix martix=new sparseMartix();
		//Pattern pattern=Pattern.compile("\\d*");//ԭ�������жϹ�עID�Ƿ��������֣�
	
		martix.read();//���ö��������Ա������µ�Excel��
		Workbook book=Workbook.getWorkbook(new File("D:\\test2.xls"));//��ȡ�µ�Excel��
		Sheet sheet=book.getSheet(0);//0�����һ�����������
		int rows=sheet.getRows();
		/*
		int cols=sheet.getColumns();
		String colname1=sheet.getCell(0,0).getContents().trim();
		String colname2=sheet.getCell(1,0).getContents().trim();
		String colname3=sheet.getCell(3,0).getContents().trim(); 
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
			{//���ڶ���Ϊ�յ�ʱ���Թ�������
				double C=Double.parseDouble(g);
				if(C>0)//c����ڶ��е�ֵ�����ڶ���Ϊ��ʱ��Ҳ���������û�г���
				{
					//System.out.println(ID+","+g);
					String list=sheet.getCell(2,z).getContents();
					List<focusCount> threeValueList=new ArrayList<>();
					String[] b = list.split(",");
					for (int j = 0; j < b.length;j++) 
					{
						//System.out.println("list is: "+list);
						int A=Integer.parseInt(b[j]);
						threeValueList.add(new focusCount(A-1,B-1,C));//��ֵ�ͽ���Ԫ����У������浽List��
					}
					arrayList.add(threeValueList);
				} 	
			}else{
				continue;
			}
				
		}
			 System.out.println("��ȡ���");
			 System.out.println(date.format(new Date()));// new Date()Ϊ��ȡ��ǰϵͳʱ��
			    	
	
			double[] rankMartix=new double[N];//ԭʼrankֵ
			double[][] rankResult=new double[N][2];//������rankֵ
			for(int i=0;i<N;i++)
			{
				rankMartix[i]=k;
			}
			
			martix.rankCal(rankMartix,rankResult);
			System.out.println("�������");
			System.out.println(date.format(new Date()));// new Date()Ϊ��ȡ��ǰϵͳʱ��
			
			
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
		
			System.out.println("�������");
			System.out.println(date.format(new Date()));// new Date()Ϊ��ȡ��ǰϵͳʱ��
			 
			 
			//ֻ���20���˵�rankֵ
			if(N<20)
			{
				n=N;
			}else{
				n=20;
			}
			
			for(int i=0;i<n;i++)//������֮���rankֵ���
			{
				//DecimalFormat df=new DecimalFormat("0.000");
				System.out.println("��"+(i+1)+"��"+rankResult[i][0]);
			}
			//�����㷨
			//��rankֵ��ȵ����
			/*
			 * for(int j=0;j<n;j++)
			{
				boolean bool=true;
				for(int i=0;i<N;i++){
					if(rankResult[i]==sortArray[j]&&bool){
						rankResult[i]=0;
						rankResult[i]-=i;
						bool=false;
						System.out.println(i);
						}
				}
			}
			*/
			System.out.println(date.format(new Date()));// new Date()Ϊ��ȡ��ǰϵͳʱ��
	}
}