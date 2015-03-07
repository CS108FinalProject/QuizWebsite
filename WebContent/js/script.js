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
    function storePendingQuiz(pendingQuiz) {
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
            clearPendingQuiz();
            console.log( getPendingQuiz() );

            templates.renderQuestionType();
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

                quizMetaData = { name: quizName, description: description, isImmediate: isImmediate, 
                                isRandom: isRandom, isSinglePage: isSinglePage };
                var newQuiz = { quizMetaData: quizMetaData, questions: new Array() };

                // will overwrite whatever is in pending quiz with this form
                var pendingQuiz = getPendingQuiz();
                pendingQuiz = newQuiz;
                storePendingQuiz(pendingQuiz);
                
                console.log( getPendingQuiz() );
                
                $('#right-pane').html( templates.renderQuestionType() );

            }

        });
    });


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
                    var questionInfo = {};
                    questionInfo["type"] = "Fill In The Blank";
                    questions.push( questionInfo );
                    getPendingQuiz().questions = questions;
                    rightPane.innerHTML = templates.renderFillInTheBlankQuestion();
                    break;
                case "Multiple Choice":
                    questions.type = "Multiple Choice";
                    rightPane.innerHTML = templates.renderMultipleChoiceQuestion();
                    break;
                case "Picture":
                    questions.type = "Fill In The Blank";
                    rightPane.innerHTML = templates.renderPictureQuestion();
                    break;
                case "Multi-Response":
                    questions.type = "Fill In The Blank";
                    rightPane.innerHTML = templates.renderMultiResponseQuestion();
                    break;
                case "Matching":
                    questions.type = "Fill In The Blank";
                    rightPane.innerHTML = templates.renderMatchingQuestion();
                    break;
                case "Response":
                    questions.type = "Fill In The Blank";
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
                        rightPane.innerHTML = templates.renderQuestionType();
                        break;
                case "Create Question":
                        rightPane.innerHTML = templates.renderSubmissionForm();
                        break;
                case "Add Another Quesiton":
                        rightPane.innerHTML = templates.renderQuestionType();
                        break;
                case "Finish and Create Quiz":
                        //rightPane.innerHTML = templates.renderQuizForm();
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
