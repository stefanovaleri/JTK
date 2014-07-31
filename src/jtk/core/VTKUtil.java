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
package jtk.core;

import vtk.vtkActor;
import vtk.vtkActorCollection;
import vtk.vtkAlgorithm;
import vtk.vtkAlgorithmOutput;
import vtk.vtkCellData;
import vtk.vtkCompositeDataPipeline;
import vtk.vtkDataArray;
import vtk.vtkDataObject;
import vtk.vtkDataSet;
import vtk.vtkDoubleArray;
import vtk.vtkExecutive;
import vtk.vtkInformation;
import vtk.vtkInformationDoubleKey;
import vtk.vtkInformationDoubleVectorKey;
import vtk.vtkMapper;
import vtk.vtkMultiBlockDataSet;
import vtk.vtkObject;
import vtk.vtkOpenFOAMReader;
import vtk.vtkOutlineFilter;
import vtk.vtkPointData;
import vtk.vtkPolyData;
import vtk.vtkQuadricLODActor;
import vtk.vtkUnstructuredGrid;

public class VTKUtil {

    private static final double SPECULAR = 0.1;
    private static final double DIFFUSE = 0.8;
    private static final double AMBIENT = 0.1;

//    public static BoundingBox computeBoundingBox(List<Controller3D> controllers, boolean visibleOnly) {
//
//        List<vtkActor> allVisibleActors = new ArrayList<>();
//
//        for (Controller3D controller : controllers) {
//            Collection<vtkActor> actors = controller.getActorsList();
//            for (vtkActor vtkActor : actors) {
//                if (!visibleOnly || (visibleOnly && vtkActor.GetVisibility() == 1)) {
//                    allVisibleActors.add(vtkActor);
//                }
//            }
//        }
//
//        return computeBoundingBox(allVisibleActors);
//    }

//    public static BoundingBox computeBoundingBox(Collection<vtkActor> actors) {
//        double xmin = Double.MAX_VALUE;
//        double xmax = -Double.MAX_VALUE;
//        double ymin = Double.MAX_VALUE;
//        double ymax = -Double.MAX_VALUE;
//        double zmin = Double.MAX_VALUE;
//        double zmax = -Double.MAX_VALUE;
//
//        for (vtkActor actor : actors) {
//            if (actor != null) {
//                vtkMapper mapper = actor.GetMapper();
//                double[] bounds = mapper.GetBounds();
//                xmin = Math.min(xmin, bounds[0]);
//                xmax = Math.max(xmax, bounds[1]);
//                ymin = Math.min(ymin, bounds[2]);
//                ymax = Math.max(ymax, bounds[3]);
//                zmin = Math.min(zmin, bounds[4]);
//                zmax = Math.max(zmax, bounds[5]);
//            }
//        }
//
//        return new BoundingBox(xmin, xmax, ymin, ymax, zmin, zmax);
//    }

