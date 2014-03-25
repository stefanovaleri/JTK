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
//import eu.engys.util.plaf.HelyxLookAndFeel;
//import eu.engys.vtk.VTKView3DPanel;
//
//public class vtkMorphExamplePlane2 {
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
//    	    double[] bounds = new double[6];
//
//    	    // Create a sphere to warp
//    	    vtkPlaneSource plane = new vtkPlaneSource();
//    	    plane.SetOrigin(0, 0, 0);
////    	    plane.SetCenter(0, 0, 0);
////    	    plane.SetNormal(0, 0, 1);
//			plane.SetPoint1(5, 0 , 0);
//			plane.SetPoint2(0, 5, 0);
//			plane.SetXResolution(10);
//			plane.SetYResolution(10);
//			plane.Update();
//    	    plane.GetOutput().GetBounds(bounds);
//    	    
//    	    bounds[5] = 1;
//    	    bounds[5] = 1;
//
//    	    // Generate some scalars on the polydata
////    	    vtkElevationFilter ele = new vtkElevationFilter();
////    	    ele.SetInputConnection(plane.GetOutputPort());
////    	    ele.SetLowPoint(0,0,-0.5);
////    	    ele.SetHighPoint(0,0,0.5);
////    	    ele.SetLowPoint((bounds[1] + bounds[0]) / 2.0,
////    	    		(bounds[3] + bounds[2]) / 2.0,
////    	    		-bounds[5]);
////    	    ele.SetHighPoint((bounds[1] + bounds[0]) / 2.0,
////    	    		(bounds[3] + bounds[2]) / 2.0,
////    	    		bounds[5]);
////
////    	    ele.Update();
//
//    	    vtkPolyData input = new vtkPolyData();
//    	    input.ShallowCopy(plane.GetOutput());    
//    	   
//    	    // Now create a control mesh
//    	    vtkPoints pts = createControlPoints(bounds);
//    	    vtkCellArray tris = createControlFaces();
//    	   
//    	    vtkPolyData pd = new vtkPolyData();
//    	    pd.SetPoints(pts);
//    	    pd.SetPolys(tris);
//    	   
//    	    // Display the control mesh
//    	    vtkActor meshActor = createControlMeshActor(pd);
//    	   
//    	    // Display the warped polydata
//    	    vtkActor polyActor = createWarpedActor(input, pd);
//    	    
//    	    // Display the original data
//    	    vtkActor origActor = createOriginalActor(plane.GetOutput());
//    	    
//    	    renderer.AddActor(origActor);
//    	    renderer.AddActor(polyActor);
//    	    renderer.AddActor(meshActor);
//    	   
//    	    renderer.GetActiveCamera().SetPosition(1,1,1);
//    	    renderer.ResetCamera();
//    	    renderer.SetBackground(.2, .3, .4);
//    	   
////    	    renWin.SetSize(300,300);
////    	    renWin.Render();
////    	   
////    	    iren.Start();
////    	    
//    }
//
//	private static vtkCellArray createControlFaces() {
//		vtkCellArray tris = new vtkCellArray();
//		tris.InsertNextCell(3);
//		tris.InsertCellPoint(2); tris.InsertCellPoint(0); tris.InsertCellPoint(3);
//		tris.InsertNextCell(3);
//		tris.InsertCellPoint(1); tris.InsertCellPoint(2); tris.InsertCellPoint(3);
////    	    tris.InsertNextCell(3);
////    	    tris.InsertCellPoint(2); tris.InsertCellPoint(0); tris.InsertCellPoint(4);
////    	    tris.InsertNextCell(3);
////    	    tris.InsertCellPoint(1); tris.InsertCellPoint(2); tris.InsertCellPoint(4);
////    	    tris.InsertNextCell(3);
////    	    tris.InsertCellPoint(3); tris.InsertCellPoint(1); tris.InsertCellPoint(4);
////    	    tris.InsertNextCell(3);
////    	    tris.InsertCellPoint(0); tris.InsertCellPoint(3); tris.InsertCellPoint(4);
////    	    tris.InsertNextCell(3);
////    	    tris.InsertCellPoint(0); tris.InsertCellPoint(2); tris.InsertCellPoint(5);
////    	    tris.InsertNextCell(3);
////    	    tris.InsertCellPoint(2); tris.InsertCellPoint(1); tris.InsertCellPoint(5);
////    	    tris.InsertNextCell(3);
////    	    tris.InsertCellPoint(1); tris.InsertCellPoint(3); tris.InsertCellPoint(5);
////    	    tris.InsertNextCell(3);
////    	    tris.InsertCellPoint(3); tris.InsertCellPoint(0); tris.InsertCellPoint(5);
//		return tris;
//	}
//
//	private static vtkPoints createControlPoints(double[] bounds) {
//		vtkPoints pts = new vtkPoints();
//		pts.SetNumberOfPoints(4);
////    	    pts.SetNumberOfPoints(5);
////    	    pts.SetNumberOfPoints(6);
//		pts.SetPoint(0,
//		              bounds[0] - .1 * (bounds[1] - bounds[0]),
//		              (bounds[3] + bounds[2]) / 2.0,
//		              (bounds[5] + bounds[4]) / 2.0);
//		pts.SetPoint(1,
//		              bounds[1] + .1 * (bounds[1] - bounds[0]),
//		              (bounds[3] + bounds[2]) / 2.0,
//		              (bounds[5] + bounds[4]) / 2.0);
//		pts.SetPoint(2,
//		              (bounds[1] + bounds[0]) / 2.0,
//		              bounds[2] - .1 * (bounds[3] - bounds[2]),
//		              (bounds[5] + bounds[4]) / 2.0);
//		pts.SetPoint(3,
//		              (bounds[1] + bounds[0]) / 2.0,
//		              bounds[3] + .1 * (bounds[3] - bounds[2]),
//		              (bounds[5] + bounds[4]) / 2.0);
////    	    pts.SetPoint(4,
////    	                  (bounds[1] + bounds[0]) / 2.0,
////    	                  (bounds[3] + bounds[2]) / 2.0,
////    	                  bounds[4] - .1 * (bounds[5] - bounds[4]));
////    	    pts.SetPoint(5,
////    	                  (bounds[1] + bounds[0]) / 2.0,
////    	                  (bounds[3] + bounds[2]) / 2.0,
////    	                  bounds[5] + .1 * (bounds[5] - bounds[4]));
//		return pts;
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
//		pd.GetPoints().GetPoint(3, controlPoint);
//		pd.GetPoints().SetPoint(3, 
//				controlPoint[0],
//				controlPoint[1],
//				2);
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
