package jtk.examples;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import vtk.vtkNativeLibrary;

public class ExamplesMain {

	private static final Logger logger = Logger.getLogger(ExamplesMain.class.getName());
	
	static {
		if (!vtkNativeLibrary.LoadAllNativeLibraries()) {
			for (vtkNativeLibrary lib : vtkNativeLibrary.values()) {
				String libName = lib.GetLibraryName();
				if (lib.IsLoaded()) {
					logger.info(libName + " loaded");
				} else {
					logger.warning(libName + " NOT loaded");
				}
			}
		}
		vtkNativeLibrary.DisableOutputWindow(null);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new ExamplesMain();
			}
		});
	}

	public ExamplesMain() {
		JPanel panel = new JPanel(new GridLayout(4, 2));
		panel.add(new JButton(new AbstractAction("Extrude Test (dev)") {
			@Override
			public void actionPerformed(ActionEvent e) {
				new ExtrudeTest().demo();
			}
		}));
		panel.add(new JButton(new AbstractAction("Morph Example - Octahedron") {
			@Override
			public void actionPerformed(ActionEvent e) {
				new MorphExampleSphereOctahedron().demo();
			}
		}));
		panel.add(new JButton(new AbstractAction("Morph Example - Plane") {
			@Override
			public void actionPerformed(ActionEvent e) {
				new vtkMorphExamplePlane().demo();
			}
		}));
		panel.add(new JButton(new AbstractAction("Morph Example - Lattice 1") {
			@Override
			public void actionPerformed(ActionEvent e) {
				new MorphExampleSphereLattice().demo();
			}
		}));
		panel.add(new JButton(new AbstractAction("Logo Example") {
			@Override
			public void actionPerformed(ActionEvent e) {
				new LogoExample().demo();
			}
		}));

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(panel);
		frame.setSize(800, 600);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
