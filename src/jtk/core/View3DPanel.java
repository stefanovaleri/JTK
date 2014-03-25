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
import java.awt.event.KeyListener;

import jtk.core.VTKRenderPanel.Position;
import vtk.vtkActor;
import vtk.vtkRenderWindowInteractor;
import vtk.vtkRenderer;

public interface View3DPanel {

	vtkRenderWindowInteractor getRenderWindowInteractor();

	vtkRenderer GetRenderer();

	void lock();

	void Render();

	void unlock();

	void clear();

	void setCameraPosition(Position xPos);
	void resetCamera();

	void wheelForward();
	void wheelBackward();

	void zoomReset();
	void resetZoomLater();
	void resetZoomAndWait();

	void clearSelection();

	void setRepresentation(Representation surface);
	Representation getRepresentation(); 

	void renderLater();
	void renderAndWait();

	void addActor(vtkActor internalMeshActor);

	void selectActors(vtkActor... pickedActor);

	void setActorVisible(boolean visible, vtkActor... actor);

	void setLowRendering();
	void setHighRendering();

	void setActorColor(Color c, vtkActor... actor);

    void addKeyListener(KeyListener listener);

    void removeKeyListener(KeyListener listener);

	void dispose();

}
