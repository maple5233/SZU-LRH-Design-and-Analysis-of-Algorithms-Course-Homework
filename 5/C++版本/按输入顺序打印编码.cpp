//构建huffman树的过程中有大量增删操作 又不需要考虑元素的顺序和位置 因此链表来存放森林最佳 但是OJ系统不支持LIST库 只好改用数组
#include<string>
#include<iostream>
using namespace std;

#define datatype myInt //数据类型
#define binNodePosi binNode*   //结点位置
#define HasLChild(x) ((x).lChild)//有左子树
#define HasRChild(x) ((x).rChild)//有右子树
#define HasChild(x) (HasLChild(x)||HasRChild(x)) //至少拥有一个孩子
#define IsLeaf(x) (!HasChild(x))//是叶子
#define IsRoot(x) (!((x).parent))//是树根

class myInt
{
public:
	int weight;//权重
	int index;//输入的顺序
	myInt(int w = 0, int in = 0)
	{
		weight = w;
		index = in;
	}
};//节点数据类
class myTable
{
public:
	myInt data;
	string code;
	myTable()
	{
		myInt a(0, 0);
		string co = "";
		data = a;
		code = co;
	}
	myTable(myInt data, string code)
	{
		this->data = data;
		this->code = code;
	}
};//词典类
class binNode
{
public:
	binNodePosi parent;
	binNodePosi lChild;
	binNodePosi rChild;
	datatype data;
	binNode(datatype dat, binNodePosi p = NULL, binNodePosi l = NULL, binNodePosi r = NULL)
		:data(dat), parent(p), lChild(l), rChild(r){}
};
class binTree
{
public:
	int _size;
	binNodePosi _root;
public:
	binTree() :_size(0), _root(NULL){}
	/*树接入接口*/
	binNodePosi attachAsLC(binNodePosi x, binTree* &S)//S本身置空 S当作节点x的左子树接入
	{
		if (x->lChild = S->_root)
		{
			x->lChild->parent = x;//接入
			_size += S->_size;
			S->_root = NULL;
			delete S;//释放原树
			return x;
		}
		return NULL;
	}
	binNodePosi attachAsRC(binNodePosi x, binTree* &S)//S本身置空 S当作节点x的左子树接入
	{
		if (x->rChild = S->_root)
		{
			x->rChild->parent = x;//接入
			_size += S->_size;
			S->_root = NULL;
			delete S;//释放原树
			return x;
		}
		return NULL;
	}
	/******根节点接入接口*****/
	binNodePosi  insertAsRoot(datatype const& e)
	{
		_size = 1;
		_root = new binNode(e);
		return _root;
	}
};

binTree* forest[100];//空森林
int size = 0;//森林规模
myTable table[100];//词典数组
int j = 0;

/*编码森林初始化*/
void initForest()
{
	int i;
	cin >> size;
	myInt* ch = new myInt[size];

	for (i = 0; i < size; i++)
	{
		ch[i].index = i;
		cin >> ch[i].weight;
	}
	for (i = 0; i < size; i++)
	{
		binTree* tree = new binTree();
		tree->insertAsRoot(ch[i]);
		forest[i] = tree;
	}//构造森林
}
/*找出最小权重Huffman字符所在的树并删除之*/
binTree* minChar(binTree** forest)
{
	int i;
	binTree* p = forest[0];//从首个开始寻找
	binTree* minChar = p;
	int minWeight = p->_root->data.weight;
	for (i = 0; i<size; i++)//使用寻找最小
	{
		if (minWeight > forest[i]->_root->data.weight)
		{
			minWeight = forest[i]->_root->data.weight;
			minChar = forest[i];
		}
	}
	/*从森林中删除minChar*/
	for (i = 0; i < size; i++)
	{
		if (forest[i] == minChar)
			break;
	}
	for (; i < size - 1; i++)
	{
		forest[i] = forest[i + 1];
	}
	size--;
	return minChar;
}
/*huffman编码算法*/
binTree* generateTree(binTree** forest)
{
	while (1<size)
	{
		binTree* T1 = minChar(forest);
		binTree* T2 = minChar(forest);
		binTree* S = new binTree();
		myInt tmp;
		tmp.weight = T1->_root->data.weight + T2->_root->data.weight;
		tmp.index = 999;
		S->insertAsRoot(tmp);
		S->attachAsLC(S->_root, T1);
		S->attachAsRC(S->_root, T2);
		forest[size] = S;//将S放回森林
		size++;
	}
	return forest[0];
}
/*遍历获取编码*/
void generateCT(string code, binNodePosi v)
{
	if (IsLeaf(*v))
	{
		myInt c(v->data.weight, v->data.index);
		myTable tmp(c, code);
		table[j] = tmp;
		j++;
		return;
	}
	if (HasLChild(*v))//left=0
	{
		generateCT(code + '0', v->lChild);
		//code不直接先+'0' 而是在作为参数传递给下一层的时候再加'0'是为了不影响下一个if
	}
	if (HasRChild(*v))//right=1
	{
		generateCT(code + '1', v->rChild);
	}
}
/*构成编码字典*/
void generateTable(binTree* tree)
{
	string code = "";
	generateCT(code, tree->_root);
}
/*打印编码表*/
void tableDisplay()
{
	int i, k;
	for (i = 0; i <j; i++)
	{
		for (k = 0; k < j; k++)
		{
			if (table[k].data.index == i)
				cout << table[k].data.weight << "-" << table[k].code << endl;
		}
	}//按输入次序打印
}

int main()
{
	int T;
	cin >> T;
	while (T--)
	{
		initForest();//构建森林
		binTree* huffmanTree = generateTree(forest);//构建huffman树
		generateTable(huffmanTree);//构建编码字典
		tableDisplay();
	}
	return 0;
}