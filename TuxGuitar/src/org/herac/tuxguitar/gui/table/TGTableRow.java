package org.herac.tuxguitar.gui.table;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Spinner;
import org.herac.tuxguitar.song.models.TGTrack;


public class TGTableRow {
	private TGTable table;
	private Composite row;
	private CLabel number;
	private CLabel name;
	private CLabel instrument;
    private Spinner volumeControl;
    private Button visibleInMultitrackCheckbox;
    private Button soloCheckbox;
    private Button muteCheckbox;
	private Composite painter;
	private MouseListener mouseListenerLabel;
	private MouseListener mouseListenerCanvas;
	private PaintListener paintListenerCanvas;
	
	public TGTableRow(TGTable table){
		this.table = table;
		this.init();
	}
	
	public void init(){
		MouseListener mouseListenerLabel = new MouseListenerLabel();
		MouseListener mouseListenerCanvas = new MouseListenerCanvas();
		PaintListener paintListenerCanvas = new PaintListenerCanvas();

		this.row = new Composite(this.table.getRowControl(),SWT.NONE );
		this.row.setLayoutData(new GridData(SWT.FILL,SWT.TOP,true,false));
		
		this.number = new CLabel(this.row,SWT.LEFT);
		this.number.addMouseListener(mouseListenerLabel);
		this.table.addRowItem(this.table.getColumnNumber(),this.number,true);

        this.visibleInMultitrackCheckbox = new Button(this.row, SWT.CHECK | SWT.NO_FOCUS);
        this.table.addRowItem(this.table.getColumnVIMT(),
                              this.visibleInMultitrackCheckbox, true);
		
		this.name = new CLabel(this.row,SWT.LEFT);
		this.name.addMouseListener(mouseListenerLabel);
		this.table.addRowItem(this.table.getColumnName(),this.name,true);
		
		this.instrument = new CLabel(this.row,SWT.LEFT);
		this.instrument.addMouseListener(mouseListenerLabel);
		this.table.addRowItem(this.table.getColumnInstrument(),this.instrument,true);

        this.volumeControl = new Spinner(this.row, SWT.CENTER);
        this.table.addRowItem(this.table.getColumnVolume(), this.volumeControl, true);

        this.soloCheckbox = new Button(this.row, SWT.CHECK | SWT.NO_FOCUS);
        this.table.addRowItem(this.table.getColumnSolo(), this.soloCheckbox, true);
        
        this.muteCheckbox = new Button(this.row, SWT.CHECK | SWT.NO_FOCUS);
        this.table.addRowItem(this.table.getColumnMute(), this.muteCheckbox, true);
		
		this.painter = new Composite(this.row,SWT.DOUBLE_BUFFERED);
		this.painter.addMouseListener(mouseListenerCanvas);
		this.painter.addPaintListener(paintListenerCanvas);
		this.table.addRowItem(this.table.getColumnCanvas(),this.painter,false);

		this.row.pack();
	}
	
	public void setBackground(Color background){
		this.number.setBackground(background);
		this.name.setBackground(background);
		this.instrument.setBackground(background);
        this.muteCheckbox.setBackground(background);
	}
	
	public void dispose(){
		this.row.dispose();
	}
	
	public Composite getPainter() {
		return this.painter;
	}
	
	public CLabel getInstrument() {
		return this.instrument;
	}
	
	public CLabel getName() {
		return this.name;
	}
	
	public CLabel getNumber() {
		return this.number;
	}

    public Button getVisibleInMultitrackCheckbox()
    {
        return this.visibleInMultitrackCheckbox;
    }

    public Spinner getVolumeControl() {
        return this.volumeControl;
    }
    
    public Button getSoloCheckbox() {
        return this.soloCheckbox;
    }

    public Button getMuteCheckbox() {
        return this.muteCheckbox;
    }
	
	public MouseListener getMouseListenerLabel() {
		return this.mouseListenerLabel;
	}
	
	public void setMouseListenerLabel(MouseListener mouseListenerLabel) {
		this.mouseListenerLabel = mouseListenerLabel;
	}
	
	public MouseListener getMouseListenerCanvas() {
		return this.mouseListenerCanvas;
	}
	
	public void setMouseListenerCanvas(MouseListener mouseListenerCanvas) {
		this.mouseListenerCanvas = mouseListenerCanvas;
	}
	
	public PaintListener getPaintListenerCanvas() {
		return this.paintListenerCanvas;
	}
	
	public void setPaintListenerCanvas(PaintListener paintListenerCanvas) {
		this.paintListenerCanvas = paintListenerCanvas;
	}
	
	private class MouseListenerLabel implements MouseListener{
		
		public MouseListenerLabel(){
			super();
		}
		
		public void mouseDoubleClick(MouseEvent e) {
			if(getMouseListenerLabel() != null){
				getMouseListenerLabel().mouseDoubleClick(e);
			}
		}
		
		public void mouseDown(MouseEvent e) {
			if(getMouseListenerLabel() != null){
				getMouseListenerLabel().mouseDown(e);
			}
		}
		
		public void mouseUp(MouseEvent e) {
			if(getMouseListenerLabel() != null){
				getMouseListenerLabel().mouseUp(e);
			}
		}
	}
	
	private class MouseListenerCanvas implements MouseListener{
		
		public MouseListenerCanvas(){
			super();
		}
		
		public void mouseDoubleClick(MouseEvent e) {
			if(getMouseListenerCanvas() != null){
				getMouseListenerCanvas().mouseDoubleClick(e);
			}
		}
		
		public void mouseDown(MouseEvent e) {
			if(getMouseListenerCanvas() != null && e.button != 4 && e.button != 5){
                System.out.println("canvas mouse down " + e.button);
                
				getMouseListenerCanvas().mouseDown(e);
			}
		}
		
		public void mouseUp(MouseEvent e) {
			if(getMouseListenerCanvas() != null && e.button != 4 && e.button != 5){
                System.out.println("canvas mouse up " + e.button);
				getMouseListenerCanvas().mouseUp(e);
			}
		}
	}
	
	private class PaintListenerCanvas implements PaintListener{
		
		public PaintListenerCanvas(){
			super();
		}
		
		public void paintControl(PaintEvent e) {
			if(getPaintListenerCanvas() != null){
				getPaintListenerCanvas().paintControl(e);
			}
		}
	}
}
