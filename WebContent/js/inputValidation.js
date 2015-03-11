(function(window, document, undefined) {

	function validateForm(questionType, question, answer, blank) {
	        if ( question === "" ) {
	            alert("You Must Enter A Question.");
	            return false;
	        } else if ( blank === "" ) {
	            alert("You Must Enter A Blank");
	            return false;
	        } else if ( answer === "" ) {
	            alert("You Must Enter A Value For The Answer");
	            return false;
	        } else {
	            return true;
	        }
	    }
	
})(this, this.document);