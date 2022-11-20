

(() => {
	'use strict'

  	feather.replace({ 'aria-hidden': 'true' })
  	
	codeEditorLoader();
	codeEditorButtomLoader();
})()

function codeEditorButtomLoader(){
	var buttomRestore = document.getElementById('buttomRestore');
	var buttomDelete = document.getElementById('buttomDelete');
		
	buttomRestore.addEventListener('click', () => {
		var filename = buttomRestore.getAttribute("filename")
		console.log(filename);
	});
	
	buttomDelete.addEventListener('click', () => {
		var filename = buttomDelete.getAttribute("filename")
		console.log(filename);
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