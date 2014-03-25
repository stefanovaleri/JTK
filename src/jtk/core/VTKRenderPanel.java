/*--------------------------------*- Java -*---------------------------------*\
 |o                                                                   |                                                                                     
 |    o     o       | HelyxOS: The Open Source GUI for OpenFOAM              |
 |   o   O   o      | Copyright (C) 2012-2013 ENGYS                          |
 |    o     o       | http://www.engys.com                                   |
 |       o          |                                                        |
 |---------------------------------------------------------------------------|
 |License                                                                    |
 |   This file is part of HelyxOS.                                           |
 |                                                                           |
 |   HelyxOS is free software; you can redistribute it and/or modify it      |
 |   under the terms of the GNU General Public License as published by the   |
 |   Free Software Foundation; either version 2 of the License, or (at your  |
 |   option) any later version.                                              |
 |                                                                           |
 |   HelyxOS is distributed in the hope that it will be useful, but WITHOUT  |
 |   ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or   |
 |   FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License   |
 |   for more details.                                                       |
 |                                                                           |
 |   You should have received a copy of the GNU General Public License       |
 |   along with HelyxOS; if not, write to the Free Software Foundation,      |
 |   Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA            |
\*---------------------------------------------------------------------------*/
package jtk.core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

import vtk.vtkActor;
import vtk.vtkActorCollection;
import vtk.vtkGenericRenderWindowInteractor;
import vtk.vtkInteractorStyleTrackballCamera;
import vtk.vtkPanel;
import vtk.vtkProperty;
import vtk.vtkRenderWindow;
import vtk.vtkRenderer;

public class VTKRenderPanel extends vtkPanel implements View3DPanel {

	private static final int DELAY = 2000;
	
	public class DelayTimer extends Timer implements ActionListener {

		public DelayTimer() {
			super(DELAY, null);
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent evt) {
			TimerEvent();
		}
	}

	public class CountdownTimer extends Timer implements ActionListener {
		
		private int counter = DELAY;
		
		public CountdownTimer() {
			super(UPDATE_RATE, null);
			addActionListener(this);
		}
		
		@Override
		public void stop() {
			super.stop();
			counter = DELAY;
		}

		public void actionPerformed(ActionEvent evt) {
//			System.out.println("Full rendering in "+(counter/1000.0)+"s");
			if (counter <= 0) {
				stop();
				return;
			}
			counter -= UPDATE_RATE;
		}
	}

	private static  final int UPDATE_RATE = 250;
	
    private static final double LOWEST_RATE = 0.001;
    private static final double LOW_RATE = 0.01;
    private static final double HIGH_RATE = 5.0;
    private static final double HIGHEST_RATE = 15000;

    private static final double SELECTION_OPACITY = 0.2;

    private vtkGenericRenderWindowInteractor iren;
    private vtkProperty SELECTION_PROPERTY = new vtkProperty();
    protected Timer timer = new DelayTimer();
    protected Timer countdown = new CountdownTimer();
    
    private HashMap<vtkActor, vtkProperty> selectionMap = new HashMap<vtkActor, vtkProperty>();
    private HashMap<vtkActor, Double> opacityMap = new HashMap<vtkActor, Double>();
    private HashMap<vtkActor, Integer> representationsMap = new HashMap<vtkActor, Integer>();

    private VTKMouseHandler handler;

	private vtkInteractorStyleTrackballCamera style;
//	private Timer fullRenderingTimer;
//	private Timer labelTimer;

    public VTKRenderPanel() {
        Initialize();
    }

    public void Delete() {
        iren = null;
        super.Delete();
    }

    public void lock() {
        Lock();
    }

    public void unlock() {
        UnLock();
    }
    
    @Override
    public void dispose() {
    	DestroyTimer();
    	
    	SELECTION_PROPERTY = null;
    	
    	iren.SetRenderWindow(null);
    	iren = null;
    	
//    	super.dispose();
    	
    	Initialize();
    }
    
