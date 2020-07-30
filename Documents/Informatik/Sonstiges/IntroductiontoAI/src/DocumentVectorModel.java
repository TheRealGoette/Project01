
/**
 * generates a vector as a double field out of a DocumentModel with calculated weights for every word
 * @author marcogoette
 *
 */
public class DocumentVectorModel {
    
    private double[] _weights;				// contains the calculated weights at the same index the words have in the dictionary
    private DocumentModel documentModel;
    private InvertedIndex invertedIndex;
    private int numberOfDocuments;			// Number of Documents in the file
    /**
     * inizializes the needed variables      
     * @param documentModel
     * @param invertedIndex
     * @param numberOfDocuments
     */
    public DocumentVectorModel (DocumentModel documentModel, InvertedIndex invertedIndex, int numberOfDocuments) {
    	
    	this.documentModel = documentModel;
    	this.invertedIndex = invertedIndex;
    	this.numberOfDocuments = numberOfDocuments;
    	_weights = new double[invertedIndex.getDictionary().length()];
    	
    	computeWordFrequency();				// computes the frequency for every word
        
    	computeWeights();					// computes the weights for every word
    }
    /**
     * generates an early version of the vector containing for every word in how many of the documents of the file it occurs
     * for every word in the document the weight increases at the according index if the word is part of the dictionary
     */
    public void computeWordFrequency() {
    	int index;
        for (String word : documentModel.get_wordList()) {
        	index = invertedIndex.getDictionary().indexOfWord(word);
        	if (index != -1)
            	_weights[index]++;
        }
    }
    /**
     * uses the vector from before and computes the actual weights for every word
     * to normalize all values (should be between 0 - 1) they get devided by the maximum weight at the end
     * the if-clauses are essential to prevent the devision by zero
     */
    public void computeWeights() {
    	double max = 0;
        for (int i = 0; i < invertedIndex.getDictionary().get_wordList().size(); i++) {
        	int frequency = invertedIndex.getOccurence()[i];
        	if (frequency == 0.0 || frequency == numberOfDocuments)
        		_weights[i] = 0;
        	else if (_weights[i] == 0.0)
        		_weights[i] = 0;
        	else
        		_weights[i] = ((1 + Math.log10(_weights[i])) * Math.log10((numberOfDocuments)/frequency));
        	if (_weights[i] > max && !Double.isInfinite(_weights[i]))
        		max = _weights[i];
        }
        
        for (int i = 0; i < _weights.length; i++)
        	_weights[i] /= max;
    }
    /**
     * method to calculate the Cosine Similarity between two DocumentVectorModels
     * the whole formula is divided in separate parts
     * @param d1
     * @param d2
     * @return double value of the Cosine SImilarity
     */
    public static double cosineSimilarity (DocumentVectorModel d1, DocumentVectorModel d2) {
        // implement this
    	double sumproduct = 0;
    	double sumPowD1 = 0, sumPowD2 = 0;
    	double v1, v2;
    	for (int i = 0; i < d1.get_weights().length; i++) {
    		v1 = d1.get_weights()[i];
    		v2 = d2.get_weights()[i];
    		sumproduct += (v1 * v2);
    		sumPowD1 += (v1 * v1);
    		sumPowD2 += (v2 * v2);
    	}
        return sumproduct/(Math.sqrt(sumPowD1)*Math.sqrt(sumPowD2));
    }

	public double[] get_weights() {
		return _weights;
	}

	public void set_weights(double[] _weights) {
		this._weights = _weights;
	}
}
