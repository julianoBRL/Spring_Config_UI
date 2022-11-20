

(() => {
	'use strict'

  	feather.replace({ 'aria-hidden': 'true' })
  	
	codeEditorLoader();
	codeEditorButtomLoader();
})()

function codeEditorButtomLoader(){
	var buttomSave = document.getElementById('buttomSave');
	var buttomDelete = document.getElementById('buttomDelete');
	var formEditor = document.getElementById('formEditor');
	var codeEditor = document.getElementById('codeEditor');
	
	codeEditor.addEventListener('input', () => {
	   buttomSave.disabled = false;
	});
	
	buttomSave.addEventListener('click', () => {
		formEditor.action = "/ui/saveFile"
		formEditor.submit();
	   console.log("buttomSave");
	});
	
	buttomDelete.addEventListener('click', () => {
		formEditor.action = "/ui/deleteFile"
		formEditor.submit();
	   console.log("buttomDelete");
	});
	
}

function codeEditorLoader(){
	var codeEditor = document.getElementById('codeEditor');
	var lineCounter = document.getElementById('lineCounter');
	
	codeEditor.addEventListener('scroll', () => {
    	lineCounter.scrollTop = codeEditor.scrollTop;
    	lineCounter.scrollLeft = codeEditor.scrollLeft;
	});
	 
	var lineCountCache = 0;
	function line_counter() {
	      var lineCount = codeEditor.value.split('\n').length;
	      var outarr = new Array();
	      if (lineCountCache != lineCount) {
	         for (var x = 0; x < lineCount; x++) {
	            outarr[x] = (x + 1) + '.';
	         }
	         lineCounter.value = outarr.join('\n');
	      }
	      lineCountCache = lineCount;
	}
	codeEditor.addEventListener('input', () => {
	    line_counter();
	});
	line_counter();
}