/*
 * Created on 17-dic-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.herac.tuxguitar.gui.actions.duration;

import org.eclipse.swt.events.TypedEvent;
import org.herac.tuxguitar.gui.TuxGuitar;
import org.herac.tuxguitar.gui.actions.Action;
import org.herac.tuxguitar.gui.editors.tab.Caret;
import org.herac.tuxguitar.gui.undo.undoables.measure.UndoableMeasureGeneric;
import org.herac.tuxguitar.song.models.TGDuration;

/**
 * @author julian
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SetThirtySecondDurationAction extends Action {
	public static final String NAME = "action.note.duration.set-thirty-second";
	public static final int VALUE = TGDuration.THIRTY_SECOND;

	public SetThirtySecondDurationAction() {
		super(NAME, AUTO_LOCK | AUTO_UNLOCK | AUTO_UPDATE | DISABLE_ON_PLAYING | KEY_BINDING_AVAILABLE);
	}

	protected int execute(TypedEvent e) {
		if (getSelectedDuration().getValue() != VALUE) {
			//comienza el undoable
			UndoableMeasureGeneric undoable = UndoableMeasureGeneric.startUndo();

			getSelectedDuration().setValue(VALUE);
			getSelectedDuration().setDotted(false);
			getSelectedDuration().setDoubleDotted(false);
			setDurations();

			//termia el undoable
			addUndoableEdit(undoable.endUndo());
		}
		return 0;
	}

	private void setDurations() {
		Caret caret = getEditor().getTablature().getCaret();
		caret.changeDuration(getSelectedDuration().clone(getSongManager().getFactory()));
		TuxGuitar.instance().getFileHistory().setUnsavedFile();
		fireUpdate(getEditor().getTablature().getCaret().getMeasure().getNumber());
	}

	public TGDuration getSelectedDuration() {
		return getEditor().getTablature().getCaret().getDuration();
	}
}
