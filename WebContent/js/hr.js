$(document).ready(function() {
	
	//New Employee button and dialog
    $("#btn_new_document_link").click(function() {
    	$('#btn_new_document_dialog').dialog('open');
		return true;
    });
    buildBasicCreateDialog('form_new_employee','btn_new_document_dialog',650,400,'&create=创建&employee.account.id=1');
    
    //New department button and dialog
    $("#btn_new_department_link").click(function() {
    	$('#btn_new_department_dialog').dialog('open');
		return true;
    });
    buildBasicCreateDialog('form_new_department','btn_new_department_dialog',650,150,"&create=创建");
    
    //New position button and dialog
    $("#btn_new_position_link").click(function() {
    	$('#btn_new_position_dialog').dialog('open');
		return true;
    });
    buildBasicCreateDialog('form_new_position','btn_new_position_dialog',650,150,"&create=创建");
    
    //New coordinate button
    $("#btn_new_coordinate_link").click(function() {
    	$('#btn_new_coordinate_dialog').dialog('open');
		return true;
    });
    buildBasicCreateDialog('form_new_coordinate','btn_new_coordinate_dialog',500,350,"&create=创建");
    
    $(".datepickerClass").datepicker({
    	changeMonth: true,
		changeYear: true,
		dateFormat: 'yy-mm-dd' 
    });
    
    $(".form_function_list").each(function(){
    	$(this).submit(function(){
    		var submit = true;
    		$(this).find("input[clicked=true]").each(function(){
    			var name = $(this).attr("name");
            	var value= "";
            	var url= "";
            	if(name == "detail"){
            		name = $(this).prev().prev().attr("name");
            		value = $(this).prev().prev().attr("value");
            		url = window.location.pathname + "?"+name+"="+value+"&detail=";
            		window.open(url, "Employee Detail ID:"+value, ["width=650,height=400,scrollbars=yes"]);
            		submit = false;
            	}
            	if(name == "contract"){
            		value = $(this).prev().prev().prev().attr("value");
            		$('#contract_target_id').val(value);
            		$('#contract_label_name').html("员工:" + $(this).parents('td').next().next().html());
            		$('#btn_new_contract_dialog').dialog('open');
            		submit=false;
            	}
            	if(name == "idcards"){
            		name = $(this).prev().prev().prev().prev().attr("name");
            		value = $(this).prev().prev().prev().prev().attr("value");
            		url = window.location.pathname + "?"+name+"="+value+"&idcards=";
            		window.open(url, "Employee identity cards:"+value, ["width=800,height=400,scrollbars=yes"]);
            		submit =false;
            	}
    		});
    		return submit;
    	});
    });  

    $(".form_function_list input[type=submit]").click(function() {
        $("input[type=submit]", $(this).parents("form")).removeAttr("clicked");
        $(this).attr("clicked", "true");
    });
 });


$(document).ready(function(){
    $(".btn_contract_view_employee").click(function(){
    	var url = $(this).val();
    	var targetId = $(this).prev().val();
    	url += "?targetId="+targetId +"&detail=";
    	window.open(url, "Contract Employee Detail ID:"+targetId, ["width=650,height=400,scrollbars=yes"]);
    });
	$(".btn_contract_view_all").click(function(){
		var url = $(this).val();
    	var targetId = $(this).prev().prev().val();
    	url += "?targetId="+targetId +"&viewall=";
    	window.open(url, "Contracts For Employee Detail ID:"+targetId, ["width=1200,height=400,scrollbars=yes"]);
	});
	$(".btn_contract_delete").click(function(){
		var url = $(this).val();
		var targetId = $(this).prev().prev().prev().val();
		var params = "targetId=" + targetId + "&delete=";
		$.ajax({
			url:url,
			type:"post",
			dataType:'text',
			data:params,
			success:function(response){
				console.log("ajax response = "+response);
				alert(response);
				location.reload();
			},
			error:function(response){
				alert("删除合同失败");
			}
		});
	})
});


$(document).ready(function() {
	$("#btn_new_contract_dialog").dialog({
		autoOpen: false,
		bgiframe: true,
		resizable: true,
		height:250,
		width:500,
		modal: true,
		overlay: {
			backgroundColor: '#000',
			opacity: 0.5
		},
		buttons: {
			'Create':function(){
					var form = $('#form_new_contract');
					var params = $('#form_new_contract').serialize() + "&createcontract=";
					$.ajax({
						url:form.action,
						type:"post",
						dataType:'text',
						data:params,
						success:function(response){
							console.log("ajax response = "+response);
							$("#btn_new_contract_dialog").dialog('close');
							alert(response);
							clearFormTextBox("form_new_contract");
						},
						error:function(response){
							alert("errors");
						}
					});
			},
			Cancel: function() {
				$(this).dialog('close');
			}
		}
	});
});

function clearFormTextBox(form){
//	alert("clearing");
	$('#'+form + ' input[type=text],select').each(function(){
//		alert($(this).val());
		$(this).val('');
	});
}

function buildBasicCreateDialog(formId, dialogId, width, height, extraParam){
	$("#"+dialogId).dialog({
		autoOpen: false,
		bgiframe: true,
		resizable: true,
		height:height,
		width:width,
		modal: true,
		overlay: {
			backgroundColor: '#000',
			opacity: 0.5
		},
		buttons: {
			'Create':function(){
					var form = $('#'+formId);
					var params = $('#'+formId).serialize() + extraParam;
					$.ajax({
						url:form.action,
						type:"post",
						dataType:'text',
						data:params,
						success:function(response){
							console.log("ajax response = "+response);
							$("#"+dialogId).dialog('close');
							alert(response);
							clearFormTextBox(formId);
							location.reload();
						},
						error:function(response){
							alert("errors");
						}
					});
			},
			Cancel: function() {
				$(this).dialog('close');
			}
		}
	});
}

function deleteDepartment(dpid){
	alert("delete "+dpid);
	$.ajax({
		url:form.action,
		type:"post",
		dataType:'text',
		data:params,
		success:function(response){
			console.log("ajax response = "+response);
			$("#"+dialogId).dialog('close');
			alert(response);
			clearFormTextBox(formId);
		},
		error:function(response){
			alert("errors");
		}
	});
}
