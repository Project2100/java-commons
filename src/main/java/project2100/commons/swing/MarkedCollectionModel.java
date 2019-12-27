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

import java.awt.Color;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.*;
import java.util.HashSet;
import java.util.List;
import java.util.function.Supplier;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;

/**
 * An extended Collection model that implements marking capabilities as seen in
 * the markDecorator. Mark index handling is centralized.
 *
 * @author Project2100
 * @param <E>
 * @param <L>
 */
public class MarkedCollectionModel<E, L extends List<E>> extends CollectionListModel<E, L> {

    private int mark;
    private Color markColor = Color.orange;
    private final HashSet<JList<E>> views = new HashSet<>();

    public MarkedCollectionModel(L collection) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        super(collection);
        mark = 0;
    }

    public MarkedCollectionModel(Supplier<L> collConstructor) {
        super(collConstructor);
        mark = 0;
    }

    @Override
    public void clear() {
        super.clear();
        mark = 0;
    }

    /**
     * Gets the mark's current index.
     *
     * @return the index of the mark
     */
    public int getMark() {
        return mark;
    }

    /**
     * Displaces the mark by the specified offset. Triggers a repaint of the
     * connected {@link JList} component.
     *
     * @param offset
     * @return
     */
    public int incrementMark(int offset) {
        int old = mark;
        mark += offset;
        views.forEach(JList::repaint);
        return mark;
    }

    /**
     * Sets this model's mark at the specified index. Triggers a repaint of the
     * connected {@link JList} component.
     *
     * @param index the mark's new index
     */
    public void setMark(int index) {
        int old = mark;
        mark = index;
        views.forEach(JList::repaint);
    }

    /**
     * Gets the mark's current background color
     *
     * @return the mark's color
     */
    public Color getMarkColor() {
        return markColor;
    }

    /**
     * Changes the mark's color to the given one
     *
     * @param markColor he new color
     */
    public void setMarkColor(Color markColor) {
        this.markColor = markColor;
    }

    /**
     * Changes the cellRenderer of this JList to a new one displaying the mark.
     *
     * @param component the JList to decorate
     */
    public void applyDecorator(JList<E> component) {

        if (!component.getModel().equals(this))
            throw new IllegalArgumentException("Given JList has a different model!");

        MarkedCollectionDecorator<E> deco = new MarkedCollectionDecorator<>();

        views.add(component);
        component.setCellRenderer(deco);
        component.addPropertyChangeListener("cellRenderer", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent event) {
                if (event.getOldValue().equals(deco) && !event.getNewValue().equals(deco)) {
                    views.remove(component);
                    component.removePropertyChangeListener("cellRenderer", this);
                }
            }
        });
    }

    public class MarkedCollectionDecorator<E> implements ListCellRenderer<E> {

        // Unsafe typecheck - each get call returns a new instance
        private final ListCellRenderer<? super E> delegate
                = (ListCellRenderer<? super E>) UIManager.getLookAndFeelDefaults()
                .get("List.cellRenderer");

        @Override
        public Component getListCellRendererComponent(JList<? extends E> list, E value, int index, boolean isSelected, boolean cellHasFocus) {
            Component c = delegate.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (!isSelected && index == mark)
                c.setBackground(markColor);
            return c;
        }

    }

    //__________________________________________________________________________
//    /**
//     *
//     * @param source the <code>ListModel</code> that changed, typically "this"
//     * @param index0 one end of the new interval
//     * @param index1 the other end of the new interval
//     * @see EventListenerList
//     * @see DefaultListModel
//     */
//    //TODO Document correctly
//    protected void fireMarkChanged(Object source, int index0, int index1) {
//        Object[] listeners = listenerList.getListenerList();
//        ListMarkEvent e = null;
//
//        System.out.println(source);
//
//        for (int i = listeners.length - 2; i >= 0; i -= 2)
//            if (listeners[i] == ListMarkListener.class) {
//                if (e == null)
//                    e = new ListMarkEvent(source, index0, index1);
//                ((ListMarkListener) listeners[i + 1]).markChanged(e);
//            }
//    }
//    // add, remove marklisteners
//
//    //TODO Construct the event correctly
//    public static class ListMarkEvent extends EventObject {
//
//        public final int oldIndex, newIndex;
//
//        public ListMarkEvent(Object source, int oldIndex, int newIndex) {
//            super(source);
//            this.oldIndex = oldIndex;
//            this.newIndex = newIndex;
//        }
//    }
//
//    @FunctionalInterface
//    public static interface ListMarkListener {
//
//        public void markChanged(ListMarkEvent event);
//    }
}
