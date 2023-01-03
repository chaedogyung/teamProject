function writeSave(){
	//alert("aaa")
	if(document.noticeWriteform.user.value==""){
	  alert("작성자를 입력해 주세요");
	  document.noticeWriteform.user.focus();
	  return false;
	}
	if(document.noticeWriteform.n_subject.value==""){
	  alert("제목을 입력해 주세요");
	  document.noticeWriteform.n_subject.focus();
	  return false;
	}
	
	if(document.noticeWriteform.n_content.value==""){
	  alert("내용을 입력해 주세요");
	  document.noticeWriteform.n_content.focus();
	  return false;
	}
        
	if(document.noticeWriteform.u_pass.value==""){
	  alert("암호를 입력해 주세요");
	  document.noticeWriteform.u_pass.focus();
	  return false;
	}
	//document.noticeWriteform.submit(); //noticeWriteform에서 했기때문에 주석
	return true; //추가
 }

function loginCheck(){
	
	if(document.loginForm.id.value==""){
		alert("ID를 입력하세요");
		document.loginForm.id.focus();
		return false;
	}
	
	if(document.loginForm.pw.value==""){
		alert("PW를 입력하세요");
		document.loginForm.pw.focus();
		return false;
	}

	return true; //추가
}//loginCheck() end