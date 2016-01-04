/*
 * Created on 02-dic-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.herac.tuxguitar.gui.items.tool;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.herac.tuxguitar.gui.TuxGuitar;
import org.herac.tuxguitar.gui.actions.composition.ChangeTempoAction;
import org.herac.tuxguitar.gui.actions.composition.ChangeTimeSignatureAction;
import org.herac.tuxguitar.gui.actions.insert.RepeatAlternativeAction;
import org.herac.tuxguitar.gui.actions.insert.RepeatCloseAction;
import org.herac.tuxguitar.gui.actions.insert.RepeatOpenAction;
import org.herac.tuxguitar.gui.items.ComboToolItem;
import org.herac.tuxguitar.gui.items.ToolItems;
import org.herac.tuxguitar.player.base.MidiPlayerListener;
import org.herac.tuxguitar.player.base.MidiPlayerMode;
import org.herac.tuxguitar.song.models.TGMeasure;

/**
 * @author julian
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CompositionToolItems extends ToolItems {
	public static final String NAME = "composition.items";
	
	private ToolItem tempo;
    private ComboToolItem tempoPercentage;
	private ToolItem timeSignature;
	private ToolItem repeatOpen;
	private ToolItem repeatClose;
	private ToolItem repeatAlternative;
    protected static final int[] DEFAULT_PERCENTS = new int[] {25, 50, 75, 100, 125, 150, 175, 200 };
	
	public CompositionToolItems() {
		super(NAME);
	}
	
	public void showItems(ToolBar toolBar) {
		this.tempo = new ToolItem(toolBar, SWT.PUSH);
		this.tempo.addSelectionListener(TuxGuitar.instance().getAction(ChangeTempoAction.NAME));

        this.tempoPercentage = new ComboToolItem(toolBar);
        TempoPercentageAdapter adapter = new TempoPercentageAdapter(this.tempoPercentage);
        MidiPlayerMode mode = TuxGuitar.instance().getPlayer().getMode();
        for(int i = 0; i < DEFAULT_PERCENTS.length; i++)
        {
            MenuItem item = this.tempoPercentage.newSubItem(SWT.CHECK);
            item.setText(Integer.toString(DEFAULT_PERCENTS[i]) + "%");
            item.addSelectionListener(adapter);
            if (mode.getSimplePercent() == DEFAULT_PERCENTS[i])
                this.tempoPercentage.select(i);
        }
        TuxGuitar.instance().getPlayer().addListener(new MidiPlayerListener() {
            public void notifyStarted() { }
            public void notifyStopped() { }
            public void notifyLoop() {
                TuxGuitar.instance().getDisplay().asyncExec(new Runnable() {
                    public void run() {
                        int newTempo = TuxGuitar.instance().getPlayer().getMode().getCurrentPercent();
                        CompositionToolItems.this.tempoPercentage.setText(Integer.toString(newTempo) + "%");
                    }
                });
            }
        });

		this.timeSignature = new ToolItem(toolBar, SWT.PUSH);
		this.timeSignature.addSelectionListener(TuxGuitar.instance().getAction(ChangeTimeSignatureAction.NAME));
		
		new ToolItem(toolBar, SWT.SEPARATOR);
		
		this.repeatOpen = new ToolItem(toolBar, SWT.CHECK);
		this.repeatOpen.addSelectionListener(TuxGuitar.instance().getAction(RepeatOpenAction.NAME));
		
		this.repeatClose = new ToolItem(toolBar, SWT.CHECK);
		this.repeatClose.addSelectionListener(TuxGuitar.instance().getAction(RepeatCloseAction.NAME));
		
		this.repeatAlternative = new ToolItem(toolBar, SWT.CHECK);
		this.repeatAlternative.addSelectionListener(TuxGuitar.instance().getAction(RepeatAlternativeAction.NAME));
		
		this.loadIcons();
		this.loadProperties();
	}
	
	public void loadProperties() {
		this.tempo.setToolTipText(TuxGuitar.getProperty("composition.tempo"));
		this.timeSignature.setToolTipText(TuxGuitar.getProperty("composition.timesignature"));
		this.repeatOpen.setToolTipText(TuxGuitar.getProperty("repeat.open"));
		this.repeatClose.setToolTipText(TuxGuitar.getProperty("repeat.close"));
		this.repeatAlternative.setToolTipText(TuxGuitar.getProperty("repeat.alternative"));
	}
	
	public void loadIcons() {
		this.tempo.setImage(TuxGuitar.instance().getIconManager().getCompositionTempo());
		this.timeSignature.setImage(TuxGuitar.instance().getIconManager().getCompositionTimeSignature());
		this.repeatOpen.setImage(TuxGuitar.instance().getIconManager().getCompositionRepeatOpen());
		this.repeatClose.setImage(TuxGuitar.instance().getIconManager().getCompositionRepeatClose());
		this.repeatAlternative.setImage(TuxGuitar.instance().getIconManager().getCompositionRepeatAlternative());
	}
	
	public void update() {
		TGMeasure measure = TuxGuitar.instance().getTablatureEditor().getTablature().getCaret().getMeasure();
		boolean running = TuxGuitar.instance().getPlayer().isRunning();
		this.tempo.setEnabled( !running );
        MidiPlayerMode mode = TuxGuitar.instance().getPlayer().getMode();
        int percent = mode.getSimplePercent();
        for (int i = 0; i < DEFAULT_PERCENTS.length; i++)
            if (percent == DEFAULT_PERCENTS[i])
                this.tempoPercentage.select(i);
        if (mode.isLoop() && mode.getType() == MidiPlayerMode.TYPE_CUSTOM) {
            this.tempoPercentage.setText(Integer.toString(mode.getCurrentPercent()) + "%");
        }
        this.tempoPercentage.setEnabled( !running );
		this.timeSignature.setEnabled( !running );
		this.repeatOpen.setEnabled( !running );
		this.repeatOpen.setSelection(measure != null && measure.isRepeatOpen());
		this.repeatClose.setEnabled( !running );
		this.repeatClose.setSelection(measure != null && measure.getRepeatClose() > 0);
		this.repeatAlternative.setEnabled( !running );
		this.repeatAlternative.setSelection(measure != null && measure.getHeader().getRepeatAlternative() > 0);
	}

    private class TempoPercentageAdapter extends SelectionAdapter
    {
        private ComboToolItem percentageWidget;
        
        public TempoPercentageAdapter(ComboToolItem item)
        {
            this.percentageWidget = item;
        }
        
        public void widgetSelected(SelectionEvent e)
        {
            int index = this.percentageWidget.getSelectionIndex();
            MidiPlayerMode mode = TuxGuitar.instance().getPlayer().getMode();
            mode.setSimplePercent(DEFAULT_PERCENTS[index]);
            mode.reset();
        }
    }
}