    public static void printDatasetData(vtkOpenFOAMReader reader) {
        info("[reader] ---------------" + reader.GetFileName() + "----------------");
        int patchesNumber = reader.GetNumberOfPatchArrays();
        info("[reader]      Patches Number: " + patchesNumber);
        info("[reader]      Patches List: ");
        for (int i = 0; i < patchesNumber; i++) {
            String patchName = reader.GetPatchArrayName(i);
            int status = reader.GetPatchArrayStatus(patchName);
            info("[reader]          (" + i + ") " + patchName + ", status: " + (status == 0 ? "disabled" : "enabled"));
        }

        int cellArraysNumber = reader.GetNumberOfCellArrays();
        info("[reader]      Cell Arrays Number: " + cellArraysNumber);
        info("[reader]      Cell Arrays List: ");
        for (int i = 0; i < cellArraysNumber; i++) {
            String cellArrayName = reader.GetCellArrayName(i);
            int status = reader.GetCellArrayStatus(cellArrayName);
            info("[reader]          (" + i + ") " + cellArrayName + ", status: " + (status == 0 ? "disabled" : "enabled"));
        }

        int pointArraysNumber = reader.GetNumberOfPointArrays();
        info("[reader]      Point Arrays Number: " + pointArraysNumber);
        info("[reader]      Point Arrays List: ");
        for (int i = 0; i < pointArraysNumber; i++) {
            String pointArrayName = reader.GetPointArrayName(i);
            int status = reader.GetPointArrayStatus(pointArrayName);
            info("[reader]          (" + i + ") " + pointArrayName + ", status: " + (status == 0 ? "disabled" : "enabled"));
        }

        info("[reader]      Decompose Polyhedra: " + reader.GetDecomposePolyhedra());

        int lagrangianArraysNumber = reader.GetNumberOfLagrangianArrays();
        info("[reader]      Lagrangian Arrays Number: " + lagrangianArraysNumber);
        info("[reader]      Lagrangian Arrays List: ");
        for (int i = 0; i < lagrangianArraysNumber; i++) {
            String lagrangianArrayName = reader.GetLagrangianArrayName(i);
            int status = reader.GetLagrangianArrayStatus(lagrangianArrayName);
            info("[reader]          (" + i + ") " + lagrangianArrayName + ", status: " + (status == 0 ? "disabled" : "enabled"));
        }

        info("[reader]      Read Zones: " + reader.GetReadZones());

        vtkDoubleArray times = reader.GetTimeValues();
        if (times != null) {
            System.out.print("[reader]      Time Values: [");
            for (int i = 0; i < times.GetSize(); i++) {
                System.out.print(times.GetValue(i) + ", ");
            }
            info("]");
        }
        
        vtkExecutive exe = reader.GetExecutive();
        vtkCompositeDataPipeline pipeline = (vtkCompositeDataPipeline) exe;
        vtkInformation outInfo = exe.GetOutputInformation(0);
        vtkInformationDoubleVectorKey TIME_STEPS = pipeline.TIME_STEPS();
        vtkInformationDoubleKey UPDATE_TIME_STEP = pipeline.UPDATE_TIME_STEP();

        int nTimeSteps = outInfo.Length(TIME_STEPS); // Get the number of time steps
        info("[pipeline]    Time Values: " + nTimeSteps);
        info("[pipeline]    Time Values: current is " + outInfo.Get(UPDATE_TIME_STEP));
        for (int i = 0; i < nTimeSteps; i++) {
            double timeValue = outInfo.Get(TIME_STEPS, i); // Get the i-th time value
            info("[pipeline]       Step: " + i + ", Value: " + timeValue);
            
        }

        vtkMultiBlockDataSet dataset = reader.GetOutput();
        int blocksNumber = dataset.GetNumberOfBlocks();
        info("[dataset]\tBlocks Number: " + blocksNumber);
        for (int i = 0; i < blocksNumber; i++) {
            vtkDataObject block = dataset.GetBlock(i);
            readBlock(block, i, "\t");
        }
        info("[reader] ---------------" + reader.GetFileName() + "----------------");
    }
    
    private static void info(String msg){
        System.out.println(msg);
    }

    public static void readBlock(vtkDataObject block, int i, String indent) {
        if (block instanceof vtkMultiBlockDataSet) {
            info("[dataset]" + indent + "Block " + i + ": MultiBlockDataset");
            vtkMultiBlockDataSet subDataset = (vtkMultiBlockDataSet) block;

            int subblockNumbers = subDataset.GetNumberOfBlocks();
            info("[dataset]" + indent + "Block " + i + " has " + subblockNumbers + " Sub Blocks");

            for (int j = 0; j < subblockNumbers; j++) {
                vtkDataObject subBlock = subDataset.GetBlock(j);
                readBlock(subBlock, j, indent + indent);
            }
        } else if (block instanceof vtkUnstructuredGrid) {
            info("[dataset]" + indent + "Block " + i + ": UnstructuredGrid");
            readFields((vtkDataSet) block);
        } else if (block instanceof vtkPolyData) {
            info("[dataset]" + indent + "Block " + i + ": PolyData");
            readFields((vtkDataSet) block);
        } else {
            info("[dataset]" + indent + "Block " + i + ": OTHER " + block);
        }
    }

    public static void readFields(vtkDataSet dataSet) {
        vtkPointData pointData = dataSet.GetPointData();
        int arraysNumber = pointData.GetNumberOfArrays();
        info("[dataset]\tPointData Arrays Number: " + arraysNumber);
        for (int i = 0; i < arraysNumber; i++) {
            info("[dataset]\t\t array " + i + ": " + pointData.GetArrayName(i));
        }
        vtkCellData cellData = dataSet.GetCellData();
        info("[dataset]\tCellData Arrays Number: " + arraysNumber);
        for (int i = 0; i < cellData.GetNumberOfArrays(); i++) {
            info("[dataset]\t\t array " + i + ": " + cellData.GetArrayName(i));
        }
        
    }

    public static void setRepresentation(Representation representation, vtkActorCollection actors) {
        for (int i = 0; i < actors.GetNumberOfItems(); i++) {
            setRepresentation(representation, (vtkActor) actors.GetItemAsObject(i));
        }
    }

