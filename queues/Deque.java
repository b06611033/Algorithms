/* *****************************************************************************
 *  Name: PO HAN HOU
 *  Date: 2021/11/17
 *  Description: Deque
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private int size;
    private Node first;
    private Node last;

    private class Node {
        Item item;
        Node next;
        Node previous;

    }

    // construct an empty deque
    public Deque() {
        first = null;
        size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return first == null;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException();
        Node oldfirst = first;
        first = new Node();
        first.item = item;
        first.next = oldfirst;
        size++;
        if (size > 1) oldfirst.previous = first;
        else last = first;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException();
        Node oldlast = last;
        last = new Node();
        last.item = item;
        last.previous = oldlast;
        size++;
        if (size > 1) oldlast.next = last;
        else first = last;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException();
        Item fi = first.item;
        if (size > 1) {
            first = first.next;
            first.previous = null;
        }
        else {
            first = null;
            last = null;
        }
        size--;
        return fi;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException();
        Item li = last.item;
        if (size > 1) {
            last = last.previous;
            last.next = null;
        }
        else {
            first = null;
            last = null;
        }
        size--;
        return li;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current
                    != null;       // not current.next != null, because when current points to
        }                                 // the last item, exception will be thrown, and it won't be returned

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }

        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> dq = new Deque<>();
        dq.addLast(10);
        dq.addFirst(31);
        dq.addLast(54);
        dq.addFirst(22);
        dq.removeFirst();
        dq.removeLast();
        StdOut.println(dq.isEmpty());
        StdOut.println(dq.size());
        Iterator<Integer> i = dq.iterator();
        while (i.hasNext()) {
            StdOut.println(i.next());
        }
    }
}
