package jtk.examples;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import jtk.core.VTKRenderPanel;
import vtk.vtkActor;
import vtk.vtkCellArray;
import vtk.vtkDeformPointSet;
import vtk.vtkElevationFilter;
import vtk.vtkNativeLibrary;
import vtk.vtkPoints;
import vtk.vtkPolyData;
import vtk.vtkPolyDataMapper;
import vtk.vtkRenderer;
import vtk.vtkSphereSource;

public class MorphExampleSphereOctahedron implements JTKDemo {

	@Override
	public void demo() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JFrame frame = new JFrame();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.getContentPane().setLayout(new BorderLayout());
				
				final VTKRenderPanel panel = new VTKRenderPanel();
				
				// Create a renderer and render window
				addActor(panel.GetRenderer());
				
				frame.setSize(800, 800);
				frame.getContentPane().add(panel, BorderLayout.CENTER);
				// frame.getContentPane().add(getButtonsPanel(renderer),
				// BorderLayout.SOUTH);
				frame.setVisible(true);
			}
		});
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

    private static void addActor(vtkRenderer renderer) {
    	    double[] bounds = new double[6];

    	    // Create a sphere to warp
    	    vtkSphereSource sphere = new vtkSphereSource();
    	    sphere.SetThetaResolution(51);
    	    sphere.SetPhiResolution(17);
    	    sphere.Update();
    	    sphere.GetOutput().GetBounds(bounds);

    	    // Generate some scalars on the polydata
    	    vtkElevationFilter ele = new vtkElevationFilter();
    	    ele.SetInputConnection(sphere.GetOutputPort());
//    	    ele.SetLowPoint(0,0,-0.5);
//    	    ele.SetHighPoint(0,0,0.5);
    	    ele.SetLowPoint((bounds[1] + bounds[0]) / 2.0,	(bounds[3] + bounds[2]) / 2.0, -bounds[5]);
    	    ele.SetHighPoint((bounds[1] + bounds[0]) / 2.0, (bounds[3] + bounds[2]) / 2.0, bounds[5]);
    	    ele.Update();

    	    vtkPolyData input = new vtkPolyData();
    	    input.ShallowCopy(ele.GetOutput());    
    	   
    	    // Now create a control mesh, in this case a octagon that encloses
    	    // the point set
    	    vtkPoints pts = new vtkPoints();
    	    pts.SetNumberOfPoints(6);
    	    pts.SetPoint(0,
    	                  bounds[0] - .1 * (bounds[1] - bounds[0]),
    	                  (bounds[3] + bounds[2]) / 2.0,
    	                  (bounds[5] + bounds[4]) / 2.0);
    	    pts.SetPoint(1,
    	                  bounds[1] + .1 * (bounds[1] - bounds[0]),
    	                  (bounds[3] + bounds[2]) / 2.0,
    	                  (bounds[5] + bounds[4]) / 2.0);
    	    pts.SetPoint(2,
    	                  (bounds[1] + bounds[0]) / 2.0,
    	                  bounds[2] - .1 * (bounds[3] - bounds[2]),
    	                  (bounds[5] + bounds[4]) / 2.0);
    	    pts.SetPoint(3,
    	                  (bounds[1] + bounds[0]) / 2.0,
    	                  bounds[3] + .1 * (bounds[3] - bounds[2]),
    	                  (bounds[5] + bounds[4]) / 2.0);
    	    pts.SetPoint(4,
    	                  (bounds[1] + bounds[0]) / 2.0,
    	                  (bounds[3] + bounds[2]) / 2.0,
    	                  bounds[4] - .1 * (bounds[5] - bounds[4]));
    	    pts.SetPoint(5,
    	                  (bounds[1] + bounds[0]) / 2.0,
    	                  (bounds[3] + bounds[2]) / 2.0,
    	                  bounds[5] + .1 * (bounds[5] - bounds[4]));
    	   
    	    vtkCellArray tris = new vtkCellArray();
    	    tris.InsertNextCell(3);
    	    tris.InsertCellPoint(2); tris.InsertCellPoint(0); tris.InsertCellPoint(4);
    	    tris.InsertNextCell(3);
    	    tris.InsertCellPoint(1); tris.InsertCellPoint(2); tris.InsertCellPoint(4);
    	    tris.InsertNextCell(3);
    	    tris.InsertCellPoint(3); tris.InsertCellPoint(1); tris.InsertCellPoint(4);
    	    tris.InsertNextCell(3);
    	    tris.InsertCellPoint(0); tris.InsertCellPoint(3); tris.InsertCellPoint(4);
    	    tris.InsertNextCell(3);
    	    tris.InsertCellPoint(0); tris.InsertCellPoint(2); tris.InsertCellPoint(5);
    	    tris.InsertNextCell(3);
    	    tris.InsertCellPoint(2); tris.InsertCellPoint(1); tris.InsertCellPoint(5);
    	    tris.InsertNextCell(3);
    	    tris.InsertCellPoint(1); tris.InsertCellPoint(3); tris.InsertCellPoint(5);
    	    tris.InsertNextCell(3);
    	    tris.InsertCellPoint(3); tris.InsertCellPoint(0); tris.InsertCellPoint(5);
    	   
    	    vtkPolyData pd = new vtkPolyData();
    	    pd.SetPoints(pts);
    	    pd.SetPolys(tris);
    	   
    	    // Display the control mesh
    	    vtkPolyDataMapper meshMapper = new vtkPolyDataMapper();
    	    meshMapper.SetInputData(pd);
    	    vtkActor meshActor = new vtkActor();
    	    meshActor.SetMapper(meshMapper);
    	    meshActor.GetProperty().SetRepresentationToWireframe();
    	    meshActor.GetProperty().SetColor(0,0,0);
    	   
    	    // Do the intitial weight generation
    	    vtkDeformPointSet deform = new vtkDeformPointSet();
    	    deform.SetInputData(input);
    	    deform.SetControlMeshData(pd);
    	    deform.Update(); // this creates the initial weights
    	   
    	    // Now move one point and deform
    	    double[] controlPoint = new double[3];
    	    pts.GetPoint(5, controlPoint);
    	    pts.SetPoint(5, controlPoint[0], controlPoint[1], bounds[5] + 2 * (bounds[5] - bounds[4]));
    	    pts.Modified();
    	   
    	    // Display the warped polydata
    	    vtkPolyDataMapper polyMapper = new vtkPolyDataMapper();
    	    polyMapper.SetInputConnection(deform.GetOutputPort());
    	    vtkActor polyActor = new vtkActor();
    	    polyActor.SetMapper(polyMapper);
    	   
    	    vtkPolyData originput = new vtkPolyData();
    	    originput.ShallowCopy(sphere.GetOutput());
    	    
    	    vtkPolyDataMapper origMapper = new vtkPolyDataMapper();
    	    origMapper.SetInputData(originput);
    	    
    	    vtkActor origActor = new vtkActor();
    	    origActor.SetMapper(origMapper);
    	    origActor.GetProperty().SetRepresentationToWireframe();
    	    origActor.GetProperty().SetEdgeColor(1,1,0);
    	    origActor.GetProperty().SetLineWidth(2);
    	    
    	    renderer.AddActor(origActor);
    	    renderer.AddActor(polyActor);
    	    renderer.AddActor(meshActor);
    	   
    	    renderer.GetActiveCamera().SetPosition(1,1,1);
    	    renderer.ResetCamera();
    	    renderer.SetBackground(.2, .3, .4);
    	   
//    	    renWin.SetSize(300,300);
//    	    renWin.Render();
//    	   
//    	    iren.Start();
//    	    
    }
}
