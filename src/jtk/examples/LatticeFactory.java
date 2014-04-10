package jtk.examples;

import vtk.vtkCellArray;
import vtk.vtkPoints;
import vtk.vtkPolyData;
import vtk.vtkTransform;
import vtk.vtkTransformPolyDataFilter;

public class LatticeFactory {

	
	public static vtkPolyData create1x1x1OctaLattice(double[] position, double scale) {
		vtkPoints points = createOctaPoints1();
		vtkCellArray faces = createOctaFaces1();
		
		vtkPolyData lattice = new vtkPolyData();
		lattice.SetPoints(points);
		lattice.SetPolys(faces);
		
		vtkTransform t = new vtkTransform();
		t.Translate(position);
		t.Scale(scale, scale, scale);
		
		vtkTransformPolyDataFilter filter = new vtkTransformPolyDataFilter();
		filter.SetInputData(lattice);
		filter.SetTransform(t);
		filter.Update();
		
		printInfo(filter.GetOutput());
		
		return filter.GetOutput();
	}

	public static vtkPolyData create1x1x0Lattice(double[] position, double scale) {
		vtkPoints points = createPoints0();
		vtkCellArray faces = createFaces0();
		
		vtkPolyData lattice = new vtkPolyData();
		lattice.SetPoints(points);
		lattice.SetPolys(faces);
		
		printInfo(lattice);
		
		vtkTransform t = new vtkTransform();
		t.Translate(position);
		t.Scale(scale, scale, scale);

		vtkTransformPolyDataFilter filter = new vtkTransformPolyDataFilter();
		filter.SetInputData(lattice);
		filter.SetTransform(t);
		filter.Update();
		
		return filter.GetOutput();
	}

	public static vtkPolyData create1x1x1Lattice(double[] position, double scale) {
		vtkPoints points = createPoints1();
		vtkCellArray faces = createFaces1();
		
		vtkPolyData lattice = new vtkPolyData();
		lattice.SetPoints(points);
		lattice.SetPolys(faces);
		
		vtkTransform t = new vtkTransform();
		t.Translate(position);
		t.Scale(scale, scale, scale);
		
		vtkTransformPolyDataFilter filter = new vtkTransformPolyDataFilter();
		filter.SetInputData(lattice);
		filter.SetTransform(t);
		filter.Update();
		
		printInfo(filter.GetOutput());
		
		return filter.GetOutput();
	}

	private static void printInfo(vtkPolyData lattice) {
		vtkPoints latticePts = lattice.GetPoints();
		int nPoints = latticePts.GetNumberOfPoints();
		
		vtkCellArray latticePolys = lattice.GetPolys();
		int nCells = latticePolys.GetNumberOfCells();
		int maxSize = latticePolys.GetMaxCellSize();
		int nTriangles = latticePolys.GetNumberOfConnectivityEntries() / 4;

		System.out.println("Points    : "+nPoints);
		System.out.println("Cells     : "+nCells);
		System.out.println("Max dim   : "+maxSize);
		System.out.println("Triangles : "+nTriangles);
		
		if ( nTriangles != nCells )
		{
			System.err.println("Control mesh must be a closed, manifold triangular mesh");
		}
	}

	public static vtkPolyData create2x2x2Lattice() {
		vtkPoints points = createPoints2();
    	vtkCellArray faces = createFaces2();

    	vtkPolyData lattice = new vtkPolyData();
    	lattice.SetPoints(points);
    	lattice.SetPolys(faces);
    	
    	return lattice;
	}
	
	private static vtkPoints createPoints0() {
		vtkPoints pts = new vtkPoints();
		pts.SetNumberOfPoints(3);
		pts.SetPoint(0, 0, 0, 0);
		pts.SetPoint(1, 1, 0, 0);
		pts.SetPoint(2, 1, 1, 0);
		
		return pts;
		
	}