    private void Initialize() {
        style = new vtkInteractorStyleTrackballCamera();
        
        iren = new vtkGenericRenderWindowInteractor();
        iren.SetRenderWindow(rw);
        iren.SetInteractorStyle(style);

        double[] color1 = new double[] { 0.2, 0.2, 0.2 };
        double[] color2 = new double[] { 0.6, 0.6, 0.6 };
        double[] colorSelection = new double[] {1, 1, 1};

        handler = new VTKMouseHandler(this);
        addMouseListener(handler);
        addMouseMotionListener(handler);
        addMouseWheelListener(handler);
        addKeyListener(handler);

        ren.GradientBackgroundOn();
        ren.SetBackground(color1);
        ren.SetBackground2(color2);
        ren.SetGradientBackground(true);
        
        SELECTION_PROPERTY = new vtkProperty();
        SELECTION_PROPERTY.EdgeVisibilityOn();
        SELECTION_PROPERTY.SetEdgeColor(colorSelection);
        SELECTION_PROPERTY.SetLineWidth(1);
        // SELECTION_PROPERTY.SetColor(WHITE_COLOR);
        SELECTION_PROPERTY.SetLighting(true);
        SELECTION_PROPERTY.SetOpacity(1);

        // new CenterOfRotationWidget(this);

//        iren.AddObserver("TimerEvent", this, "TimerEvent");
//        iren.AddObserver("CreateTimerEvent", this, "StartTimer");
//        iren.AddObserver("DestroyTimerEvent", this, "DestroyTimer");
        
        iren.SetDesiredUpdateRate(HIGHEST_RATE);
        iren.SetStillUpdateRate(LOW_RATE);
        iren.Start();

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent event) {
                updateSize(getWidth(), getHeight());
            }
        });
    }
    
    public vtkGenericRenderWindowInteractor getRenderWindowInteractor() {
        return this.iren;
    }

    private void updateSize(int w, int h) {
        if (windowset == 1) {
            Lock();
            iren.SetSize(w, h);
            // rw.SetSize(w, h);
            iren.ConfigureEvent();
            UnLock();
        }
    }

    @Override
    public synchronized void Render() {
        if (!rendering) {
            rendering = true;
            if (rw != null) {
                if (windowset == 0) {
                	createBufferStrategy(2);
                    // set the window id and the active camera
                    RenderCreate(rw);
                    Lock();
                    rw.SetSize(getWidth(), getHeight());
                    UnLock();
                    windowset = 1;
                    // notify observers that we have a renderwindow created
                    // windowSetObservable.notifyObservers();
                }
//                System.out.println("VTKRenderPanel.Render()");
                Lock();
                rw.Render();
                UnLock();
            }
            rendering = false;
        }
    }

    public void renderLater() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Lock();
                Render();
                UnLock();
            }
        });
    }

    public void renderAndWait() {
    	try {
			SwingUtilities.invokeAndWait(new Runnable() {
			    @Override
			    public void run() {
			        Lock();
			        Render();
			        UnLock();
			    }
			});
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }

    public void resetZoomLater() {
    	SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	zoomReset();
            }
        });
    }

    public void resetZoomAndWait() {
    	try {
			SwingUtilities.invokeAndWait(new Runnable() {
			    @Override
			    public void run() {
			        zoomReset();
			    }
			});
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }

    @Override
    public void setHighRendering() {
//    	System.out.println("VTKRenderPanel.setHighRendering()");
    	if (iren == null) return; 
    	StartTimer();
    }

    @Override
    public void setLowRendering() {
    	if (iren == null) return;
    	DestroyTimer();
//    	System.out.println("VTKRenderPanel.setLowRendering()");
    	Lock();
        iren.SetStillUpdateRate(HIGHEST_RATE);
		iren.LeftButtonPressEvent();
		iren.LeftButtonReleaseEvent();
        UnLock();
    }

    public void TimerEvent() {
//    	System.out.println("VTKRenderPanel.TimerEvent()");
		Lock();
		iren.SetStillUpdateRate(LOW_RATE);
		iren.LeftButtonPressEvent();
		iren.LeftButtonReleaseEvent();
		UnLock();
//		renderAndWait();
    }
    
    public void StartTimer() {
//    	System.out.println("VTKRenderPanel.StartTimer()");
        if (timer.isRunning()) {
        	timer.stop();
        	countdown.stop();
        }

        countdown.start();
        
        timer.setDelay(DELAY);
        timer.setRepeats(false);
        timer.start();
    }

    public void DestroyTimer() {
//    	System.out.println("VTKView3DPanel.DestroyTimer()");
        if (timer.isRunning()) {
        	timer.stop();
        	countdown.stop();
        }
    }
    @Override
    public void wheelForward() {
    	if (iren == null) return; 
        Lock();
        iren.MouseWheelForwardEvent();
        UnLock();
    }

    @Override
    public void wheelBackward() {
    	if (iren == null) return; 
        Lock();
        iren.MouseWheelBackwardEvent();
        UnLock();
    }

    @Override
    public void zoomReset() {
    	if (iren == null) return; 
        Lock();
        GetRenderer().ResetCamera();
        Render();
        UnLock();
    }

    public enum Position {
        X_POS, X_NEG, Y_POS, Y_NEG, Z_POS, Z_NEG
    }

    @Override
    public void setCameraPosition(Position pos) {
        vtkRenderer renderer = GetRenderer();
        double[] fp = renderer.GetActiveCamera().GetFocalPoint();
        double[] p = renderer.GetActiveCamera().GetPosition();
        double dist = Math.sqrt(Math.pow(p[0] - fp[0], 2) + Math.pow(p[1] - fp[1], 2) + Math.pow(p[2] - fp[2], 2));

        Lock();
        switch (pos) {
        case X_POS:
            renderer.GetActiveCamera().SetPosition(fp[0] - dist, fp[1], fp[2]);
            renderer.GetActiveCamera().SetViewUp(0, 0, 1);
            break;
        case X_NEG:
            renderer.GetActiveCamera().SetPosition(fp[0] + dist, fp[1], fp[2]);
            renderer.GetActiveCamera().SetViewUp(0, 0, 1);
            break;
        case Y_POS:
            renderer.GetActiveCamera().SetPosition(fp[0], fp[1] - dist, fp[2]);
            renderer.GetActiveCamera().SetViewUp(0, 0, 1);
            break;
        case Y_NEG:
            renderer.GetActiveCamera().SetPosition(fp[0], fp[1] + dist, fp[2]);
            renderer.GetActiveCamera().SetViewUp(0, 0, 1);
            break;
        case Z_POS:
            renderer.GetActiveCamera().SetPosition(fp[0], fp[1], fp[2] - dist);
            renderer.GetActiveCamera().SetViewUp(0, 1, 0);
            break;
        case Z_NEG:
            renderer.GetActiveCamera().SetPosition(fp[0], fp[1], fp[2] + dist);
            renderer.GetActiveCamera().SetViewUp(0, 1, 0);
            break;
        }
        Render();
        UnLock();
    }

    @Override
    public void addActor(vtkActor actor) {
        //VTKUtil.setRepresentation(representation, actor);
        GetRenderer().AddActor(actor);
    }

    @Override
    public void clearSelection() {
        restorePreviousSelection();
        restoreOpacityAndRepresentation();
    }

    public void selectActors(vtkActor... actors) {
    	List<vtkActor> notSelectedActors = purgeAlreadySelectedActors(actors);
    	if (actors.length > 0 && notSelectedActors.isEmpty()) {
    		return;
    	}
        Lock();
        restorePreviousSelection();
        restoreOpacityAndRepresentation();
        if (actors.length > 0) {
            setOpacityAndRepresentation();
            for (vtkActor actor : notSelectedActors) {
                _selectActor(actor);
            }
        }
        UnLock();
        renderAndWait();
    }

    private List<vtkActor> purgeAlreadySelectedActors(vtkActor[] actors) {
    	List<vtkActor> notSelectedActors = new ArrayList<>();
    	for (vtkActor vtkActor : actors) {
			if (!selectionMap.containsKey(vtkActor)) {
				notSelectedActors.add(vtkActor);
			}
		}
		return notSelectedActors;
	}

	void restorePreviousSelection() {
        for (vtkActor selectedActor : selectionMap.keySet()) {
            restoreProperty(selectedActor);
        }
        selectionMap.clear();
    }

    private void restoreProperty(vtkActor selectedActor) {
        // System.out.println("VTKView3DPanel.restoreProperty()");
        vtkProperty prop = selectionMap.get(selectedActor);
        selectedActor.GetProperty().SetColor(prop.GetColor());
        selectedActor.GetProperty().SetEdgeColor(prop.GetEdgeColor());
        selectedActor.GetProperty().SetLineWidth(prop.GetLineWidth());
        selectedActor.GetProperty().SetLighting(prop.GetLighting());
        selectedActor.GetProperty().SetRepresentation(prop.GetRepresentation());
        selectedActor.GetProperty().SetEdgeVisibility(prop.GetEdgeVisibility());
        selectedActor.GetProperty().SetOpacity(prop.GetOpacity());
    }

    private void restoreOpacityAndRepresentation() {
        // System.out.println("VTKView3DPanel.restoreOpacity()");
        for (vtkActor actor : opacityMap.keySet()) {
            Double opacity = opacityMap.get(actor);
            actor.GetProperty().SetOpacity(opacity.doubleValue());
        }
        for (vtkActor actor : representationsMap.keySet()) {
            Integer representation = representationsMap.get(actor);
            actor.GetProperty().SetRepresentation(representation.intValue());
        }
        opacityMap.clear();
        representationsMap.clear();
    }

    private void setOpacityAndRepresentation() {
        vtkActorCollection actors = ren.GetActors();
        for (int a = 0; a < actors.GetNumberOfItems(); a++) {
            vtkActor actor = (vtkActor) actors.GetItemAsObject(a);
            if (actor != null) {
                opacityMap.put(actor, actor.GetProperty().GetOpacity());
                representationsMap.put(actor, actor.GetProperty().GetRepresentation());
                actor.GetProperty().SetOpacity(SELECTION_OPACITY);
                actor.GetProperty().SetRepresentationToSurface();
            }
        }
    }

    private void _selectActor(vtkActor selectedActor) {
        // System.out.println("VTKView3DPanel._selectActor()");
        if (selectedActor != null) {
            saveActorProperty(selectedActor);
            applySelectionProperty(selectedActor);
        }
    }

    void saveActorProperty(vtkActor selectedActor) {
        vtkProperty prop = new vtkProperty();
        prop.SetColor(selectedActor.GetProperty().GetColor());
        prop.SetEdgeColor(selectedActor.GetProperty().GetEdgeColor());
        prop.SetLineWidth(selectedActor.GetProperty().GetLineWidth());
        prop.SetLighting(selectedActor.GetProperty().GetLighting());
        prop.SetRepresentation(selectedActor.GetProperty().GetRepresentation());
        prop.SetEdgeVisibility(selectedActor.GetProperty().GetEdgeVisibility());
        prop.SetOpacity(selectedActor.GetProperty().GetOpacity());

        selectionMap.put(selectedActor, prop);
    }

    private void applySelectionProperty(vtkActor selectedActor) {
        selectedActor.GetProperty().SetEdgeVisibility(SELECTION_PROPERTY.GetEdgeVisibility());
        selectedActor.GetProperty().SetEdgeColor(SELECTION_PROPERTY.GetEdgeColor());
        selectedActor.GetProperty().SetLineWidth(SELECTION_PROPERTY.GetLineWidth());
        selectedActor.GetProperty().SetLighting(SELECTION_PROPERTY.GetLighting());
        selectedActor.GetProperty().SetOpacity(SELECTION_PROPERTY.GetOpacity());
    }

    public void setActorColor(Color c, vtkActor... actors) {
        if (c == null)
            return;

        double[] color = new double[] { c.getRed() / 255.0, c.getGreen() / 255.0, c.getBlue() / 255.0 };
        double opacity = c.getAlpha() / 255.0;

        for (vtkActor actor : actors) {
            if (actor == null)
                continue;
            if (selectionMap.containsKey(actor)) {
                vtkProperty prop = selectionMap.get(actor);
                prop.SetColor(color);
                prop.SetOpacity(opacity);
                prop.SetRepresentationToSurface();
            }
            vtkProperty prop = actor.GetProperty();
            prop.SetColor(color);
            prop.SetOpacity(opacity);
            prop.SetRepresentationToSurface();
        }
    }

    @Override
    public void setActorVisible(boolean b, vtkActor... actors) {
        Lock();
        for (vtkActor actor : actors) {
            if (actor != null) {
                if (b) {
                    actor.VisibilityOn();
                } else {
                    actor.VisibilityOff();
                }
            }
        }
        Render();
        UnLock();
    }

    @Override
    public void clear() {
        selectionMap.clear();
        opacityMap.clear();
        representationsMap.clear();
    }

    Representation representation = Representation.WIREFRAME;

    public void setRepresentation(Representation representation) {
        this.representation = representation;
    }

    public Representation getRepresentation() {
        return representation;
    }

    @Override
    @Deprecated
    public vtkRenderer GetRenderer() {
        return super.GetRenderer();
    }

    @Override
    @Deprecated
    public vtkRenderWindow GetRenderWindow() {
        return super.GetRenderWindow();
    }

    @Override
    public void paint(Graphics g) {
        // System.out.println("VTKView3DPanel.paint()");
        super.paint(g);
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        repaint();
    }

}
