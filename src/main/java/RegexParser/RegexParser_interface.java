package RegexParser;

public interface RegexParser_interface {
	/**
	Checks the regex lexicon to avoid not allowed regex.
	
			@param re: Regex
			@param sigma: alphabet
	@return returns an integer. If the value is 0, no errors; else, there is an error. The different error codes refer to different error popups.
	*/
	int Scanner (String re, char[]sigma);
	/**
	Checks the regex sintax to avoid not allowed regex.
	
			@param re: Regex
			@param sigma: alphabet
	@return returns an integer. If the value is 0, no errors; else, there is an error. The 	different error codes refer to different error popups.
	*/
	int Parser (String re, char[] sigma);
	/**
	Runs the regex division process and the recursive substitution process
	
			@param regex: Regex
			@param sigma: alphabet
	*/	
	void Resolve(String regex, char[] sigma);

}
