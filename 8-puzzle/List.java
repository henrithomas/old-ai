import java.util.Iterator;
import java.util.NoSuchElementException;
public class List<T>
{

    protected Node head;
    protected Node tail;
    protected int size;

    public List()
    {
        head = null;
        tail = null;
        size = 0;
    }
    
    public void push(T data)
    {
        insert(data, 0);
    }

    public void enqueue(T data)
    {
        insert(data, size);
    }
    
    public Node top()
    {
        Node top = head;
        return top;
    }
    
    public void pop()
    {
        head = head.getNext();
        this.size--;
    }

    public void swapToHead(int index)
    {
    	if(index > size)
    	{
    		System.out.println("Unable to swap.");
    	}
    	else
    	{
    		Node tempNode = getNode(index);
    		if(index == this.size)
    		{
    			getNode(index - 1).setNext(null);
    			tail = getNode(index - 1);
    			tempNode.setNext(head);
    			head = tempNode;
    		}
    		else if (index == 0)
    		{
    			tempNode = null;
    		}
    		else 
    		{
    			getNode(index - 1).setNext(getNode(index).getNext());
    			tempNode.setNext(head);
    			head = tempNode;
    		}
    	}
    }
    
    private Node getNode(int index)
    {
        if (index > size)
        {
            throw new IllegalArgumentException("No node at: " + index + " list size " + size);
        }
        else
        {
            Node temp = head;
            for(int i = 1; i < index; i++)
            {
                temp = temp.getNext();
            }
            return temp;
        }
    }

    public T getData(int index)
    {
        return getNode(index).getData();
    }

    public void setSize(int sze)
    {
        size = sze;
    }
    
    public int size()
    {
        return this.size;
    }
    
    public void insert(T data, int index)
    {
        if (index > size)
        {
            throw new IllegalArgumentException("Tried insert at: " + index + " list size is " + size);
        }
        if(size == 0)
        {
            head = new Node(data);
            tail = head;
        }
        else
        {
            Node newNode = new Node(data);
            
            //if inserting at head
            if (index == 0)
            {
                newNode.setNext(head);
                head = newNode;
                tail = head;
            }
            else if (index == this.size)
            {
                if(tail != null)
                {
                    tail.setNext(newNode);
                }
                tail = newNode;
            }
            else
            {
                Node current = getNode(index);
                newNode.setNext(current.getNext());
                current.setNext(newNode);
            }
            

        }
        size++;
    }

    public Iterator<T> iterator()
    {
        Iterator <T> it = new Iterator<T>()
        {
            private Node cursor = List.this.head;
            
            @Override
            public boolean hasNext()
            {
                return this.cursor != null;
            }
            @Override
            public T next()
            {
                if(this.hasNext() == true)
                {
                    T CurrentObj = cursor.getData();
                    cursor = cursor.getNext();
                    return CurrentObj;
                }
                else
                {
                    throw new NoSuchElementException();
                }
            }
            @Override
            public void remove()
            {
                throw new NoSuchElementException();
            }
        };
            return it;
    }
    
protected class Node
{
    Node next;
    T data;

    public Node(T d)
    {
        this(d, null);
    }

    public Node(T d, Node n)
    {
        this.next = n;
        this.data = d;
    }

    public T getData()
    {
        return this.data;
    }

    public void setData(T data)
    {
        this.data = data;
    }

    public Node getNext()
    {
        return this.next;
    }

    public void setNext(Node nextNode)
    {
        this.next = nextNode;
    }
    
    public boolean hasNext()
    {
        if(this.next != null)
            return true;
        else
            return false;
    }
  }
}
