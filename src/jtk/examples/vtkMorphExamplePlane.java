package jtk.examples;
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
//import vtk.vtkCellArray;
//import vtk.vtkDeformPointSet;
//import vtk.vtkElevationFilter;
//import vtk.vtkNativeLibrary;
//import vtk.vtkPlaneSource;
//import vtk.vtkPoints;
//import vtk.vtkPolyData;
//import vtk.vtkPolyDataMapper;
//import vtk.vtkRenderer;
//import vtk.vtkSphereSource;
//import vtk.vtkTriangleFilter;
//import eu.engys.util.plaf.HelyxLookAndFeel;
//import eu.engys.vtk.VTKView3DPanel;
//
//public class vtkMorphExamplePlane {
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
//    private static void addActor(vtkRenderer renderer) {
//    	    // Create a plane to warp
//    	    vtkPlaneSource plane1 = new vtkPlaneSource();
//    	    plane1.SetOrigin(0, 0, 0);
//			plane1.SetPoint1(5, 0 , 0);
//			plane1.SetPoint2(0, 5, 0);
//			plane1.SetCenter(0, 0, 0);
//			plane1.SetNormal(0, 0, 1);
//			plane1.SetXResolution(10);
//			plane1.SetYResolution(10);
//			plane1.Update();
//
//    	    vtkPolyData input = new vtkPolyData();
//    	    input.ShallowCopy(plane1.GetOutput());    
//
//    	    // Now create a control mesh
//    	    vtkPoints pts = createControlPoints();
//    	    vtkCellArray tris = createControlFaces();
//    	   
//    	    vtkPolyData control = new vtkPolyData();
//    	    control.SetPoints(pts);
//    	    control.SetPolys(tris);
//    	    
//    	    // Display the control mesh
//    	    vtkActor meshActor = createControlMeshActor(control);
//    	   
//    	    // Display the warped polydata
//    	    vtkActor polyActor = createWarpedActor(input, control);
//    	    
//    	    // Display the original data
//    	    vtkActor origActor = createOriginalActor(input);
//    	    
//    	    renderer.AddActor(origActor);
//    	    renderer.AddActor(polyActor);
//    	    renderer.AddActor(meshActor);
//    	   
////    	    renderer.GetActiveCamera().SetPosition(1,1,1);
//    	    renderer.ResetCamera();
////    	    renderer.SetBackground(.2, .3, .4);
//    	   
////    	    renWin.SetSize(300,300);
////    	    renWin.Render();
////    	   
////    	    iren.Start();
////    	    
//    }
//
//	private static vtkPoints createControlPoints() {
//		vtkPoints pts = new vtkPoints();
//		pts.SetNumberOfPoints(9);
//		pts.SetPoint(0, 0, 0, 0);
//		pts.SetPoint(1, 1, 0, 0);
//		pts.SetPoint(2, 2, 0, 0);
//		pts.SetPoint(3, 0, 1, 0);
//		pts.SetPoint(4, 1, 1, 0);
//		pts.SetPoint(5, 2, 1, 0);
//		pts.SetPoint(6, 0, 2, 0);
//		pts.SetPoint(7, 1, 2, 0);
//		pts.SetPoint(8, 2, 2, 0);
//		return pts;
//	}
//
//	private static vtkCellArray createControlFaces() {
//		vtkCellArray tris = new vtkCellArray();
//		tris.InsertNextCell(3);
//		tris.InsertCellPoint(0); tris.InsertCellPoint(1); tris.InsertCellPoint(3);
//
//		tris.InsertNextCell(3);
//		tris.InsertCellPoint(1); tris.InsertCellPoint(2); tris.InsertCellPoint(4);
//
//		tris.InsertNextCell(3);
//		tris.InsertCellPoint(3); tris.InsertCellPoint(1); tris.InsertCellPoint(4);
//		
//		tris.InsertNextCell(3);
//		tris.InsertCellPoint(4); tris.InsertCellPoint(2); tris.InsertCellPoint(5);
//
//		tris.InsertNextCell(3);
//		tris.InsertCellPoint(3); tris.InsertCellPoint(4); tris.InsertCellPoint(6);
//		
//		tris.InsertNextCell(3);
//		tris.InsertCellPoint(4); tris.InsertCellPoint(5); tris.InsertCellPoint(7);
//
//		tris.InsertNextCell(3);
//		tris.InsertCellPoint(6); tris.InsertCellPoint(4); tris.InsertCellPoint(7);
//		
//		tris.InsertNextCell(3);
//		tris.InsertCellPoint(7); tris.InsertCellPoint(5); tris.InsertCellPoint(8);
//		return tris;
//	}
//
//	private static vtkActor createWarpedActor(vtkPolyData input, vtkPolyData pd) {
//		// Do the intitial weight generation
//		vtkDeformPointSet deform = new vtkDeformPointSet();
//		deform.SetInputData(input);
//		deform.SetControlMeshData(pd);
//		deform.Update(); // this creates the initial weights
//  	   
//		// Now move one point and deform
//		double[] controlPoint = new double[3];
//		pd.GetPoints().GetPoint(4, controlPoint);
//		pd.GetPoints().SetPoint(4, 
//				controlPoint[0],
//				controlPoint[1],
//				1);
//		pd.GetPoints().Modified();
//  	   
//		vtkPolyDataMapper warpMapper = new vtkPolyDataMapper();
//		warpMapper.SetInputConnection(deform.GetOutputPort());
//		warpMapper.ScalarVisibilityOff();
//		
//		vtkActor warpActor = new vtkActor();
//		warpActor.SetMapper(warpMapper);
//		warpActor.SetPosition(0, 0, -2);
//		warpActor.GetProperty().SetRepresentationToSurface();
//		warpActor.GetProperty().EdgeVisibilityOn();
//		warpActor.GetProperty().SetEdgeColor(0, 0, 0);
//		warpActor.GetProperty().SetColor(1, 0, 0);
//		return warpActor;
//	}
//
//	private static vtkActor createControlMeshActor(vtkPolyData pd) {
//		vtkPolyDataMapper meshMapper = new vtkPolyDataMapper();
//		meshMapper.SetInputData(pd);
//		vtkActor meshActor = new vtkActor();
//		meshActor.SetMapper(meshMapper);
//		meshActor.SetPosition(0, 0, -1);
//		meshActor.GetProperty().SetRepresentationToWireframe();
//		meshActor.GetProperty().SetColor(0,0,0);
//		return meshActor;
//	}
//
//	private static vtkActor createOriginalActor(vtkPolyData originput) {
//		vtkPolyDataMapper origMapper = new vtkPolyDataMapper();
//		origMapper.SetInputData(originput);
//		origMapper.ScalarVisibilityOff();
//		
//		vtkActor origActor = new vtkActor();
//		origActor.SetMapper(origMapper);
//		origActor.GetProperty().SetRepresentationToWireframe();
//		origActor.GetProperty().SetEdgeColor(1,1,0);
//		origActor.GetProperty().SetLineWidth(2);
//		return origActor;
//	}
//}
