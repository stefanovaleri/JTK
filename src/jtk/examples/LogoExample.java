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
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import jtk.core.VTKRenderPanel;
import vtk.vtkCoordinate;
import vtk.vtkImageData;
import vtk.vtkLogoRepresentation;
import vtk.vtkLogoWidget;
import vtk.vtkPNGReader;

public class LogoExample implements JTKDemo {

	private vtkLogoWidget logoWidget;
	private VTKRenderPanel vtkRendererPanel;
	private vtkLogoRepresentation logoRepresentation;
	private vtkImageData imageData;
//	private AxesWidget axesWidget;

	public void demo(String[] args) {
		this.imageData = createImageData();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				vtkRendererPanel = new VTKRenderPanel();
				createRepresentation();
				createWidget();
				logoWidget.On();
				//axesWidget = new AxesWidget(vtkRendererPanel);
				
				final JFrame frame = new JFrame("Logo Example");
				frame.addComponentListener(new ComponentListener() {
					
					@Override
					public void componentShown(ComponentEvent e) {
						placeRepresentationDisplay(frame.getSize());
					}
					
					@Override
					public void componentResized(ComponentEvent e) {
						placeRepresentationDisplay(frame.getSize());
					}
					
					@Override
					public void componentMoved(ComponentEvent e) {
						//sample.placeRepresentation();
					}
					
					@Override
					public void componentHidden(ComponentEvent e) {
						//sample.placeRepresentation();
					}
				});
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.getContentPane().setLayout(new BorderLayout());
				frame.setSize(400, 400);
				frame.getContentPane().add(vtkRendererPanel, BorderLayout.CENTER);
				frame.setVisible(true);
			}
		});
	}

	private void createWidget() {
		logoWidget = new vtkLogoWidget();
		logoWidget.SetInteractor(vtkRendererPanel.getRenderWindowInteractor());
		logoWidget.SetRepresentation(logoRepresentation);
		logoWidget.ResizableOff();
		logoWidget.SelectableOff();
		logoWidget.ProcessEventsOff();
	}

	private void createRepresentation() {
		logoRepresentation = new vtkLogoRepresentation();
		logoRepresentation.SetImage(imageData);
		
		logoRepresentation.DragableOff();
		logoRepresentation.PickableOff();
		logoRepresentation.SetShowBorderToOff();
		logoRepresentation.ProportionalResizeOff();
		logoRepresentation.GetImageProperty().SetDisplayLocationToBackground();
		logoRepresentation.GetImageProperty().SetOpacity(1);
		logoRepresentation.GetBorderProperty().SetOpacity(0);
		logoRepresentation.VisibilityOn();
	}

	private void placeRepresentation() {
		System.out.println("VtkTOUSESample.placeRepresentation() "+Arrays.toString(imageData.GetBounds()));
		double imageWidth = imageData.GetBounds()[1];
		double imageHeight = imageData.GetBounds()[3];

		vtkCoordinate position2 = logoRepresentation.GetPosition2Coordinate();
		position2.SetCoordinateSystemToDisplay();
		
		vtkCoordinate position1 = logoRepresentation.GetPositionCoordinate();
		position1.SetCoordinateSystemToNormalizedViewport();
		position1.SetValue(1, 0);
		
		double x = position1.GetComputedDoubleDisplayValue(vtkRendererPanel.GetRenderer())[0];
		double y = position1.GetComputedDoubleDisplayValue(vtkRendererPanel.GetRenderer())[1];
		
		System.out.println("VtkTOUSESample.placeRepresentation(): " + x);
		
		position1.SetCoordinateSystemToDisplay();
		position1.SetValue(x - imageWidth, y);

		logoRepresentation.SetPosition2(x + imageWidth, y + imageHeight);
	}

	private void placeRepresentationDisplay_NoResize() {
		logoRepresentation.GetPositionCoordinate().SetCoordinateSystemToNormalizedViewport();
		logoRepresentation.GetPosition2Coordinate().SetCoordinateSystemToDisplay();
		logoRepresentation.SetPosition(0, 0);

		logoRepresentation.GetPositionCoordinate().SetCoordinateSystemToDisplay();

		double imageWidth = imageData.GetBounds()[1];
		double imageHeight = imageData.GetBounds()[3];
		logoRepresentation.SetPosition2(logoRepresentation.GetPosition()[0] + imageWidth, logoRepresentation.GetPosition()[1] + imageHeight);
	}

	private void placeRepresentationDisplay(Dimension size) {
		System.out.println("VtkTOUSESample.placeRepresentation() "+Arrays.toString(imageData.GetBounds()));
		logoRepresentation.GetPositionCoordinate().SetCoordinateSystemToDisplay();
		logoRepresentation.GetPosition2Coordinate().SetCoordinateSystemToDisplay();
		double imageWidth = imageData.GetBounds()[1];
		double imageHeight = imageData.GetBounds()[3];
//		double bottomLeftCornerX = 10;
//		double bottomLeftCornerY = 10;
//		double topRightCornerX = bottomLeftCornerX + imageWidth;
//		double topRightCornerY = bottomLeftCornerY + imageHeight;
		double bottomLeftCornerX = size.width - 150;
		double bottomLeftCornerY = 4;
		double topRightCornerX = 136;
		double topRightCornerY = 27;
		
		logoRepresentation.SetPosition(bottomLeftCornerX, bottomLeftCornerY);
		logoRepresentation.SetPosition2(topRightCornerX, topRightCornerY);
		
		vtkRendererPanel.lock();
		vtkRendererPanel.getRenderWindowInteractor().LeftButtonPressEvent();
		vtkRendererPanel.getRenderWindowInteractor().LeftButtonReleaseEvent();
		vtkRendererPanel.unlock();
	}

	private vtkImageData createImageData() {
		vtkPNGReader reader = new vtkPNGReader();
		reader.SetFileName("/home/davide/workspace/ofgui/dist/HELYX/img/ENGYS.png");
		reader.Update();
		return reader.GetOutput();
	}
}
