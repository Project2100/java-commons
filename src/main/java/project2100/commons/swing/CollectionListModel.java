/*
 * MIT License
 *
 * Copyright (c) 2018 Andrea Proietto
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package project2100.commons.swing;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;
import javax.swing.AbstractListModel;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.UIManager;

/**
 * A {@link ListModel} founded over a collection-like {@link List}. Everything
 * parameterized.
 *
 * @author Project2100
 * @param <L>
 * @param <E>
 */
public class CollectionListModel<E, L extends List<E>> extends AbstractListModel<E> implements Iterable<E> {

    private final L delegate;

    public CollectionListModel(Supplier<L> listSupplier, Class<E> o) {
        delegate = listSupplier.get();
    }
    
    public CollectionListModel(Supplier<L> listSupplier) {
        delegate = listSupplier.get();
    }

    // TODO work on Collections static methods
    public CollectionListModel(L list) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        //TODO make a runtimeException?
        delegate = (L) list.getClass().getConstructor().newInstance();
        // may throw UOE ESPECIALLY ON UNMODIFIABLE COLLECTIONS!, check boolean too
        delegate.addAll(list);
    }

    @Override
    public int getSize() {
        return delegate.size();
    }

    @Override
    public E getElementAt(int index) {
        return delegate.get(index);
    }

    @Override
    public Iterator<E> iterator() {
        // XXX Safe?
        return delegate.iterator();
    }

    public Stream<E> stream() {
        return delegate.stream();
    }

    public Stream<E> parallelStream() {
        return delegate.parallelStream();
    }

    /**
     * Replaces the element at the specified position in this list with the
     * specified element.
     *
     * @param index index of element to replace
     * @param element element to be stored at the specified position
     * @return the element previously stored at the specified position
     * @throws IndexOutOfBoundsException
     */
    public E set(int index, E element) {
        E old = delegate.get(index);
        delegate.set(index, element);
        fireContentsChanged(this, index, index);
        return old;
    }

    /**
     * Inserts the specified element at the specified position in this list.
     *
     * @param index index at which the specified element is to be inserted
     * @param element element to be inserted
     * @throws IndexOutOfBoundsException
     * @see List#add(Object)
     */
    public void add(int index, E element) {
        delegate.add(index, element);
        fireIntervalAdded(element, index, index);
    }

    /**
     * Appends the specified element at the end of this list.
     *
     * @param element element to be inserted
     */
    public void append(E element) {
        add(delegate.size(), element);
    }

    /**
     * Tells the number of elements in this list model.
     *
     * @return the list's size
     * @see List#size()
     */
    public int size() {
        return delegate.size();
    }
    
    
    //TODO javadoc
    public boolean isEmpty(){
        return delegate.isEmpty();
    }

    /**
     * Empties this list model.
     *
     * @throws UnsupportedOperationException if the underlying list
     * implementation does not support the {@code clear()} operation
     * @see List#clear()
     */
    public void clear() {
        delegate.clear();
    }

    // Debugging purposes, for now... generics can be very bitchy
    private Class<L> getListClass() {
        return (Class<L>) delegate.getClass();
    }

    public static void main(String[] args) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ArrayList<String> al1 = new ArrayList<>();
        al1.add("abc");
        al1.add("def");

        // UNSAFE CAST - THIS WILL FAIL
//        CollectionListModel<String, LinkedList<String>> cce = new CollectionListModel<>(new ArrayList<>());
//        cce.delegate.descendingIterator();
        CollectionListModel<String, ArrayList<String>> model1 = new CollectionListModel<>(al1);
        CollectionListModel<Object, LinkedList<Object>> model2 = new CollectionListModel<Object, LinkedList<Object>>(LinkedList::new);
        CollectionListModel<Object, LinkedList<Object>> model3 = new CollectionListModel<>(LinkedList::new, Object.class);

        model2.append(new Object());
        model2.append(new Object());
        
        
        

        System.out.println(model1.getElementAt(0).getClass() + " - " + model1.getListClass());
        System.out.println(model2.getElementAt(0).getClass() + " - " + model2.getListClass());

        System.out.println(Arrays.toString(model1.stream().toArray()));
        System.out.println(Arrays.toString(model2.stream().toArray()));

        MarkedCollectionModel<String, ArrayList<String>> mcm = new MarkedCollectionModel<>((Supplier<ArrayList<String>>) ArrayList::new);

        JList<String> list = new JList<>(mcm);

        mcm.applyDecorator(list);

//        UIManager.getLookAndFeelDefaults().keySet().stream().map(Object::toString).sorted(String.CASE_INSENSITIVE_ORDER).forEach(System.out::println);
        
        for (int index = 0; index < 3; index++)
            System.out.println(UIManager.getLookAndFeelDefaults().get("List.cellRenderer").hashCode());

    }
}
