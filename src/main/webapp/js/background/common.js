//alert modal
function alertModal(msg, title) {
	
	if(typeof title != "undefined") {
		$("#alertModalHeader").html(title);
	}
	$("#alertModal").find(".modal-body p").html(msg);
	
	$("#alertModalLink").click();
	
}
