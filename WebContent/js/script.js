(function(window, document, undefined) {

    // got it
    var numberOfPoints = 0;
    var numberCorrect = 0;

    // start time & end time
    var startTime;
    var endTime;
    // pane elementss
    var quizName = getUrlVar("quiz"); 
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
    var leftPaneQuizzesTemplate = document.getElementById('left-pane-quizzes-template');
    var editQuizOptionsTemplate = document.getElementById('edit-quiz-options-template');


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
        renderSubmissionForm: Handlebars.compile(submissionForm.innerHTML),
        renderLeftPaneQuizzes: Handlebars.compile(leftPaneQuizzesTemplate.innerHTML ),
        renderEditQuizOptions: Handlebars.compile( editQuizOptionsTemplate.innerHTML )
    };

    if ( quizName === "" ) {
        rightPane.innerHTML = templates.renderQuizForm();
    }

    // HELPER FUNCTIONS 

    // StopWatch
    function Stopwatch(){
      var startTime, endTime, instance = this;

      this.start = function (){
        startTime = new Date();
      };

      this.stop = function (){
        endTime = new Date();
      }

      this.clear = function (){
        startTime = null;
        endTime = null;
      }

      this.getSeconds = function(){
        if (!endTime){
        return 0;
        }
        return Math.round((endTime.getTime() - startTime.getTime()) / 1000);
      }

      this.getMinutes = function(){
        return instance.getSeconds() / 60;
      }      
      this.getHours = function(){
        return instance.getSeconds() / 60 / 60;
      }    
      this.getDays = function(){
        return instance.getHours() / 24;
      }   
    }
    

    var watch = new Stopwatch();
    

    /****End of Stopwatch ****/

    /* Returns the pendingQuiz stored in localStorage. */
    function getPendingQuiz() {
        if (!localStorage.pendingQuiz) {
            localStorage.pendingQuiz = JSON.stringify({});
        }

        return JSON.parse(localStorage.pendingQuiz);
    }

    function storeMyQuizzes(myQuizzes) {
        localStorage.myQuizzes = JSON.stringify( myQuizzes );
    }

    function getMyQuizzes() {
        if (!localStorage.myQuizzes) {
            localStorage.myQuizzes = JSON.stringify({});
        }
        return JSON.parse( localStorage.myQuizzes );
    }

    function clearMyQuizzes() {
        localStorage.removeItem("myQuizzes");
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

    function storeQuestions(questions) {
        localStorage.questions = JSON.stringify(questions);
    }

    function getQuizzes() {

    }

    // tested -> works
    function storeQuizToTake( quizToTake ) {
        localStorage.quizToTake = JSON.stringify( quizToTake );
    }

    // tested -> works
    function getQuizToTake() {
        if (!localStorage.quizToTake) {
            localStorage.quizToTake = JSON.stringify({});
        }
        return JSON.parse( localStorage.quizToTake );
    }

    // TO DO
    function generateOnePageQuiz() {
        var template = document.getElementById('one-page-display-template');
        var render = Handlebars.compile( template.innerHTML );
        var questions = getQuizToTake().questions;
        var onepage = getQuizToTake().quizMetaData.is_one_page;
        $('#right-pane').html( render( {questions: questions, onepage: onepage}) );
    }

    // TO DO
    function generateMultiplePageQuiz(isImmediate) {
            generateMultiplePageQuizWithNoFeedBack();
    }

    // TO DO
    function generateMultiplePageQuizWithFeedBack() {
        console.log("Hello");
    }

    var MPIndex = 0;
    function generateMultiplePageQuizWithNoFeedBack() {
        var template;
        var render;
        console.log("No feedback yet");
        console.log( getQuizToTake() );
        var questions = getQuizToTake().questions;

        if ( questions[MPIndex].type === "Fill_Blank" ) {
            template = document.getElementById('multiple-page-FITB-template');
            render = Handlebars.compile( template.innerHTML );
        } else if ( questions[MPIndex].type === "Response"  ) {
            template = document.getElementById('multiple-page-RES-template');
            render = Handlebars.compile( template.innerHTML );
        } else if ( questions[MPIndex].type === "Multiple_Choice" ) {
            template = document.getElementById('multiple-page-MC-template');
            render = Handlebars.compile( template.innerHTML );
        } else if ( questions[MPIndex].type === "Picture"  ) {
            template = document.getElementById('multiple-page-PIC-template');
            render = Handlebars.compile( template.innerHTML );
        } else if ( questions[MPIndex].type === "Multi_Response"  ) {
            template = document.getElementById('multiple-page-MR-template');
            render = Handlebars.compile( template.innerHTML );
        } else if ( questions[MPIndex].type === "Matching" ) {
            template = document.getElementById('multiple-page-MATCH-template');
            render = Handlebars.compile( template.innerHTML );
        }

        $('#right-pane').html( render( {curQuestion: questions[MPIndex]} ) );

        // listen to next question button
        

    }


    function storeBooleanTypeForQuestion( response ) { 
        // store boolean types of question types
        var questions = response.data.questions;
        for(var i = 0; i < questions.length; i++) {
            switch( questions[i].type ) {
                case "Response":
                    questions[i].isResponse = true;
                    questions[i].id = Math.random();
                    break;
                case "Fill_Blank":
                    questions[i].isFillInTheBlank = true;
                    questions[i].id = Math.random();
                    questions[i].blankedQuestion = formatBlankedQuestion( questions[i] );
                    break;
                case "Multiple_Choice":
                    questions[i].isMultipleChoice = true;
                    questions[i].id = Math.random();
                    break;
                case "Picture":
                    questions[i].isPicture = true;
                    questions[i].id = Math.random();
                    break;
                case "Multi_Response":
                    questions[i].isMultiResponse = true;
                    questions[i].id = Math.random();
                    break;
                case "Matching":
                    questions[i].isMatching = true;
                    questions[i].id = Math.random();
                    break;
                default:
                    // don't add any booleans
            }
        }
        response.data.questions = questions;
        storeQuizToTake( response.data )
    }

    function formatBlankedQuestion( data ) {
        var idString = data.id;
        var question = data.question;
        var answers = data.answers;
        var modifiedQuestion = question;
        for(var obj in answers) {
            modifiedQuestion = modifiedQuestion.replace(obj, '<input class=' + idString + ' type=\"text\">')
        }
        return modifiedQuestion;
    }

    // LISTENERS
     $(document).ready(function() {


        // Create new Quiz Form on click
        $('#new-quiz-button').click(function() {
            $('#right-pane').html( templates.renderQuizForm() );
        });

        $('#my-quizzes').click( function() {
            var creator = getUrlVar("user");
            $('#my-quizzes').prop("disabled",true);
            var URL = "/QuizWebsite/GetData";
            var myRequest = { request: { type: "allCreatorQuizzesString", creator: creator } };
            $.ajax({
                url: URL,
                    type: 'POST',
                    async: true,
                    dataType: 'json',
                    data: { json: JSON.stringify(myRequest) },
                    contentType: 'application/x-www-form-urlencoded',

                    success: function(data, textStatus, jqXHR) {
                        $('#my-quizzes').prop("disabled",false);
                        storeMyQuizzes(data);
                        displayOnLeftPane(data);
                    }
            }); 

        });


        // Listen to Dropdown of Selected
        $("#quiz_selection").on("change", function() {

            // jQuery
            var creator = getUrlVar("user");
            var selectedVal = $(this).find(':selected').val();
            var selectedText = $(this).find(':selected').text();
            var type;
            var myRequest;
            switch (selectedVal) {
                case "mostPopularQuizzes":
                    type = "mostPopularQuizzes";
                    myRequest = {request: { type: type, creator: creator  } };
                    break;
                case "allQuizzes":
                    type = "allQuizzes";
                    myRequest = {request: { type: type } };
                    break;
                case "recentlyCreatedQuizzes":
                    type = "recentlyCreatedQuizzes";
                    myRequest = {request: { type: type, numRecords: 10} };
                    break;
                default:
                // do nothing
            }

            var URL = "/QuizWebsite/GetData";
            var myRequest = { request: { type: type, numRecords: 10 } };
            $.ajax({
                url: URL,
                type: 'POST',
                async: true,
                dataType: 'json',
                data: { json: JSON.stringify(myRequest) },
                contentType: 'application/x-www-form-urlencoded',

                success: function(data, textStatus, jqXHR) {
                    displayOnLeftPane( data );
                }
            }); 
        });


        /*************************RIGHT PANE EVENT LISTENERS**************************/

        // Listen to quiz submission

        // Listen to Quiz Creation Form
        $('#right-pane').on('click', '#main_add_question', function(event) {
            event.preventDefault();
            // clear local storage of any quizzes that might be pending
            clearPendingQuiz();
            var creator = getUrlVar("user");
            var quizName = $('#quiz_name').val();


            var description = $('#quiz_description').val();
            var isImmediate = $('#quiz_immediate').is(':checked');
            var isRandom = $('#quiz_random').is(':checked');
            var isOnePage = $('#quiz_single_page').is(':checked');

            if ( (quizName === null || quizName === "")) {
                alert("You must enter a quiz name");
            } else if ( (description === null || description == "") ) {
                alert("You must enter a description");
            } else {

                // adds user information
                quizMetaData = { quiz_name: quizName, creator: creator, date_created: "", description: description, is_immediate: isImmediate, 
                    is_random: isRandom, is_one_page: isOnePage}

                var newQuiz = { quizMetaData: quizMetaData, questions: new Array()}
                
                updatePendingQuiz(newQuiz);  
                
                // this will take the user to the question types page
                $('#right-pane').html( templates.renderQuestionType() );
            }

        });
        
        $('#right-pane').on('click', '#next-question', function(event) {
            event.preventDefault();
            var template;
            var render;
            console.log("Next Question");
            console.log( getQuizToTake() );
            var questions = getQuizToTake().questions;
            var quizMetaData = getQuizToTake().quizMetaData;
            
            keepTrackOfScore( questions[MPIndex], quizMetaData.is_immediate );
            if ( MPIndex < questions.length - 1 ) {
                
                
                MPIndex++;


                if ( questions[MPIndex].type === "Fill_Blank" ) {
                    template = document.getElementById('multiple-page-FITB-template');
                    render = Handlebars.compile( template.innerHTML );
                } else if ( questions[MPIndex].type === "Response"  ) {
                    template = document.getElementById('multiple-page-RES-template');
                    render = Handlebars.compile( template.innerHTML );
                } else if ( questions[MPIndex].type === "Multiple_Choice" ) {
                    template = document.getElementById('multiple-page-MC-template');
                    render = Handlebars.compile( template.innerHTML );
                } else if ( questions[MPIndex].type === "Picture"  ) {
                    template = document.getElementById('multiple-page-PIC-template');
                    render = Handlebars.compile( template.innerHTML );
                } else if ( questions[MPIndex].type === "Multi_Response"  ) {
                    template = document.getElementById('multiple-page-MR-template');
                    render = Handlebars.compile( template.innerHTML );
                } else if ( questions[MPIndex].type === "Matching" ) {
                    template = document.getElementById('multiple-page-MATCH-template');
                    render = Handlebars.compile( template.innerHTML );
                }
                
                $('#right-pane').html( render( { curQuestion: questions[MPIndex] } ) );
                console.log( questions[MPIndex]);
            } else {
                watch.stop();
                var minutes = watch.getMinutes();
                watch.clear();

                var questions = getQuizToTake().questions;
                var totalScore;
                
                // information to send back
                var percentageScore = ( numberCorrect / numberOfPoints ) * 100.0;
                var score = percentageScore.toFixed(2);
                var elapsed_time = minutes.toFixed(2);
                var date =  moment().format('YYYY/MM/DD HH:mm');
                var quiz_name = quizName;
                var user = getUrlVar('user');
                
                var renderQuickSummary = Handlebars.compile( document.getElementById('results-preview-template').innerHTML );
                $('#right-pane').html( renderQuickSummary( {score: score, user: user, quiz_name: quiz_name, 
                elapsed_time: elapsed_time, date: date}) );
            }

        });

        function keepTrackOfScore( question, isImmediate ) {
            console.log( isImmediate );
         // Picture and Multiple Choice Scoring Work
            if ( question.type === "Picture" ||
                question.type === "Response" ) {
                numberOfPoints++;
                var isAnswer = false;
                var idString = question.id;
                var answer = document.getElementById(idString).value.trim().toLowerCase();

                var listOfAnswers = question.answers;
                if ( answer != "" ) {
                    if ( listOfAnswers.indexOf(answer) != -1 ) {
                        isAnswer = true;
                    } 
                }

                if ( isAnswer ) { 
                    if ( isImmediate ) { 
                        alert("You answered Correctly");
                    }
                    numberCorrect++; 
                } else {
                    if ( isImmediate ) {
                        alert("Your Answer Is Wrong");
                    }
                }
            } else if ( question.type === "Multiple_Choice" ) {
                // Multiple Choice Question Works
                numberOfPoints++;
                var thisQuestion = question;
                var idString = question.id;
                var array = document.getElementsByClassName(idString);
                var theSame = true;
                for(var j = 0; j < array.length; j++ ) {
                    if ( !(thisQuestion.answers[array[j].name] === array[j].checked ) ) {
                        theSame = false;
                    }
                }

                if ( theSame ) {
                    if ( isImmediate ) { 
                        alert("You answered Correctly");
                    }
                    numberCorrect++;
                } else {
                    if ( isImmediate ) {
                        alert("Your Answer Is Wrong");
                    }
                }
            } else if ( question.type === "Multi_Response" ) {
                var thisQuestion = question;
                var idString = question.id;
                console.log( idString );
                var array = document.getElementsByClassName(idString);
                console.log( array );
                var inOrder = true;

                if ( thisQuestion.isOrdered ) {
                    for(var j = 0; j < array.length; j++ ) {
                        numberOfPoints++;
                        var trimmed = array[j].value.trim().toLowerCase();
                        if(  thisQuestion.answers.indexOf( array[j].value ) != j ) {
                            inOrder = false;
                        }
                    }
                    
                    if (inOrder) { 
                        if ( isImmediate ) {
                            alert("You answered Correctly");
                        }
                        numberCorrect += array.length; 
                    } else {
                        if ( isImmediate ) {
                            alert("Your Answer Is Wrong");
                        }
                    }

                } else {
                    var isValid = true;
                    console.log( array.length);
                    for(var j = 0; j < array.length; j++ ) {
                        numberOfPoints++;
                        var isValid = true;
                        var trimmed = array[j].value.trim().toLowerCase();
                        if(  thisQuestion.answers.indexOf( array[j].value ) == -1 ) {
                            isValid = false;
                        }
                    }

                    if ( isValid ) {
                        if ( isImmediate ) {
                                alert("You answered Correctly");
                            }
                        numberCorrect++;
                    } else {
                        if ( isImmediate ) {
                            alert("Your Answer Is Wrong");
                        }
                    }


                }
            } else if ( question.type === "Matching" ) {
                var thisQuestion = question;
                var idString = question.id;
                var array = document.getElementsByClassName(idString);
                var count = 0;
                var MatchAnswers = thisQuestion.answers;
                var trimmed = array[count].value.trim().toLowerCase();
                for(var key in MatchAnswers ) {
                    if ( trimmed === MatchAnswers[key] ) {
                        numberCorrect++;
                        numberOfPoints++;
                        count++;
                    } else {
                        numberOfPoints++;
                        count++;
                    }
                }
            } else if ( question.type === "Fill_Blank") {
                // Paused on fill in the blank question
                var thisQuestion = question;
                console.log( thisQuestion );
                var idString = question.id;
                var array = document.getElementsByClassName(idString);
                var possibleAnswers = thisQuestion.answers;     // a key value pairs from string to array of possiblities
                
                console.log( array );                                                // string represents a blank (w/ its possible values)
                var isCorrect = true;
                var count = 0;
                for( var key in possibleAnswers) {
                    numberOfPoints++;
                    var trimmed = array[count].value.trim().toLowerCase();
                    if ( possibleAnswers[key].indexOf( trimmed ) == -1 ) {
                        isCorrect = false;
                    } else {
                        if ( isImmediate ) {
                            alert("You answered Correctly");
                        }
                        numberCorrect++;
                    }
                    count++;
                }

                if ( isImmediate && (!isCorrect) ) {
                    alert("Your Answer Is Wrong");
                } 

            }

            console.log( numberCorrect );
            console.log( numberOfPoints );

        }

        // Listen to FILL IN THE BLANK QUESTION
        $('#right-pane').on('click', '#add_fandb_blank_answer', function(event) {
            event.preventDefault();
            var type = "Fill_Blank";
            var quiz = getPendingQuiz();
            var questions = quiz.questions;
            var questionInfo = questions[questions.length - 1];
                // a question, answer and a blank obtained from the client interface
            var question = $('#fill_in_the_blank_question').val().trim().toLowerCase();
            var answer = $('#enter_answer').val().trim().toLowerCase();
            var blank = $('#enter_blank').val().trim().toLowerCase();

 
            if ( validateForm(type, question, answer, blank) ) {
                if ( typeof questionInfo === 'undefined' ) {
                    initializeFITBQuestionInfo(type, question, answer, blank);
                } else {
                    addAnotherFandBAnswer(type, question, answer, blank);
                }
            }
        });

        //TODO: Ask Eliezer what does .pop() removes an item from.
        // Listen to MULTIPLE CHOICE QUESTION
        $('#right-pane').on('click', '#add_mc_option', function(event) {
            event.preventDefault();
            var type = "Multiple_Choice";
            var quiz = getPendingQuiz();
            var questions = quiz.questions;
            var questionInfo = questions[questions.length - 1];
            // a question, answer and a blank obtained from the client interface
            
            var question = $('#mc_question').val().trim().toLowerCase();
            var option = $('#mc_option').val().trim().toLowerCase();
            var isAnswer = $('#is_mc_answer').is(':checked');



            if ( typeof questionInfo === 'undefined' ) {
                initializeMCQuestionInfo(type, question, option, isAnswer);
            } else {
                addAnotherMCOption(type, question, option, isAnswer);
            }
        });

        // Listen to PICTURE QUESTION
        $('#right-pane').on('click', '#add_p_answer', function(event) {
            event.preventDefault();
            var type = "Picture";
            var quiz = getPendingQuiz();
            var questions = quiz.questions;
            var questionInfo = questions[questions.length - 1];
            // a question, answer and a blank obtained from the client interface
            
            var question = $('#pic_question').val().trim().toLowerCase();
            var answer = $('#pic_answer').val().trim().toLowerCase();
            var pictureURL = $('#pic_url').val().trim().toLowerCase();


            if ( typeof questionInfo === 'undefined' ) {
                initializePicQuestionInfo(type, question, answer, pictureURL);
            } else {
                addAnotherPicAnswer(type, question, answer, pictureURL);
            }
        });

        // Listen to RESPONSE QUESTION
        $('#right-pane').on('click', '#add_res_answer', function(event) {
            event.preventDefault();
            var type = "Response";
            var quiz = getPendingQuiz();
            var questions = quiz.questions;
            var questionInfo = questions[questions.length - 1];
            // a question, answer and a blank obtained from the client interface


            
            var question = $('#res_question').val().trim().toLowerCase();
            var answer = $('#res_answer').val().trim().toLowerCase();


            if ( typeof questionInfo === 'undefined' ) {
                initializeResQuestionInfo(type, question, answer);
            } else {
                addAnotherResAnswer(type, question, answer);
            }
        });

        // Listen to MULTI RESPONSE QUESTION
        $('#right-pane').on('click', '#add_mr_answer', function(event) {
            event.preventDefault();
            var type = "Multi_Response";
            var quiz = getPendingQuiz();
            var questions = quiz.questions;
            var questionInfo = questions[questions.length - 1];
            // a question, answer and a blank obtained from the client interface
            
            var question = $('#mr_question').val().trim().toLowerCase();
            var answer = $('#mr_answer').val().trim().toLowerCase();
            var isOrdered = $('#mr_in_order').is(':checked');


            if ( typeof questionInfo === 'undefined' ) {
                initializeMRQuestionInfo(type, question, answer, isOrdered);
            } else {
                addAnotherMRAnswer(type, question, answer, isOrdered);
            }
        });

        // Listen to MATCHING QUESTION
        $('#right-pane').on('click', '#add_match_answer', function(event) {
            event.preventDefault();
            var type = "Matching";
            var quiz = getPendingQuiz();
            var questions = quiz.questions;
            var questionInfo = questions[questions.length - 1];
            // a question, answer and a blank obtained from the client interface
            
            var question_header = $('#match_question_header').val().trim().toLowerCase();
            var left_question = $('#match_question').val().trim().toLowerCase();
            var right_answer = $('#match_answer').val().trim().toLowerCase();

            if ( typeof questionInfo === 'undefined' ) {
                initializeMatchQuestionInfo(type, question_header, left_question, right_answer);
            } else {
                addAnotherMatchPair(type, question_header, left_question, right_answer);
            }
        });

        // Listen to quiz submission
        $('#right-pane').on('click', '#add_match_answer', function(event) {
            event.preventDefault();
        });

    });

    // Utiliity Functions
    // Given a query string "?to=email&why=because&first=John&Last=smith"
    // getUrlVar("to")  will return "email"
    // getUrlVar("last") will return "smith"
     
    // Source: (https://gist.github.com/varemenos/2531765#file-getparam-js)
    // Slightly more concise and improved version based on http://www.jquery4u.com/snippets/url-parameters-jquery/

    function displayOnLeftPane(quizzes) {
        $('#left-pane').html( templates.renderLeftPaneQuizzes( { success: quizzes.status.success, quizzes: quizzes.data} ) );
    }

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

    function getUrlVar(key){
        var result = new RegExp(key + "=([^&]*)", "i").exec(window.location.search); 
        return result && unescape(result[1]) || ""; 
    }

    /******************************MATCHING QUESTION*****************************/
    function initializeMatchQuestionInfo(type, question_header, left_question, right_answer) {
        // record the questions main information because it's the first time
        var questionInfo = {};
        questionInfo.type = type;
        questionInfo.question = question_header;

        // possible answers to question
        var matchingPairs = { };
        matchingPairs[left_question] = right_answer;
        questionInfo.matchingPairs = matchingPairs;

        // store the information in local storage
        var quiz = getPendingQuiz();
        var questions = quiz.questions;
        clearLastSelectedQuestionType();
        questions.push( questionInfo );
        quiz.questions = questions;

        updatePendingQuiz(quiz);
        $('#match_question').val('');
        $('#match_answer').val('');
        $('#add_match_answer').prop('value', 'Add Another Answer');
    }
    function addAnotherMatchPair(type, question_header, left_question, right_answer) {
        // get information from interface
        var quiz = getPendingQuiz();
        var questions = quiz.questions;
        var questionInfo = questions.pop();
        
        if ( questionInfo.question == question_header ) {
            questionInfo.question = question_header;

            var matchingPairs = questionInfo.matchingPairs;
            matchingPairs[left_question] = right_answer;    // overwrite the contents
            questionInfo.matchingPairs = matchingPairs;

            // clear input
            $('#match_question').val('');
            $('#match_answer').val('');
            $('#add_match_answer').prop('value', 'Add Another Pair');

            questions.push( questionInfo );
            quiz.questions = questions;
            updatePendingQuiz(quiz);
        } else {
            initializeMatchQuestionInfo(type, question_header, left_question, right_answer);
        }
    }
    
    /******************MULTI RESPONSE HELPERS******************************/
    function initializeMRQuestionInfo(type, question, answer, isOrdered) {
        // record the questions main information because it's the first time
        var questionInfo = {};
        questionInfo.type = type;
        questionInfo.question = question;
        questionInfo.isOrdered = isOrdered;

        // possible answers to question
        var answers = new Array(answer);
        questionInfo.answers = answers

        // store the information in local storage
        var quiz = getPendingQuiz();
        var questions = quiz.questions;
        clearLastSelectedQuestionType();
        questions.push( questionInfo );
        quiz.questions = questions;
        updatePendingQuiz(quiz);
        $('#mr_answer').val('');
        $('#add_mr_answer').prop('value', 'Add Another Answer');
    }
    function addAnotherMRAnswer(type, question, answer, isOrdered) {
        // get information from interface
        var quiz = getPendingQuiz();
        var questions = quiz.questions;
        var questionInfo = questions.pop();

        if ( questionInfo.question === question && (questionInfo.isOrdered === isOrdered) ) {
            questionInfo.question = question;
            // listen to add more button
            var answers = questionInfo.answers;
            if ( answers.indexOf(answer) === -1 ) {
                var answers = questionInfo.answers;
                answers.push(answer);
                questionInfo.answers = answers;
                $('#mr_answer').val('');
                $('#add_mr_answer').prop('value', 'Add Another Answer');
            } else {
                $('#mr_answer').val('');
                $('#add_mr_answer').prop('value', 'Add Another Answer');
            }
            
            questions.push( questionInfo );
            quiz.questions = questions;
            updatePendingQuiz(quiz);
        } else {
            initializeMRQuestionInfo(type, question, answer, isOrdered);
        }
    }
    /******************RESPONSE HELPERS******************************/
    function initializeResQuestionInfo(type, question, answer) {
        // record the questions main information because it's the first time
        var questionInfo = {};
        questionInfo.type = type;
        questionInfo.question = question;

        // possible answers to question
        var answers = new Array(answer);
        questionInfo.answers = answers

        // store the information in local storage
        var quiz = getPendingQuiz();
        var questions = quiz.questions;
        clearLastSelectedQuestionType();
        questions.push( questionInfo );
        quiz.questions = questions;
        updatePendingQuiz(quiz);
        $('#res_answer').val('');
        $('#add_res_answer').prop('value', 'Add Another Answer');
    }
    function addAnotherResAnswer(type, question, answer) {
        // get information from interface
        var quiz = getPendingQuiz();
        var questions = quiz.questions;
        var questionInfo = questions.pop();

        if ( questionInfo.question === question ) {
            questionInfo.question = question;
        
            // listen to add more button
            var answers = questionInfo.answers;
            if ( answers.indexOf(answer) === -1 ) {
                var answers = questionInfo.answers;
                answers.push(answer);
                questionInfo.answers = answers;
                $('#res_answer').val('');
                $('#add_res_answer').prop('value', 'Add Another Answer');
            } else {
                $('#res_answer').val('');
                $('#add_res_answer').prop('value', 'Add Another Answer');
            }
            
            questions.push( questionInfo );
            quiz.questions = questions;
            updatePendingQuiz(quiz);
        } else {
            initializeResQuestionInfo(type, question, answer);
        }
    }
    
    /******************PICTURE QUESTION HELPERS******************************/
    function initializePicQuestionInfo(type, question, answer, pictureURL) {
        // record the questions main information because it's the first time
        var questionInfo = {};
        questionInfo.type = type;
        questionInfo.question = question;

        // possible answers to question
        var answers = new Array(answer);
        questionInfo.answers = answers
        questionInfo.pictureURL = pictureURL;

        // store the information in local storage
        var quiz = getPendingQuiz();
        var questions = quiz.questions;
        clearLastSelectedQuestionType();
        questions.push( questionInfo );
        quiz.questions = questions;
        updatePendingQuiz(quiz);
        $('#pic_answer').val('');
        $('#add_p_answer').prop('value', 'Add Another Answer');
    }
    function addAnotherPicAnswer(type, question, answer, pictureURL) {
        // get information from interface
        var quiz = getPendingQuiz();
        var questions = quiz.questions;
        var questionInfo = questions.pop();

        if ( questionInfo.question === question && (questionInfo.pictureURL === pictureURL) ) {
            questionInfo.question = question;
        
            // listen to add more button
            var answers = questionInfo.answers;
            if ( answers.indexOf(answer) === -1 ) {
                var answers = questionInfo.answers;
                answers.push(answer);
                questionInfo.answers = answers;
                $('#pic_answer').val('');
                $('#add_p_answer').prop('value', 'Add Another Answer');
            } else {
                $('#pic_answer').val('');
                $('#add_p_answer').prop('value', 'Add Another Answer');
            }
            
            questions.push( questionInfo );
            quiz.questions = questions;
            updatePendingQuiz(quiz);
        } else {
            initializePicQuestionInfo(type, question, answer, pictureURL);
        }
    }
    /******************MULTIPLE CHOICE HELPERS******************************/
    function initializeMCQuestionInfo(type, question, option, isAnswer) {
        // record the questions main information because it's the first time
        var questionInfo = {};
        questionInfo.type = type;
        questionInfo.question = question;
        //TODO: Do we need to handle the case that A question is entered without an option?
        // object for possible options
        var options = {};
        options[option] = isAnswer;
        questionInfo.options = options;

        // store the information in local storage
        var quiz = getPendingQuiz();
        var questions = quiz.questions;
        //questions.pop();
        questions.push( questionInfo );
        quiz.questions = questions;

        updatePendingQuiz(quiz);
        $('#mc_option').val('');
        $('#is_mc_answer').attr('checked', false);
    }

    function addAnotherMCOption(type, question, option, isAnswer) {
        // get information from interface
        var quiz = getPendingQuiz();
        var questions = quiz.questions;
        var questionInfo = questions.pop();

        if ( questionInfo.question == question ) {
            questionInfo.question = question;
        
            // if questions is changed

            // listen to add more button
            var options = questionInfo.options;
            if ( !(option in options) ) {
                var options = questionInfo.options;
                options[option] = isAnswer;
                questionInfo.options = options
                $('#mc_option').val('');
                $('#is_mc_answer').attr('checked', false);
            } else {
                $('#mc_option').val('');
                $('#is_mc_answer').attr('checked', false);
            }
            
            questions.push( questionInfo );
            quiz.questions = questions;
            updatePendingQuiz(quiz);
        } else {
            clearLastSelectedQuestionType();
            initializeMCQuestionInfo(type, question, option, isAnswer);
        }
    }
    
    /******************FILL IN THE BLANK HELPERS****************************/

    // If we enter the question form for the first time
    // we want to initialize our questions array
    function initializeFITBQuestionInfo(type, question, answer, blank) {

        // record main question information such as type and question string
        var questionInfo = {};
        questionInfo.type = type;
        questionInfo.question = question.trim().toLowerCase();
        
        // object for blank and set of answers
        var blanksAndAnswers = {};
        blanksAndAnswers[blank] = new Array(answer);
        questionInfo.blanksAndAnswers = blanksAndAnswers;

        // finally store information in local storage
        var quiz = getPendingQuiz();
        var questions = quiz.questions;
        questions.pop();
        questions.push( questionInfo );
        quiz.questions = questions;

        updatePendingQuiz(quiz);
        $('#enter_answer').val('');
        $('#add_fandb_blank_answer').prop('value', 'Add Another Answer');
    }

    function addAnotherFandBAnswer(type, question, answer, blank ) {
        
        // get information from interface
        var quiz = getPendingQuiz();
        var questions = quiz.questions;
        var questionInfo = questions.pop();
        
        if ( questionInfo.question == question ) {
            questionInfo.question = question;

            var blanksAndAnswers = questionInfo.blanksAndAnswers;
            if ( blank in blanksAndAnswers ) {
                var answers = blanksAndAnswers[blank];
                if ( answers.indexOf(answer) === -1 ) {
                    answers.push( answer );
                    blanksAndAnswers[blank] = answers;
                    questionInfo.blanksAndAnswers = blanksAndAnswers;
                }

                // clear input
                $('#enter_answer').val('');
                $('#add_fandb_blank_answer').prop('value', 'Add Another Answer')
            } else {
                var bAndA = questionInfo.blanksAndAnswers;
                bAndA[blank] = new Array(answer);
                questionInfo.blanksAndAnswers = bAndA;
                $('#enter_answer').val('');
                $('#add_fandb_blank_answer').prop('value', 'Add Another Answer')
            }

            questions.push( questionInfo );
            quiz.questions = questions;
            updatePendingQuiz(quiz);
        } else {
            clearLastSelectedQuestionType();
            initializeFITBQuestionInfo(type, question, answer, blank);
        }
    }
    



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
                    rightPane.innerHTML = templates.renderFillInTheBlankQuestion();
                    break;
                case "Multiple Choice":
                    rightPane.innerHTML = templates.renderMultipleChoiceQuestion();
                    break;
                case "Picture":
                    rightPane.innerHTML = templates.renderPictureQuestion();
                    break;
                case "Multi-Response":
                    rightPane.innerHTML = templates.renderMultiResponseQuestion();
                    break;
                case "Matching":
                    rightPane.innerHTML = templates.renderMatchingQuestion();
                    break;
                case "Response":
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
                    rightPane.innerHTML = templates.renderQuestionType();
                    break;
                case "Create Question":
                    rightPane.innerHTML = templates.renderSubmissionForm();
                    break;
                case "Cancel Quiz":
                    clearPendingQuiz();
                    rightPane.innerHTML = templates.renderQuizForm();
                    break;
                case "Add Another Quesiton":
                    rightPane.innerHTML = templates.renderQuestionType();
                    break;
                case "Finish and Create Quiz":  
                    var date = moment().format('YYYY/MM/DD HH:mm');
                    var quiz = getPendingQuiz();
                    var quizMetaData = quiz.quizMetaData;
                    quizMetaData.date_created = date;
                    quiz.quizMetaData = quizMetaData;
                    updatePendingQuiz(quiz);    

                    var URL = "/QuizWebsite/CreateQuiz";
                    var createdQuiz = getPendingQuiz();
                    console.log( createdQuiz );
                    $.ajax({
                        url: URL,
                        type: 'POST',
                        async: true,
                        dataType: 'json',
                        data: { json: JSON.stringify(createdQuiz) },
                        contentType: 'application/x-www-form-urlencoded',

                        success: function(data, textStatus, jqXHR) {
                            if ( data.status.success ) {
                                $('#right-pane').html('Quiz Added Successfully');
                            } else {
                                $('#right-pane').html('An Error Occurred While Attempting to Create Your Quiz');
                            }
                            
                        }
                    }); 

                    break;
                default:
                        // do nothing
            }
        } else if ( event.target.value === "Submit" ) {
            watch.stop();
            
            var minutes = watch.getMinutes();
            watch.clear();

            var questions = getQuizToTake().questions;
            // var numberOfPoints = 0;
            // var numberCorrect = 0;
            var totalScore;
           
            for(var i = 0; i < questions.length; i++) {
                // Picture and Multiple Choice Scoring Work
                if ( questions[i].type === "Picture" ||
                    questions[i].type === "Response" ) {
                    var questionIndex = i;
                    numberOfPoints++;
                    var isAnswer = false;
                    var idString = questions[i].id;
                    var answer = document.getElementById(idString).value;

                    var listOfAnswers = questions[questionIndex].answers;
                    if ( answer != "" ) {
                        if ( listOfAnswers.indexOf(answer) != -1 ) {
                            isAnswer = true;
                        } 
                    }

                    if ( isAnswer ) { numberCorrect++; };
                } else if ( questions[i].type === "Multiple_Choice" ) {
                    // Multiple Choice Question Works
                    numberOfPoints++;
                    var thisQuestion = questions[i];
                    var idString = questions[i].id;
                    var array = document.getElementsByClassName(idString);
                    var theSame = true;
                    for(var j = 0; j < array.length; j++ ) {
                        if ( !(thisQuestion.answers[array[j].name] === array[j].checked ) ) {
                            theSame = false;
                        }
                    }

                    if ( theSame ) {
                        numberCorrect++;
                    } 
                } else if ( questions[i].type === "Multi_Response" ) {
                    var thisQuestion = questions[i];
                    var idString = questions[i].id;
                    var array = document.getElementsByClassName(idString);
                    var inOrder = true;

                    if ( thisQuestion.isOrdered ) {
                        for(var j = 0; j < array.length; j++ ) {
                            numberOfPoints++;
                            if(  thisQuestion.answers.indexOf( array[j].value ) != j ) {
                                inOrder = false;
                            }
                        }
                        if (inOrder) { numberCorrect += array.length; }
                    } else {
                        for(var j = 0; j < array.length; j++ ) {
                            numberOfPoints++;
                            if(  thisQuestion.answers.indexOf( array[j].value ) != -1 ) {
                                numberCorrect++;
                            }
                        }
                    }
                } else if ( questions[i].type === "Matching" ) {
                    var thisQuestion = questions[i];
                    var idString = questions[i].id;
                    var array = document.getElementsByClassName(idString);
                    var count = 0;
                    var MatchAnswers = thisQuestion.answers;
                    for(var key in MatchAnswers ) {
                        if ( array[count].value === MatchAnswers[key] ) {
                            numberCorrect++;
                            numberOfPoints++;
                            count++;
                        } else {
                            numberOfPoints++;
                            count++;
                        }

                    }
                } else if ( questions[i].type === "Fill_Blank") {
                    
                    // Paused on fill in the blank question
                    var thisQuestion = questions[i];
                    console.log( thisQuestion );
                    var idString = questions[i].id;
                    var array = document.getElementsByClassName(idString);
                    var possibleAnswers = thisQuestion.answers;     // a key value pairs from string to array of possiblities
                                                                    // string represents a blank (w/ its possible values)
                        
                    console.log( array );  
                    var isCorrect = true;
                    var count = 0;
                    for( var key in possibleAnswers) {
                        numberOfPoints++;
                        console.log( possibleAnswers[key] ) ;
                        console.log( array[count].value );
                        var trimmed = array[count].value.trim().toLowerCase();
                        console.log( possibleAnswers[key].indexOf( trimmed));
                        if ( possibleAnswers[key].indexOf( trimmed ) == -1) {
                            isCorrect = false;
                        } else {
                            numberCorrect++;
                        }
                        count++;
                    }

                }
            }

            // information to send back
            var percentageScore = ( numberCorrect / numberOfPoints ) * 100.0;
            var score = percentageScore.toFixed(2);
            var elapsed_time = minutes.toFixed(2);
            var date =  moment().format('YYYY/MM/DD HH:mm');
            var quiz_name = quizName;
            var user = getUrlVar('user');



            var renderQuickSummary = Handlebars.compile( document.getElementById('results-preview-template').innerHTML );
            $('#right-pane').html( renderQuickSummary( {score: score, user: user, quiz_name: quiz_name, 
                elapsed_time: elapsed_time, date: date}) );
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

     /*************************TAKE SINGLE PAGE QUIZ LOGIC****************************/

     //request database for the quiz
     if ( quizName != "") {
            watch.start();
            var URL = "/QuizWebsite/GetData";
            var request = {request: { type: "quiz", quiz_name: quizName}} // tested works
            $.ajax({
                   url: URL,
                   type: 'POST',
                   async: true,
                   dataType: 'json',
                   data: { json: JSON.stringify(request) },
                   contentType: 'application/x-www-form-urlencoded',

                   success: function(response, textStatus, jqXHR) {
                       if ( response.status.success ) {
                           storeBooleanTypeForQuestion( response );
                       }
                       startTime = new Date();
                       // tested -> works proper way to obtain booleans
                       var onePage = getQuizToTake().quizMetaData.is_one_page;
                       var isImmediate = getQuizToTake().quizMetaData.is_immediate;
                       if ( onePage ) {           // checked works
                           generateOnePageQuiz();
                       } else {
                           // test all values
                           generateMultiplePageQuiz(isImmediate);
                       }

                   }
            }); 
       }


    
})(this, this.document);
