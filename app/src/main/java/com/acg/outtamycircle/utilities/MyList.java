package com.acg.outtamycircle.utilities;

import android.support.annotation.NonNull;

import java.util.Iterator;

public class MyList<T> implements Iterable<T>{
    private final Iterator<T> iterator;

    public MyList(){
        iterator = new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public T next() {
                return null;
            }

            @Override
            public void remove() {

            }
        };
    }

    @NonNull
    @Override
    public Iterator<T> iterator() {
        return iterator;
    }
}
