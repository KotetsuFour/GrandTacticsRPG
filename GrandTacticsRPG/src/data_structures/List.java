package data_structures;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

/**
 * A custom array-list with personalized functionality (and without undesired functionality)
 * @author Avery Hawkins
 *
 */
public class List<E> implements Serializable {
	
	private int size;
	
	private E[] list;
	
	private int limit;

	@SuppressWarnings("unchecked")
	public List() {
		this.size = 0;
		this.list = (E[]) new Object[10];
		this.limit = Integer.MAX_VALUE;
	}
	
	@SuppressWarnings("unchecked")
	public List(int initLength) {
		this.size = 0;
		this.list = (E[]) new Object[initLength];
		this.limit = Integer.MAX_VALUE;
	}
	
	@SuppressWarnings("unchecked")
	public List(int initLength, int limit) {
		this.size = 0;
		this.list = (E[]) new Object[initLength];
		this.limit = limit;
	}
	
	public boolean add(E e) {
		if (size == limit) {
			return false;
		}
		if (size == list.length) {
			resize();
		}
		list[size] = e;
		size++;
		return true;
	}
	
	public boolean add(int idx, E e) {
		if (idx < 0 || idx > size) {
			throw new IndexOutOfBoundsException();
		}
		if (size == limit) {
			return false;
		}
		if (size + 1 == list.length) {
			resize();
		}
		for (int q = size; q > idx; q--) {
			list[q] = list[q - 1];
		}
		list[idx] = e;
		size++;
		return true;
	}
	
	public boolean addWithoutRepeating(E e) {
		if (size == limit) {
			return false;
		}
		for (int q = 0; q < size; q++) {
			if (e == list[q]) {
				return false;
			}
		}
		if (size == list.length) {
			resize();
		}
		list[size] = e;
		size++;
		return true;
	}
	
	public E remove(int idx) {
		if (idx < 0 || idx >= size) {
			throw new IndexOutOfBoundsException("Invalid index " + idx + ", size is " + size);
		}
		E ret = list[idx];
		for (int q = idx; q < list.length - 1; q++) {
			list[q] = list[q + 1];
		}
		size--;
		return ret;
	}
	
	public int remove(E e) {
		for (int q = 0; q < list.length; q++) {
			if (list[q] == e) {
				remove(q);
				return q;
			}
		}
		return -1;
	}
	
	public E get(int q) {
		if (q < 0 || q >= size) {
			throw new IndexOutOfBoundsException();
		}
		return list[q];
	}
	
	public void set(int idx, E e) {
		if (idx < 0 || idx > size) {
			throw new IndexOutOfBoundsException();
		}
		if (idx == list.length) {
			add(e);
			return;
		}
		list[idx] = e;
	}
	
	public int indexOf(E e) {
		for (int q = 0; q < list.length; q++) {
			if (list[q] == e) {
				return q;
			}
		}
		return -1;
	}

	@SuppressWarnings("unchecked")
	private void resize() {
		E[] newList = (E[]) new Object[Math.min(limit, list.length * 2)];
		for (int q = 0; q < list.length; q++) {
			newList[q] = list[q];
		}
		list = newList;
	}

	public int size() {
		return size;
	}
	
	public int limit() {
		return limit;
	}

	public boolean isEmpty() {
		return size() == 0;
	}
	
	public boolean isFull() {
		return size() == limit();
	}

	public boolean contains(E e) {
		for (int q = 0; q < list.length; q++) {
			if (list[q] == e) {
				return true;
			}
		}
		return false;
	}

	public Iterator<E> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	public void addAll(Collection<? extends E> c) {
		Iterator<E> it = (Iterator<E>) c.iterator();
		while (it.hasNext() && add(it.next())) {
			//Keep going
		}
	}

	public void addAll(List<E> l) {
		for (int q = 0; q < l.size() && !(isFull()); q++) {
			add(l.get(q));
		}
	}

	@SuppressWarnings("unchecked")
	public void reset() {
		size = 0;
		list = (E[]) new Object[10];
	}
	
	@SuppressWarnings("unchecked")
	public void reset(int initLength) {
		size = 0;
		list = (E[]) new Object[initLength];
	}
	
}
