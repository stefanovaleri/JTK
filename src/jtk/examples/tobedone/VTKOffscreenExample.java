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
//package eu.engys.gui.vtk.depot;
//
//import vtk.vtkActor;
//import vtk.vtkGraphicsFactory;
//import vtk.vtkImagingFactory;
//import vtk.vtkPNGWriter;
//import vtk.vtkPolyDataMapper;
//import vtk.vtkRenderWindow;
//import vtk.vtkRenderer;
//import vtk.vtkSphereSource;
//import vtk.vtkWindowToImageFilter;
//import eu.engys.gui.vtk.VTKUtil;
//
//public class VTKOffscreenExample {
//
//    public static void main(String[] args) {
//        VTKUtil.LoadAllNativeLibraries();
//        // Setup offscreen rendering
//        vtkGraphicsFactory graphics_factory = new vtkGraphicsFactory();
//        graphics_factory.SetOffScreenOnlyMode(1);
//        graphics_factory.SetUseMesaClasses(1);
//
//        vtkImagingFactory imaging_factory = new vtkImagingFactory();
//        imaging_factory.SetUseMesaClasses(1);
//
//        // Create a sphere
//        vtkSphereSource sphereSource = new vtkSphereSource();
//
//        // Create a mapper and actor
//        vtkPolyDataMapper mapper = new vtkPolyDataMapper();
//        mapper.SetInputConnection(sphereSource.GetOutputPort());
//
//        vtkActor actor = new vtkActor();
//        actor.SetMapper(mapper);
//
//        // A renderer and render window
//        vtkRenderer renderer = new vtkRenderer();
//        vtkRenderWindow renderWindow = new vtkRenderWindow();
//        renderWindow.SetOffScreenRendering(1);
//        renderWindow.AddRenderer(renderer);
//
//        // Add the actors to the scene
//        renderer.AddActor(actor);
//        renderer.SetBackground(1, 1, 1); // Background color white
//
//        renderWindow.Render();
//
//        vtkWindowToImageFilter windowToImageFilter = new vtkWindowToImageFilter();
//        windowToImageFilter.SetInput(renderWindow);
//        windowToImageFilter.Update();
//
//        vtkPNGWriter writer = new vtkPNGWriter();
//        writer.SetFileName("screenshot.png");
//        writer.SetInputConnection(windowToImageFilter.GetOutputPort());
//        writer.Write();
//    }
//}
