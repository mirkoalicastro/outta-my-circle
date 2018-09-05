package com.acg.outtamycircle.utilities;

import android.support.annotation.NonNull;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyList<T> implements Iterable<T>{
    private final MyIterator iterator;
    private Node<T> head = null;

    public MyList(){
        iterator = new MyIterator();
    }

    @NonNull
    @Override
    public Iterator<T> iterator() {
        return iterator;
    }


    public void add(T key){
        head = new Node<>(key, head);
        resetIterator();
    }


    private class Node<T>{
        final T key;
        Node<T> next, prev;

        Node(T key, Node<T> next){
            this.key = key;
            this.next = next;
            this.prev = null;
        }
    }


    private class MyIterator implements Iterator<T> {
        Node<T> next, cur;

        MyIterator(){
            next = head;
            cur = null;
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public T next() {
            if(!hasNext()) throw new NoSuchElementException();
            cur = next;
            T ret = cur.key;
            next = cur.next;
            return ret;
        }

        @Override
        public void remove() {
            if(cur == null)
                throw new IllegalStateException("first get next bitch");
            if(cur == head)
                head = head.next;
            else
                cur.prev.next = cur.next;
        }
    }

    public void remove(T el){
        Node<T> prev = null;
        for(T key : this) {
            if(key.equals(el)) {
                if (prev == null)
                    head = head.next;
                else
                    prev.next = prev.next.next;
                resetIterator();
                return;
            }
            if(prev == null)
                prev = head;
            else
                prev = prev.next;
        }
        throw new NoSuchElementException("Cannot find the element to remove");
    }

    public void resetIterator(){
        iterator.next = head;
        iterator.cur = null;
    }
}
