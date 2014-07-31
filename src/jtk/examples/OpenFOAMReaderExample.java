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
package jtk.examples;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;

import javax.swing.AbstractAction;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import jtk.core.VTKUtil;

import vtk.vtkActor;
import vtk.vtkBoxWidget;
import vtk.vtkCompositeDataGeometryFilter;
import vtk.vtkCompositeDataPipeline;
import vtk.vtkCutter;
import vtk.vtkExecutive;
import vtk.vtkInteractorStyleTrackballCamera;
import vtk.vtkMultiBlockDataSet;
import vtk.vtkOpenFOAMReader;
import vtk.vtkPicker;
import vtk.vtkPlane;
import vtk.vtkPolyDataMapper;
import vtk.vtkRenderWindow;
import vtk.vtkRenderWindowInteractor;
import vtk.vtkRenderer;
import vtk.vtkTransform;

public class OpenFOAMReaderExample implements JTKDemo {

    private vtkBoxWidget boxWidget;
    private vtkPicker picker;

    @Override
    public void demo(String[] args) {
        System.out.println("OpenFOAMReaderExample.demo() "+Arrays.toString(args));
        if (args.length == 0) {
            loadMesh(getFile());
        } else {
            File file = new File(args[0]);
            if (file.exists()) {
                loadMesh(file);
            } else {
                System.err.println("'args[0]': wrong parameter");
                System.exit(-1);
            }
        }
    }

    private static File getFile() {
        JFileChooser fc = new JFileChooser(Preferences.userRoot().get("last.dir", null));
        int retVal = fc.showOpenDialog(null);
        if (retVal == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fc.getSelectedFile();
            Preferences.userRoot().put("last.dir", selectedFile.getAbsolutePath());
            return selectedFile;
        } else {
            return null;
        }
    }

//    public void loadMesh(File file, Boolean parallel) {
//        long start = System.currentTimeMillis();
//
//        vtkOpenFOAMReader reader = new vtkOpenFOAMReader();
//        // vtkOpenFOAMReader reader = new vtkOpenFOAMReader();
//        reader.SetFileName(file.getAbsolutePath());
//        // if (parallel.booleanValue())
//        // reader.SetCaseType(0);
//        // else {
//        // reader.SetCaseType(1);
//        // }
//
//        vtkCompositeDataGeometryFilter gf = new vtkCompositeDataGeometryFilter();
//        gf.SetInputConnection(0, reader.GetOutputPort(0));
//
//        vtkPolyDataMapper meshMapper = new vtkPolyDataMapper();
//        meshMapper.SetInputConnection(gf.GetOutputPort());
//        meshMapper.CreateDefaultLookupTable();
//
//        vtkPlane plane = new vtkPlane();
//        plane.SetOrigin(meshMapper.GetCenter());
//        plane.SetNormal(-0.287D, 0.0D, 0.9579D);
//
//        vtkCutter cutter = new vtkCutter();
//        cutter.SetCutFunction(plane);
//        cutter.SetInputData(meshMapper.GetInput());
//        cutter.Update();
//
//        vtkPolyDataMapper cutterMapper = new vtkPolyDataMapper();
//        cutterMapper.SetInputConnection(cutter.GetOutputPort());
//
//        vtkActor planeActor = new vtkActor();
//        planeActor.GetProperty().SetColor(1.0D, 1.0D, 0.0D);
//        planeActor.GetProperty().SetLineWidth(2.0D);
//        planeActor.SetMapper(cutterMapper);
//
//        vtkActor meshActor = new vtkActor();
//        meshActor.SetMapper(meshMapper);
//        vtkRenderer ren = new vtkRenderer();
//
//        vtkRenderWindow renWin = new vtkRenderWindow();
//        renWin.AddRenderer(ren);
//
//        ren.AddActor(meshActor);
//
//        ren.SetBackground(0.1D, 0.2D, 0.4D);
//        renWin.SetSize(1024, 768);
//
//        meshMapper.Update();
//
//        vtkRenderWindowInteractor iren = new vtkRenderWindowInteractor();
//        iren.SetRenderWindow(renWin);
//
//        long end = System.currentTimeMillis();
//        System.out.println("Execution time was " + (end - start) + " ms.");
//
//        iren.Start();
//    }