    public static void setRepresentation(Representation representation, vtkActor actor) {
        switch (representation) {
        case SURFACE:
            actor.GetProperty().SetRepresentationToSurface();
            actor.GetProperty().EdgeVisibilityOff();
            break;
        case WIREFRAME:
            actor.GetProperty().SetRepresentationToWireframe();
            actor.GetProperty().EdgeVisibilityOff();
            break;
        case SURFACE_WITH_EDGES:
            actor.GetProperty().SetRepresentationToSurface();
            actor.GetProperty().EdgeVisibilityOn();
            break;
        case OUTLINE:
            actor.GetProperty().SetRepresentationToSurface();
            actor.GetProperty().EdgeVisibilityOff();
            break;

        default:
            break;
        }
    }

//    public static void gc() {
//        if (VTKSettings.librariesAreLoaded()) {
//            vtkReferenceInformation info = vtkObject.JAVA_OBJECT_MANAGER.gc(true);
//            logger.info("K: " + info.listKeptReferenceToString());
//            logger.info("R: " + info.listRemovedReferenceToString());
//        }
//    }

    /* internal mesh */
//    public static vtkActor newLODInternalMeshActor(vtkUnstructuredGrid dataset) {
//        vtkActor actor = newLODActor(dataset);
//
//        actor.GetMapper().ScalarVisibilityOff();
//        actor.VisibilityOff();
//        actor.GetProperty().SetOpacity(1);
//
//        return actor;
//    }

    /* cell zones */
//    public static vtkActor newLODActor(vtkUnstructuredGrid dataset) {
//        vtkUnstructuredGrid input = new vtkUnstructuredGrid();
//        input.ShallowCopy(dataset);
//
//        vtkDataSetSurfaceFilter filter = new vtkDataSetSurfaceFilter();
//        filter.SetInput(input);
//        filter.Update();
//        input.Delete();
//
//        vtkActor actor = newQuadricLODActor(filter.GetOutput(), true);
//        filter.Delete();
//
//        return actor;
//    }

    /* patches, face zones, surfaces */
//    public static vtkActor newLODActor(vtkPolyData dataset, boolean visible) {
//        vtkPolyData input = new vtkPolyData();
//        input.ShallowCopy(dataset);
//
//        if (input.GetActualMemorySize() > MEMORY) {
//            return newQuadricLODActor(input, visible);
//        } else {
//            return newActor(input, visible);
//        }
//    }

//    private static vtkActor newQuadricLODActor(vtkPolyData input, boolean visible) {
//        vtkPolyDataMapper mapper = new vtkPolyDataMapper();
//        mapper.SetInput(input);
//        input.Delete();
//
//        vtkQuadricLODActor actor = new vtkQuadricLODActor();
//        actor.SetDataConfigurationToXYZVolume();
//        actor.SetMapper(mapper);
//        actor.SetVisibility(visible ? 1 : 0);
//        mapper.Update();
//        mapper.Delete();
//
//        actor.GetProperty().SetAmbient(AMBIENT);
//        actor.GetProperty().SetDiffuse(DIFFUSE);
//        actor.GetProperty().SetSpecular(SPECULAR);
//
//        return actor;
//    }

//    public static vtkActor newActor(vtkPolyData dataset, boolean visible) {
//        vtkPolyDataMapper mapper = new vtkPolyDataMapper();
//        mapper.SetInput(dataset);
//
//        vtkActor actor = new vtkActor();
//        actor.SetMapper(mapper);
//        actor.SetVisibility(visible ? 1 : 0);
//        mapper.Delete();
//
//        actor.GetProperty().SetAmbient(AMBIENT);
//        actor.GetProperty().SetDiffuse(DIFFUSE);
//        actor.GetProperty().SetSpecular(SPECULAR);
//
//        return actor;
//    }

    public static void exit() {
        vtkObject.JAVA_OBJECT_MANAGER.deleteAll();
    }

//    public static void changeDataset(vtkActor actor, vtkPolyData subset) {
//        vtkMapper mapper = actor.GetMapper();
//        ((vtkPolyDataMapper) mapper).SetInput((vtkPolyData) subset);
//    }

//    public static void changeDataset(vtkActor actor, vtkUnstructuredGrid subset) {
//        vtkMapper mapper = actor.GetMapper();
//        vtkAlgorithmOutput filterOutput = mapper.GetInputConnection(0, 0);
//        vtkPolyDataAlgorithm filter = (vtkPolyDataAlgorithm) filterOutput.GetProducer();
//
//        VTKUtil.deleteDataset(filter.GetInput());
//        filter.SetInput(subset);
//        mapper.Update();
//    }

