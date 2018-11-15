import java.util.ArrayList;
import java.util.HashMap;

class InitializeFibHeap {
	Node max = null;
	int size; //size of the Fibonacci heap
	ArrayList<Node> rootList = new ArrayList<Node>();	//Rootlist to be used after remove max
	HashMap<Integer, Node> degreeMap = new HashMap<Integer, Node>();	//degreetable
	
	//Check if the fibonacci heap is empty
	public boolean isEmpty(){
		if(max == null){
			return true;
		}
		
		else
			return false;
	}
	
	//return the max node
	public Node peekMax(){
		return max;
	}
	
	
	//Take the key and value and insert it into a tree and return the pointer to newly inserted node
	Node insert(String key, int val){
		
		//create a node with gven key and value
		Node n = new Node(key, val);
		//set its degree to 0
		n.degree = 0;
		n.parent = null;	//set its parent to null
		n.child = null;	//set itschild to null
		n.childCut = false;	//set childcut to false
		//check if max is null
		if(max == null){
			//set max to newly created node
			max = n;
			size = size+1;
		}
		else{
			//meld node into the existing heap
			max = meld(n, max);
			size = size+1;
		}
		
		return n;
	}
	
	
	//Inserts a node into an existing tree by patching the node into existing doubly linked list
	Node meld(Node m, Node n){
		
		Node temp;
		
		//if both nodes are null, return null
		if((n == null) &&(m == null))
			return null;
		
		//if the heap is null but the newly created node is not null, return heap with newly created node
		else if((m != null) && (n == null))
			return m;
		
		//if the newly created node is null, return the heap
		else if((n != null) && (m == null))
			return n;
		
		//Otherwise, patch the newly created node into the existing heap list
		else{
			m.prev.next = n;
			n.prev.next = m;
			temp = m.prev;
			m.prev = n.prev;
			n.prev = temp;
			
			//if the newly created node has a value greater than current max, return the heap with max pointer to new node
			if(m.val > n.val)
				return m;
			
			//simply return the heap
			else
				return n;			
		}
	}
	
	//return the removed max node
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
			//if the parent's child pointer points to node under consideration, shift the pointer to next node,
			//provided it is also a child of the parent.
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
			//return the removed max node
			return altmax;
		}
		
		else{
			//if the node has no children, simply reorganize the residual tree such that it points to a new max node
			consolidate(max);
			size = size -1;
			//return the removed node
			return altmax;
		}
		
	}
	
	//better to consolidate first and then find max because less roots to traverse to find roots
	Node consolidate(Node heap){
		//max = heap;
		//Traverse through the top level nodes and add them to the rootList
		rootList.add(heap);
		Node ini = heap.next;
		while(ini != heap){
			rootList.add(ini);
			ini = ini.next;
		}
		
		//for each top level node, call pairwise combine
		for(Node n: rootList){
			
			//only consolidate if the node has no parents
			if(n.parent == null)
				pairwiseCombine(n);
		}
		
		//clear the rootlist and degreemap
		rootList.clear();
		degreeMap.clear();
		return max;
	}
	
	
	//pairwise combine nodes with same degree
	void pairwiseCombine(Node insNode){
		//get the node with degree that is equal to the node under consideration
		Node current = degreeMap.get(insNode.degree);
		
		//if a node does not exist in the hashmap, insert the current node into hashmap
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
			
			//if the existing heap max node is greater than the inserted node, make the inserted node the child of existing heap
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
				
				//meld the newly inserted node with children of existing heap
				current.parent = null;
				current.child = meld(current.child, insNode);
				//set parent node of the child
				current.child.parent = current;
				//increase degree
				current.degree = current.degree+1;
				//set childcut value of the child to false
				insNode.childCut = false;
				//recursively call pairwise combine to insert the new heap into degree table
				pairwiseCombine(current);
			}
			
			//otherwise, make the existing heap a child of the inserted node
			else{
				
				//compare max
				if(insNode.val >= max.val)
					max = insNode;
				
				//make existing heap a child of inserted node
				current.next.prev = current.prev;
				current.prev.next = current.next;
				current.next = current;
				current.prev = current;
				//set parent value of existing heap
				current.parent = insNode;
				insNode.parent = null;
				//meld the existing heap with children of inserted node
				insNode.child = meld(insNode.child, current);
				insNode.child.parent = insNode;
				//increase degree
				insNode.degree = insNode.degree + 1;
				current.childCut = false;
				//recursively call pairwise combine to insert the new heap into degree table
				pairwiseCombine(insNode);
			}
		}

		
	}
	
	//increase the value of the node
	void increaseKey(Node n, int inc){
		n.val = n.val + inc;
		
		//if the node has parents and the node value is greater than its parents
		if((n.parent != null) && (n.val > n.parent.val))
			childCut(n);//call child cut if the node value is greater than its parents
		
		//check the max value
		if(n.val > max.val)
			max = n;
	}
	
	
	//Call for cascading cut
	void childCut(Node n){
		n.childCut = false;
		Node parent = n.parent;
		
		//if the node is top level, no need to child cut
		if(parent == null)
			return;
		
		//otherwise, decrease the degree
		parent.degree = parent.degree - 1;
		
		
		
		//patch n's siblings
		n.prev.next = n.next;
		n.next.prev = n.prev;
		
		//if the parent's child is the current node
		if(parent.child == n){
			//if the node has siblings
			if(n.next != n){
				//if the next node's parent is also the same
				if(n.next.parent == parent){
					//shift child pointer to sibling
					parent.child = n.next;
				}
				else{//check if prev is linked to parent;
					if(n.prev.parent == parent)
						parent.child = n.prev;
					
					else//if there are no children, set child pointer to null
						parent.child = null;
				}
			}
			
			else
				parent.child = null;
		}
		
		n.prev = null;
		n.next = null;
		n.parent = null;
		//meld the node with top level nodes
		max = meld(max, n);
		//if the parent's childcut value is already true
		if(parent.childCut == true)
			childCut(parent);	//recursively call childcut
		
		else
			parent.childCut = true;//else, set childcut value to true
		
	}
	//get size of the heap
	int getSize(){
		return size;
	}
	
	
}