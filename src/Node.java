
class Node {
	String key;
	int val;
	Node parent, child, next, prev;
	boolean childCut;
	int degree;
	
	Node(String key, int val){
		this.key = key;
		this.val = val;
		next = this;
		prev = this;
	}
}
