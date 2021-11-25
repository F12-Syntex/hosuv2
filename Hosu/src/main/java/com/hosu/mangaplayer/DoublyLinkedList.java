package com.hosu.mangaplayer;

import java.util.ArrayList;
import java.util.List;

public class DoublyLinkedList<T>{

	private Node node = null;

	public void add(T data) {	
		Node node = new Node(null, null, data);
		
		/*
		 * is head?
		 */
		if(this.node == null) {
			this.node = node;
			return;
		}
		
		/*
		 * append tail.
		 */
		node.prev = this.tail();
		this.tail().next = node;
	}
	
	public void add(T data, int element) {
		
		//out of bounds?
		if(element < 0 || element > this.size()) {
			throw new IndexOutOfBoundsException();
		}
		
		Node node = new Node(null, null, data);
		
		/*
		 * is head?
		 */
		if(element == 0) {
			final Node clone = this.node;
			node.next = clone;
			clone.prev = node;
			this.node = node;
			return;
		}
		
		/*
		 * is tail?
		 */
		if(element == this.size()-1) {
			this.add(data);
			return;
		}
		
		//add element of index "element"
		
		Node temp = this.get(element - 1).next;
		Node prev = this.get(element - 1);
		
		prev.next = node;
		
		node.next = temp;
		
		node.prev = prev;
		
	}
	
	
	/*
	 * remove data
	 */
	public void remove(T data) {
		for(int i = 0; i < this.size(); i++) {
			if(data == this.get(i).getData()) {
				this.remove(this.get(i));
			}
		}
	}
	
	/*
	 * remove node
	 */
	public void remove(Node remove) {
		
		 //No elements?
		if(this.node == null) {
			return;
		}
		
		 //is head?
		if(remove == this.node) {
			 //Head with no elements?
			if(remove.next == null) {
				this.node = null;
				return;
			}
			 //Head with elements
			remove.next.prev = null;
			this.node = remove.next;
			return;
		}
		
		//is tail?
		if(remove.next == null) {
			//will have a prev element, as element is not head.
			remove.prev.next = null;
			return;
		}
		
		//element within body.
		
		//prev node's next should be the removes next, and vice vertha.
		remove.prev.next = remove.next;
		remove.next.prev = remove.prev;
	}
	
	/*
	 * retrive node
	 */
	public DoublyLinkedList<T>.Node get(int element) {
		//get head aka first element.
		Node node = this.head();
		int index = 0;
		
		if(element < 0 || element > this.size()) {
			throw new IndexOutOfBoundsException();
		}
		
		while(index != element) {
			index++;
			node = node.next;
		}
		
		return node;
	}
	
	/*
	 * retrive nodes
	 */
	public List<DoublyLinkedList<T>.Node> getNodes(T data) {
		//get head aka first element.
		Node node = this.head();
		int index = 0;
		
		List<DoublyLinkedList<T>.Node> nodes = new ArrayList<>();
		
		while(index < this.size()) {
			if(node.getData() == data || node.getData().equals(data)) {
				nodes.add(node);
			}
			node = node.next;
			index++;
		}
		
		return nodes;
	}
	
	/*
	 * retrieve the head node.
	 */
	public DoublyLinkedList<T>.Node head() {
		return this.node;
	}
	
	/*
	 * get tail node.
	 */
	public DoublyLinkedList<T>.Node tail(){
		Node tail = this.node;
		//get last node.
		while(tail.next != null) {
			tail = tail.next;
		}
		return tail;
	}
	
	/*
	 * get size of list.
	 */
	public int size() {
		
		//return 0 if list is empty
		if(node == null) {
			return 0;
		}
		
		//start with 1 element, ( the head )
		int elements = 1;
		
		Node head = this.node;
		
		//increment as we find more nodes
		while(head.next != null){
			head = head.next;
			elements++;
		}
		
		//return elements
		return elements;
	}
	
	/*
	 * nodes class
	 */
	public class Node{
		public Node next;
		public Node prev;
		public T data;
		public Node(DoublyLinkedList<T>.Node next, DoublyLinkedList<T>.Node prev, T data) {
			super();
			this.next = next;
			this.prev = prev;
			this.data = data;
		}
		
		public T getData() {
			return this.data;
		}
		
		public List<DoublyLinkedList<T>.Node> getParents(){
			List<DoublyLinkedList<T>.Node> nodes = new ArrayList<DoublyLinkedList<T>.Node>();
			DoublyLinkedList<T>.Node temp = this;
			while(temp.next != null) {
				nodes.add(temp.next);
				temp = temp.next;
			}
			return nodes;
		}
		
		public List<DoublyLinkedList<T>.Node> getChildren(){
			List<DoublyLinkedList<T>.Node> nodes = new ArrayList<DoublyLinkedList<T>.Node>();
			DoublyLinkedList<T>.Node temp = this;
			while(temp.prev != null) {
				nodes.add(temp.prev);
				temp = temp.prev;
			}
			return nodes;
		}
	}

	public void printNodes() {
		System.out.println("printing all nodes");
		
		for(int i = 0; i < this.size(); i++) {
			System.out.println(i + ": " + this.get(i).getData());
		}
	}
	
	
}
