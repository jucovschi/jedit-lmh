struct AutoRequest {
	1: string filePath,
	2: i32 row,
	3: i32 col 
}

struct AutoResponse {	
	1: string suggestion,
	2: string category
}

service stex_autocomplete {
	AutoResponse autocomplete(1: AutoRequest request)
}