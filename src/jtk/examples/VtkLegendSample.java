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
package jtk.examples;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import jtk.core.VTKRenderPanel;
import vtk.vtkActor;
import vtk.vtkFloatArray;
import vtk.vtkLookupTable;
import vtk.vtkNativeLibrary;
import vtk.vtkPNGWriter;
import vtk.vtkPolyData;
import vtk.vtkPolyDataMapper;
import vtk.vtkRenderer;
import vtk.vtkScalarBarActor;
import vtk.vtkScalarBarRepresentation;
import vtk.vtkScalarBarWidget;
import vtk.vtkSphereSource;
import vtk.vtkWindowToImageFilter;

public class VtkLegendSample {

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

        renderer.GradientBackgroundOn();
        renderer.SetBackground(1, 1, 1);
        renderer.SetBackground2(0, 0, 0);

        // Create a lookup table to share between the mapper and the scalarbar
        vtkLookupTable hueLut = new vtkLookupTable();
        hueLut.SetHueRange(0.7, 0);
        hueLut.Build();

        final vtkScalarBarActor scalarBar = new vtkScalarBarActor();
        scalarBar.SetLookupTable(hueLut);
        scalarBar.SetTitle("Title");
        scalarBar.SetNumberOfLabels(4);
        scalarBar.GetPositionCoordinate().SetCoordinateSystemToNormalizedDisplay();
        scalarBar.GetTitleTextProperty().SetFontFamilyToArial();
        scalarBar.GetTitleTextProperty().ItalicOff();
        scalarBar.GetTitleTextProperty().SetJustificationToCentered();
        scalarBar.SetMaximumHeightInPixels(500);
        scalarBar.SetMaximumWidthInPixels(100);

        scalarBar.VisibilityOn();
        //
        vtkScalarBarRepresentation barRep = new vtkScalarBarRepresentation();
        barRep.SetScalarBarActor(scalarBar);
        barRep.SetOrientation(1);
        barRep.GetPositionCoordinate().SetValue(0.8, 0.2);
        barRep.GetPosition2Coordinate().SetValue(0.2, 0.6);

        final vtkScalarBarWidget widget = new vtkScalarBarWidget();
        widget.SetInteractor(panel.getRenderWindowInteractor());
        widget.SetScalarBarActor(scalarBar);
        widget.SetDefaultRenderer(renderer);
        widget.SetCurrentRenderer(renderer);
        widget.SetRepresentation(barRep);    

        // actor1.GetMapper().SetLookupTable(hueLut);

        widget.On();

        // Add the actors to the scene
        // Create an interactor
        vtkActor actor1 = getActor1();
        vtkActor actor2 = getActor2();
        renderer.AddActor(actor1);
        renderer.AddActor(actor2);
        // renderer.AddActor2D(scalarBar);

        // Timer timer = new Timer(true);
        // timer.schedule(new TimerTask() {
        // @Override
        // public void run() {
        // scalarBar.SetTitle(scalarBar.GetTitle() + "M");
        // System.out.println("TITLE: " + scalarBar.GetTitle());
        // panel.renderLater();
        // }
        //
        // }, 5000, 2000);

        frame.setSize(800, 800);
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.getContentPane().add(getButtonsPanel(renderer), BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private static JPanel getButtonsPanel(final vtkRenderer renderer) {
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(new JButton(new AbstractAction("PRINT") {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                vtkWindowToImageFilter filter = new vtkWindowToImageFilter();
                filter.SetInput(renderer.GetVTKWindow());
                filter.Update();
                vtkPNGWriter jw = new vtkPNGWriter();
                
                DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
                //get current date time with Date()
                Date date = new Date();
                
                jw.SetFileName("C:\\Users\\davide\\Desktop\\" + dateFormat.format(date) + ".png");
                jw.SetInputConnection(filter.GetOutputPort());
                jw.Write();
                jw.Delete();
                filter.Delete();
                
            }
        }));
        return panel;
    }

    private static vtkActor getActor1() {
        vtkSphereSource sphere = new vtkSphereSource();
        sphere.SetCenter(-1, 0, 0);
        sphere.SetRadius(1);
        sphere.Update();

        // Create scalar data to associate with the vertices of the sphere
        int numPts = sphere.GetOutput().GetPoints().GetNumberOfPoints();
        vtkFloatArray scalars = new vtkFloatArray();
        scalars.SetNumberOfValues(numPts);
        for (int i = 0; i < numPts; ++i) {
            scalars.SetValue(i, (i) / numPts);
        }
        vtkPolyData poly = new vtkPolyData();
        poly.DeepCopy(sphere.GetOutput());
        poly.GetPointData().SetScalars(scalars);

        vtkPolyDataMapper mapper = new vtkPolyDataMapper();
        mapper.SetInputData(poly);
        mapper.ScalarVisibilityOn();
        mapper.SetScalarModeToUsePointData();
        mapper.SetColorModeToMapScalars();

        vtkActor actor = new vtkActor();
        actor.SetMapper(mapper);
        return actor;
    }

    private static vtkActor getActor2() {
        vtkSphereSource sphere = new vtkSphereSource();
        sphere.SetCenter(1, 0, 0);
        sphere.SetRadius(1);
        sphere.Update();

        // Create scalar data to associate with the vertices of the sphere
        int numPts = sphere.GetOutput().GetPoints().GetNumberOfPoints();
        vtkFloatArray scalars = new vtkFloatArray();
        scalars.SetNumberOfValues(numPts);
        for (int i = 0; i < numPts; ++i) {
            scalars.SetValue(i, (i) / numPts);
        }
        vtkPolyData poly = new vtkPolyData();
        poly.DeepCopy(sphere.GetOutput());
        poly.GetPointData().SetScalars(scalars);

        vtkPolyDataMapper mapper = new vtkPolyDataMapper();
        mapper.SetInputData(poly);
        mapper.ScalarVisibilityOn();
        mapper.SetScalarModeToUsePointData();
        mapper.SetColorModeToMapScalars();

        vtkActor actor = new vtkActor();
        actor.SetMapper(mapper);
        return actor;
    }
}
