package co.kr.teamSpring_Trip;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;//HashMap사용
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.ibatis.session.SqlSession;//Mybatis 사용

import model.member.MemberDto;////////

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import javax.naming.NamingException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class MemberController {

   @Autowired
   private SqlSession sqlSession;//변수선언
   
   @RequestMapping("main.do")
   public String mm() {
      return "main";//뷰리톤 main.jsp
   }
   
   //회원가입
   @RequestMapping("insertForm.do")
   public String insertForm() {
      //return "member/insertForm";//�� ���� member/insertForm.jsp ����� �ۼ�
      return ".main.member.insertForm";//�� ���� member/insertForm.jsp ����� �ۼ�
   }
   
   //id�ߺ� üũ
   @RequestMapping(value="idCheck.do",method=RequestMethod.POST)
   public String idCheck(String m_id,Model model)
   throws IOException,NamingException{
      
      //System.out.println("id:"+id);//id:abb
      int check=-1;//�����
      
      MemberDto memberDto=sqlSession.selectOne("member.selectOne",m_id);
      if(memberDto==null) {
         check=1;//��� ������ id
      }
      model.addAttribute("check",check);
      //System.out.println("check:"+check);//check:1
      
      return "/member/idCheck";//�� ����  idCheck.jsp ����� �ۼ�
    }
   
   //ȸ�� ����
   @RequestMapping(value="insertPro.do",method=RequestMethod.POST)
   public String memInsert(@ModelAttribute("memberDto") MemberDto memberDto, HttpServletRequest request)
   throws IOException,NamingException{
	   
	   String m_addr=request.getParameter("m_addr");
	   String m_addr2=request.getParameter("m_addr2");
	  
	   memberDto.setM_addr(m_addr+","+m_addr2);
      
      sqlSession.insert("member.insertMember",memberDto);
      //return "/member/loginForm";//�丮�� member/loginForm.jsp �����
      return ".main.member.loginForm";//�丮�� member/loginForm.jsp �����
   }
   
   //�α��� ��
   @RequestMapping("loginForm.do")
   public String loginF() {
	   //return "member/loginForm";//�丮��, loginForm.jsp �ۼ�
	   return ".main.member.loginForm";//�丮��, loginForm.jsp �ۼ�
   }
   
   //�α���(DB����  Ȯ��)
   @RequestMapping(value="loginPro.do",method=RequestMethod.POST)
   public String loginM(String m_id,String m_pw, Model model)
   throws IOException,NamingException{
	   HashMap<String, String> map=new HashMap<String,String>();
	   map.put("m_id",m_id);
	   map.put("m_pw",m_pw);
	   
	   MemberDto memberDto=sqlSession.selectOne("member.selectLogin",map);
	   if(memberDto==null) {
		   model.addAttribute("msg","ID�Ǵ� �н����尡 ��ġ���� �ʽ��ϴ�");
		   //return "/member/loginForm";//�丮��
		   return ".main.member.loginForm";//�丮��
	   }
	   
	   model.addAttribute("memberDto",memberDto);
	   //return "/member/loginSuccess";//�丮��
	   return ".main.member.loginSuccess";//�丮��
   }
   
   //�α׾ƿ�
   @RequestMapping("logOut.do")
   public String logOut() {
	  // return "/member/logOut";//�� ����
	   return ".main.member.logOut";//�� ����
   }
   
   //ȸ�� ���� ��
   @RequestMapping(value="editForm.do", method=RequestMethod.POST)
   public String editF(String m_id, Model model)
   throws IOException,NamingException{
	   MemberDto memberDto=sqlSession.selectOne("member.selectOne",m_id);
	   String m_addr=memberDto.getM_addr();
	   String m_addr1[]=m_addr.split(",");
	   m_addr=m_addr1[0];//�����ȣ �˻��Ͽ� ���� �ּ�
	   String m_addr2=m_addr1[1];//���� ���� ���ּ�
	   
	   model.addAttribute("m_addr",m_addr);
	   model.addAttribute("m_addr2",m_addr2);
	   model.addAttribute("memberDto",memberDto);
	   
	  // return "/member/editForm";//�丮�� /member/editForm.jsp �����
	   return ".main.member.editForm";//�丮�� /member/editForm.jsp �����
   }
   
   //DBȸ�� ���� ����
   @RequestMapping(value="editPro.do", method=RequestMethod.POST)
   public String editP(@ModelAttribute("memberDto") MemberDto memberDto, HttpServletRequest request)
   throws IOException,NamingException{
	   
	   System.out.println("ID:"+memberDto.getM_id());
	   
	   String m_addr=request.getParameter("m_addr");
	   String m_addr2=request.getParameter("m_addr2");	  
	   memberDto.setM_addr(m_addr+","+m_addr2);
	   
	   sqlSession.update("member.memberUpdate",memberDto);
	   return "main";//views/main.jsp �����
   }
   
   //ȸ��Ż��
   @RequestMapping("deleteMember.do")
   public String deleteP(String m_id) 
		   throws IOException,NamingException{

	   sqlSession.delete("member.memberDelete",m_id);
	   return "main";//�丮��, main �ۼ�
   }
}//class end