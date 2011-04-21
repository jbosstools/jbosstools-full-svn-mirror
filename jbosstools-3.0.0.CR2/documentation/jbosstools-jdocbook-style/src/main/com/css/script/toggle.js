// attach handler to window object
  Event.observe(window,'load',initializeEmailClient,false);

// initialize email application
function initializeEmailClient(){
  Event.observe('feedback-mailform', 'submit', sendEmail);
}

 // send http request
function sendEmail(e){

  // prevent form from submitting
  Event.stop(e);
  var params='subject='+$F('subject')+'&message='+escape($F('message'))+'&name='+$F('name')+'&email='+$F('email')+'&path='+window.location;
  var xmlobj=new Ajax.Updater('feedback-state','script/send_mail.php',{method:'get',parameters: params});
}

