// Imports the Google Cloud client library

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.instance.imagefilter.ColorLayoutFilter;
import weka.filters.unsupervised.instance.imagefilter.PHOGFilter;
import java.util.Random;


public class QuickstartSample {
    public static void main (String[] args) throws Exception {

        ConverterUtils.DataSource source = new ConverterUtils.DataSource("train-data/letras.arff");
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
        Instances newData2 = Filter.useFilter(trainingSubset, remove);   // apply filter


        NaiveBayes nb = new NaiveBayes();
        nb.buildClassifier(newData2);
        System.out.println(nb.getCapabilities().toString());

        Evaluation eval = new Evaluation(newData2);
        eval.crossValidateModel(nb, newData2, 10, new Random(1));
        System.out.println(eval.toMatrixString("Matriz de confucion"));
        System.out.println(eval.toClassDetailsString("Details"));
        System.out.println(eval.toSummaryString());






    }
}