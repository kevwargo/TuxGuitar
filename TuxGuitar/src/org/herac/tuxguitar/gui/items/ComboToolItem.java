package org.herac.tuxguitar.gui.items;

import java.util.List;
import java.util.ArrayList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;


public class ComboToolItem extends SelectionAdapter
{
    private ToolItem item;
    private Menu subMenu;
    private List<MenuItem> subItems;
    private int selection;

    public ComboToolItem(ToolBar parent)
    {
        this.item = new ToolItem(parent, SWT.DROP_DOWN);
        this.item.addSelectionListener(this);
        this.subMenu = new Menu(parent.getShell());
        this.subItems = new ArrayList<MenuItem>();
    }

    public ToolItem getItem()
    {
        return this.item;
    }

    public MenuItem getSubItem(int index)
    {
        return this.subItems.get(index);
    }

    public MenuItem newSubItem(int style)
    {
        MenuItem newItem = new MenuItem(this.subMenu, style);
        newItem.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent e) {
                    ComboToolItem.this.select(
                        ComboToolItem.this.subItems.indexOf(e.widget));
                }
            });
        this.subItems.add(newItem);
        return newItem;
    }

    public void removeSubItem(int index)
    {
        this.subItems.remove(index);
    }

    public void select(int index)
    {
        if (index < 0 || index >= this.subItems.size())
            return;
        this.item.setText(getSubItem(index).getText());
        this.selection = index;
        this.subItems.get(index).setSelection(true);
        for (int i = 0; i < this.subItems.size(); i++)
            if (i != index)
                this.subItems.get(i).setSelection(false);
    }

    public int getSelectionIndex()
    {
        if (this.subItems.size() == 0)
            return -1;
        return this.selection;
    }

    public MenuItem getSelectedItem()
    {
        if (this.subItems.size() > 0)
            return this.subItems.get(this.selection);
        return null;
    }

    public void setEnabled(boolean enabled)
    {
        this.item.setEnabled(enabled);
    }

    public void widgetSelected(SelectionEvent event)
    {
        if (event.detail == SWT.ARROW)
        {
            ToolItem item = (ToolItem) event.widget;
            Rectangle rect = item.getBounds();
            Point pt = item.getParent().toDisplay(new Point(rect.x, rect.y));
            this.subMenu.setLocation(pt.x, pt.y + rect.height);
            this.subMenu.setVisible(true);
        }
    }

    public void setText(String text) {
        this.item.setText(text);
    }
}
