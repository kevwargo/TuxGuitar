/*
 * Created on 17-dic-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.herac.tuxguitar.gui.actions.view;

import org.eclipse.swt.events.TypedEvent;
import org.herac.tuxguitar.gui.TuxGuitar;
import org.herac.tuxguitar.gui.actions.Action;

/**
 * @author julian
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ShowMatrixAction extends Action {
	public static final String NAME = "action.view.show-matrix";
	
	public ShowMatrixAction() {
		super(NAME, AUTO_LOCK | AUTO_UNLOCK | AUTO_UPDATE | KEY_BINDING_AVAILABLE);
	}
	
	protected int execute(TypedEvent e) {
		if(TuxGuitar.instance().getMatrixEditor().isDisposed()) {
			TuxGuitar.instance().getMatrixEditor().show();
		}else {
			TuxGuitar.instance().getMatrixEditor().dispose();
		}
		return 0;
	}
}
