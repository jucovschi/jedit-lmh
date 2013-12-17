function autocomplete(file, content, line, col, callback) {
	data = JSON.stringify({
			file : file,
			content : content,
			line : line,
			col: col
	});
	
	$.ajax({
		  type: "POST",
		  url: "http://localhost:8082/autocomplete",
		  data: data,
		  success: callback,
		  dataType: "json"
		});
}