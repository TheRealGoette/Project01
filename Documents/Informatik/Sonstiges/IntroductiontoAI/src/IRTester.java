
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.text.DecimalFormat;

/**
 * to compute the Cosine Similarity between ".txt"-Documents in a file
 * @author marcogoette
 *
 */
public class IRTester {
    
    /**
     * The path to a directory containing several documents to be processed.
     */
    private final String _sourceDir;
    private String pathStopwords = "stopwords.txt"; //stopwords path
    private static boolean debug = false;		//to process the debug command
    
    private ArrayList<DocumentModel>       _documentModels;
    private ArrayList<DocumentVectorModel> _documentVectorModels;
    private Dictionary                     _dictionary;
    private InvertedIndex                  _invertedIndex;
    private ArrayList<String>              docNames;			//list of document names as listed in the file
    private int                            numberOfDocuments;	//number of documents in the file
    private Object[][] 					   matrix;				//necessary for printing the calculations at the end
    private static DecimalFormat df = new DecimalFormat("0.000");	//to print the results in the right format
    
    /**
     * Constructs an IRTester.
     * 
     * @param sourceDir - the directory that contains text files to be processed
     */
    public IRTester(String sourceDir) {
        _sourceDir = sourceDir;
        
        _documentModels = new ArrayList<>();
        _documentVectorModels = new ArrayList<>();
        _dictionary = new Dictionary();
        docNames = new ArrayList<>();
        
    }
    
    /**
     * Runs the IR tester program by using different methods
     */
    public void run()
    {
    	System.out.println(pathStopwords);
        // get list of file names
    	System.out.println("Getting list of file names");
        ArrayList<String> filenames = getTextFileList ();
        
        // read documents and create a list of documents
        System.out.println("---------------------------------------------------------------------------");
    	System.out.println("Reading documents and create a list of documents");
        readDocuments (filenames);
        
        // process documents to transform text data into a useful form
        System.out.println("---------------------------------------------------------------------------");
    	System.out.println("Building dictionary");
    	buildDictionary();
    	
    	// compute Document Vector Models
        System.out.println("---------------------------------------------------------------------------");
    	System.out.println("Computing Document Vector Models");
    	buildDocumentVectorModels();

        
        // build document-document matrix
        System.out.println("---------------------------------------------------------------------------");
    	System.out.println("Calculating Matrix...");
    	matrix = computeDocDocMatrix(_documentVectorModels);
        
        // print document-document matrix
        System.out.println("---------------------------------------------------------------------------");
    	System.out.println("Printing Matrix...");
    	printMatrix(matrix);
    	
    	// print _invertedIndex if debug = true
    	if (debug == true) {
    		 System.out.println("---------------------------------------------------------------------------");
    		 System.out.println("Printing Inverted Index...");
    	}
    	_invertedIndex.printInvertedIndex();
    }
    
    public static void main(String args[]) {
    	
    	try {								//look for the "debug" command
			if (args[1].equals("-debug"))
				debug = true;
    	} catch (ArrayIndexOutOfBoundsException e) {
    		System.out.println("debug = " + debug);
    	}
			
        new IRTester(args[0]).run();		//run the program
        
    }
    
