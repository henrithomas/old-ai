
public class BoardNode 
{
	int[] BoardState;
	int widthHeight;
	int g;
	int h;
	int f;
	BoardNode parent;
	BoardNode up;
	BoardNode down;
	BoardNode left;
	BoardNode right;
	
	public BoardNode()
	{
		BoardState = null;
		widthHeight = 0;
		parent = null;
		up = null;
		down = null;
		left = null;
		right = null;
	}
	public BoardNode(int[] board, int wh, BoardNode newParent, BoardNode newUp, BoardNode newDown, BoardNode newLeft, BoardNode newRight)
	{
		BoardState = board;
		widthHeight = wh;
		parent = newParent;
		up = newUp;
		down = newDown;
		left = newLeft;
		right = newRight;
	}
	
	public BoardNode(int[] board, int wh, BoardNode newParent)
	{
		BoardState = board;
		widthHeight = wh;
		parent = newParent;
	}
	
	public BoardNode(int[] board, int wh)
	{
		BoardState = board;
		widthHeight = wh;
	}
	public void setBoardState(int[] board)
	{
		BoardState = board;
	}
	
	public void setWidthHeight(int wh)
	{
		widthHeight = wh;
	}
	
	public void setParent(BoardNode newParent)
	{
		parent = newParent;
	}
	
	public void setUp (BoardNode newUp)
	{
		up = newUp;
	}
	
	public void setDown (BoardNode newDown)
	{
		down = newDown;
	}
	
	public void setLeft (BoardNode newLeft)
	{
		left = newLeft;
	}
	
	public void setRight (BoardNode newRight)
	{
		right = newRight;
	}
	
	public void setUpDownLeftRight(BoardNode newUp, BoardNode newDown, BoardNode newLeft, BoardNode newRight)
	{
		up = newUp;
		down = newDown;
		left = newLeft;
		right = newRight;
	}
	
	public void setNode(BoardNode newParent, BoardNode newUp, BoardNode newDown, BoardNode newLeft, BoardNode newRight, int[] board, int wh)
	{
		parent = newParent;
		up = newUp;
		down = newDown;
		left = newLeft;
		right = newRight;
		BoardState = board;
		widthHeight = wh;
	}
	
	public void setG(int data)
	{
		g = data;
	}
	
	public void setH(int data)
	{
		h = data;
	}
	
	public void setF()
	{
		f = g + h;
	}
	
	public int[] getBoardState()
	{
		return BoardState;
	}
	
	public BoardNode getParent()
	{
		return parent;
	}
	
	public BoardNode getUp()
	{
		return up;
	}
	
	public BoardNode getDown()
	{
		return down;
	}
	
	public BoardNode getLeft()
	{
		return left;
	}
	
	public BoardNode getRight()
	{
		return right;
	}
	
	public int getG()
	{
		return g;
	}
	
	public int getH()
	{
		return h;
	}

	public int getF()
	{
		return f;
	}
	public void print()
	{
		//System.out.println("Board:");
		for (int i = 0; i < widthHeight; i++)
		{	
			System.out.println(BoardState[widthHeight*i] + " " + BoardState[widthHeight*i+1] + " " + BoardState[widthHeight*i+2]);
		}
		System.out.println("");
	}
}
