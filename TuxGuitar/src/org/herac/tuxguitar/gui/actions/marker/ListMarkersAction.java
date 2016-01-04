/*
 * Created on 17-dic-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.herac.tuxguitar.gui.actions.marker;

import org.eclipse.swt.events.TypedEvent;
import org.herac.tuxguitar.gui.actions.Action;
import org.herac.tuxguitar.gui.marker.MarkerList;

/**
 * @author julian
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ListMarkersAction extends Action {
	public static final String NAME = "action.marker.list";

	public ListMarkersAction() {
		super(NAME, AUTO_LOCK | AUTO_UNLOCK | AUTO_UPDATE);
	}

	protected int execute(TypedEvent e) {
		if (MarkerList.instance().isDisposed()) {
			MarkerList.instance().show();
		}
		else {
			MarkerList.instance().dispose();
		}
		return 0;
	}
}
