import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.SMO;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.instance.imagefilter.PHOGFilter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.util.Random;

public class MainPanel extends JPanel {

    public static JTextArea txtrContent = new JTextArea();
    static Instances dataSetFinal = null;
    private static JTextField txtEnterClass;


    public MainPanel() {
        setBackground(Color.WHITE);
        setLayout(null);
        txtrContent.setBounds(12, 28, 636, 290);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(0, 0, 648, 330);
        scrollPane.setViewportView(txtrContent);
        add(scrollPane);
        txtrContent.setEditable(false);

        JButton btnPhoto = new JButton("PROCESS CHARACTER IMAGE");
        btnPhoto.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                int result = fileChooser.showOpenDialog(btnPhoto);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    System.out.println("Selected file: " + selectedFile.getAbsolutePath());
                    try {
                        classifySingleInstance(selectedFile.getAbsolutePath());
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        btnPhoto.setBounds(241, 395, 320, 43);
        add(btnPhoto);
        
        txtEnterClass = new JTextField();
        txtEnterClass.setToolTipText("Enter Class");
        txtEnterClass.setBounds(12, 395, 166, 43);
        add(txtEnterClass);
        txtEnterClass.setColumns(10);
        
        JLabel lblClassAtribute = new JLabel("Class Atribute");
        lblClassAtribute.setFont(new Font("DejaVu Sans Mono", Font.PLAIN, 16));
        lblClassAtribute.setBounds(12, 355, 218, 28);
        add(lblClassAtribute);
        
        JButton btnNewButton = new JButton("Process from local dataset");
        btnNewButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		try {
        			processLocalDataset();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        });
        btnNewButton.setBounds(241, 451, 320, 49);
        add(btnNewButton);
        
        JButton btnNewButton_1 = new JButton("Procesar con 10-fold");
        btnNewButton_1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		try {
					processDataset();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        	}
        });
        btnNewButton_1.setBounds(241, 512, 320, 43);
        add(btnNewButton_1);

    }


    public static void classifySingleInstance(String path) throws Exception {
        File a = new File(path);
        a.renameTo(new File("/home/eduardo/Documents/weka-java-google/train-data/test-data/" + a.getName()));
        //a.delete();

        String filename = "train-data/test.arff";
        FileWriter fwriter = new FileWriter(filename, true); //true will append the new instance
        fwriter.write("test-data/"+a.getName() + ", "+txtEnterClass.getText().toUpperCase()+"\n");//appends the string to the file
        fwriter.close();

        Instances data = getFilteredDataSet("train-data/letras.arff");
        Instances testData = getFilteredDataSet("train-data/test.arff");
        
        SMO svm = new SMO();
		svm.buildClassifier(data);

        Evaluation eval = new Evaluation(data);
        eval.evaluateModel(svm, testData);

        //eval.crossValidateModel(nb, data, 10, new Random(1));

        txtrContent.append(eval.toMatrixString("Matriz de confucion"));
        System.out.println(eval.toMatrixString("Matriz de confucion"));
        txtrContent.append(eval.toClassDetailsString("Details"));
        System.out.println(eval.toClassDetailsString("Details"));
        txtrContent.append(eval.toSummaryString());
        System.out.println(eval.toSummaryString());

    }
    
    
    public static void processLocalDataset() throws Exception {

        Instances data = getFilteredDataSet("train-data/letras.arff");
        Instances testData = getFilteredDataSet("train-data/test.arff");

        
        SMO svm = new SMO();
		svm.buildClassifier(data);

        Evaluation eval = new Evaluation(data);
        eval.evaluateModel(svm, testData);

        //eval.crossValidateModel(nb, data, 10, new Random(1));

        txtrContent.append(eval.toMatrixString("Matriz de confucion"));
        System.out.println(eval.toMatrixString("Matriz de confucion"));
        txtrContent.append(eval.toClassDetailsString("Details"));
        System.out.println(eval.toClassDetailsString("Details"));
        txtrContent.append(eval.toSummaryString());
        System.out.println(eval.toSummaryString());

    }
    
    
    
    public static void processDataset() throws Exception {

        Instances data = getFilteredDataSet("train-data/letras.arff");
        Evaluation eval = new Evaluation(data);


        /*NaiveBayes nb = new NaiveBayes();
        nb.buildClassifier(data);
        

        eval.crossValidateModel(nb, data, 10, new Random(1));
        

        txtrContent.append(eval.toMatrixString("Matriz de confucion"));
        System.out.println(eval.toMatrixString("Matriz de confucion"));
        txtrContent.append(eval.toClassDetailsString("Details"));
        System.out.println(eval.toClassDetailsString("Details"));
        txtrContent.append(eval.toSummaryString());
        System.out.println(eval.toSummaryString());*/
        
        
        
		SMO svm = new SMO();
		svm.buildClassifier(data);
        eval.crossValidateModel(svm, data, 10, new Random(1));
        
        
        txtrContent.append(eval.toMatrixString("Matriz de confucion"));
        System.out.println(eval.toMatrixString("Matriz de confucion"));
        txtrContent.append(eval.toClassDetailsString("Details"));
        System.out.println(eval.toClassDetailsString("Details"));
        txtrContent.append(eval.toSummaryString());
        System.out.println(eval.toSummaryString());
        

    }


    public static Instances getFilteredDataSet(String path) throws Exception {

        ConverterUtils.DataSource source = new ConverterUtils.DataSource(path);
        Instances data = source.getDataSet();
        if (data.classIndex() == -1)
            data.setClassIndex(data.numAttributes() - 1);
        String[] options = new String[2];
        options[0] = "-D";
        options[1] = "train-data";
        PHOGFilter filter = new PHOGFilter();
        filter.setInputFormat(data);
        filter.setOptions(options);

        Instances trainingSubset = Filter.useFilter(data, filter);

        String[] rem_options = new String[2];
        rem_options[0] = "-R";
        rem_options[1] = "1";
        Remove remove = new Remove();
        remove.setOptions(rem_options);
        remove.setInputFormat(trainingSubset);
        return Filter.useFilter(trainingSubset, remove);


    }
}























