package things;

/**
 * A LinkedList implementation for the game
 * @author Ömer
 *
 */

public class MyLinkedList {
	public Node head = null;
	public Node tail = null;

	public void add(GridSquare a) {
		// Adds a new node that contains the value 'a' to the end of the list
		Node newNode = new Node(a);
		if (head == null)
			head = tail = newNode;
		else {
			tail.next = newNode;
			newNode.prev = tail;
			tail = newNode;
			tail.next = null;
		}
	}

	public void insert(GridSquare coor, int index)
	{
		// Inserts a new node that contains the value 'a' at 'index.
		// If index is beyond the end of the list, it should be added as the last element.

		Node newNode = new Node(coor);
		Node current = head;
		int currentIndex = 0;

		if (index == 0) {
			// newNode must be the new head
			newNode.next = head;   // If head was null, no problem.
			head = newNode;
			if (head.next == null) {
				// This is the only node in the list. Update also tail.
				tail = head;
			}
			else {
				head.next.prev = head;
			}
			return;   // We are done. Skip the rest of the method.
		}

		// If we reach this point, we are sure current is not null.
		// Iterate until current is the node before insertion point.
		while ((currentIndex!=index-1) && (current.next!=null)) {
			current = current.next;
			currentIndex++;
		}

		// newNode should be inserted after 'current'.
		if (current.next==null) {
			// newNode is to become the last node.
			newNode.next=null;
			newNode.prev = current;
			current.next=newNode;
			
			tail=newNode;
		}
		else {
			newNode.next=current.next;
			newNode.prev = current;
			current.next=newNode;
		}
	}

	public Node get(int index) {
		Node result = head;
		for(int i = 0; i < index; i++) {
			if(result.next == null) {
				throw new IndexOutOfBoundsException();
			}
			result = result.next;
		}
		return result;
	}
	
	public int size() {
		int size = 0;
		if(head == null) {
			return 0;
		}
		Node current = head;
		while(current != null) {
			size++;
			current = current.next;
		}
		return size;
	}
	
	public void remove(int index) {
		if(head == null)
			throw new IndexOutOfBoundsException();
		
		if(index == 0) {
			head = head.next;
			if(head != null) {
				head.prev = null;
			}
			else {
				tail = null;
			}
			return;
		}
		
		if(index >= size()) {
			throw new IndexOutOfBoundsException();
		}
		else {
			Node current = head;
			for(int i = 0; i < index; i++) {
				current = current.next;
			}
			if(current == tail) {
				tail = current.prev;
				tail.next = null;
			}
			else {
				current.prev.next = current.next;
				current.next.prev = current.prev;
			}
		}
	}
	
	public String toString() {
		String result = "[";

		if (head != null) {
			Node current = head;
			while(current.next != null) {
				result += current.gridSquare+", ";
				current = current.next;
			}
			result += current.gridSquare;
		}
		result += "]";
		return result;
	}

}
