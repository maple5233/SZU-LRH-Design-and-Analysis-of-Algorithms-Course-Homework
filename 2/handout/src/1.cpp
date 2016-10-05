/*二维最近点对问题-分治法*/
#include <iostream>
#include <cmath>
#include <stdlib.h> 
#include <time.h>  
#include <stdio.h>
using namespace std;
const int M = 100000; //最大问题规模;

/*用类PointX和PointY表示依x坐标和y坐标排好序的点*/  
class PointX {
public:
	/*比较器*/
	bool operator<=(PointX a)const
	{
		return (x <= a.x);
	}
	int ID; //点编号  
	double x, y; //点坐标   
};
class PointY {
public:
	bool operator<=(PointY a)const
	{
		return(y <= a.y);
	}
	int p; //同一点在数组x中的坐标排好序后的序号（方便从中间切割）
	double x, y; //点坐标  
};
/*解决问题的类*/
class Graph
{
private:
	/*基本属性*/
	int size;//点的数量
	PointX X[M];//点对数组
	PointX a;//最近点对其中一个点
	PointX b;//最近点对另外一个点
	double d;//最近点对的距离
	/*计算距离和拷贝数组*/
	template <class Type> double dis(const Type&u, const Type&v)
	{//返回两个点的距离 Type可以是PointX或者PointY
		double dx = u.x - v.x;
		double dy = u.y - v.y;
		return sqrt(dx*dx + dy*dy);
	}
	template <class Type> void Copy(Type a[], Type b[], int left, int right)
	{//拷贝数组b至a Type可以是PointX或者PointY
		int i;
		for (i = left; i <= right; i++)
			a[i] = b[i];
	}
	/*归并排序 此处采用也是分治法的应用的归并排序 其实用其他排序方法也可以 
	由于对不同类定义好了对应的比较器 Type为PointX时按x坐标排序 为PointY时按照y坐标来排序*/
	template <class Type> void Merge(Type c[], Type d[], int l, int m, int r)
	{//c d合并为有序的d
		int i = l, j = m + 1, k = l;
		while ((i <= m) && (j <= r))
		{
			if (c[i] <= c[j])
			{
				d[k++] = c[i++];
			}
			else
			{
				d[k++] = c[j++];
			}
		}
		// 合并剩余的部分
		if (i>m)
		{
			for (int q = j; q <= r; q++)
			{
				d[k++] = c[q];
			}
		}
		else
		{
			for (int q = i; q <= m; q++)
			{
				d[k++] = c[q];
			}
		}
	}
	template <class Type>  void MergeSort(Type a[], Type b[], int left, int right)
	{//归并排序
		if (left<right)
		{
			int i = (left + right) / 2;
			MergeSort(a, b, left, i);
			MergeSort(a, b, i + 1, right);//分割子序列
			Merge(a, b, left, i, right);//合并到数组b  
			Copy(a, b, left, right);//复制回数组a         
		}
	}
	/*求最小点对*/
	void closest(PointX X[], PointY Y[], PointY Z[], int l, int r, PointX& a, PointX& b, double& d)
	{
		int i, j;
		if (r - l == 1) //两点的情形 递归基 
		{
			a = X[l];
			b = X[r];
			d = dis(X[l], X[r]);
			return;
		}

		if (r - l == 2) //三点的情形 递归基
		{
			double d1 = dis(X[l], X[l + 1]);
			double d2 = dis(X[l + 1], X[r]);
			double d3 = dis(X[l], X[r]);

			if (d1 <= d2 && d1 <= d3)
			{
				a = X[l];
				b = X[l + 1];
				d = d1;
				return;
			}

			if (d2 <= d3)
			{
				a = X[l + 1];
				b = X[r];
				d = d2;
			}
			else {
				a = X[l];
				b = X[r];
				d = d3;
			}
			return;
		}

		//多于3点的情形，用分治法 递归到递归基为止  
		int m = (l + r) / 2;//为了编码方便起见 这里用头尾平均数 实际应该用中位数
		int f = l, g = m + 1;

		//在算法预处理阶段Cpair2()，已经将数组X中的点依x坐标排序，将数组Y中的点依y坐标排序  
		//算法分割阶段，将子数组X[l:r]均匀划分成两个不相交的子集，取m=(l+r)/2  
		//X[l:m]和X[m+1:r]就是满足要求的分割。  
		for (i = l; i <= r; i++)
		{
			if (Y[i].p>m) 
				Z[g++] = Y[i];//右半部分为l,m
			else 
				Z[f++] = Y[i];//左半部分为m+1,r
		}

		closest(X, Z, Y, l, m, a, b, d);//求解X[l:m] 把分好的Z当作Y来用 Z和Y交替循环使用
	

		PointX ar, br;
		double dr;
		closest(X, Z, Y, m + 1, r, ar, br, dr);//求解X[m+1:r] 把分好的Z当作Y来用 Z和Y交替循环使用

		if (dr<d)
		{
			a = ar;
			b = br;
			d = dr;
		}//比较两个子问题的解

		/*以上求得两边的最近点对 以下合并解*/
		Merge(Z, Y, l, m, r);//重构数组Y  
		//对P1中所有点p，对排好序的点列作一次扫描
		//就可以找出所有最接近点对的候选者，对P1中每一点最多只要检查P2中排好序的相继6个点。

		//d矩形条内的点置于Z[l:k-1]中  
		int k = l;
		for (i = l; i <= r; i++)
		{
			if (fabs(X[m].x - Y[i].x)<d)//fabs()是求浮点数的绝对值
			{
				Z[k++] = Y[i];
			}
		}

		
		//搜索Z[l:k-1]  这种搜索一趟最多只有6个点
		for ( i = l; i<k; i++)
		{//对于每个p
			for ( j = i + 1; j<k && Z[j].y - Z[i].y<d; j++)
			{
				double dp = dis(Z[i], Z[j]);
				if (dp<d)
				{
					d = dp;
					a = X[Z[i].p];
					b = X[Z[j].p];
				}
			}
		}
	}
	bool Cpair2(int n)//n是本次比较的问题的点的个数
	{//预处理
		int i;
		if (n<2) return false;
		PointX* tmpX = new PointX[n];
		MergeSort(X, tmpX, 0, n - 1);//按x来排序

		PointY* Y = new PointY[n];
		for (i = 0; i<n; i++) //将数组X中的点复制到数组Y中  
		{
			Y[i].p = i;
			Y[i].x = X[i].x;
			Y[i].y = X[i].y;
		}

		PointY* tmpY = new PointY[n];
		MergeSort(Y, tmpY, 0, n - 1);//数组Y按y来排序
		//tmpX tmpY 都是归并排序所需要的临时数组

		PointY* Z = new PointY[n];
		closest(X, Y, Z, 0, n - 1, a, b, d);//求解问题

		delete[]Y;
		delete[]Z;
		delete[]tmpX;
		delete[]tmpY;
		return true;
	}
public:
	void initX()
	{
		int i;
		srand((unsigned)time(NULL));
		cout << "请输入点对数："<<endl;
		cin >> this->size;
		for (i = 0; i<this->size; i++)
		{
			X[i].ID = i;//编号
			X[i].x=rand()%100000;//随机数范围0-100000;
			X[i].y=rand()%100000;
			// cout << X[i].x<<"-"<<X[i].y<<endl;
		}
	}
	void display()
	{
		Cpair2(size);
		cout << "最邻近点对为：(" << a.x << "," << a.y << ")和(" << b.x << "," << b.y << ") " << endl;
		cout << "最邻近距离为： " << d << endl;
	}
};
int main()
{
	clock_t t_start, t_end;
	Graph gr;
	gr.initX();
	t_start = clock();
	gr.display();
	t_end = clock();
	cout << "Runtime： " << (double)(t_end- t_start ) * 1000.0 / CLOCKS_PER_SEC << " ms!" << endl;
	return 0;
}