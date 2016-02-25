package controllers;

import models.CandidateClassManager;
import models.SaveWrapper;
import models.javacandidatestruct.CandidateClass;
import models.javacandidatestruct.JavaAttribute;
import models.javacandidatestruct.JavaMethod;
import views.CandidateClassFrame;
import views.LanguageManager;
import views.ViewsManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Nicolas Burroni
 * @since 7/26/2014
 */
public class VisualizerController {

	private CandidateClassManager modelManager;
	private ViewsManager viewManager;
	private String initialChooserPath = "C:\\";

	public VisualizerController(){
		modelManager = new CandidateClassManager();
		viewManager = makeViewsManager();
		//que las funciones muestren el body en nueva ventana cuando se las clickea
		//TODO tambien cargar los archivos que linkea el parser
		//TODO toolbar para mostrar ventana nueva, volver al inicio, etc...
		//TODO remane, delete, comments, ccd ranking (boton derecho, submenu color ranking y que cambie el color del frame)
	}

    private ViewsManager makeViewsManager() {
        return new ViewsManager(this::methodPressed, this::rankingChosen,this::browseAndVisualize,this::Load,this::Save);
    }

    public void runVisualizer(){
		viewManager.showStartupDialog(e -> browseAndVisualizeLegacyFile());
	}

	public void browseAndVisualize(ActionEvent event){
		//LoadSave();

		//Save();
		viewManager = makeViewsManager();
		//viewManager.addCandidateClass();
		modelManager = new CandidateClassManager();
		browseAndVisualizeLegacyFile();
        viewManager.showVisualizer();
	}

    public void Save(ActionEvent event){
        String name = JOptionPane.showInputDialog(null,LanguageManager.getString("Nameforsavedfile"));
        if (name != null && !Objects.equals(name, "")){
            Save(name);
        }
    }

	private void Save(String nombre) {
		try
		{
			String path = Paths.get(".").toAbsolutePath().normalize().toString() + "\\" + nombre + ".parser";
			FileOutputStream fileOut =
					new FileOutputStream(path);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(new SaveWrapper(modelManager.getCandidateClassList(),modelManager.getAssociatedFiles()));
			out.close();
			fileOut.close();
			System.out.printf("Serialized data is saved in " + path );
		}catch(IOException i)
		{
			i.printStackTrace();
		}

	}

    public void Load(ActionEvent event){

            JFileChooser chooser = new JFileChooser();
            JFrame chooserFrame = new JFrame();
            chooserFrame.setSize(300, 300);
            chooserFrame.setLocationRelativeTo(null);
            int choice = chooser.showOpenDialog(chooserFrame);
            String returnVal;
            if(choice == JFileChooser.ERROR_OPTION){
                returnVal = null;
            } else if (choice == JFileChooser.CANCEL_OPTION){
                returnVal = "";
            } else {
                returnVal = chooser.getSelectedFile().getAbsolutePath();
            }
            if (returnVal != null && !Objects.equals(returnVal, "")){
                LoadSave(returnVal);
            }
    }

	private void LoadSave(String path) {

		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		ObjectInputStream os = null;
		try {
			os = new ObjectInputStream(fileInputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] encoded;
		try {
			SaveWrapper save = (SaveWrapper) os.readObject();
			viewManager = makeViewsManager();
			modelManager = new CandidateClassManager();
            for (File file : save.getFiles()) {
				try {
					encoded = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
				} catch (NoSuchFileException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Se buscara path relativo");

						String[] split = file.getPath().split("\\\\");
						String fileName = split[split.length-1];
						String fullPath = save.getFiles().get(0).getParent() + "/" + fileName;
						encoded = Files.readAllBytes(Paths.get(fullPath));


				}

				String name = file.getName().contains("/") || file.getName().contains("\\") ? file.getName().split("/|\\\\")[file.getName().split("/|\\\\").length-1] : file.getName();
                viewManager.addLegacyCode(name, new String(encoded));
            }
            for (CandidateClass ccd : save.getCcds()) {
				List<JavaAttribute> ccdAttributes = ccd.getAttributes();
				List<JavaMethod> ccdMethods = ccd.getMethods();
				List<String> attributes = new ArrayList<>();
				List<String> methods = new ArrayList<>();
				List<String> methodBodies = new ArrayList<>();
				for (JavaAttribute attribute : ccdAttributes) {
					attributes.add(attribute.toString());
				}
				for (JavaMethod method : ccdMethods) {
					methods.add(method.getUMLString());
					methodBodies.add(method.getBody());
				}
				viewManager.addCandidateClass(ccd.getName(), attributes, methods, methodBodies);
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        viewManager.showVisualizer();
	}

	public void browseAndVisualizeLegacyFile(){
		String result = viewManager.browseLegacyFile(initialChooserPath);
		initialChooserPath = new File(result).getParent();
		if(result != null){
			modelManager.generateClasses(result);
			showCCDs();
//			showFile(result);
			showFiles();
		}
		viewManager.showVisualizer();
	}

	/*public void addLegacyFile(){
		String result = viewManager.showFileChooser();
		if(result != null){
			modelManager.generateClasses(result);
			showCCDs();
			showFile(result);
		}
	}*/

	public void showCCDs(){
		List<CandidateClass> ccds = modelManager.getCandidateClasses();
		for (CandidateClass ccd : ccds) {
			List<JavaAttribute> ccdAttributes = ccd.getAttributes();
			List<JavaMethod> ccdMethods = ccd.getMethods();
			List<String> attributes = new ArrayList<>();
			List<String> methods = new ArrayList<>();
			List<String> methodBodies = new ArrayList<>();
			for (JavaAttribute attribute : ccdAttributes) {
				attributes.add(attribute.toString());
			}
			for (JavaMethod method : ccdMethods) {
				methods.add(method.getUMLString());
				methodBodies.add(method.getBody());
			}
			viewManager.addCandidateClass(ccd.getName(), attributes, methods, methodBodies);
		}
	}

	public void showFiles(){
		List<File> files = modelManager.getAssociatedFiles();
//TODO		String relativePath = files.get(0).getPath();   buscar archivos del mismo nombre en la misma carpeta
		for (File file : files) {
			showFile(file);
		}
	}

	public void showFile(String path){
		showFile(new File(path));
	}

	public void showFile(File toShow){
		String text = "";
		try {
			BufferedReader reader = new BufferedReader(new FileReader(toShow));
			String line;
			do {
				line = reader.readLine();
				text = text.concat(line + "\n");
			} while(line != null);
		} catch (java.io.IOException ignored) {}

		String name = toShow.getName().contains("/") || toShow.getName().contains("\\") ? toShow.getName().split("/|\\\\")[toShow.getName().split("/|\\\\").length-1] : toShow.getName();
		viewManager.addLegacyCode(name, text);
	}

	public void methodPressed(ActionEvent event){
		((CandidateClassFrame.MethodButton) event.getSource()).showMethodBodyFrame();
	}

	public void rankingChosen(ActionEvent event){
		JMenuItem item = (JMenuItem) event.getSource();
		int rank = Integer.parseInt(item.getText());

	}
}
