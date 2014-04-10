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

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.SwingUtilities;

import vtk.vtkGenericRenderWindowInteractor;

public class VTKMouseHandler implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {

    private boolean isDragging = false;
    private VTKRenderPanel vtkPanel;

    public VTKMouseHandler(VTKRenderPanel vtkPanel) {
        this.vtkPanel = vtkPanel;
    }

    private int ctrlPressed(InputEvent e) {
        return (e.getModifiers() & InputEvent.CTRL_MASK) == InputEvent.CTRL_MASK ? 1 : 0;
    }

    private int shiftPressed(InputEvent e) {
        return (e.getModifiers() & InputEvent.SHIFT_MASK) == InputEvent.SHIFT_MASK ? 1 : 0;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // System.out.println("MOUSE_PRESSED");
        vtkPanel.setLowRendering();

        vtkPanel.lock();
        vtkGenericRenderWindowInteractor iren = vtkPanel.getRenderWindowInteractor();
        iren.SetEventInformationFlipY(e.getX(), e.getY(), ctrlPressed(e), shiftPressed(e), '0', 0, "0");

        if ((e.getModifiers() & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK) {
            iren.LeftButtonPressEvent();
        } else if ((e.getModifiers() & InputEvent.BUTTON2_MASK) == InputEvent.BUTTON2_MASK) {
            iren.MiddleButtonPressEvent();
        } else if ((e.getModifiers() & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK) {
            iren.SetEventInformation(e.getX(), e.getY(), ctrlPressed(e), shiftPressed(e), '0', 0, "0");
            iren.RightButtonPressEvent();
        }

        vtkPanel.unlock();

        isDragging = false;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    	vtkGenericRenderWindowInteractor iren = vtkPanel.getRenderWindowInteractor();
        iren.SetEventInformationFlipY(e.getX(), e.getY(), ctrlPressed(e), shiftPressed(e), '0', 0, "0");

        vtkPanel.lock();

        if ((e.getModifiers() & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK) {
            iren.LeftButtonReleaseEvent();
        } else if ((e.getModifiers() & InputEvent.BUTTON2_MASK) == InputEvent.BUTTON2_MASK) {
            iren.MiddleButtonReleaseEvent();
        } else if ((e.getModifiers() & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK) {
            iren.RightButtonReleaseEvent();
        }

        vtkPanel.unlock();

        if (!isDragging) {
            int[] pos = iren.GetEventPosition();
            //PickManager.pickTo(vtkPanel).pick(pos[0], pos[1]);
        }

        vtkPanel.setHighRendering();

        isDragging = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    	vtkGenericRenderWindowInteractor iren = vtkPanel.getRenderWindowInteractor();
        // System.out.println("MOUSE_ENTERED");
        // this.requestFocus();
        iren.SetEventInformationFlipY(e.getX(), e.getY(), 0, 0, '0', 0, "0");
        iren.EnterEvent();
    }

    @Override
    public void mouseExited(MouseEvent e) {
    	vtkGenericRenderWindowInteractor iren = vtkPanel.getRenderWindowInteractor();
        // System.out.println("MOUSE_EXITED");
        iren.SetEventInformationFlipY(e.getX(), e.getY(), 0, 0, '0', 0, "0");
        iren.LeaveEvent();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    	vtkGenericRenderWindowInteractor iren = vtkPanel.getRenderWindowInteractor();
        iren.SetEventInformationFlipY(e.getX(), e.getY(), ctrlPressed(e), shiftPressed(e), '0', 0, "0");

        vtkPanel.lock();
        iren.MouseMoveEvent();
        vtkPanel.unlock();
        isDragging = false;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // System.out.println("MOUSE_DRAGGED");
    	vtkGenericRenderWindowInteractor iren = vtkPanel.getRenderWindowInteractor();
        if (SwingUtilities.isRightMouseButton(e)) {
            iren.SetEventInformation(e.getX(), e.getY(), ctrlPressed(e), shiftPressed(e), '0', 0, "0");
        } else {
            iren.SetEventInformationFlipY(e.getX(), e.getY(), ctrlPressed(e), shiftPressed(e), '0', 0, "0");
        }

        vtkPanel.lock();
        iren.MouseMoveEvent();
        vtkPanel.unlock();

        isDragging = true;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        // System.out.println("MOUSE_WHEEL_MOVED");
        vtkPanel.setLowRendering();
        vtkGenericRenderWindowInteractor iren = vtkPanel.getRenderWindowInteractor();
        iren.SetEventInformationFlipY(e.getX(), e.getY(), ctrlPressed(e), shiftPressed(e), '0', 0, "0");

        vtkPanel.lock();
        if (e.getWheelRotation() < 0)
            iren.MouseWheelForwardEvent();
        else
            iren.MouseWheelBackwardEvent();
        vtkPanel.unlock();
        vtkPanel.setHighRendering();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
