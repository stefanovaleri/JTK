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

import vtk.vtkActor;
import vtk.vtkCellArray;
import vtk.vtkInteractorStyleTrackballActor;
import vtk.vtkInteractorStyleTrackballCamera;
import vtk.vtkLinearExtrusionFilter;
import vtk.vtkPoints;
import vtk.vtkPolyData;
import vtk.vtkPolyDataMapper;
import vtk.vtkRenderWindow;
import vtk.vtkRenderWindowInteractor;
import vtk.vtkRenderer;
import vtk.vtkRotationalExtrusionFilter;

public class ExtrudeTest implements JTKDemo {

    /*
     * Instance variable of RenderWindowIneractor for reference by addObserver
     * Callback.
     */
    vtkRenderWindowInteractor iren = null;
    /*
     * TrackballActor style interactor for addObserver callback reference
     */
    vtkInteractorStyleTrackballActor astyle = new vtkInteractorStyleTrackballActor();
    /*
     * TrackballCamera style interactor for addObserver callback reference
     */
    vtkInteractorStyleTrackballCamera cstyle = new vtkInteractorStyleTrackballCamera();
    /*
     * Interactor state for addObserver callback reference.
     */
    char curIStyle = 'A'; // interaction style A = Actor C = camera,

    // toggled by 'C' key handler.
 
    /*
     * toggleStyle is a callback set on the renderWindowInteractor for the
     * CharEvent using AddObserver. It expects the this pointer for the class
     * and refers to the iren, curIStyle, cstyle and astyle attributes. it
     * toggles the interaction between TrackballActor and TrackballCamera when
     * the 'c' key is pressed.
     */

    void toggleStyle() {
        if (iren.GetKeyCode() == 'c' | iren.GetKeyCode() == 'C') {
            if (curIStyle == 'A') {
                curIStyle = 'C';
                iren.SetInteractorStyle(cstyle);
            } else {
                curIStyle = 'A';
                iren.SetInteractorStyle(astyle);
            }
        }

    }

