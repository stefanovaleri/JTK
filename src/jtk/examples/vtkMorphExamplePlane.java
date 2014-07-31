package jtk.examples;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

import jtk.core.VTKRenderPanel;
import vtk.vtkActor;
import vtk.vtkCellArray;
import vtk.vtkDeformPointSet;
import vtk.vtkGenericRenderWindowInteractor;
import vtk.vtkPlaneSource;
import vtk.vtkPointPicker;
import vtk.vtkPoints;
import vtk.vtkPolyData;
import vtk.vtkPolyDataMapper;
import vtk.vtkVertexGlyphFilter;
import vtk.vtkWorldPointPicker;

public class vtkMorphExamplePlane implements JTKDemo {

	private static final int CONTROL_ACTOR_OFFSET = -1;
	private vtkVertexGlyphFilter vertexFilter;
	private VTKRenderPanel panel;
	private vtkPolyData control;

	private boolean edit = false;

	public void demo(String[] args) {
    	SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				JFrame frame = new JFrame();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.getContentPane().setLayout(new BorderLayout());
				
				panel = new VTKRenderPanel();
				
				addActors();
				
				frame.setSize(800, 800);
				frame.getContentPane().add(panel, BorderLayout.CENTER);
				frame.getContentPane().add(getButtonsPanel(), BorderLayout.SOUTH);
				frame.setVisible(true);
			}
		});
    }
	
    private JPanel getButtonsPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(new JToggleButton(new AbstractAction("Interactive") {
            @Override
            public void actionPerformed(ActionEvent e) {
            	toggleInteractorStyle();
            }

        }));
        return panel;
    }
    
    private int id1 = -1, id2 = -1, id3 = -1;
    
    private void toggleInteractorStyle() {
    	if (edit) {
    		if (id1 >= 0) { 
    			panel.getRenderWindowInteractor().GetInteractorStyle().RemoveObserver(id1);
    			panel.getRenderWindowInteractor().GetInteractorStyle().RemoveObserver(id2);
    			panel.getRenderWindowInteractor().GetInteractorStyle().RemoveObserver(id3);
    		}
    		edit = false;
    	} else {
    		MyStyle observer = new MyStyle(panel);
    		observer.setPipeline(control, vertexFilter);
    		id1 = panel.getRenderWindowInteractor().GetInteractorStyle().AddObserver("MouseMoveEvent", observer, "MouseMove");
    		id2 = panel.getRenderWindowInteractor().GetInteractorStyle().AddObserver("MiddleButtonReleaseEvent", observer, "MiddleButtonUp");
    		id3 = panel.getRenderWindowInteractor().GetInteractorStyle().AddObserver("MiddleButtonPressEvent", observer, "MiddleButtonDown");

    		edit = true;
    	}
    }

    private void addActors() {
    	// Create a plane to warp
    	vtkPlaneSource plane1 = new vtkPlaneSource();
    	plane1.SetOrigin(0, 0, 0);
    	plane1.SetPoint1(5, 0 , 0);
    	plane1.SetPoint2(0, 5, 0);
    	plane1.SetCenter(0, 0, 0);
    	plane1.SetNormal(0, 0, 1);
    	plane1.SetXResolution(4);
    	plane1.SetYResolution(4);
    	plane1.Update();

    	vtkPolyData input = new vtkPolyData();
    	input.ShallowCopy(plane1.GetOutput());    

    	// Now create a control mesh
    	vtkPoints pts = createControlPoints();
    	vtkCellArray tris = createTriangulatedControlFaces();
//    	vtkCellArray tetra = createSquareControlFaces();

    	control = new vtkPolyData();
    	control.SetPoints(pts);
    	control.SetPolys(tris);
//    	control.SetPolys(tetra);

    	// Display the control mesh
    	createControlMeshActor(control);

    	// Display the warped polydata
    	createWarpedActor(input, control);

    	// Display the original data
    	createOriginalActor(input);

    	panel.resetZoomLater();
    }

	private static vtkPoints createControlPoints() {
		vtkPoints pts = new vtkPoints();
		pts.SetNumberOfPoints(9);
		pts.SetPoint(0, 0, 0, 0);
		pts.SetPoint(1, 1, 0, 0);
		pts.SetPoint(2, 2, 0, 0);
		pts.SetPoint(3, 0, 1, 0);
		pts.SetPoint(4, 1, 1, 0);
		pts.SetPoint(5, 2, 1, 0);
		pts.SetPoint(6, 0, 2, 0);
		pts.SetPoint(7, 1, 2, 0);
		pts.SetPoint(8, 2, 2, 0);
		return pts;
	}

	private static vtkCellArray createSquareControlFaces() {
		vtkCellArray tetra = new vtkCellArray();
		tetra.InsertNextCell(4);
		tetra.InsertCellPoint(0); tetra.InsertCellPoint(1); tetra.InsertCellPoint(4); tetra.InsertCellPoint(3);
		
		tetra.InsertNextCell(4);
		tetra.InsertCellPoint(1); tetra.InsertCellPoint(2); tetra.InsertCellPoint(5); tetra.InsertCellPoint(4);
		
		tetra.InsertNextCell(4);
		tetra.InsertCellPoint(3); tetra.InsertCellPoint(4); tetra.InsertCellPoint(7); tetra.InsertCellPoint(6);
		
		tetra.InsertNextCell(4);
		tetra.InsertCellPoint(4); tetra.InsertCellPoint(5); tetra.InsertCellPoint(8); tetra.InsertCellPoint(7);
		
		return tetra;
	}
	
	private static vtkCellArray createTriangulatedControlFaces() {
		vtkCellArray tris = new vtkCellArray();
		tris.InsertNextCell(3);
		tris.InsertCellPoint(0); tris.InsertCellPoint(1); tris.InsertCellPoint(4);

		tris.InsertNextCell(3);
		tris.InsertCellPoint(4); tris.InsertCellPoint(3); tris.InsertCellPoint(0);

		tris.InsertNextCell(3);
		tris.InsertCellPoint(1); tris.InsertCellPoint(2); tris.InsertCellPoint(5);
		
		tris.InsertNextCell(3);
		tris.InsertCellPoint(5); tris.InsertCellPoint(4); tris.InsertCellPoint(1);

		tris.InsertNextCell(3);
		tris.InsertCellPoint(3); tris.InsertCellPoint(4); tris.InsertCellPoint(7);
		
		tris.InsertNextCell(3);
		tris.InsertCellPoint(7); tris.InsertCellPoint(6); tris.InsertCellPoint(3);

		tris.InsertNextCell(3);
		tris.InsertCellPoint(4); tris.InsertCellPoint(5); tris.InsertCellPoint(8);
		
		tris.InsertNextCell(3);
		tris.InsertCellPoint(8); tris.InsertCellPoint(7); tris.InsertCellPoint(4);
		return tris;
	}

	private void createControlMeshActor(vtkPolyData pd) {
		vertexFilter = new vtkVertexGlyphFilter();
		vertexFilter.SetInputData(pd);
		vertexFilter.Update();
		
		vtkPolyDataMapper meshVertexMapper = new vtkPolyDataMapper();
		meshVertexMapper.SetInputData(vertexFilter.GetOutput());

		vtkActor meshVertexActor = new vtkActor();
		meshVertexActor.SetMapper(meshVertexMapper);
		meshVertexActor.SetPosition(0, 0, CONTROL_ACTOR_OFFSET);
		meshVertexActor.GetProperty().SetRepresentationToSurface();
		meshVertexActor.GetProperty().SetColor(0,0,0.5);
		meshVertexActor.GetProperty().SetPointSize(10);
		meshVertexActor.GetProperty().SetEdgeColor(0,0,1);

		vtkPolyDataMapper meshMapper = new vtkPolyDataMapper();
		meshMapper.SetInputData(pd);

		vtkActor meshActor = new vtkActor();
		meshActor.SetMapper(meshMapper);
		meshActor.PickableOff();
		meshActor.SetPosition(0, 0, CONTROL_ACTOR_OFFSET);
		meshActor.GetProperty().SetRepresentationToSurface();
		meshActor.GetProperty().SetColor(0,0,1);
		meshActor.GetProperty().SetEdgeColor(0, 0, 0.5);
		meshActor.GetProperty().EdgeVisibilityOn();
    	panel.addActor(meshVertexActor);
    	panel.addActor(meshActor);
	}

	private void createWarpedActor(vtkPolyData input, vtkPolyData pd) {
		// Do the intitial weight generation
		vtkDeformPointSet deform = new vtkDeformPointSet();
		deform.SetInputData(input);
		deform.SetControlMeshData(pd);
		deform.Update(); // this creates the initial weights
  	   
		// Now move one point and deform
//		double[] controlPoint = new double[3];
//		pd.GetPoints().GetPoint(4, controlPoint);
//		pd.GetPoints().SetPoint(4, controlPoint[0], controlPoint[1], 1);
//		pd.GetPoints().Modified();
  	   
		vtkPolyDataMapper warpMapper = new vtkPolyDataMapper();
		warpMapper.SetInputConnection(deform.GetOutputPort());
		warpMapper.ScalarVisibilityOff();
		
		vtkActor warpActor = new vtkActor();
		warpActor.PickableOff();
		warpActor.SetMapper(warpMapper);
		warpActor.SetPosition(0, 0, -2);
		warpActor.GetProperty().SetRepresentationToSurface();
		warpActor.GetProperty().EdgeVisibilityOn();
		warpActor.GetProperty().SetEdgeColor(0,0,0);
		warpActor.GetProperty().SetColor(1,0,0);
		
    	panel.addActor(warpActor);
	}

	private void createOriginalActor(vtkPolyData originput) {
		vtkPolyDataMapper origMapper = new vtkPolyDataMapper();
		origMapper.SetInputData(originput);
		origMapper.ScalarVisibilityOff();
		
		vtkActor origActor = new vtkActor();
		origActor.PickableOff();
		origActor.SetMapper(origMapper);
		origActor.GetProperty().SetRepresentationToWireframe();
		origActor.GetProperty().SetEdgeColor(1,1,0);
		origActor.GetProperty().SetLineWidth(2);
		
		panel.addActor(origActor);
	}
	
}
