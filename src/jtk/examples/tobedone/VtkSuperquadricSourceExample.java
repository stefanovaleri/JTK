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
package jtk.examples.tobedone;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import jtk.core.VTKRenderPanel;
import vtk.vtkActor;
import vtk.vtkNativeLibrary;
import vtk.vtkPolyDataMapper;
import vtk.vtkRenderer;
import vtk.vtkSuperquadricSource;

public class VtkSuperquadricSourceExample {

    static {
        vtkNativeLibrary.LoadAllNativeLibraries();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());

        final VTKRenderPanel panel = new VTKRenderPanel();

        // Create a renderer and render window
        final vtkRenderer renderer = panel.GetRenderer();

        renderer.AddActor(getActor());

        frame.setSize(800, 800);
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.getContentPane().add(getButtonsPanel(renderer), BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private static JPanel getButtonsPanel(final vtkRenderer renderer) {
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(new JButton(new AbstractAction("Print") {

            
            
            @Override
            public void actionPerformed(ActionEvent e) {
                
            }
        }));
        return panel;
    }

    private static vtkActor getActor() {
        vtkSuperquadricSource ring = new vtkSuperquadricSource();
        ring.SetCenter(0, 0, 0);
        
        /*
         * Proporzione tra i 2 diametri e l'altezza
         */
        ring.SetScale(1, 1, 1);
        
        /*
         * Set the number of points in the longitude direction. Initial value is 1.0
         */
        ring.SetThetaResolution(1024);
        
        /*
         * Set the number of points in the latitude direction. Initial value is 1.0
         */
        ring.SetPhiResolution(1024);

        /*
         * Set/Get Superquadric east/west roundness. Values range from 0
         * (rectangular) to 1 (circular) to higher orders. Initial value is 1.0.
         */
        ring.SetThetaRoundness(1);

        /*
         * Set/Get Superquadric north/south roundness. Values range from 0 (rectangular) to 1 (circular) to higher orders. Initial value is 1.0. 
         */
        ring.SetPhiRoundness(0);
        
        /*
         * Set/Get Superquadric isotropic size. Initial value is 0.5;  
         */
        ring.SetSize(0.4);
        
        ring.ToroidalOn();
        ring.Update();

        vtkPolyDataMapper mapper = new vtkPolyDataMapper();
        mapper.SetInputData(ring.GetOutput());

        vtkActor actor = new vtkActor();
        actor.SetMapper(mapper);
        return actor;
    }
}
