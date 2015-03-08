(function(window, document, undefined) {
    // pane elementss
    var rightPane = document.getElementById('right-pane');
    var leftPane = document.getElementById('left-pane');

    // This array will include question objects
    // with a subject and a question
    var questions = new Array(); // will contain list of 'question' objects
    var questions = new Set();
    var newQuestion;
    var newSubject;
    var currentObjectId;
    var quizMetaData = {};


    // script elements that correspond to Handlebars templates
    var quizFormTemplate = document.getElementById('quiz-form-template');
    var questionTypeTemplate = document.getElementById('question-type-template');
    var fillInTheBlankTemplate = document.getElementById('fill-in-the-blank-template');
    var multipleChoiceTemplate = document.getElementById('multiple-choice-template');
    var pictureTemplate = document.getElementById('picture-template');
    var multiResponseTemplate = document.getElementById('multi-response-template');
    var matchingTemplate = document.getElementById('matching-template');
    var responseTemplate = document.getElementById('response-template');
    var submissionForm = document.getElementById('submission-template');
    


    // compiled Handlebars templates
    var templates = {
        renderQuizForm: Handlebars.compile(quizFormTemplate.innerHTML),
        renderQuestionType: Handlebars.compile(questionTypeTemplate.innerHTML),
        renderFillInTheBlankQuestion: Handlebars.compile(fillInTheBlankTemplate.innerHTML),
        renderMultipleChoiceQuestion: Handlebars.compile(multipleChoiceTemplate.innerHTML),
        renderPictureQuestion: Handlebars.compile(pictureTemplate.innerHTML),
        renderMultiResponseQuestion: Handlebars.compile(multiResponseTemplate.innerHTML),
        renderMatchingQuestion: Handlebars.compile(matchingTemplate.innerHTML),
        renderResponseQuestion: Handlebars.compile(responseTemplate.innerHTML),
        renderSubmissionForm: Handlebars.compile(submissionForm.innerHTML)
    };

    // HELPER FUNCTIONS 

    /* Returns the pendingQuiz stored in localStorage. */
    function getPendingQuiz() {
        if (!localStorage.pendingQuiz) {
            localStorage.pendingQuiz = JSON.stringify({});
        }

        return JSON.parse(localStorage.pendingQuiz);
    }

    /* updates the pending quiz in localStorage. 
     *
     * Arguments: 
     * pendingQuiz -- an object containing quizMetaData and questions
     */
    function updatePendingQuiz(pendingQuiz) {
        localStorage.pendingQuiz =  JSON.stringify(pendingQuiz);
    }

    /* clears all the quiz info in local storage */
    function clearPendingQuiz() {
        localStorage.removeItem( "pendingQuiz")
    }


    // /* Returns the questions stored in localStorage. */
    // function getStoredQuestions() {
    //     if (!localStorage.questions) {
    //         // default to empty array
    //         localStorage.questions = JSON.stringify([]);
    //     }

    //     return JSON.parse(localStorage.questions);
    // }

    /* Store the given questions array in localStorage.
     *
     * Arguments:
     * questions -- the questions array to store in localStorage
     */
    function storeQuestions(questions) {
        localStorage.questions = JSON.stringify(questions);
    }


    // we want to render this HTML only once
    //rightPane.innerHTML = templates.renderQuizForm();
     var newQuestionForm = document.querySelector('#interactors .btn');
        newQuestionForm.addEventListener("click", function(event) {
                event.preventDefault();
                rightPane.innerHTML = templates.renderQuizForm();
    });

    // LISTENERS
     $(document).ready(function() {
        // Create new Quiz Form on click
        $('#new_quiz_button').click(function() {
            templates.renderQuizForm();
        });

        // Listen to Quiz Creation Form
        $('#right-pane').on('click', '#main_add_question', function(event) {
            event.preventDefault();

            // clear local storage of any quizzes that might be pending

            var quizName = $('#quiz_name').val();
            var description = $('#quiz_description').val();
            var isImmediate = $('#quiz_immediate').is(':checked');
            var isRandom = $('#quiz_random').is(':checked');
            var isSinglePage = $('#quiz_single_page').is(':checked');

            if ( (quizName === null || quizName === "")) {
                alert("You must enter a quiz name");
            } else if ( (description === null || description == "") ) {
                alert("You must enter a password");
            } else {

                // adds user information
                quizMetaData = { name: quizName, description: description, isImmediate: isImmediate, 
                    isRandom: isRandom, isSinglePage: isSinglePage}

                var newQuiz = { quizMetaData: quizMetaData, questions: new Array()}
                
                updatePendingQuiz(newQuiz);  
                
                // this will take the user to the question types page
                $('#right-pane').html( templates.renderQuestionType() );
            }

        });
    });
    

    // Helper which will add the questions type 
    // based on a click on the question types page
    function addQuestionInfo(questionType) {
        var quiz = getPendingQuiz();
        var questions = quiz.questions;
        var questionInfo = {};
        questionInfo["type"] = questionType;
        questions.push( questionInfo );
        quiz.questions = questions;;
        updatePendingQuiz( quiz );
    }

    // If the user goes back to the question type form
    // from a question page we clear last question 
    // in the local storage
    function clearLastSelectedQuestionType() {
        var quiz = getPendingQuiz();
        var questions = quiz.questions;
        questions.pop();
        quiz.questions = questions;
        updatePendingQuiz( quiz);
    }

    /*
     * This function listens to all click events on the page
     * and responds to particular cases:
     * 
     * 1. Rendering specific question pages for a selected question
     *    type.
     *
     */
    rightPane.addEventListener("click", function(event)  {

        // Choosing Question Types
        if ( event.target.className === "btn types") {

            var quiz = getPendingQuiz();
            var questions = quiz.questions;
            switch ( event.target.value ) {
                case "Fill In The Blank":
                    addQuestionInfo("Fill In The Blank");
                    rightPane.innerHTML = templates.renderFillInTheBlankQuestion();
                    break;
                case "Multiple Choice":
                    addQuestionInfo("Multiple Choice");
                    rightPane.innerHTML = templates.renderMultipleChoiceQuestion();
                    break;
                case "Picture":
                    addQuestionInfo("Picture");
                    rightPane.innerHTML = templates.renderPictureQuestion();
                    break;
                case "Multi-Response":
                    addQuestionInfo("Multi-Response");
                    rightPane.innerHTML = templates.renderMultiResponseQuestion();
                    break;
                case "Matching":
                    addQuestionInfo("Matching");
                    rightPane.innerHTML = templates.renderMatchingQuestion();
                    break;
                case "Response":
                    addQuestionInfo("Response");
                    rightPane.innerHTML = templates.renderResponseQuestion();
                    break;
                default: 
                    // do nothing
            }
            // Midway Form Processing
        } else if ( event.target.className === "btn question_staging" || event.target.className
            === "btn submission") {
            switch( event.target.value ) {
                case "Return to Question Type":
                        // Indicates user changed mind on question type 
                        clearLastSelectedQuestionType();
                        console.log( getPendingQuiz() );
                        rightPane.innerHTML = templates.renderQuestionType();
                        break;
                case "Create Question":

                        rightPane.innerHTML = templates.renderSubmissionForm();
                        break;
                case "Add Another Quesiton":
                        rightPane.innerHTML = templates.renderQuestionType();
                        break;
                case "Finish and Create Quiz":
                        var URL = "/QuizWebsite/CreateQuiz";
                        var createdQuiz = getPendingQuiz();
                        // $.ajax({
                        //     url: URL,
                        //     type: 'POST',
                        //     async: true,
                        //     dataType: 'json',
                        //     data: { json: JSON.stringify(newQuiz)},
                        //     contentType: 'application/x-www-form-urlencoded',

                        //     success: function(data, textStatus, jqXHR) {
                        //         console.log( data );
                        //     }
                        // });       
                        // $('#right-pane').html( templates.renderQuestionType() );
                        break;
                default:
                        // do nothing
            }
        }
           
    });

    /*
     * FILL IN THE BLANK EVENT LISTENER
     *
     * This section registers mouse clicks on the add answer
     * and create question buttons on the right pane of the fill
     * in the blank question form.
     *
     */
     // rightPane.addEventListener("click", function(event) {
     //    event.preventDefault();
     // })  


    
})(this, this.document);
