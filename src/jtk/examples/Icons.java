package jtk.examples;
///*--------------------------------*- Java -*---------------------------------*\
// |o                                                                   |                                                                                     
// |    o     o       | HelyxOS: The Open Source GUI for OpenFOAM              |
// |   o   O   o      | Copyright (C) 2012-2013 ENGYS                          |
// |    o     o       | http://www.engys.com                                   |
// |       o          |                                                        |
// |---------------------------------------------------------------------------|
// |License                                                                    |
// |   This file is part of HelyxOS.                                           |
// |                                                                           |
// |   HelyxOS is free software; you can redistribute it and/or modify it      |
// |   under the terms of the GNU General Public License as published by the   |
// |   Free Software Foundation; either version 2 of the License, or (at your  |
// |   option) any later version.                                              |
// |                                                                           |
// |   HelyxOS is distributed in the hope that it will be useful, but WITHOUT  |
// |   ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or   |
// |   FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License   |
// |   for more details.                                                       |
// |                                                                           |
// |   You should have received a copy of the GNU General Public License       |
// |   along with HelyxOS; if not, write to the Free Software Foundation,      |
// |   Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA            |
//\*---------------------------------------------------------------------------*/
//package eu.engys.vtk.depot;
//
//import vtk.vtkActor;
//import vtk.vtkAppendPolyData;
//import vtk.vtkCamera;
//import vtk.vtkCellArray;
//import vtk.vtkCubeSource;
//import vtk.vtkCylinderSource;
//import vtk.vtkIdList;
//import vtk.vtkInteractorStyleTrackballCamera;
//import vtk.vtkLineSource;
//import vtk.vtkNativeLibrary;
//import vtk.vtkPlaneSource;
//import vtk.vtkPolyData;
//import vtk.vtkPolyDataMapper;
//import vtk.vtkPolyDataNormals;
//import vtk.vtkRenderWindow;
//import vtk.vtkRenderWindowInteractor;
//import vtk.vtkRenderer;
//import vtk.vtkSphereSource;
//import vtk.vtkTubeFilter;
//
//public class Icons {
//
//    static {
//        vtkNativeLibrary.LoadAllNativeLibraries();
//    }
//
//    public static void main(String[] args) {
//
//        vtkRenderer renderer = new vtkRenderer();
//        vtkRenderWindow renderWindow = new vtkRenderWindow();
//        renderWindow.AddRenderer(renderer);
//        vtkRenderWindowInteractor renderWindowInteractor = new vtkRenderWindowInteractor();
//        renderWindowInteractor.SetRenderWindow(renderWindow);
//        vtkInteractorStyleTrackballCamera style = new vtkInteractorStyleTrackballCamera();
//        renderWindowInteractor.SetInteractorStyle(style);
//
//        vtkCamera camera = new vtkCamera();
//        camera.SetPosition(10, 20, 20);
//        camera.SetFocalPoint(0, 0, 0);
//        renderer.SetActiveCamera(camera);
//
//        addCube(renderer);
////         addSphere(renderer);
////         addCylinder(renderer);
////         addRing(renderer);
//        // addPlane(renderer);
//
//        renderer.SetBackground(0, 1, 0); // Background color green
//
//        renderWindow.Render();
//        renderWindowInteractor.Start();
//    }
//
//    private static void addCube(vtkRenderer renderer) {
//        vtkCubeSource source = new vtkCubeSource();
//        source.SetCenter(0.0, 0.0, 0.0);
//        source.SetXLength(6);
//        source.SetYLength(6);
//        source.SetZLength(6);
//
//        vtkPolyDataMapper mapper = new vtkPolyDataMapper();
//        mapper.SetInputConnection(source.GetOutputPort());
//
//        vtkActor actor = new vtkActor();
////        actor.SetScale(0.15);
//        actor.SetMapper(mapper);
//        actor.GetProperty().SetRepresentationToSurface();
//        actor.GetProperty().EdgeVisibilityOn();
//        renderer.AddActor(actor);
//    }
//
//    private static void addSphere(vtkRenderer renderer) {
//        vtkSphereSource source = new vtkSphereSource();
//        source.SetCenter(0.0, 0.0, 0.0);
//        source.SetRadius(4.0);
//        source.SetThetaResolution(100);
//        source.SetPhiResolution(100);
//
//        vtkPolyDataMapper mapper = new vtkPolyDataMapper();
//        mapper.SetInputConnection(source.GetOutputPort());
//
//        vtkActor actor = new vtkActor();
//        actor.SetMapper(mapper);
//        actor.GetProperty().SetRepresentationToSurface();
//        // actor.GetProperty().EdgeVisibilityOn();
//        renderer.AddActor(actor);
//    }
//
//    private static void addCylinder(vtkRenderer renderer) {
//        vtkCylinderSource source = new vtkCylinderSource();
//        source.SetCenter(0.0, 0.0, 0.0);
//        source.SetHeight(6);
//        source.SetRadius(4);
//        source.SetResolution(100);
//
//        vtkPolyDataMapper mapper = new vtkPolyDataMapper();
//        mapper.SetInputConnection(source.GetOutputPort());
//
//        vtkActor actor = new vtkActor();
//        actor.SetMapper(mapper);
//        actor.GetProperty().SetRepresentationToSurface();
//        actor.GetProperty().EdgeVisibilityOn();
//        renderer.AddActor(actor);
//    }
//
//    private static void addRing(vtkRenderer renderer) {
//        vtkLineSource lineSource = new vtkLineSource();
//        lineSource.SetPoint1(0, -3, 0);
//        lineSource.SetPoint2(0, 3, 0);
//
//        vtkTubeFilter internalTubeFilter = new vtkTubeFilter();
//        internalTubeFilter.SetInputConnection(lineSource.GetOutputPort());
//        internalTubeFilter.SetCapping(0);
//        internalTubeFilter.SetRadius(2);
//        internalTubeFilter.SetNumberOfSides(50);
//        internalTubeFilter.Update();
//
//        vtkTubeFilter externalTubeFilter = new vtkTubeFilter();
//        externalTubeFilter.SetInputConnection(lineSource.GetOutputPort());
//        externalTubeFilter.SetCapping(0);
//        externalTubeFilter.SetRadius(4);
//        externalTubeFilter.SetNumberOfSides(50);
//        externalTubeFilter.Update();
//
//        vtkAppendPolyData append = new vtkAppendPolyData();
//        append.AddInputConnection(internalTubeFilter.GetOutputPort());
//        append.AddInputConnection(externalTubeFilter.GetOutputPort());
//        append.Update();
//
//        vtkPolyData outputMesh = new vtkPolyData();
//        outputMesh.DeepCopy(append.GetOutput());
//        vtkCellArray outputTriangles = outputMesh.GetPolys();
//
//        int length = internalTubeFilter.GetOutput().GetNumberOfPoints();
//        for (int ptId = 0; ptId < 50; ptId++) {
//            // Triangle one extremity
//            vtkIdList triangle = new vtkIdList();
//            triangle.InsertNextId(ptId);
//            triangle.InsertNextId(ptId + length);
//            triangle.InsertNextId((ptId + 1) % 50 + length);
//            outputTriangles.InsertNextCell(triangle);
//
//            triangle = new vtkIdList();
//            triangle.InsertNextId(ptId);
//            triangle.InsertNextId((ptId + 1) % 50 + length);
//            triangle.InsertNextId((ptId + 1) % 50);
//            outputTriangles.InsertNextCell(triangle);
//
//            // Triangle the other extremity
//            int offset = length - 50;
//            triangle = new vtkIdList();
//            triangle.InsertNextId(ptId + offset);
//            triangle.InsertNextId(ptId + +offset + length);
//            triangle.InsertNextId((ptId + 1) % 50 + offset + length);
//            outputTriangles.InsertNextCell(triangle);
//
//            triangle = new vtkIdList();
//            triangle.InsertNextId((ptId + 1) % 50 + length + offset);
//            triangle.InsertNextId((ptId + 1) % 50 + offset);
//            triangle.InsertNextId(ptId + offset);
//            outputTriangles.InsertNextCell(triangle);
//        }
//
//        vtkPolyDataNormals normals = new vtkPolyDataNormals();
//        normals.SetInput(outputMesh);
//        normals.Update();
//
//        vtkPolyDataMapper mapper = new vtkPolyDataMapper();
//        mapper.SetInput(normals.GetOutput());
//
//        vtkActor actor = new vtkActor();
//        actor.SetMapper(mapper);
//        actor.GetProperty().SetRepresentationToSurface();
//        actor.GetProperty().EdgeVisibilityOn();
//        renderer.AddActor(actor);
//
//    }
//
//    private static void addPlane(vtkRenderer renderer) {
//        vtkPlaneSource planeSource = new vtkPlaneSource();
//        // planeSource.SetOrigin(0, 0, 0);
//        planeSource.SetPoint1(0, 0, 0);
//        planeSource.SetPoint2(0, 15, 0);
//
//        planeSource.SetCenter(35, 0, 0);
//        planeSource.SetNormal(0, 0, 1);
//
//        // Create a mapper and actor
//        vtkPolyDataMapper mapper = new vtkPolyDataMapper();
//        mapper.ImmediateModeRenderingOn();
//        mapper.SetInputConnection(planeSource.GetOutputPort());
//
//        vtkActor actor = new vtkActor();
//        actor.SetMapper(mapper);
//        actor.GetProperty().SetRepresentationToSurface();
//        actor.GetProperty().EdgeVisibilityOn();
//        renderer.AddActor(actor);
//    }
//
//}
