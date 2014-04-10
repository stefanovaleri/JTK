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
package vtk;

import java.awt.Canvas;
import java.awt.Graphics;

import javax.swing.SwingUtilities;

public class vtkPanel extends Canvas {
	
    private static final long serialVersionUID = 1L;
    
    protected vtkRenderWindow rw;
    protected vtkRenderer ren;
    protected int windowset = 0;
    protected boolean rendering = false;

    public void dispose() {
    	rendering = true;

    	ren.RemoveAllLights();
    	ren.RemoveAllObservers();
    	ren.RemoveAllViewProps();

    	rw.RemoveRenderer(ren);

    	ren.Delete();
    	rw.Delete();

    	ren = null;
    	rw = null;

    	vtkObject.JAVA_OBJECT_MANAGER.deleteAll();

    	rw = new vtkRenderWindow();
    	ren = new vtkRenderer();

    	rw.AddRenderer(ren);
    	windowset = 0;
    	rendering = false;
    }
    
    public void Delete() {
        if (rendering) {
            return;
        }
        rendering = true;
        // We prevent any further rendering

        if (this.getParent() != null) {
            this.getParent().remove(this);
        }
        // Free internal VTK objects
        ren = null;
        // On linux we prefer to have a memory leak instead of a crash
        if (!rw.GetClassName().equals("vtkXOpenGLRenderWindow")) {
            rw = null;
        } else {
            System.out.println("The renderwindow has been kept arount to prevent a crash");
        }
    }

    protected native int RenderCreate(vtkRenderWindow id0);

    protected native int Lock();

    protected native int UnLock();

    public vtkPanel() {
    	this.rw = new vtkRenderWindow();
        this.ren = new vtkRenderer();
        rw.AddRenderer(ren);
    }

    public void Report() {
        Runnable updateAComponent = new Runnable() {
            public void run() {
                Lock();
                System.out.println("direct rendering = " + (rw.IsDirect() == 1));
                System.out.println("opengl supported = " + (rw.SupportsOpenGL() == 1));
                System.out.println("report = " + rw.ReportCapabilities());
                UnLock();
            }
        };

        SwingUtilities.invokeLater(updateAComponent);

    }

    public vtkRenderer GetRenderer() {
        return ren;
    }

    public vtkRenderWindow GetRenderWindow() {
        return rw;
    }

    public void addNotify() {
        super.addNotify();
        windowset = 0;
        rendering = false;
        rw.SetForceMakeCurrent();
        repaint();
    }

    public void removeNotify() {
    	if(windowset == 0) {
            super.removeNotify();
            return;
        }
    	if(rw != null)
        {
    		Lock();
            rw.Finalize();
            UnLock();
            windowset = 0;
        }
    	rendering = true;
        super.removeNotify();
    }

    public synchronized void Render() {
        if (!rendering) {
            rendering = true;
            if (rw != null) {
                if (windowset == 0) {
                	createBufferStrategy(2);
                    // set the window id
                    RenderCreate(rw);
                    Lock();
                    rw.SetSize(getWidth(), getHeight());
                    UnLock();
                    windowset = 1;
                }
                Lock();
                rw.Render();
                UnLock();
            }
            rendering = false;
        }
    }

    public boolean isWindowSet() {
        return (this.windowset == 1);
    }

    public void paint(Graphics g) {
        this.Render();
    }

    public void update(Graphics g) {
        paint(g);
    }

    public void resetCameraClippingRange() {
        Lock();
        ren.ResetCameraClippingRange();
        UnLock();
    }

    public void resetCamera() {
        Lock();
        ren.ResetCamera();
        UnLock();
    }

}