    /*
     * the doit() method constructs the extrusion profiles, builds the extruded
     * objects, creates the mappers and actors, renderWindow and Interactor. and
     * specifies the CharEvent callback for the interaction control.
     */
    @Override
    public void demo() {

        vtkPoints W21x101 = new vtkPoints();

        vtkCellArray lineArray = new vtkCellArray(); // declare the storage for
                                                     // the points

        vtkCellArray rotArray = new vtkCellArray(); // declare the storage for
                                                    // the points

        /*
         * Point data for the profile objects for extrusion
         */
        W21x101.InsertPoint(0, 6.145, 10.68, 0.0);
        W21x101.InsertPoint(1, -6.145, 10.68, 0.0);
        W21x101.InsertPoint(2, -6.145, 9.88, 0.0);
        W21x101.InsertPoint(3, -0.44, 9.88, 0.0);
        W21x101.InsertPoint(4, -0.44, -9.88, 0.0);
        W21x101.InsertPoint(5, -6.145, -9.88, 0.0);
        W21x101.InsertPoint(6, -6.145, -10.68, 0.0);
        W21x101.InsertPoint(7, 6.145, -10.68, 0.0);
        W21x101.InsertPoint(8, 6.145, -9.88, 0.0);
        W21x101.InsertPoint(9, 0.44, -9.88, 0.0);
        W21x101.InsertPoint(10, 0.44, 9.88, 0.0);
        W21x101.InsertPoint(11, 6.145, 9.88, 0.0);
        W21x101.InsertPoint(12, -0.44, 10.68, 0.0);
        W21x101.InsertPoint(13, -0.44, -10.68, 0.0);
        W21x101.InsertPoint(14, 0.44, 10.68, 0.0);
        W21x101.InsertPoint(15, 0.44, -10.68, 0.0);

        /*
         * alternative rectangles for rotation extrusion filter parallel to z
         * axis do not show artifacts in shape and end planes.
         */
        W21x101.InsertPoint(16, 0, 10.68, 6.145);
        W21x101.InsertPoint(17, 0, 10.68, -6.145);
        W21x101.InsertPoint(18, 0, 9.88, -6.145);
        W21x101.InsertPoint(19, 0, 9.88, -0.44);
        W21x101.InsertPoint(20, 0, -9.88, -0.44);
        W21x101.InsertPoint(21, 0, -9.88, -6.145);
        W21x101.InsertPoint(22, 0, -10.68, -6.145);
        W21x101.InsertPoint(23, 0, -10.68, 6.145);
        W21x101.InsertPoint(24, 0, -9.88, 6.145);
        W21x101.InsertPoint(25, 0, -9.88, 0.44);
        W21x101.InsertPoint(26, 0, 9.88, 0.44);
        W21x101.InsertPoint(27, 0, 9.88, 6.145);
        W21x101.InsertPoint(28, 0, 10.68, -0.44);
        W21x101.InsertPoint(29, 0, -10.68, -0.44);
        W21x101.InsertPoint(30, 0, 10.68, 0.44);
        W21x101.InsertPoint(31, 0, -10.68, 0.44);

        /*
         * The following two rotArray cells define equal size rectangles the
         * first uses 8 points, the second uses 4 points. Even though these two
         * profiles look visually identical at the start end when the rotational
         * extrusion filter has a non zero value for DeltaRadius the rectangle
         * defined with 8 points becomes a truncated wedge shape and shows an
         * endplane capping artifact as well. The 4 point rectangle remains
         * rectangular. I did not expect adding more points on the polygon to
         * change to shape extruded.
         */
        rotArray.InsertNextCell(9);
        rotArray.InsertCellPoint(0);
        rotArray.InsertCellPoint(14);
        rotArray.InsertCellPoint(12);
        rotArray.InsertCellPoint(1);
        rotArray.InsertCellPoint(2);
        rotArray.InsertCellPoint(3);
        rotArray.InsertCellPoint(10);
        rotArray.InsertCellPoint(11);
        rotArray.InsertCellPoint(0);

        rotArray.InsertNextCell(5);
        rotArray.InsertCellPoint(5);
        rotArray.InsertCellPoint(6);
        rotArray.InsertCellPoint(7);
        rotArray.InsertCellPoint(8);
        rotArray.InsertCellPoint(5);
        /*
         * alternative rotArray profile that does not have artifacts in
         * extrusion:
         * 
         * rotArray.InsertNextCell(9); rotArray.InsertCellPoint( 16);
         * rotArray.InsertCellPoint(30); rotArray.InsertCellPoint(28);
         * rotArray.InsertCellPoint( 17); rotArray.InsertCellPoint( 18);
         * rotArray.InsertCellPoint(19); rotArray.InsertCellPoint(26);
         * rotArray.InsertCellPoint( 27); rotArray.InsertCellPoint( 16);
         * 
         * rotArray.InsertNextCell(5); rotArray.InsertCellPoint( 21);
         * rotArray.InsertCellPoint( 22); rotArray.InsertCellPoint( 23);
         * rotArray.InsertCellPoint( 24); rotArray.InsertCellPoint( 21);
         */

        /*
         * The following definition of the I beam does not create end plane
         * artifacts when CappingOn() is set by the extrusion filter.
         */

        lineArray.InsertNextCell(5);
        lineArray.InsertCellPoint(0);
        lineArray.InsertCellPoint(1);
        lineArray.InsertCellPoint(2);
        lineArray.InsertCellPoint(11);
        lineArray.InsertCellPoint(0);

        lineArray.InsertNextCell(5);
        lineArray.InsertCellPoint(3);
        lineArray.InsertCellPoint(4);
        lineArray.InsertCellPoint(9);
        lineArray.InsertCellPoint(10);
        lineArray.InsertCellPoint(3);

        lineArray.InsertNextCell(5);
        lineArray.InsertCellPoint(5);
        lineArray.InsertCellPoint(6);
        lineArray.InsertCellPoint(7);
        lineArray.InsertCellPoint(8);
        lineArray.InsertCellPoint(5);

        /*
         * The following polygon profile creates endplane artifacts when
         * CappingOn() is set for the extrusion filter.
         */

        /*
         * lineArray.InsertNextCell(13); //set the size of data to hold
         * 
         * lineArray.InsertCellPoint( 0); lineArray.InsertCellPoint( 1);
         * lineArray.InsertCellPoint( 2); lineArray.InsertCellPoint( 3);
         * lineArray.InsertCellPoint( 4); lineArray.InsertCellPoint( 5);
         * lineArray.InsertCellPoint( 6); lineArray.InsertCellPoint( 7);
         * lineArray.InsertCellPoint( 8); lineArray.InsertCellPoint( 9);
         * lineArray.InsertCellPoint(10); lineArray.InsertCellPoint(11);
         * lineArray.InsertCellPoint( 0);
         */

        // create the holding structure for the lineProfile
        vtkPolyData LineProfile = new vtkPolyData();
        LineProfile.SetPoints(W21x101);
        LineProfile.SetPolys(lineArray);

        // create the holding structure for the rotProfile
        vtkPolyData rotProfile = new vtkPolyData();
        rotProfile.SetPoints(W21x101);
        rotProfile.SetPolys(rotArray);

        // create the rotational extrusion mechanism to extrude the rotProfile

        vtkRotationalExtrusionFilter rotExtrusion = new vtkRotationalExtrusionFilter();
        rotExtrusion.SetResolution(120);
        rotExtrusion.SetTranslation(120);
        rotExtrusion.SetDeltaRadius(20.0);
        rotExtrusion.SetAngle(720.0);

        rotExtrusion.CappingOn();
        rotExtrusion.SetInputData(rotProfile);

        vtkPolyDataMapper rotMapper = new vtkPolyDataMapper();
        rotMapper.SetInputData(rotExtrusion.GetOutput());

        // Create an actor to represent the line. The actor orchestrates
        // rendering of
        // the mapper's graphics primitives. An actor also refers to properties
        // via a
        // vtkProperty instance, and includes an internal transformation matrix.
        // We
        // set this actor's mapper to be lineMapper which we created above.
        vtkActor rotActor = new vtkActor();
        rotActor.SetMapper(rotMapper);

        // create the linear extrusion mechanism to extrude the profile
        vtkLinearExtrusionFilter Extrusion = new vtkLinearExtrusionFilter();

        Extrusion.SetExtrusionTypeToVectorExtrusion();
        Extrusion.SetVector(0.0, 0.0, 120.0);
        Extrusion.CappingOn();
        Extrusion.SetInputData(LineProfile);

        vtkPolyDataMapper lineMapper = new vtkPolyDataMapper();
        lineMapper.SetInputData(Extrusion.GetOutput());

        // Create an actor to represent the line. The actor orchestrates
        // rendering of
        // the mapper's graphics primitives. An actor also refers to properties
        // via a
        // vtkProperty instance, and includes an internal transformation matrix.
        // We
        // set this actor's mapper to be lineMapper which we created above.
        vtkActor lineActor = new vtkActor();
        lineActor.SetMapper(lineMapper);

        // Create the Renderer and assign actors to it. A renderer is like a
        // viewport. It is part or all of a window on the screen and it is
        // responsible for drawing the actors it has. We also set the
        // background color here.
        vtkRenderer ren1 = new vtkRenderer();
        ren1.AddActor(lineActor);
        ren1.AddActor(rotActor);
        ren1.SetBackground(0.1, 0.2, 0.4);

        // Finally we create the render window which will show up on the screen
        // We put our renderer into the render window using AddRenderer. We
        // also set the size to be 300 pixels by 300.
        vtkRenderWindow renWin = new vtkRenderWindow();
        renWin.AddRenderer(ren1);
        renWin.SetSize(600, 600);

        // The vtkRenderWindowInteractor class watches for events (e.g.,
        // keypress,
        // mouse) in the vtkRenderWindow. These events are translated into
        // event invocations that VTK understands (see VTK/Common/vtkCommand.h
        // for all events that VTK processes). Then observers of these VTK
        // events can process them as appropriate.
        iren = new vtkRenderWindowInteractor();
        iren.SetRenderWindow(renWin);

        // By default the vtkRenderWindowInteractor instantiates an instance
        // of vtkInteractorStyle. vtkInteractorStyle translates a set of events
        // it observes into operations on the camera, actors, and/or properties
        // in the vtkRenderWindow associated with the vtkRenderWinodwInteractor.
        // Here we specify a particular interactor style.
        curIStyle = 'C';
        iren.SetInteractorStyle(cstyle);

        // add observer for key event:
        iren.AddObserver("CharEvent", this, "toggleStyle");
        // Start the event loop.
        iren.Initialize();
        iren.Start();

    }
}
