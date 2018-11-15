import java.util.ArrayList;
import java.util.HashMap;

class InitializeFibHeap {
	Node max = null;
	int size; //check if size is used anywhere
	ArrayList<Node> rootList = new ArrayList<Node>();
	HashMap<Integer, Node> degreeMap = new HashMap<Integer, Node>();
	
	
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
	
	Node insert(String key, int val){
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
		
		return n;
	}
	
	
	//Inserts a node into an existing tree by patching the node into existing doubly linked list
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
	
	
	Node removeMax(){
		Node altmax = null;
		Node newHeap;
		
		//regardless of the fact that max has siblings, patch siblings
		max.prev.next = max.next;
		max.next.prev = max.prev;
		
		altmax = max;
		//set max to something else which won't necessarily be the max of that tree
		max = max.next;
		//sever connections between siblings by making the next and prev point to itself
		altmax.prev = altmax;
		altmax.next = altmax;
		
		
		//if prev max has children
		
		if(altmax.degree > 0){	//track degree carefully
			Node maxchild = altmax.child;
			//sever connections between parent and children
			maxchild.parent = null;
			maxchild = maxchild.next;
			while(maxchild != altmax.child && maxchild.parent == altmax){
				maxchild.parent = null;
				maxchild = maxchild.next;
			}
			
			altmax.child = null;
			//meld sibling tree with children of prev max.
			//max pointer won't be pointing to true max
			newHeap = meld(max, maxchild);
			//find new max and assign it to max variable
			//max = findMax(newHeap);
			consolidate(newHeap);
			size = size -1;
			return altmax;
		}
		
		else{
			consolidate(max);
			size = size -1;
			return altmax;
		}
		
	}
	
	//better to consolidate first and then find max because less roots to traverse to find roots
	Node consolidate(Node heap){
		//max = heap;
		rootList.add(heap);
		Node ini = heap.next;
		while(ini != heap){
			rootList.add(ini);
			ini = ini.next;
		}
		
		for(Node n: rootList){
			
			//only consolidate if the node has no parents
			if(n.parent == null)
				pairwiseCombine(n);
		}
		rootList.clear();
		degreeMap.clear();
		return max;
	}
	
	//How will you keep track of the heap?
	
	void pairwiseCombine(Node insNode){
		Node current = degreeMap.get(insNode.degree);
		
		/*
		//check if the node is already in the hashmap
		if(insNode == current) //https://stackoverflow.com/questions/13387742/compare-two-objects-with-equals-and-operator
			return;
		*/
		
		if(current == null){
			
			//compare max
			if(insNode.val >= max.val)
				max = insNode;
			
			degreeMap.put(insNode.degree, insNode);
			return;
		}
		
		else{
			//remove the existing heap from degree table
			degreeMap.remove(current.degree);
			if(current.val >= insNode.val){
				
				//compare max
				if(current.val >= max.val)
					max = current;
				//current is bigger, ins is smaller
				insNode.next.prev = insNode.prev;
				insNode.prev.next = insNode.next;
				insNode.next = insNode;
				insNode.prev = insNode;
				insNode.parent = current;
				
				//just in case
				current.parent = null;
				current.child = meld(current.child, insNode);
				current.child.parent = current;
				current.degree = current.degree+1;
				insNode.childCut = false;
				pairwiseCombine(current);
			}
			
			else{
				
				//compare max
				if(insNode.val >= max.val)
					max = insNode;
				
				current.next.prev = current.prev;
				current.prev.next = current.next;
				current.next = current;
				current.prev = current;
				current.parent = insNode;
				insNode.parent = null;
				insNode.child = meld(insNode.child, current);
				insNode.child.parent = insNode;
				insNode.degree = insNode.degree + 1;
				current.childCut = false;
				pairwiseCombine(insNode);
			}
		}

		
	}
	
	void increaseKey(Node n, int inc){
		n.val = n.val + inc;
		
		//if the node has parents and the node value is greater than its parents
		if((n.parent != null) && (n.val > n.parent.val))
			childCut(n);
			
		if(n.val > max.val)
			max = n;
	}
	
	void childCut(Node n){
		n.childCut = false;
		Node parent = n.parent;
		
		if(parent == null)
			return;
		
		parent.degree = parent.degree - 1;
		
		
		
		//patch n's siblings
		n.prev.next = n.next;
		n.next.prev = n.prev;
		
		
		if(parent.child == n){
			if(n.next != n){
				if(n.next.parent == parent){
					parent.child = n.next;
				}
				else{//check if prev is linked to parent;
					if(n.prev.parent == parent)
						parent.child = n.prev;
					
					else
						parent.child = null;
				}
			}
			
			else
				parent.child = null;
		}
		
		n.prev = null;
		n.next = null;
		n.parent = null;
		
		max = meld(max, n);
		
		if(parent.childCut == true)
			childCut(parent);
		
		else
			parent.childCut = true;
		
	}
	
	int getSize(){
		return size;
	}
	
	
}