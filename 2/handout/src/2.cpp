/*二维最近点对问题-蛮力法*/
#include <iostream>
#include <cmath>
#include <stdlib.h> 
#include <time.h>  
using namespace std;
const int M = 100000; //最大问题规模;
class Point {
public:
	double x, y; //点坐标   
};
class Graph
{
public:
	int size;//点的数量
	Point X[M];//点对数组
	Point a;//最近点对其中一个点
	Point b;//最近点对另外一个点
	double d;//最近点对的距离	
	double dis(const Point&u, const Point&v)
	{//返回两个点的距离 
		double dx = u.x - v.x;
		double dy = u.y - v.y;
		return sqrt(dx*dx + dy*dy);
	}
	/*求最小点对*/
	void closest()
	{
		a=X[0];
		b=X[1];
		d=dis(a,b);
		for (int i = 0; i < size; ++i)
		{
			for (int j = i+1; j < size; ++j)
			{
				int nd=dis(X[i],X[j]);
				if(nd < d)
				{
					a=X[i];
					b=X[j];
					d=nd;
				}
			}
		}
	}
	void initX()
	{
		int i;
		srand((unsigned)time(NULL));
		cout << "请输入点对数："<<endl;
		cin >> this->size;
		for (i = 0; i<this->size; i++)
		{
			X[i].x=rand()%100000; // 随机数在0-100000之间;
			X[i].y=rand()%100000;
			// cout << X[i].x<<"-"<<X[i].y<<endl;
		}
	}
	void display()
	{
		closest();
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