    /**
     * Returns a list of file names with extension .txt.
     * 
     * @return a list of file names
     */
    private ArrayList<String> getTextFileList()
    {
        File folder = new File (_sourceDir);
        
        ArrayList<String> filenames = new ArrayList<>();
        
        File[] fileList = folder.listFiles (new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name)
            {
                return name.endsWith(".txt");
            }
            });
        
        if (fileList == null) {
            return filenames; 
        }
        
        for (File file : fileList)
        {
            filenames.add(_sourceDir + File.separator + file.getName());
            docNames.add(file.getName());								// added to get the Text-Document Names as Strings in an ArrayList
        }
        numberOfDocuments = docNames.size();							// to memorize the number of Documents in the file
        return filenames;
    }
    
    

    /**
     * Reads documents given a list of files. An error message is printed to 
     * standard error for documents not successfully read.
     * 
     * @param filenames - the list of filenames of text files
     * 
     */
    private void readDocuments(ArrayList<String> filenames) {
        for (int i=0; i < filenames.size(); i++) {
            try {
                DocumentModel documentModel = new DocumentModel(filenames.get(i));
                _documentModels.add(documentModel);
            }
            catch (Exception xcp) {
                System.err.println ("unable to read file " + filenames.get(i));
            }
        }
    }
    /**
     * to have a complet dictionary every unique String value of the DocumentModel's wordList gets added to the dictionary
     * after done so all the stopwords provided in a wordList of a DocumentModel are removed form the dictionary
     */
    private void buildDictionary() {
    	for (DocumentModel documentModel : _documentModels) {	//add every document in the file to the dictionary
    		for (String word : documentModel.get_wordList())
    			_dictionary.addWord(word);
    		System.out.println("Text added; Dictionary length: " + _dictionary.length());
    	}
    	_dictionary.sortDictionary();							//sorting once at the end to improve efficiency
    	
    	DocumentModel stopwords = null;							//read the stopword as a document model
		try {
			stopwords = new DocumentModel(pathStopwords);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    	for (String word : stopwords.get_wordList())
			_dictionary.get_wordList().remove(word);			//remove all stopwords from the dictionary
    	System.out.println("Stopwords removed; Dictionary length: " + _dictionary.length());  	
    }
    /**
     * invertedIndex gets implemented
     * for every documentModel in the ArrayList _documentModels a DocumentVectorModel is created and added to the ArrayList _documentVectorModels
     */
    private void buildDocumentVectorModels() {
    	_invertedIndex = new InvertedIndex(_dictionary);
    	for (int i = 0; i < _documentModels.size(); i++)		//add every document model to the inverted index
    		_invertedIndex.addRelation(_documentModels.get(i).get_wordList(), docNames.get(i));
    	System.out.println("Inverted Index complete");
		System.out.print("Progress of Document Vector Models");
    	for (int i = 0; i < _documentModels.size(); i++) {		//calculate weights for every document Model
    		_documentVectorModels.add(new DocumentVectorModel(_documentModels.get(i), _invertedIndex, _documentModels.size()));
    		System.out.print("  " + Math.round(((double)(i+1)/(double)numberOfDocuments)*100) + "%");		//to show the programs process on the console
    	}
    	System.out.println();
    }
    /**
     * calculate the Cosine Similarity for every DocumentVectorModel in _documentVectorModels with every DocumentVectorModel
     * @param DVMs - List containing all earlier calculated DocumentVectorModels
     * @return - matrix containing all computed Cosine Similarities
     */
    private Object[][] computeDocDocMatrix(ArrayList<DocumentVectorModel> DVMs) {
    	Object[][] matrix = new Object[numberOfDocuments+1][numberOfDocuments+1];
    	matrix[0][0] = "";
    	
    	for (int i = 0; i < numberOfDocuments; i++) {			//get the according column and row labels form the docNames variable
    		matrix[0][i+1] = docNames.get(i);
    		matrix[i+1][0] = docNames.get(i);
    	}	
    	for (int i = 0; i < numberOfDocuments; i++) {
    		for (int j = 0; j < numberOfDocuments; j++)			//calculate the cosine similarity of every document with every document in the file 
    			matrix[i+1][j+1] = df.format(DocumentVectorModel.cosineSimilarity(DVMs.get(i), DVMs.get(j)));
    	}
    	return matrix;
    }
    /**
     * prints all Objects o the parameter in form of a table
     * @param matrix
     */
    private void printMatrix(Object[][] matrix) {
        System.out.println("---------------------------------------------------------------------------");
    	for (Object[] row : matrix) {
    		for (Object object : row)							//print every row of the matrix
    			System.out.print("\t" + object);
    		System.out.println();
    	}
    }
    	
}
