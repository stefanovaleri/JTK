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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;

import javax.swing.JFrame;

import vtk.vtkActor;
import vtk.vtkDataObject;
import vtk.vtkDataSetMapper;
import vtk.vtkFileOutputWindow;
import vtk.vtkInteractorStyleTrackballCamera;
import vtk.vtkLODActor;
import vtk.vtkMultiBlockDataSet;
import vtk.vtkOpenFOAMReader;
import vtk.vtkPOpenFOAMReader;
import vtk.vtkPlane;
import vtk.vtkPolyData;
import vtk.vtkPolyDataMapper;
import vtk.vtkRenderWindowPanel;
import vtk.vtkTableBasedClipDataSet;
import vtk.vtkUnstructuredGrid;

public class VtkVolumeSample {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        MyVtkRenderWindowPanel vtkPanel = new MyVtkRenderWindowPanel();
        String file = "C:\\Users\\davide\\workspace\\vtk\\log.txt";

        vtkFileOutputWindow outwin = new vtkFileOutputWindow();
        outwin.SetFileName(file);
        outwin.SetInstance(outwin);

        frame.setLayout(new BorderLayout());
        frame.getContentPane().add(vtkPanel.getPanel(), BorderLayout.CENTER);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    static class MyVtkRenderWindowPanel {

        private vtkRenderWindowPanel renderWindowPanel;

        public MyVtkRenderWindowPanel() {
//            VTKUtil.LoadAllNativeLibraries();
            File f = new File("C:\\Users\\davide\\Progetti\\Parallel\\AERO-test01-des\\AERO-test01-des.foam");
            vtkOpenFOAMReader reader = getOpenFOAMReader(f);
            init(reader);
        }

        public void init(vtkOpenFOAMReader reader) {
            // A renderer and render window
            renderWindowPanel = new vtkRenderWindowPanel();
            renderWindowPanel.setPreferredSize(new Dimension(600, 600));
            renderWindowPanel.setInteractorStyle(new vtkInteractorStyleTrackballCamera());
            renderWindowPanel.GetRenderer().SetBackground(new double[] { 0.5, 0.1, 0.8 });

            vtkMultiBlockDataSet dataset = reader.GetOutput();

            try {
                int blocksNumber = dataset.GetNumberOfBlocks();
                for (int i = 0; i < blocksNumber; i++) {
                    vtkDataObject block = dataset.GetBlock(i);
                    if (block instanceof vtkMultiBlockDataSet) {
                        // createPatchActors((vtkMultiBlockDataSet) block,null);
                    } else if (block instanceof vtkUnstructuredGrid) {
                        vtkUnstructuredGrid grid = (vtkUnstructuredGrid) block;

                        vtkPlane plane = new vtkPlane();
                        plane.SetOrigin(0, 0, 0);
                        plane.SetNormal(0, 1, 0);

                        vtkTableBasedClipDataSet clipper = new vtkTableBasedClipDataSet();
                        clipper.SetInputData(grid);
                        clipper.SetClipFunction(plane);
                        clipper.Update();

                        vtkDataSetMapper normalMapper = new vtkDataSetMapper();
                        normalMapper.SetInputData(clipper.GetOutput());

                        vtkActor normalActor = new vtkActor();
                        normalActor.SetMapper(normalMapper);
                        normalActor.GetProperty().EdgeVisibilityOn();
                        normalActor.RotateX(-90);
                        getPanel().GetRenderer().AddActor(normalActor);

                        // vtkCutter cutter = new vtkCutter();
                        // cutter.SetInput(grid);
                        // cutter.SetCutFunction(plane);
                        // cutter.Update();
                        //
                        // vtkDataSetMapper surfaceMapper = new
                        // vtkDataSetMapper();
                        // surfaceMapper.SetInput(cutter.GetOutput());
                        //
                        // vtkActor surfaceActor = new vtkActor();
                        // surfaceActor.SetMapper(surfaceMapper);
                        // surfaceActor.GetProperty().EdgeVisibilityOn();
                        // getPanel().GetRenderer().AddActor(surfaceActor);
                    }
                }
            } catch (Exception e) {
                System.out.println("VtkVolumeSample.MyVtkRenderWindowPanel.init()NOOOO");
            }

            renderWindowPanel.GetRenderer().Render();

        }

        private void createPatchActors(vtkMultiBlockDataSet block, double[] color) {
            int subblockNumbers = block.GetNumberOfBlocks();
            for (int i = 0; i < subblockNumbers; i++) {
                vtkDataObject subBlock = block.GetBlock(i);
                if (subBlock instanceof vtkPolyData) {
                    createColoredActor((vtkPolyData) subBlock, color);
                } else if (subBlock instanceof vtkMultiBlockDataSet) {
                    createCellZonesActors((vtkMultiBlockDataSet) subBlock, color);
                }
            }
        }

        private void createCellZonesActors(vtkMultiBlockDataSet block, double[] color) {
            int subblockNumbers = block.GetNumberOfBlocks();
            for (int i = 0; i < subblockNumbers; i++) {
                vtkDataObject subBlock = block.GetBlock(i);
                if (subBlock instanceof vtkPolyData) {
                    createColoredActor((vtkPolyData) subBlock, color);
                }
            }
        }

        private vtkActor createColoredActor(vtkPolyData polyData, double[] color) {

            // vtkPlane plane = new vtkPlane();
            // vtkClipPolyData clipper = new vtkClipPolyData();
            // clipper.SetClipFunction(plane);
            // clipper.InsideOutOn();
            // clipper.SetInputConnection(polyData.GetProducerPort());
            //
            // vtkImplicitPlaneRepresentation planeRepresentation = new
            // vtkImplicitPlaneRepresentation();
            // planeRepresentation.SetPlaceFactor(1.25);
            // planeRepresentation.GetPlane(plane);

            vtkPolyDataMapper mapper = new vtkPolyDataMapper();
            mapper.ScalarVisibilityOff();
            // mapper.SetInputConnection(clipper.GetOutputPort());
            mapper.SetInputData(polyData);
            vtkLODActor actor = new vtkLODActor();
            actor.SetMapper(mapper);
            actor.SetNumberOfCloudPoints(10000);
            getPanel().GetRenderer().AddActor(actor);
            return actor;
        }

        public vtkRenderWindowPanel getPanel() {
            return renderWindowPanel;
        }

        private static vtkOpenFOAMReader getOpenFOAMReader(File foamFile) {
            vtkPOpenFOAMReader reader = new vtkPOpenFOAMReader();
            reader.SetCaseType(0);
            reader.SetFileName(foamFile.getAbsolutePath());
            reader.CreateCellToPointOn();
            reader.DisableAllCellArrays();
            reader.DisableAllLagrangianArrays();
            reader.DisableAllPointArrays();
            reader.EnableAllPatchArrays();
            reader.CacheMeshOff();
            reader.DecomposePolyhedraOn();

            reader.ReadZonesOn();
            reader.SetPatchArrayStatus("internalMesh", 1);

            reader.SetPatchArrayStatus("CaseA_tunnel_inlet", 1);
            reader.SetPatchArrayStatus("CaseA_tunnel_outlet", 1);
            reader.SetPatchArrayStatus("CaseA_tunnel_side_ymin", 1);
            reader.SetPatchArrayStatus("CaseA_tunnel_side_ymax", 1);
            reader.SetPatchArrayStatus("CaseA_tunnel_roof", 1);
            reader.SetPatchArrayStatus("CaseA_tunnel_ground", 1);
            reader.SetPatchArrayStatus("CaseA_tunnel_ground_front", 1);
            reader.SetPatchArrayStatus("CaseA_walls_building", 1);

            reader.Update();
            return reader;
        }

    }

}
