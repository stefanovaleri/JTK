package jtk.examples;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.Arrays;
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
	    if (args.length == 0) {
	        SwingUtilities.invokeLater(new Runnable() {
	            @Override
	            public void run() {
	                new ExamplesMain();
	            }
	        });
	    } else {
	        for (int i = 0; i < args.length; i++) {
	            final String arg = args[i];

	            if (arg.charAt(0) == '-') {
	                switch (arg) {
	                    case "-list":
	                        System.out.println("Available examples:");
	                        System.out.println("    1. Extrude Test");
	                        System.out.println("    2. Morph - Octahedron");
	                        System.out.println("    3. Morph - Plane");
	                        System.out.println("    4. Lattice");
	                        System.out.println("    5. Logo");
	                        System.out.println("    6. OpenFOAM Reader");
    	                    break;
	                    case "-example":
	                        if (i == args.length - 1) {
	                            System.err.println("FATAL ERROR : Missing example number. Use -list");
	                            System.exit(-1);
	                        }

	                        String n = args[++i];
                        String[] newArgs = Arrays.copyOfRange(args, i+1, args.length);
                        switch (n) {
	                        
                                case "1": new ExtrudeTest().demo(newArgs); break;
                                case "2": new MorphExampleSphereOctahedron().demo(newArgs); break;
                                case "3": new vtkMorphExamplePlane().demo(newArgs); break;
                                case "4": new MorphExampleSphereLattice().demo(newArgs); break;
                                case "5": new LogoExample().demo(newArgs); break;
                                case "6": new OpenFOAMReaderExample().demo(newArgs); break;

                            default:
                                break;
                            }
	                        break;
	                    default: break;
	                }
	            }
	        }
	    }
	}

	public ExamplesMain() {
		JPanel panel = new JPanel(new GridLayout(4, 2));
		panel.add(new JButton(new AbstractAction("Extrude Test (dev)") {
			@Override
			public void actionPerformed(ActionEvent e) {
				new ExtrudeTest().demo(null);
			}
		}));
		panel.add(new JButton(new AbstractAction("Morph Example - Octahedron") {
			@Override
			public void actionPerformed(ActionEvent e) {
				new MorphExampleSphereOctahedron().demo(null);
			}
		}));
		panel.add(new JButton(new AbstractAction("Morph Example - Plane") {
			@Override
			public void actionPerformed(ActionEvent e) {
				new vtkMorphExamplePlane().demo(null);
			}
		}));
		panel.add(new JButton(new AbstractAction("Morph Example - Lattice 1") {
			@Override
			public void actionPerformed(ActionEvent e) {
				new MorphExampleSphereLattice().demo(null);
			}
		}));
		panel.add(new JButton(new AbstractAction("Logo Example") {
			@Override
			public void actionPerformed(ActionEvent e) {
				new LogoExample().demo(null);
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