    public static void deleteActor(vtkActor actor) {
        vtkMapper mapper = actor.GetMapper();
        vtkDataSet dataset = mapper.GetInputAsDataSet();

        deleteDataset(dataset);

        if (actor instanceof vtkQuadricLODActor) {
            vtkQuadricLODActor lodActor = (vtkQuadricLODActor) actor;
            lodActor.GetLODFilter().Delete();
        }

        mapper.RemoveAllInputs();
        mapper.Delete();
        actor.SetMapper(null);

        actor.Delete();
    }

    public static void deleteDataset(vtkDataObject dataObject) {
        if (dataObject instanceof vtkDataSet) {
            vtkDataSet dataSet = (vtkDataSet) dataObject;
            vtkPointData pointData = dataSet.GetPointData();
            vtkDataArray pScalars = pointData.GetScalars();
            if (pScalars != null)
                pScalars.Delete();
            pointData.Delete();

            vtkCellData cellData = dataSet.GetCellData();
            vtkDataArray cScalars = cellData.GetScalars();
            if (cScalars != null)
                cScalars.Delete();
            cellData.Delete();

            dataSet.Delete();
        } else {
            System.err.println("NOT A DATASET");
        }

    }

    public static void setOutLine(Representation actual, Representation r, vtkActorCollection actors) {
        if (r == Representation.OUTLINE) {
            if (actual != Representation.OUTLINE) {
                VTKUtil.outlineActors(actors);
            }
        } else {
            if (actual == Representation.OUTLINE) {
                VTKUtil.deoutlineActors(actors);
            }
        }
    }

    public static void setOutLine(Representation r, vtkActor actor) {
        if (r == Representation.OUTLINE) {
            if (!VTKUtil.isOutlined(actor)) {
                VTKUtil.outlineActor(actor);
            }
        } else {
            if (VTKUtil.isOutlined(actor)) {
                VTKUtil.deoutlineActor(actor);
            }
        }
    }

    public static void outlineActors(vtkActorCollection actors) {
        for (int i = 0; i < actors.GetNumberOfItems(); i++) {
            vtkActor actor = (vtkActor) actors.GetItemAsObject(i);
            if (!isOutlined(actor)) {
                outlineActor(actor);
            }
        }
    }

    public static void outlineActor(vtkActor actor) {
        vtkMapper mapper = actor.GetMapper();
        vtkAlgorithmOutput datasetOutput = mapper.GetInputConnection(0, 0);

        vtkOutlineFilter outline = new vtkOutlineFilter();
        outline.SetInputConnection(datasetOutput);

        mapper.SetInputConnection(outline.GetOutputPort());
    }

    public static void deoutlineActors(vtkActorCollection actors) {
        for (int i = 0; i < actors.GetNumberOfItems(); i++) {
            vtkActor actor = (vtkActor) actors.GetItemAsObject(i);
            if (isOutlined(actor)) {
                deoutlineActor(actor);
            }
        }
    }

    public static boolean isOutlined(vtkActor actor) {
        vtkMapper mapper = actor.GetMapper();
        vtkAlgorithmOutput filterOutput = mapper.GetInputConnection(0, 0);
        vtkAlgorithm filter = filterOutput.GetProducer();
        return filter instanceof vtkOutlineFilter;
    }

    public static void deoutlineActor(vtkActor actor) {
        vtkMapper mapper = actor.GetMapper();
        vtkAlgorithmOutput filterOutput = mapper.GetInputConnection(0, 0);
        vtkAlgorithm filter = filterOutput.GetProducer();

        vtkAlgorithmOutput datasetOutput = filter.GetInputConnection(0, 0);
        mapper.SetInputConnection(datasetOutput);
    }

//    public static void applyTypeToLookupTable(FieldItem fieldItem, vtkLookupTable table) {
//        // Vector Mode
//        if (fieldItem.getComponent() <= 0) {
//            table.SetVectorModeToMagnitude();
//        } else {
//            table.SetVectorModeToComponent();
//            table.SetVectorComponent(fieldItem.getComponent() - 1);
//        }
//
//        // Range
//        table.SetRange(fieldItem.getRange());
//        table.SetNumberOfTableValues(fieldItem.getResolution());
//
//        // Colors
//        ScalarBarType scalarBarType = fieldItem.getScalarBarType();
//        
//        List<double[]> colors = scalarBarType.getColors();
//        int size = colors.size();
//        if (size == 1) {
//            table.SetHueRange(scalarBarType.getColors().get(0));
//            table.ForceBuild();
//        } else {
//            table.SetNumberOfTableValues(size);
//            for (int i = 0; i < size; i++) {
//                double[] values = colors.get(i);
//                double red = values[0];
//                double green = values[1];
//                double blue = values[2];
//                table.SetTableValue(i, red, green, blue, 1);
//            }
//            table.Build();
//        }
//    }

}