	private static vtkPoints createOctaPoints1() {
		vtkPoints pts = new vtkPoints();
		pts.SetNumberOfPoints(5);
		pts.SetPoint(0, 0, 0, 0);
		pts.SetPoint(1, 1, 0, 0);
		pts.SetPoint(2, 1, 1, 0);
		pts.SetPoint(3, 0, 1, 0);
		pts.SetPoint(4, 0.5, 0.5, 1);
		
		return pts;
	}

	private static vtkPoints createPoints1() {
		vtkPoints pts = new vtkPoints();
		pts.SetNumberOfPoints(14);
		
		pts.SetPoint(0, 0, 0, 0);
		pts.SetPoint(1, 1, 0, 0);
		pts.SetPoint(2, 1, 1, 0);
		pts.SetPoint(3, 0, 1, 0);
		
		pts.SetPoint(4, 0, 0, 1);
		pts.SetPoint(5, 1, 0, 1);
		pts.SetPoint(6, 1, 1, 1);
		pts.SetPoint(7, 0, 1, 1);

		pts.SetPoint(8, 0.5, 0.5, 0);
		pts.SetPoint(9, 0.5, 0.5, 1);
		
		pts.SetPoint(10, 0.5, 0, 0.5);
		pts.SetPoint(11, 1, 0.5, 0.5);
		pts.SetPoint(12, 0.5, 1.0, 0.5);
		pts.SetPoint(13, 0, 0.5, 0.5);
		
		return pts;
	}

	private static vtkPoints createPoints2() {
		vtkPoints pts = new vtkPoints();
		pts.SetNumberOfPoints(8);
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

	private static vtkCellArray createFaces0() {
		vtkCellArray array = new vtkCellArray();
		addFace(array, 0,1,2);
		return array;
	}

	private static vtkCellArray createFaces1() {
		vtkCellArray array = new vtkCellArray();
		addFace(array, 0,1,2,3, 8);
		addFace(array, 0,1,5,4, 10);
		addFace(array, 2,1,5,6, 11);
		addFace(array, 2,3,7,6, 12);
		addFace(array, 0,3,7,4, 13);
		addFace(array, 5,4,7,6, 9);
//		addFace(array, 0,1,2,3);
//		addFace(array, 0,1,5,4);
//		addFace(array, 2,1,5,6);
//		addFace(array, 2,3,7,6);
//		addFace(array, 0,3,7,4);
//		addFace(array, 5,4,7,6);
		
		return array;
	}

	private static vtkCellArray createOctaFaces1() {
		vtkCellArray array = new vtkCellArray();
		addFace(array, 0,1,4);
		addFace(array, 1,2,4);
		addFace(array, 2,3,4);
		addFace(array, 3,0,4);
		return array;
	}

	private static void addFace(vtkCellArray array, Integer... points) {
		if (points.length == 4) {
			array.InsertNextCell(3);
			array.InsertCellPoint(points[0]);
			array.InsertCellPoint(points[1]);
			array.InsertCellPoint(points[2]);
			array.InsertNextCell(3);
			array.InsertCellPoint(points[0]);
			array.InsertCellPoint(points[3]);
			array.InsertCellPoint(points[2]);
		} else if (points.length == 5) {
			array.InsertNextCell(3);
			array.InsertCellPoint(points[0]);
			array.InsertCellPoint(points[1]);
			array.InsertCellPoint(points[4]);
			array.InsertNextCell(3);
			array.InsertCellPoint(points[1]);
			array.InsertCellPoint(points[2]);
			array.InsertCellPoint(points[4]);
			array.InsertNextCell(3);
			array.InsertCellPoint(points[2]);
			array.InsertCellPoint(points[3]);
			array.InsertCellPoint(points[4]);
			array.InsertNextCell(3);
			array.InsertCellPoint(points[3]);
			array.InsertCellPoint(points[0]);
			array.InsertCellPoint(points[4]);
		} else {
			array.InsertNextCell(points.length);
			for (Integer integer : points) {
				array.InsertCellPoint(integer);
			}
		}
	}
	
	private static vtkCellArray createFaces2() {
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
}
