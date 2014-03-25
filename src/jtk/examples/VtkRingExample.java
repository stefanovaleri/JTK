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
//import java.awt.BorderLayout;
//import java.awt.FlowLayout;
//import java.awt.event.ActionEvent;
//
//import javax.swing.AbstractAction;
//import javax.swing.JButton;
//import javax.swing.JFrame;
//import javax.swing.JPanel;
//
//import vtk.vtkActor;
//import vtk.vtkAppendPolyData;
//import vtk.vtkCellArray;
//import vtk.vtkIdList;
//import vtk.vtkLineSource;
//import vtk.vtkNativeLibrary;
//import vtk.vtkPolyData;
//import vtk.vtkPolyDataMapper;
//import vtk.vtkPolyDataNormals;
//import vtk.vtkRenderer;
//import vtk.vtkTubeFilter;
//import eu.engys.util.plaf.HelyxLookAndFeel;
//import eu.engys.vtk.VTKView3DPanel;
//
//public class VtkRingExample {
//
//    static {
//        vtkNativeLibrary.LoadAllNativeLibraries();
//    }
//
//    public static void main(String[] args) {
//        JFrame frame = new JFrame();
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.getContentPane().setLayout(new BorderLayout());
//
//        final VTKView3DPanel panel = new VTKView3DPanel(new HelyxLookAndFeel());
//
//        // Create a renderer and render window
//        addActor(panel.GetRenderer());
//
//        frame.setSize(800, 800);
//        frame.getContentPane().add(panel, BorderLayout.CENTER);
//        // frame.getContentPane().add(getButtonsPanel(renderer),
//        // BorderLayout.SOUTH);
//        frame.setVisible(true);
//    }
//
//    private static JPanel getButtonsPanel(final vtkRenderer renderer) {
//        JPanel panel = new JPanel(new FlowLayout());
//        panel.add(new JButton(new AbstractAction("Print") {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//
//            }
//        }));
//        return panel;
//    }
//
//    // private static void addActor(vtkRenderer r) {
//    // vtkLineSource lineSourceInternal = new vtkLineSource();
//    // lineSourceInternal.SetPoint1(0, 0, 0);
//    // lineSourceInternal.SetPoint2(0.05, 0, 0);
//    //
//    // // Create a tube (cylinder) around the line
//    // vtkTubeFilter tubeFilterInternal = new vtkTubeFilter();
//    // tubeFilterInternal.SetInputConnection(lineSourceInternal.GetOutputPort());
//    // tubeFilterInternal.SetCapping(1);
//    // tubeFilterInternal.SetRadius(0.3);
//    // tubeFilterInternal.SetNumberOfSides(50);
//    // tubeFilterInternal.Update();
//    //
//    // /////////////////
//    //
//    // vtkSuperquadric ring = new vtkSuperquadric();
//    // ring.SetCenter(0, 0, 0);
//    // ring.SetScale(1, 1, 2);
//    // ring.SetThetaRoundness(1);
//    // ring.SetPhiRoundness(0);
//    // ring.SetSize(0.);
//    // ring.ToroidalOff();
//    //
//    // // vtkPolyDataMapper mapper = new vtkPolyDataMapper();
//    // // mapper.SetInputData(ring.GetOutput());
//    // //
//    // // vtkActor actor = new vtkActor();
//    // // actor.SetMapper(mapper);
//    //
//    // vtkClipPolyData clipper = new vtkClipPolyData();
//    // clipper.SetInputData(tubeFilterInternal.GetOutput());
//    // clipper.SetClipFunction(ring);
//    // clipper.Update();
//    //
//    // vtkPolyDataMapper tubeMapperInternal = new vtkPolyDataMapper();
//    // tubeMapperInternal.SetInputConnection(clipper.GetOutputPort());
//    // tubeMapperInternal.ScalarVisibilityOff();
//    //
//    // vtkActor internalActor = new vtkActor();
//    // internalActor.SetMapper(tubeMapperInternal);
//    // internalActor.GetProperty().SetColor(0, 0, 1);
//    //
//    // r.AddActor(internalActor);
//    // // r.AddActor(actor);
//    // }
//    private static void addActor(vtkRenderer vtkRenderer) {
//
//        // / ACTOR 1
//        vtkLineSource lineSourceInternal = new vtkLineSource();
//        lineSourceInternal.SetPoint1(0, 0, 0);
//        lineSourceInternal.SetPoint2(0.05, 0, 0);
//
//        // Create a tube (cylinder) around the line
//        vtkTubeFilter tubeFilterInternal = new vtkTubeFilter();
//        tubeFilterInternal.SetInputConnection(lineSourceInternal.GetOutputPort());
//        tubeFilterInternal.SetCapping(0);
//        tubeFilterInternal.SetRadius(0.2);
//        tubeFilterInternal.SetNumberOfSides(50);
//        tubeFilterInternal.Update();
//
//        // Create a tube (cylinder) around the line
//        vtkTubeFilter tubeFilterExternal = new vtkTubeFilter();
//        tubeFilterExternal.SetInputConnection(lineSourceInternal.GetOutputPort());
//        tubeFilterExternal.SetCapping(0);
//        tubeFilterExternal.SetRadius(0.3);
//        tubeFilterExternal.SetNumberOfSides(50);
//        tubeFilterExternal.Update();
//
//        // ////////////////////////////////
//
//        vtkAppendPolyData append = new vtkAppendPolyData();
//        append.AddInputConnection(tubeFilterInternal.GetOutputPort());
//        append.AddInputConnection(tubeFilterExternal.GetOutputPort());
//        append.Update();
//
//        vtkPolyData outputMesh = new vtkPolyData();
//        outputMesh.DeepCopy(append.GetOutput());
//
//        int length = tubeFilterInternal.GetOutput().GetNumberOfPoints();
//
//        vtkCellArray outputTriangles = outputMesh.GetPolys();
//        System.out.println("VtkRingExample.addActor()length: " + length);
//
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
//        System.out.println("VtkRingExample.addActor()outputTriangles: " + outputTriangles.GetSize());
//
//        // vtkRenderer.AddActor(internalActor);
//
//        vtkPolyDataNormals normals = new vtkPolyDataNormals();
//        normals.SetInput(outputMesh);
//        // normals.SetFeatureAngle(60);
//        normals.Update();
//
//        vtkPolyDataMapper m = new vtkPolyDataMapper();
//        m.SetInput(normals.GetOutput());
//
//        vtkActor a = new vtkActor();
//        a.SetMapper(m);
//        // a.GetProperty().EdgeVisibilityOn();
//
//        vtkRenderer.AddActor(a);
//
//    }
//}
