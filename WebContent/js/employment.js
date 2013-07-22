/**
 * This JS FILE is specifically for employment system pages
 */

$(document).ready(function(){
	//New Item button and dialog
	$("#btn_new_applicant_link").click(function() {
		$('#btn_new_applicant_dialog').dialog('open');
			return true;
		});
	buildNewItemDialog('form_applicant_req','btn_new_applicant_dialog',650,430,'&createApplicant=');
	
	$("#btn_new_emp_req_link").click(function() {
		$('#btn_new_emp_req_dialog').dialog('open');
			return true;
		});
	buildNewItemDialog('form_new_emp_req','btn_new_emp_req_dialog',450,410,'&createRequest=');
	
	$(".driver_exam_app").each(function(){
		$(this).submit(function(){
			$("#exam_app_id").val($(this).find("input[type=hidden]").val());
			$('#btn_new_driverexam_dialog').dialog('open');
			return false;
		});
	});
	buildNewItemDialog("form_driverexam","btn_new_driverexam_dialog",250,200,"&applyExam=");
	
	/**
	 * Add the <input hidden> tag inside any td with this editbox class will
	 * result in calling the url inside the hidden input value with &value=[input] as extra
	 * parameters
	 */
	$("td.editbox").dblclick(function(){
		var value = $(this).text();
		var link = $(this).find("input:first-child").val();
		$(this).html(
				"<input type='text' id='temp_text_box' onblur='submitEdit(this)' value='"+value+"'/>" +
				"<input type='hidden' value='"+link+"'/>"
		);
		$('#temp_text_box').focus();
	});
});
  

function submitEdit(element){
	var value = element.value;
	var link = $(element).next().val() + "&value="+encodeURIComponent(value);
//	alert(link);
	$.ajax({
		url:link,
		type:"post",
//		contentType: "text/charset=utf8;charset=UTF-8",
		dataType:'text',
		success:function(response){
//			alert(response);
			var value = $('#temp_text_box').val();
			var link = $('#temp_text_box').next().val();
			$('#temp_text_box').parent().html(
					value + "<input type='hidden' value='"+ link +"'/>"
			);
		},
		error:function(response){
			alert("修改提交失败");
		}
	});
}

function buildNewItemDialog(formId, dialogId, width, height, extra){
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
			'新建':function(){
					if(!requireItemValidation(formId)){
						return;
					}
					var form = $('#'+formId);
					var params = $('#'+formId).serialize() + extra;
					$.ajax({
						url:form.action,
						type:"post",
						dataType:'text',
						data:params,
						success:function(response){
							$("#"+dialogId).dialog('close');
							alert(response);
							location.reload();
						},
						error:function(response){
							alert("errors");
						}
					});
			},
			'取消': function() {
				$(this).dialog('close');
			}
		}
	});
}

function requireItemValidation(formId){
	var text = "";
	var valid = true;
	$('#'+formId).find('input').each(function(){
		if($(this).hasClass('required')){
			var value = $(this).val();
			if(value.trim() == ""){
				valid = false;
				var name = $(this).parent().prev().html();
				text += name + ",";
			}
		}
	});
	$('#'+formId).find('select').each(function(){
		if($(this).hasClass('required')){
			var value = $(this).val();
			if(value.trim() == ""){
				valid = false;
				var name = $(this).parent().prev().html();
				text += name + ",";
			}
		}
	});
	if(text != "")
		alert("请输入 " + text);
	return valid;
}

function callAjaxForEdit(hyperLinkClassName){
	$('.'+hyperLinkClassName).click(function(){
		var action = $(this).next().val();
		$.ajax({
			url:action,
			type:"post",
			dataType:'text',
			success:function(response){
//					alert(response);
				location.reload();
			},
			error:function(response){
				alert("errors");
			}
		});
    });
}

function callAjaxForEditWithDatePicker(hyperLinkClassName){
	$('.'+hyperLinkClassName).click(function(){
		var action = $(this).next().val();
		$('#opener').val(action);
		$('#date_select_dialog').dialog('open');
    });
}

function buildBasicDatePickDialog(dialogId, width, height){
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
			'确定':function(){
    				var dateVal = $('#date_select_box').val();
    				var action = $('#opener').val() + "&dateval="+ dateVal;
					if(dateVal.trim() == ""){
    					alert("选择日期");
						return;
					}
					
					$.ajax({
						url:action,
						type:"post",
						dataType:'text',
						success:function(response){
							location.reload();
						},
						error:function(response){
							alert("errors");
						}
					});
			},
			'取消': function() {
				$(this).dialog('close');
			}
		}
	});
}