    public void loadMesh(File file) {
        if (file == null) return;
        
        long start = System.currentTimeMillis();

        vtkOpenFOAMReader reader = new vtkOpenFOAMReader();
        // reader.SetCaseType(1);
        reader.SetFileName(file.getAbsolutePath());
        vtkExecutive exe = reader.GetExecutive();
        vtkCompositeDataPipeline pipeline = (vtkCompositeDataPipeline) exe;

        System.out.println("OpenFOAMReaderExample.loadMesh() 00000000000000000000000000000000000000000000000");
        pipeline.SetUpdateTimeStep(0, 0.0);
        reader.Update();
        VTKUtil.printDatasetData(reader);

        System.out.println("OpenFOAMReaderExample.loadMesh() 11111111111111111111111111111111111111111111111");
        pipeline.SetUpdateTimeStep(0, 1.0);
        reader.Update();
        VTKUtil.printDatasetData(reader);

        System.out.println("OpenFOAMReaderExample.loadMesh() 11111111111111111111111111111111111111111111111");
        pipeline.SetUpdateTimeStep(0, 2.0);
        reader.Update();
        VTKUtil.printDatasetData(reader);
        
//        vtkActor meshActor = getActor(reader);
//        vtkRenderer renderer = new vtkRenderer();
//
//        // Add the actors to the scene
//        renderer.AddActor(meshActor);
//        renderer.SetBackground(0.1, 0.2, 0.4);
//
//        vtkRenderWindow renderWindow = new vtkRenderWindow();
//        renderWindow.AddRenderer(renderer);
//        renderWindow.SetSize(800, 600);
//
//
//        vtkRenderWindowInteractor renderWindowInteractor = new vtkRenderWindowInteractor();
//        renderWindowInteractor.SetRenderWindow(renderWindow);
//
//        vtkInteractorStyleTrackballCamera style = new vtkInteractorStyleTrackballCamera();
//        renderWindowInteractor.SetInteractorStyle(style);

//        addPicker(renderWindowInteractor);

//        addBoxWidget(meshActor, renderWindowInteractor);

//        createPatchesListPanel(reader).setVisible(true);

        // Render and interact
//        renderWindowInteractor.Initialize();
//        renderWindowInteractor.Start();

        long end = System.currentTimeMillis();
        System.out.println("Execution time was " + (end - start) + " ms.");
    }

    public vtkActor getActor(vtkOpenFOAMReader reader) {
        vtkCompositeDataGeometryFilter gf = new vtkCompositeDataGeometryFilter();
        gf.SetInputConnection(0, reader.GetOutputPort());

        vtkPolyDataMapper meshMapper = new vtkPolyDataMapper();
        meshMapper.SetInputConnection(gf.GetOutputPort());
        meshMapper.CreateDefaultLookupTable();

        vtkActor meshActor = new vtkActor();
        meshActor.SetMapper(meshMapper);
        // meshActor.GetProperty().SetRepresentationToSurface();
        return meshActor;
    }

    public void addBoxWidget(vtkActor meshActor, vtkRenderWindowInteractor renderWindowInteractor) {
        // BOXWIDGET
        boxWidget = new vtkBoxWidget();
        boxWidget.SetInteractor(renderWindowInteractor);
        boxWidget.SetPlaceFactor(1.25);

        boxWidget.SetProp3D(meshActor);
        boxWidget.PlaceWidget();

        vtkMyCallback callback = new vtkMyCallback();
        boxWidget.AddObserver("InteractionEvent", callback, "pippo");
        // boxWidget.On();
        boxWidget.Off();
    }

    public void addPicker(vtkRenderWindowInteractor renderWindowInteractor) {
        // PICKER
        picker = new vtkPicker();
        picker.AddObserver("EndPickEvent", new vtkPickCallback(), "annotatePick");
        renderWindowInteractor.SetPicker(picker);
    }
    
    

    private JFrame createPatchesListPanel(final vtkOpenFOAMReader reader) {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        final JList<String> list = new JList<>(listModel);
        int patchesNumber = reader.GetNumberOfPatchArrays();
        for (int i = 0; i < patchesNumber; i++) {
            String patchName = reader.GetPatchArrayName(i);
            int status = reader.GetPatchArrayStatus(patchName);
            listModel.add(i, patchName);
            if (status == 1) {
                list.getSelectionModel().addSelectionInterval(i, i);
            }
        }
        list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (e.getValueIsAdjusting())
                    return;
                List<String> selection = list.getSelectedValuesList();
                reader.DisableAllPatchArrays();
                for (int i = 0; i < selection.size(); i++) {
                    String patchName = selection.get(i);
                    reader.SetPatchArrayStatus(patchName, 1);
                }
            }
        });
        JButton applyButton = new JButton(new AbstractAction("Update") {
            @Override
            public void actionPerformed(ActionEvent e) {
                reader.Update();
            }
        });
        JFrame frame = new JFrame("Prova");
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(new JScrollPane(list), BorderLayout.CENTER);
        frame.getContentPane().add(applyButton, BorderLayout.SOUTH);
        frame.setSize(100, 600);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(screen.width - frame.getWidth(), 0);
        return frame;
    }

    class vtkPickCallback {
        public void annotatePick() {
            // System.out.println(picker.GetViewProp().Print());
        }
    }

    class vtkMyCallback {
        public void pippo() {
            vtkTransform t = new vtkTransform();
            boxWidget.GetTransform(t);
            boxWidget.GetProp3D().SetUserTransform(t);
            System.out.println("Mesh3D_VTK.pippo()");
        }
    }
}
