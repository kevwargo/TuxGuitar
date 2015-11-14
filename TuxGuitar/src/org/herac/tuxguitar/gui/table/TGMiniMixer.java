package org.herac.tuxguitar.gui.table;

import java.lang.Integer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Spinner;
import org.herac.tuxguitar.gui.TuxGuitar;
import org.herac.tuxguitar.gui.editors.TGRedrawListener;
import org.herac.tuxguitar.gui.editors.TGUpdateListener;
import org.herac.tuxguitar.gui.mixer.TGMixer;
import org.herac.tuxguitar.gui.undo.undoables.track.UndoableTrackSoloMute;
import org.herac.tuxguitar.song.models.TGChannel;
import org.herac.tuxguitar.song.models.TGTrack;


public class TGMiniMixer implements TGUpdateListener, TGRedrawListener
{
    private TGTable table;
    private List<MiniTrackMixer> tracks;

    private class MiniTrackMixer implements SelectionListener, ModifyListener
    {
        private TGTrack track;
        private Button visibleInMultitrackCheckbox;
        private Button muteCheckbox;
        private Button soloCheckbox;
        private Spinner volumeControl;
        private Composite painter;

        /* painter is passed because we need to set focus away from checkboxes
           when they are selected, and painter seems to be the only real good alternative
           as checkboxes and spinner map spacebar to themselves, causing unwanted behavior
         */
        public MiniTrackMixer(TGTrack track,
                              Button visibleInMultitrackCheckbox,
                              Button muteCheckbox,
                              Button soloCheckbox,
                              Spinner volumeControl,
                              Composite painter)
        {
            super();
            this.track = track;
            this.visibleInMultitrackCheckbox = visibleInMultitrackCheckbox;
            this.muteCheckbox = muteCheckbox;
            this.soloCheckbox = soloCheckbox;
            this.volumeControl = volumeControl;
            this.painter = painter;
            this.visibleInMultitrackCheckbox.addSelectionListener(this);
            this.muteCheckbox.addSelectionListener(this);
            this.soloCheckbox.addSelectionListener(this);
            this.volumeControl.addModifyListener(this);
            this.volumeControl.setMinimum(0);
            this.volumeControl.setMaximum(127);
        }
        
        public Button getVisibleInMultitrackCheckbox()
        {
            return this.visibleInMultitrackCheckbox;
        }

        public Button getMuteCheckbox()
        {
            return this.muteCheckbox;
        }
        
        public Button getSoloCheckbox()
        {
            return this.soloCheckbox;
        }

        public Spinner getVolumeControl()
        {
            return this.volumeControl;
        }

        public TGTrack getTrack()
        {
            return this.track;
        }
        
        public void setTrack(TGTrack track)
        {
            this.track = track;
        }

        public void widgetDefaultSelected(SelectionEvent e) { }

        public void widgetSelected(SelectionEvent e)
        {
            if (e.getSource() != null)
            {
                if (e.getSource().equals(this.muteCheckbox))
                    this.applySoloMute(TGMixer.MUTE);
                else if (e.getSource().equals(this.soloCheckbox))
                    this.applySoloMute(TGMixer.SOLO);
                else if (e.getSource().equals(this.visibleInMultitrackCheckbox))
                {
                    this.track.setVisibleInMultitrack(this.visibleInMultitrackCheckbox.getSelection());
                    // TuxGuitar.instance().fireUpdate();
                    TuxGuitar.instance().getTablatureEditor().
                        doRedraw(TGRedrawListener.NORMAL);
                }
                this.painter.setFocus();
            }
        }

        public void modifyText(ModifyEvent e)
        {
            applyVolumeChange();
        }
        
        private void applySoloMute(int type)
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

        public void applyVolumeChange()
        {
            TGChannel channel = track.getChannel();
            channel.setVolume((short)Integer.parseInt(volumeControl.getText()));
            if (! TuxGuitar.instance().getMixer().isDisposed())
                TuxGuitar.instance().getMixer().fireChanges(channel, TGMixer.CHANNEL);
            TGMiniMixer.this.applyChannelSettings(channel);
            TGMiniMixer.this.updateSettings();
            if (TuxGuitar.instance().getPlayer().isRunning())
                TuxGuitar.instance().getPlayer().updateControllers();
        }
    }
    

    public TGMiniMixer(TGTable table)
    {
        this.table = table;
        table.getColumnVIMT().setTitle("MT");
        table.getColumnVolume().setTitle("V");
        table.getColumnSolo().setTitle("S");
        table.getColumnMute().setTitle("M");
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
                           table.getRow(i).getVisibleInMultitrackCheckbox(),
                           table.getRow(i).getMuteCheckbox(),
                           table.getRow(i).getSoloCheckbox(),
                           table.getRow(i).getVolumeControl(),
                           table.getRow(i).getPainter()));
        while (tracks.size() > count)
            tracks.remove(count);
        for (int i = 0; i < count; i++)
        {
            TGTrack track = TuxGuitar.instance().getSongManager().getSong().getTrack(i);
            tracks.get(i).setTrack(track);
            tracks.get(i).getMuteCheckbox().setSelection(track.isMute());
            tracks.get(i).getSoloCheckbox().setSelection(track.isSolo());
            tracks.get(i).getVolumeControl().setSelection(track.getChannel().getVolume());
            tracks.get(i).getVisibleInMultitrackCheckbox().
                setSelection(track.isVisibleInMultitrack());
        }
    }

    public void updateSettings()
    {
        for (int i = 0; i < tracks.size(); i++)
        {
            TGTrack track = tracks.get(i).getTrack();
            tracks.get(i).getSoloCheckbox().setSelection(track.isSolo());
            tracks.get(i).getMuteCheckbox().setSelection(track.isMute());
            tracks.get(i).getVolumeControl().setSelection(track.getChannel().getVolume());
        }
    }

    public void applyChannelSettings(TGChannel channel)
    {
        for (int i = 0; i < tracks.size(); i++)
        {
            TGTrack track = tracks.get(i).getTrack();
            if (track.getChannel().getChannel() == channel.getChannel())
                channel.copy(track.getChannel());
        }
    }
}
