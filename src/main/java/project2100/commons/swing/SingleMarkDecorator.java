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
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * A simple ListCellRenderer that highlights a "marked" index with a different
 * color
 *
 * @implnote If the mark is out of bounds, it will simply disappear until it
 * reenters the bounds again
 *
 * @author Project2100
 * @param <E>
 */
public class SingleMarkDecorator<E> implements ListCellRenderer<E> {

    private final JList<E> view;
    private final ListCellRenderer<? super E> delegate;
    private final Color markColor;
    private int mark;

    public SingleMarkDecorator(JList<E> view, Color markColor) {
        delegate = view.getCellRenderer();
        this.view = view;
        this.markColor = markColor;
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
     * Displaces the mark by the specified offset.
     *
     * @param offset
     * @return
     */
    public int incrementMark(int offset) {
        mark += offset;
        view.repaint();
        return mark;
    }

    /**
     * Sets this model's mark at the specified index. Triggers a repaint of the
     * connected {@link JList} component.
     *
     * @param index the mark's new index
     */
    public void setMark(int index) {
        mark = index;
        view.repaint();
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends E> list, E value, int index, boolean isSelected, boolean cellHasFocus) {
        Component c = delegate.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (!isSelected && index == mark)
            c.setBackground(markColor);
        return c;
    }
}
