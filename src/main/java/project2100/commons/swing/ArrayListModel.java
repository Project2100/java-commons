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

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.ListModel;
import javax.swing.event.EventListenerList;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * An {@link ArrayList} implementing the {@link ListModel} interface.
 *
 * @implnote Copypaste of the {@link AbstractListModel} abstract class to
 * implement model listeners
 *
 * @author Project2100
 * @param <E>
 */
public class ArrayListModel<E> extends ArrayList<E> implements ListModel<E> {

    public ArrayListModel() {
        super();
        listenerList = new EventListenerList();
    }

    public ArrayListModel(List<E> list) {
        super(list);
        listenerList = new EventListenerList();
    }

    @Override
    public int getSize() {
        return size();
    }

    @Override
    public E getElementAt(int index) {
        return get(index);
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
    @Override
    public E set(int index, E element) {
        E old = super.set(index, element);
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
    @Override
    public void add(int index, E element) {
        super.add(index, element);
        fireIntervalAdded(this, index, index);
    }

    /**
     * Appends the specified element at the end of this list.
     *
     * @param element element to be inserted
     */
    public void append(E element) {
        int index = size();
        super.add(element);
        fireIntervalAdded(this, index, index);
    }

    @Override
    public void clear() {
        int index = size();
        super.clear();
        fireIntervalRemoved(this, 0, index);
    }
    
    

    protected EventListenerList listenerList = new EventListenerList();

    /**
     * Adds a listener to the list that's notified each time a change to the
     * data model occurs.
     *
     * @param l the <code>ListDataListener</code> to be added
     */
    @Override
    public void addListDataListener(ListDataListener l) {
        listenerList.add(ListDataListener.class, l);
    }

    /**
     * Removes a listener from the list that's notified each time a change to
     * the data model occurs.
     *
     * @param l the <code>ListDataListener</code> to be removed
     */
    @Override
    public void removeListDataListener(ListDataListener l) {
        listenerList.remove(ListDataListener.class, l);
    }

    /**
     * Returns an array of all the list data listeners registered on this
     * <code>AbstractListModel</code>.
     *
     * @return all of this model's <code>ListDataListener</code>s, or an empty
     * array if no list data listeners are currently registered
     *
     * @see #addListDataListener
     * @see #removeListDataListener
     *
     * @since 1.4
     */
    public ListDataListener[] getListDataListeners() {
        return listenerList.getListeners(ListDataListener.class);
    }

    /**
     * <code>AbstractListModel</code> subclasses must call this method
     * <b>after</b>
     * one or more elements of the list change. The changed elements are
     * specified by the closed interval index0, index1 -- the endpoints are
     * included. Note that index0 need not be less than or equal to index1.
     *
     * @param source the <code>ListModel</code> that changed, typically "this"
     * @param index0 one end of the new interval
     * @param index1 the other end of the new interval
     * @see EventListenerList
     * @see DefaultListModel
     */
    protected void fireContentsChanged(Object source, int index0, int index1) {
        Object[] listeners = listenerList.getListenerList();
        ListDataEvent e = null;

        for (int i = listeners.length - 2; i >= 0; i -= 2)
            if (listeners[i] == ListDataListener.class) {
                if (e == null)
                    e = new ListDataEvent(source, ListDataEvent.CONTENTS_CHANGED, index0, index1);
                ((ListDataListener) listeners[i + 1]).contentsChanged(e);
            }
    }

    /**
     * <code>AbstractListModel</code> subclasses must call this method
     * <b>after</b>
     * one or more elements are added to the model. The new elements are
     * specified by a closed interval index0, index1 -- the enpoints are
     * included. Note that index0 need not be less than or equal to index1.
     *
     * @param source the <code>ListModel</code> that changed, typically "this"
     * @param index0 one end of the new interval
     * @param index1 the other end of the new interval
     * @see EventListenerList
     * @see DefaultListModel
     */
    protected void fireIntervalAdded(Object source, int index0, int index1) {
        Object[] listeners = listenerList.getListenerList();
        ListDataEvent e = null;

        for (int i = listeners.length - 2; i >= 0; i -= 2)
            if (listeners[i] == ListDataListener.class) {
                if (e == null)
                    e = new ListDataEvent(source, ListDataEvent.INTERVAL_ADDED, index0, index1);
                ((ListDataListener) listeners[i + 1]).intervalAdded(e);
            }
    }

    /**
     * <code>AbstractListModel</code> subclasses must call this method
     * <b>after</b> one or more elements are removed from the model.
     * <code>index0</code> and <code>index1</code> are the end points of the
     * interval that's been removed. Note that <code>index0</code> need not be
     * less than or equal to <code>index1</code>.
     *
     * @param source the <code>ListModel</code> that changed, typically "this"
     * @param index0 one end of the removed interval, including
     * <code>index0</code>
     * @param index1 the other end of the removed interval, including
     * <code>index1</code>
     * @see EventListenerList
     * @see DefaultListModel
     */
    protected void fireIntervalRemoved(Object source, int index0, int index1) {
        Object[] listeners = listenerList.getListenerList();
        ListDataEvent e = null;

        for (int i = listeners.length - 2; i >= 0; i -= 2)
            if (listeners[i] == ListDataListener.class) {
                if (e == null)
                    e = new ListDataEvent(source, ListDataEvent.INTERVAL_REMOVED, index0, index1);
                ((ListDataListener) listeners[i + 1]).intervalRemoved(e);
            }
    }

    /**
     * Returns an array of all the objects currently registered as
     * <code><em>Foo</em>Listener</code>s upon this model.
     * <code><em>Foo</em>Listener</code>s are registered using the
     * <code>add<em>Foo</em>Listener</code> method.
     * <p>
     * You can specify the <code>listenerType</code> argument with a class
     * literal, such as <code><em>Foo</em>Listener.class</code>. For example,
     * you can query a list model <code>m</code> for its list data listeners
     * with the following code:
     *
     * <pre>ListDataListener[] ldls = (ListDataListener[])(m.getListeners(ListDataListener.class));</pre>
     *
     * If no such listeners exist, this method returns an empty array.
     *
     * @param listenerType the type of listeners requested; this parameter
     * should specify an interface that descends from
     * <code>java.util.EventListener</code>
     * @return an array of all objects registered as
     * <code><em>Foo</em>Listener</code>s on this model, or an empty array if no
     * such listeners have been added
     * @exception ClassCastException if <code>listenerType</code> doesn't
     * specify a class or interface that implements
     * <code>java.util.EventListener</code>
     *
     * @see #getListDataListeners
     *
     * @since 1.3
     */
    public <T extends EventListener> T[] getListeners(Class<T> listenerType) {
        return listenerList.getListeners(listenerType);
    }
}
