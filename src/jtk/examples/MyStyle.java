package jtk.examples;

import jtk.core.VTKRenderPanel;
import vtk.vtkActor;
import vtk.vtkAlgorithm;
import vtk.vtkGenericRenderWindowInteractor;
import vtk.vtkPointPicker;
import vtk.vtkPoints;
import vtk.vtkPolyData;
import vtk.vtkPolyDataMapper;
import vtk.vtkVertexGlyphFilter;
import vtk.vtkWorldPointPicker;

class MyStyle {

	private vtkPolyData Data;
	private vtkAlgorithm Algo;

	private vtkPolyDataMapper MoveMapper;
	private vtkActor MoveActor;
	private vtkPolyData MovePolyData;
	private vtkVertexGlyphFilter MoveGlyphFilter;

	private vtkPointPicker PointPicker;
	private vtkWorldPointPicker WorldPicker;

	boolean Move;
	int SelectedPoint;

	private VTKRenderPanel panel;

	public MyStyle(VTKRenderPanel panel) {
		this.panel = panel;
		this.Move = false;
		this.PointPicker = new vtkPointPicker();
		this.WorldPicker = new vtkWorldPointPicker();

		// Setup ghost glyph
		vtkPoints points = new vtkPoints();
		points.InsertNextPoint(0, 0, 0);

		this.MovePolyData = new vtkPolyData();
		this.MovePolyData.SetPoints(points);

		this.MoveGlyphFilter = new vtkVertexGlyphFilter();
		this.MoveGlyphFilter.SetInputData(this.MovePolyData);
		this.MoveGlyphFilter.Update();

		this.MoveMapper = new vtkPolyDataMapper();
		this.MoveMapper.SetInputConnection(this.MoveGlyphFilter.GetOutputPort());

		this.MoveActor = new vtkActor();
		this.MoveActor.SetMapper(this.MoveMapper);
		this.MoveActor.VisibilityOff();
		this.MoveActor.GetProperty().SetPointSize(10);
		this.MoveActor.GetProperty().SetColor(1, 0, 0);
	}

	public void setPipeline(vtkPolyData Data, vtkAlgorithm Algo) {
		this.Data = Data;
		this.Algo = Algo;
	}
	
	public void MouseMove() {
		// System.out.println("vtkMorphExamplePlane.MyStyle.MouseMove()");
		if (!this.Move) {
			return;
		}

		vtkGenericRenderWindowInteractor interactor = panel.getRenderWindowInteractor();
		// int x = interactor.GetEventPosition()[0];
		// int y = interactor.GetEventPosition()[1];
		// interactor.FindPokedRenderer(x, y);

		this.WorldPicker.Pick(interactor.GetEventPosition()[0], interactor.GetEventPosition()[1], 0, panel.GetRenderer());
		double[] p = WorldPicker.GetPickPosition();

		this.MoveActor.SetPosition(p[0], p[1], p[2]);
		System.out.println("point coordinates: " + p[0] + " " + p[1] + " " + p[2]);
		panel.renderLater();
	}

	public void MiddleButtonUp() {
		System.out.println("vtkMorphExamplePlane.MyStyle.MiddleButtonUp()");

		this.Move = false;
		this.MoveActor.VisibilityOff();

		System.out.println("Dragging point index: " + this.SelectedPoint);
		double[] p = this.MoveActor.GetPosition();
		System.out.println("New point coordinates: " + p[0] + " " + p[1] + " " + p[2]);

		updatePipeline(p);

		panel.renderLater();
	}

	private void updatePipeline(double[] p) {
		this.Data.GetPoints().SetPoint(this.SelectedPoint, p[0], p[1], p[2] - MorphExampleSphereLattice.CONTROL_ACTOR_OFFSET);
		this.Data.Modified();

		this.Algo.Update();
	}

	public void MiddleButtonDown() {
		System.out.println("vtkMorphExamplePlane.MyStyle.OnMiddleButtonDown()");
		// Get the selected point
		vtkGenericRenderWindowInteractor interactor = panel.getRenderWindowInteractor();
		int x = interactor.GetEventPosition()[0];
		int y = interactor.GetEventPosition()[1];
		interactor.FindPokedRenderer(x, y);

		this.PointPicker.Pick(interactor.GetEventPosition()[0], interactor.GetEventPosition()[1], 0, panel.GetRenderer());

		if (this.PointPicker.GetPointId() >= 0) {
			// this.StartPan();
			this.MoveActor.VisibilityOn();
			this.Move = true;
			this.SelectedPoint = this.PointPicker.GetPointId();

			System.out.println("Dragging point index: " + this.SelectedPoint);

			double[] p = new double[3];
			this.Data.GetPoint(this.SelectedPoint, p);
			System.out.println("Dragging point coordinates: " + p[0] + " " + p[1] + " " + p[2]);
			this.MoveActor.SetPosition(p[0], p[1], p[2] + MorphExampleSphereLattice.CONTROL_ACTOR_OFFSET);

			panel.addActor(this.MoveActor);
			panel.renderLater();
		} else {
			System.out.println("Dragging point NONE");
		}
	}
}