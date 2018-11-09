
class InitializeFibHeap {
	Node max = null;
	int size; //check if size is used anywhere
	public boolean isEmpty(){
		if(max == null){
			return true;
		}
		
		else
			return false;
	}
	
	public Node peekMax(){
		return max;
	}
	
	void insert(String key, int val){
		Node n = new Node(key, val);
		n.degree = 0;
		n.parent = null;
		n.child = null;
		n.childCut = false;
		if(max == null){
			max = n;
			size++;
		}
		else{
			max = meld(n, max);
			size++;
		}
	}
	
	
	
	Node meld(Node m, Node n){
		
		Node temp;
		
		if((n == null) &&(m == null))
			return null;
		
		else if((m != null) && (n == null))
			return m;
		
		else if((n != null) && (m == null))
			return n;
		
		else{
			m.prev.next = n;
			n.prev.next = m;
			temp = m.prev;
			m.prev = n.prev;
			n.prev = temp;
			
			if(m.val > n.val)
				return m;
			
			else
				return n;			
		}
	}
	
	void increaseKey(Node n, int inc){
		n.val = n.val + inc;
		
		if((n.val > n.parent.val) && (n.parent != null))
			//implement childcut
		if(n.val > max.val)
			max = n;
	}
	
	Node removeMax(){
		Node altmax = null;
		if(max.next == max)
			max = null;
		
		else{
			max.prev.next = max.next;
			max.next.prev = max.prev;
			//set max to something else
			altmax = max;
			max = max.next;
			altmax.prev = null;
			altmax.next = null;
		}
		
		//if prev max has children
		
		if(altmax.degree != 0){	//track degree carefully
			Node maxchild = altmax.child;
			maxchild.parent = null;
			while((maxchild.next != maxchild) && (maxchild.next != null)){
				maxchild = maxchild.next;
				maxchild.parent = null;
			}
			
			max = meld(max, maxchild);
		}
		
		
		
		
	}
	
	
}