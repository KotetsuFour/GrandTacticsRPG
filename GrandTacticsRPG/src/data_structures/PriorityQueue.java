package data_structures;

public class PriorityQueue<E extends Comparable<E>> {

	private QueueNode start;
	private QueueNode end;
	private int size;
	
	public PriorityQueue() {
		start = new QueueNode(null, null, null);
		end = new QueueNode(null, start, null);
		start.setNext(end);
		size = 0;
	}
	
	public void add(E element) {
		if (element == null) {
			throw new IllegalArgumentException("Cannot add a null pointer to a priority queue");
		}
		QueueNode idx = start.getNext();
		while (idx.getElement() != null && idx.getElement().compareTo(element) > 0) {
			idx = idx.getNext();
		}
		new QueueNode(element, idx.getPrev(), idx);
		size++;
	}
	public E pop() {
		if (isEmpty()) {
			throw new IllegalArgumentException("Queue is empty and cannot pop first item");
		}
		E ret = start.getNext().getElement();
		start.setNext(start.getNext().getNext());
		start.getNext().setPrev(start);
		size--;
		return ret;
	}
	public E get(int idx) {
		if (isEmpty()) {
			throw new IllegalArgumentException("Queue is empty and cannot return an element at an index");
		}
		QueueNode check = start.getNext();
		while (idx > 0) {
			if (check.getElement() == null) {
				throw new IndexOutOfBoundsException("Attempted to access an index that doesn't exist");
			}
			check = check.getNext();
			idx--;
		}
		return check.getElement();
	}
	
	public int size() {
		return size;
	}
	public boolean isEmpty() {
		return size() == 0;
	}
	
	private class QueueNode {
		
		private QueueNode prev;
		private QueueNode next;
		private E element;
		
		public QueueNode(E element, QueueNode prev, QueueNode next) {
			setElement(element);
			setPrev(prev);
			setNext(next);
			if (prev != null) {
				prev.setNext(this);
			}
			if (next != null) {
				next.setPrev(this);
			}
		}
		
		public QueueNode getPrev() {
			return prev;
		}
		public void setPrev(QueueNode prev) {
			this.prev = prev;
		}
		public QueueNode getNext() {
			return next;
		}
		public void setNext(QueueNode next) {
			this.next = next;
		}
		public E getElement() {
			return element;
		}
		public void setElement(E element) {
			this.element = element;
		}
	}

}
