import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.JTextPane;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.border.LineBorder;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.instance.imagefilter.PHOGFilter;

import java.awt.event.ActionListener;
import java.io.File;
import java.util.Random;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

public class MainPanel extends JPanel{
	
	public static JTextArea txtrContent = new JTextArea();
	static Instances dataSetFinal = null;


	public MainPanel() {
		setBackground(Color.WHITE);
		setLayout(null);
		txtrContent.setBounds(12, 28, 636, 290);

		
		JButton btnTraindata = new JButton("TrainData");
		btnTraindata.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					trainModel();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnTraindata.setBounds(236, 379, 117, 25);
		add(btnTraindata);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 648, 330);
		scrollPane.setViewportView(txtrContent);
		add(scrollPane);
		txtrContent.setEditable(false);
		
		JButton btnPhoto = new JButton("Photo");
		btnPhoto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				int result = fileChooser.showOpenDialog(btnPhoto);
				if (result == JFileChooser.APPROVE_OPTION) {
				    File selectedFile = fileChooser.getSelectedFile();
				    System.out.println("Selected file: " + selectedFile.getAbsolutePath());
				}
			}
		});
		btnPhoto.setBounds(236, 546, 117, 25);
		add(btnPhoto);
		

	}
	
	
	public static void trainModel() throws Exception{
		
        ConverterUtils.DataSource source = new ConverterUtils.DataSource("train-data/menos_letras.arff");
        Instances data = source.getDataSet();

        if (data.classIndex() == -1)
            data.setClassIndex(data.numAttributes() - 1);


        String[] options = new String[2];
        options[0] = "-D"; options[1] = "train-data";
        PHOGFilter filter = new PHOGFilter();
        //ColorLayoutFilter filter = new ColorLayoutFilter();
        filter.setInputFormat(data);
        filter.setOptions(options);

        Instances trainingSubset = Filter.useFilter(data, filter);

        String[] rem_options = new String[2];
        rem_options[0] = "-R";                                    // "range"
        rem_options[1] = "1";                                     // first attribute
        Remove remove = new Remove();                         // new instance of filter
        remove.setOptions(rem_options);                           // set options
        remove.setInputFormat(trainingSubset);                          // inform filter about dataset **AFTER** setting options
        dataSetFinal = Filter.useFilter(trainingSubset, remove);   // apply filter


        NaiveBayes nb = new NaiveBayes();
        nb.buildClassifier(dataSetFinal);
        txtrContent.append(nb.getCapabilities().toString());
        System.out.println(nb.getCapabilities().toString());

        Evaluation eval = new Evaluation(dataSetFinal);
        eval.crossValidateModel(nb, dataSetFinal, 10, new Random(1));
        
        
        txtrContent.append(eval.toMatrixString("Matriz de confucion"));
        System.out.println(eval.toMatrixString("Matriz de confucion"));
        txtrContent.append(eval.toClassDetailsString("Details"));
        System.out.println(eval.toClassDetailsString("Details"));
        txtrContent.append(eval.toSummaryString());
        System.out.println(eval.toSummaryString());
		
	}
	
	public static void classifySingleInstance(String path) throws Exception{
		

		 Attribute filename = new Attribute("filename");
         Attribute the_class = new Attribute("class");


         // Create instances for each pollutant with attribute values latitude, longitude and pollutant itself
         DenseInstance inst_co = new DenseInstance(4);

         // Set instance's values for the attributes "latitude", "longitude", and "pollutant concentration"
         inst_co.setValue(filename, path);
         inst_co.setMissing(the_class);
         inst_co.setDataset(dataSetFinal);
         
         
         NaiveBayes nb = new NaiveBayes();
         nb.buildClassifier(dataSetFinal);
         txtrContent.append(nb.getCapabilities().toString());
         System.out.println(nb.getCapabilities().toString());

         Evaluation eval = new Evaluation(dataSetFinal);
         eval.crossValidateModel(nb, dataSetFinal, 10, new Random(1));
         
         
         txtrContent.append(eval.toMatrixString("Matriz de confucion"));
         System.out.println(eval.toMatrixString("Matriz de confucion"));
         txtrContent.append(eval.toClassDetailsString("Details"));
         System.out.println(eval.toClassDetailsString("Details"));
         txtrContent.append(eval.toSummaryString());
         System.out.println(eval.toSummaryString());	
		
	}
	
	
}























