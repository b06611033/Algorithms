/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] que;
    private int N;     // num of items

    // construct an empty randomized queue
    public RandomizedQueue() {
        que = (Item[]) new Object[1];
        N = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return N == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return N;
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < N; i++)
            copy[i] = que[i];
        que = copy;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();
        if (N == que.length) resize(2 * que.length);
        que[N] = item;
        N++;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException();
        if (N == que.length / 4) resize(que.length / 2);
        int random = StdRandom.uniform(0, N);
        Item return_item = que[random];
        N--;
        que[random] = que[N];
        que[N] = null;  // avoid loitering
        return return_item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException();
        return que[StdRandom.uniform(0, N)];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new ArrayIterator();
    }

    private class ArrayIterator implements Iterator<Item> {
        private int current = 0;
        private Item[] iter;

        public ArrayIterator() {
            iter = (Item[]) new Object[N];
            for (int i = 0; i < N; i++) {
                iter[i] = que[i];
            }
            StdRandom.shuffle(iter);
        }

        public boolean hasNext() {
            return current < N;
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = iter[current];
            current++;
            return item;
        }

        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> rq = new RandomizedQueue<>();
        rq.enqueue(1);
        rq.enqueue(2);
        rq.enqueue(3);
        rq.enqueue(4);
        rq.dequeue();
        rq.dequeue();
        StdOut.println(rq.isEmpty());
        StdOut.println(rq.size());
        StdOut.println(rq.sample());
        Iterator<Integer> it = rq.iterator();
        while (it.hasNext()) {
            StdOut.println(it.next());
        }
    }
}
