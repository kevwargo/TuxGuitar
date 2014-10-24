package org.herac.tuxguitar.gui.table;

import org.eclipse.swt.widgets.Composite;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.widgets.Button;
import org.herac.tuxguitar.song.models.TGTrack;
import org.herac.tuxguitar.gui.TuxGuitar;
import org.herac.tuxguitar.gui.editors.TGUpdateListener;
import org.herac.tuxguitar.gui.mixer.TGMixer;
import org.herac.tuxguitar.song.models.TGChannel;
import org.herac.tuxguitar.gui.editors.TGRedrawListener;
import org.herac.tuxguitar.gui.undo.undoables.track.UndoableTrackSoloMute;


public class TGMiniMixer implements TGUpdateListener, TGRedrawListener
{
    private TGTable table;
    private List<MiniTrackMixer> tracks;

    private class MiniTrackMixer extends SelectionAdapter
    {
        private TGTrack track;
        private Button muteCheckbox;
        private Button soloCheckbox;

        public MiniTrackMixer(TGTrack track, Button mcb, Button scb)
        {
            super();
            this.track = track;
            this.muteCheckbox = mcb;
            this.soloCheckbox = scb;
            this.muteCheckbox.addSelectionListener(this);
            this.soloCheckbox.addSelectionListener(this);
        }

        public Button getMuteCheckbox()
        {
            return this.muteCheckbox;
        }
        
        public Button getSoloCheckbox()
        {
            return this.soloCheckbox;
        }

        public TGTrack getTrack()
        {
            return this.track;
        }
        
        public void setTrack(TGTrack track)
        {
            this.track = track;
        }

        @Override
        public void widgetSelected(SelectionEvent e)
        {
            if (e.getSource() != null)
            {
                if (e.getSource() == this.muteCheckbox)
                    this.fireChanges(TGMixer.MUTE);
                else if (e.getSource() == this.soloCheckbox)
                    this.fireChanges(TGMixer.SOLO);
                ((Button)e.getSource()).getParent().setFocus();
            }
        }
        
        private void fireChanges(int type)
        {
            UndoableTrackSoloMute undoable = UndoableTrackSoloMute.startUndo(track);
            if (type == TGMixer.MUTE)
                TuxGuitar.instance().getSongManager().getTrackManager().changeMute(
                    track, this.muteCheckbox.getSelection());
            else if (type == TGMixer.SOLO)
                TuxGuitar.instance().getSongManager().getTrackManager().changeSolo(
                    track, this.soloCheckbox.getSelection());
            if (! TuxGuitar.instance().getMixer().isDisposed())
                TuxGuitar.instance().getMixer().fireChanges(track.getChannel(), type);
            if (TuxGuitar.instance().getPlayer().isRunning())
                TuxGuitar.instance().getPlayer().updateControllers();
            TuxGuitar.instance().getUndoableManager().addEdit(undoable.endUndo(track));
            TuxGuitar.instance().updateCache(true);
        }
    }
    

    public TGMiniMixer(TGTable table)
    {
        this.table = table;
        this.init();
    }

    public void init()
    {
        tracks = new ArrayList<MiniTrackMixer>();
        updateMixer();
    }

    public void doUpdate(int type)
    {
        if (type == TGUpdateListener.SELECTION)
            this.updateSettings();
    }

    public void doRedraw(int type)
    {
        if( type == TGRedrawListener.NORMAL ||
            type == TGRedrawListener.PLAYING_NEW_BEAT )
			this.updateMixer();
	}

    public void updateMixer()
    {
        int count = table.getRowCount();
        for (int i = tracks.size(); i < count; i++)
            tracks.add(new MiniTrackMixer(
                           TuxGuitar.instance().getSongManager().getSong().getTrack(i),
                           table.getRow(i).getMuteCheckbox(),
                           table.getRow(i).getSoloCheckbox()));
        while (tracks.size() > count)
            tracks.remove(count);
        for (int i = 0; i < count; i++)
        {
            TGTrack track = TuxGuitar.instance().getSongManager().getSong().getTrack(i);
            tracks.get(i).setTrack(track);
            tracks.get(i).getMuteCheckbox().setSelection(track.isMute());
            tracks.get(i).getSoloCheckbox().setSelection(track.isSolo());
        }
        updateSettings();
    }

    public void updateSettings()
    {
        for (int i = 0; i < tracks.size(); i++)
        {
            TGTrack track = tracks.get(i).getTrack();
            tracks.get(i).getSoloCheckbox().setSelection(track.isSolo());
            tracks.get(i).getMuteCheckbox().setSelection(track.isMute());
        }
    }

}
