package jtk.examples;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

import jtk.core.VTKRenderPanel;
import jtk.core.VTKRenderPanel.Position;
import vtk.vtkActor;
import vtk.vtkCubeSource;
import vtk.vtkDeformPointSet;
import vtk.vtkElevationFilter;
import vtk.vtkPolyData;
import vtk.vtkPolyDataMapper;
import vtk.vtkSphereSource;
import vtk.vtkVertexGlyphFilter;

public class MorphExampleSphereLattice implements JTKDemo {

	static final int CONTROL_ACTOR_OFFSET = 0;
	vtkVertexGlyphFilter vertexFilter;
	private VTKRenderPanel panel;
	private vtkPolyData control;

	private boolean edit = false;

	public void demo() {
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
        JPanel buttons = new JPanel(new FlowLayout());
        buttons.add(new JToggleButton(new AbstractAction("Interactive") {
            @Override
            public void actionPerformed(ActionEvent e) {
            	toggleInteractorStyle();
            }

        }));
        buttons.add(new JButton(new AbstractAction("+X") {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		panel.setCameraPosition(Position.X_POS);
        	}
        	
        }));
        buttons.add(new JButton(new AbstractAction("-X") {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		panel.setCameraPosition(Position.X_NEG);
        	}
        	
        }));
        buttons.add(new JButton(new AbstractAction("+Y") {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		panel.setCameraPosition(Position.Y_POS);
        	}
        	
        }));
        buttons.add(new JButton(new AbstractAction("-Y") {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		panel.setCameraPosition(Position.Y_NEG);
        	}
        	
        }));
        buttons.add(new JButton(new AbstractAction("+Z") {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		panel.setCameraPosition(Position.Z_POS);
        	}
        	
        }));
        buttons.add(new JButton(new AbstractAction("-Z") {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		panel.setCameraPosition(Position.Z_NEG);
        	}
        	
        }));
        return buttons;
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
//	    vtkPolyData input = getSphereData();
	    vtkPolyData input = getBoxData();

    	// Now create a control mesh, in this case a rectangular lattice
    	control = LatticeFactory.create1x1x1Lattice(new double[]{-2, -2, -2}, 4);

    	// Display the control mesh
    	createControlMeshActor(control);

    	// Display the warped polydata
    	createWarpedActor(input, control);

    	// Display the original data
    	createOriginalActor(input);

    	panel.GetRenderer().GetActiveCamera().ParallelProjectionOn();;
    	panel.resetZoomLater();
    }

	private vtkPolyData getSphereData() {
		vtkSphereSource sphere = new vtkSphereSource();
	    sphere.SetCenter(0, 0, 0);
	    sphere.SetRadius(0.5);
	    sphere.SetThetaResolution(51);
	    sphere.SetPhiResolution(17);
	    sphere.Update();

	    double[] sphereBounds = new double[6];
	    sphere.GetOutput().GetBounds(sphereBounds);

	    // Generate some scalars on the polydata to have some color
	    vtkElevationFilter ele = new vtkElevationFilter();
	    ele.SetInputConnection(sphere.GetOutputPort());
	    ele.SetLowPoint((sphereBounds[1] + sphereBounds[0]) / 2.0,	(sphereBounds[3] + sphereBounds[2]) / 2.0, -sphereBounds[5]);
	    ele.SetHighPoint((sphereBounds[1] + sphereBounds[0]) / 2.0, (sphereBounds[3] + sphereBounds[2]) / 2.0, sphereBounds[5]);
	    ele.Update();

    	vtkPolyData input = new vtkPolyData();
    	input.ShallowCopy(ele.GetOutput());
		return input;
	}

	private vtkPolyData getBoxData() {
		vtkCubeSource sphere = new vtkCubeSource();
		sphere.SetCenter(0, 0, 0);
		sphere.SetXLength(0.5);
		sphere.SetYLength(0.5);
		sphere.SetZLength(0.5);
		sphere.Update();
		
		double[] sphereBounds = new double[6];
		sphere.GetOutput().GetBounds(sphereBounds);
		
		// Generate some scalars on the polydata to have some color
		vtkElevationFilter ele = new vtkElevationFilter();
		ele.SetInputConnection(sphere.GetOutputPort());
		ele.SetLowPoint((sphereBounds[1] + sphereBounds[0]) / 2.0,	(sphereBounds[3] + sphereBounds[2]) / 2.0, -sphereBounds[5]);
		ele.SetHighPoint((sphereBounds[1] + sphereBounds[0]) / 2.0, (sphereBounds[3] + sphereBounds[2]) / 2.0, sphereBounds[5]);
		ele.Update();
		
		vtkPolyData input = new vtkPolyData();
		input.ShallowCopy(ele.GetOutput());
		return input;
	}

	private void createControlMeshActor(vtkPolyData pd) {
		vertexFilter = new vtkVertexGlyphFilter();
		vertexFilter.SetInputData(pd);
		vertexFilter.Update();
		
		vtkPolyDataMapper meshVertexMapper = new vtkPolyDataMapper();
		meshVertexMapper.SetInputData(vertexFilter.GetOutput());

		vtkActor meshVertexActor = new vtkActor();
		meshVertexActor.SetMapper(meshVertexMapper);
//		meshVertexActor.SetPosition(0, 0, CONTROL_ACTOR_OFFSET);
		meshVertexActor.GetProperty().SetRepresentationToSurface();
		meshVertexActor.GetProperty().SetColor(0,0,0.5);
		meshVertexActor.GetProperty().SetPointSize(10);
		meshVertexActor.GetProperty().SetEdgeColor(0,0,1);

		vtkPolyDataMapper meshMapper = new vtkPolyDataMapper();
		meshMapper.SetInputData(pd);

		vtkActor meshActor = new vtkActor();
		meshActor.SetMapper(meshMapper);
		meshActor.PickableOff();
//		meshActor.SetPosition(0, 0, CONTROL_ACTOR_OFFSET);
		meshActor.GetProperty().SetRepresentationToWireframe();
		meshActor.GetProperty().SetColor(0,0,1);
		meshActor.GetProperty().SetEdgeColor(0, 0, 0.5);
		
//		meshActor.GetProperty().SetRepresentationToSurface();
//		meshActor.GetProperty().EdgeVisibilityOn();
    	panel.addActor(meshVertexActor);
    	panel.addActor(meshActor);
	}

	private void createWarpedActor(vtkPolyData input, vtkPolyData pd) {
		// Do the intitial weight generation
		vtkDeformPointSet deform = new vtkDeformPointSet();
		deform.SetInputData(input);
		deform.SetControlMeshData(pd);
		deform.Update();
  	   
		vtkPolyDataMapper warpMapper = new vtkPolyDataMapper();
		warpMapper.SetInputData(input);
//		warpMapper.SetInputConnection(deform.GetOutputPort());
		
		vtkActor warpActor = new vtkActor();
		warpActor.PickableOff();
		warpActor.SetMapper(warpMapper);
		warpActor.GetProperty().SetRepresentationToSurface();
		warpActor.GetProperty().EdgeVisibilityOn();
		warpActor.GetProperty().SetEdgeColor(0,0,0);
		
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
