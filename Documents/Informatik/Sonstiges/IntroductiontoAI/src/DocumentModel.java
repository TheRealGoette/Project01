
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;


/**
 * Models a document as a list of words.
 * 
 * @author treichherzer
 */
public class DocumentModel {
    
    /**
     * A list of separators for separating words.
     */
    private static final String SEPARATORS = " ,:;.!?\t\r\n\"\'()*#-+[]_&%$@^_–/{}|<>=";
    
    private String            _filename;
    private ArrayList<String> _wordList;
    
    /**
     * Constructs a document model from a text file.
     * 
     * @param filename - the name of the text file to be read
     * 
     * @throws FileNotFoundException - if the file does not exist
     * @throws IOException - if the file cannot be read
     */
    DocumentModel (String filename) throws FileNotFoundException, IOException
    {
        _filename = filename;
        readWords ();
    }
    
    /**
     * Constructs a document model from a text file.
     * 
     * @throws FileNotFoundException - if the file does not exist
     * @throws IOException - if the file cannot be read
     */
    private void readWords() throws FileNotFoundException, IOException
    {
        String content = readFileContent (_filename);
        
        _wordList = getWords (content);
    }
    
    /**
     * Parses the specified text into a list of words
     * 
     * @param text the text string to be parsed
     * 
     * @return a list of words
     */
    public ArrayList<String> getWords(String text) 
    {
        ArrayList<String> words = new ArrayList<>();
        
        StringTokenizer tokenizer = new StringTokenizer(text, SEPARATORS);
        while (tokenizer.hasMoreElements()) {
            words.add(tokenizer.nextToken().trim().toLowerCase()); 			//added toLowerCase to make sure words are comparable
        }
        return words;
    }       


    private String readFileContent(String filename) throws FileNotFoundException, IOException
    {
        BufferedReader reader = new BufferedReader(new FileReader (filename));
        String         textline;
        StringBuilder  stringBuilder = new StringBuilder();
        String         lineSeparator = System.getProperty("line.separator");
    
        try {
            while(null != (textline = reader.readLine())) {
                stringBuilder.append(textline);
                stringBuilder.append(lineSeparator);
        }
            return stringBuilder.toString();
        } finally {
            reader.close();
        }
    }

	public String get_filename() {
		return _filename;
	}

	public void set_filename(String _filename) {
		this._filename = _filename;
	}

	public ArrayList<String> get_wordList() {
		return _wordList;
	}

	public void set_wordList(ArrayList<String> _wordList) {
		this._wordList = _wordList;
	}
	
	public boolean contains(String word) {
		return _wordList.contains(word);
	}
    
    
}
